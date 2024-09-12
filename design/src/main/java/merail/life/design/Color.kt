package merail.life.design

import androidx.compose.material3.CardColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalMejourneyColors = staticCompositionLocalOf { Colors() }

val Colors.shimmerColor: Color
    get() = ColorConstants.thunder

val Colors.tabsContainerColor: Color
    get() = ColorConstants.thunder
val Colors.selectedTabColor: Color
    get() = ColorConstants.riverBed
val Colors.unselectedTabColor: Color
    get() = ColorConstants.thunder
val Colors.unselectedTabTextColor: Color
    get() = ColorConstants.smokeyGrey

val Colors.cardColors: CardColors
    get() = CardColors(
        containerColor = elementBackground,
        contentColor = elementPrimary,
        disabledContainerColor = elementBackground,
        disabledContentColor = elementPrimary,
    )

val Colors.materialThemeColors: ColorScheme
    get() = lightColorScheme(
        primary = screenPrimary,
        background = screenBackground,
        onBackground = screenPrimary,
        surface = screenBackground,
    )

@Immutable
data class Colors(
    val screenPrimary: Color = ColorConstants.white,
    val screenBackground: Color = ColorConstants.black,

    val elementPrimary: Color = ColorConstants.white,
    val elementBackground: Color = ColorConstants.black,
    val elementNegative: Color = ColorConstants.merlot,

    val textPrimary: Color = ColorConstants.white,
    val textNegative: Color = ColorConstants.red,
    val textInversePrimary: Color = ColorConstants.black,

    val borderPrimary: Color = ColorConstants.white,

    val graphicPrimary: Color = ColorConstants.white,
    val graphicInversePrimary: Color = ColorConstants.black,
    val graphicInverseSecondary: Color = ColorConstants.lightGrey,
    val graphicNegativePrimary: Color = ColorConstants.red,
    val graphicNegativeSecondary: Color = ColorConstants.fairPink,
)

internal object ColorConstants {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val thunder = Color(0xFF2D2D2D)
    val riverBed = Color(0xFF495253)
    val smokeyGrey = Color(0xFF6B7171)
    val red = Color(0xFFFF0000)
    val merlot = Color(0xFF862020)
    val lightGrey = Color(0xFFDBDAD7)
    val fairPink = Color(0xFFFFEEEE)
}