import kotlin.math.abs
import kotlin.math.max

data class HydrothermalVentLine(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    fun getPoints(): List<Point> {
        return (0..max(abs(y2 - y1), abs(x2 - x1))).map { i ->
            val x = if (x2 > x1) x1 + i else if (x2 < x1) x1 - i else x1
            val y = if (y2 > y1) y1 + i else if (y2 < y1) y1 - i else y1
            Point(x, y)
        }
    }

    companion object {
        private val regex = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")

        fun parse(line: String): HydrothermalVentLine {
            return regex.matchEntire(line)!!.destructured
                .let { (x1, y1, x2, y2) ->
                    HydrothermalVentLine(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(HydrothermalVentLine::parse)
            .filter { it.x1 == it.x2 || it.y1 == it.y2 }
            .flatMap { it.getPoints() }.groupBy { it }.count { it.value.size > 1 }
    }

    fun part2(input: List<String>): Int {
        return input.map(HydrothermalVentLine::parse)
            .flatMap { it.getPoints() }.groupBy { it }.count { it.value.size > 1 }
    }

    val testInput = readInput("inputs/Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("inputs/Day05")
    println(part1(input))
    println(part2(input))
}
