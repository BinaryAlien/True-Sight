package truesight

import java.awt.Color

object Highlights {
    val red = Color(255, 100, 100)
    val orange = Color(255, 200, 100)
    val yellow = Color(255, 255, 100)
    val green = Color(100, 255, 100)
    val cyan = Color(100, 255, 255)
    val blue = Color(100, 100, 255)
    val pink = Color(255, 200, 200)
    val magenta = Color(255, 100, 255)
    val gray = Color(180, 180, 180)
    val none = Color.WHITE // null
    val selection = Color(255, 197, 153)

    private val highlights = mapOf(
        Pair("red", red),
        Pair("orange", orange),
        Pair("yellow", yellow),
        Pair("green", green),
        Pair("cyan", cyan),
        Pair("blue", blue),
        Pair("pink", pink),
        Pair("magenta", magenta),
        Pair("gray", gray),
        Pair(null, none)
    )

    fun get(highlight: String?): Color = highlights.getOrDefault(highlight, none)
}
