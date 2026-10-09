package uz.visionsystemscorp.puzzle15game.presenter.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize

/** 75 → "1:15" (Game, Win and Records all show time this way). */
fun formatElapsed(totalSeconds: Long): String =
    "${totalSeconds / 60}:${(totalSeconds % 60).toString().padStart(2, '0')}"

/** "4×4" */
@Composable
fun BoardSize.label(): String = stringResource(R.string.board_size_label, dimension)

/** "EASY" / "CLASSIC" / "HARD" */
@Composable
fun BoardSize.difficultyLabel(): String = stringResource(
    when (this) {
        BoardSize.THREE -> R.string.board_size_easy
        BoardSize.FOUR -> R.string.board_size_classic
        BoardSize.FIVE -> R.string.board_size_hard
    },
)
