package uz.visionsystemscorp.puzzle15game.presenter.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.R

val LuckiestGuy = FontFamily(Font(R.font.luckiest_guy))

val Nunito = FontFamily(
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunito_black, FontWeight.Black),
)

/** Small uppercase caption: "TIME", "MOVES", "PLAYER NAME". */
val LabelStyle = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Black,
    fontSize = 11.sp,
    letterSpacing = 1.5.sp,
    color = Sand,
)

/** Paragraph text on wooden panels. */
val BodyStyle = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    color = Cream,
)
