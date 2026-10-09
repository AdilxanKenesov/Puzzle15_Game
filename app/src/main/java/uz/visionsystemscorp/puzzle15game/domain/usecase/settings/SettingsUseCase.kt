package uz.visionsystemscorp.puzzle15game.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.model.SettingsData

interface SettingsUseCase {
    fun observe(): Flow<SettingsData>

    fun setMusic(on: Boolean)

    fun setSound(on: Boolean)

    fun setVibration(on: Boolean)

    /** Tizimdan chiqaradi va telefondagi natijalarni tozalaydi, keyingi o'yinchi toza boshlaydi. */
    suspend fun signOut()

    /** Akkauntni o'chiradi; muvaffaqiyatli bo'lsa telefondagi ma'lumot ham tozalanib, ilova boshidan boshlanadi. */
    suspend fun deleteAccount(googleIdToken: String? = null): AccountDeletion
}
