package uz.visionsystemscorp.puzzle15game.presenter.ui.util

import androidx.compose.runtime.staticCompositionLocalOf

/** Button click feedback, provided by MainActivity so components stay free of data-layer types. */
val LocalClickSound = staticCompositionLocalOf<() -> Unit> { {} }
