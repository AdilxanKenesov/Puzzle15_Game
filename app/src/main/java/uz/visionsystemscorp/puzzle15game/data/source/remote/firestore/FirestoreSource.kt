package uz.visionsystemscorp.puzzle15game.data.source.remote.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

/**
 * Firestore layout (see firestore.rules):
 * - `users/{uid}`: name
 * - `leaderboards/{dimension}/entries/{uid}`: name, moves, seconds — the player's best on that size
 *
 * Writes are fire-and-forget: Firestore queues them on disk while offline, and the rules reject
 * any result that is not an improvement.
 */
@Singleton
class FirestoreSource @Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun fetchName(uid: String): String? = user(uid).get().await().getString(FIELD_NAME)

    fun putName(uid: String, name: String) {
        user(uid).set(mapOf(FIELD_NAME to name, FIELD_UPDATED_AT to FieldValue.serverTimestamp())).logFailure("putName")
    }

    fun putBest(uid: String, dimension: Int, entry: LeaderboardEntryDto) {
        entries(dimension).document(uid).set(
            mapOf(
                FIELD_NAME to entry.name,
                FIELD_MOVES to entry.moves,
                FIELD_SECONDS to entry.seconds,
                FIELD_UPDATED_AT to FieldValue.serverTimestamp(),
            ),
        ).logFailure("putBest")
    }

    /** Renames the player's existing entries; sizes they never ranked on are skipped by Firestore. */
    fun renameEntries(uid: String, name: String, dimensions: List<Int>) {
        dimensions.forEach { dimension ->
            entries(dimension).document(uid)
                .update(mapOf(FIELD_NAME to name, FIELD_UPDATED_AT to FieldValue.serverTimestamp()))
                .logFailure("renameEntries")
        }
    }

    /** Removes everything stored for [uid]; used before a guest account is abandoned. */
    suspend fun deleteAccountData(uid: String, dimensions: List<Int>) {
        db.batch().apply {
            delete(user(uid))
            dimensions.forEach { delete(entries(it).document(uid)) }
        }.commit().await()
    }

    suspend fun fetchTop(dimension: Int, limit: Long): List<LeaderboardEntryDto> =
        ranked(dimension).limit(limit).get().await().documents.mapNotNull { it.toEntry() }

    suspend fun fetchEntry(dimension: Int, uid: String): LeaderboardEntryDto? =
        entries(dimension).document(uid).get().await().toEntry()

    /** Players strictly ahead of [moves]/[seconds]: fewer moves, or as many moves in less time. */
    suspend fun countAhead(dimension: Int, moves: Int, seconds: Long): Long {
        val fewerMoves = entries(dimension).whereLessThan(FIELD_MOVES, moves).count()
        val faster = entries(dimension).whereEqualTo(FIELD_MOVES, moves).whereLessThan(FIELD_SECONDS, seconds).count()
        return fewerMoves.get(AggregateSource.SERVER).await().count +
            faster.get(AggregateSource.SERVER).await().count
    }

    suspend fun countAll(dimension: Int): Long = entries(dimension).count().get(AggregateSource.SERVER).await().count

    private fun user(uid: String) = db.collection(USERS).document(uid)

    private fun entries(dimension: Int) = db.collection(LEADERBOARDS).document(dimension.toString()).collection(ENTRIES)

    /** Needs the composite index entries(moves ASC, seconds ASC). */
    private fun ranked(dimension: Int) = entries(dimension)
        .orderBy(FIELD_MOVES, Query.Direction.ASCENDING)
        .orderBy(FIELD_SECONDS, Query.Direction.ASCENDING)

    private fun DocumentSnapshot.toEntry(): LeaderboardEntryDto? {
        val name = getString(FIELD_NAME) ?: return null
        val moves = getLong(FIELD_MOVES) ?: return null
        val seconds = getLong(FIELD_SECONDS) ?: return null
        return LeaderboardEntryDto(name, moves.toInt(), seconds)
    }

    private fun <T> Task<T>.logFailure(operation: String) {
        addOnFailureListener { Log.w(TAG, "$operation failed", it) }
    }

    private companion object {
        const val TAG = "FirestoreSource"
        const val USERS = "users"
        const val LEADERBOARDS = "leaderboards"
        const val ENTRIES = "entries"
        const val FIELD_NAME = "name"
        const val FIELD_MOVES = "moves"
        const val FIELD_SECONDS = "seconds"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}
