import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {
        val line = input.single()
        val targetYRange = Regex("""y=(-?\d+)..(-?\d+)""").find(line)!!.destructured.let { (y1, y2) ->
            IntRange(y1.toInt(), y2.toInt())
        }

        val maxYVelocity = abs(targetYRange.first) - 1

        return (maxYVelocity + 1) * maxYVelocity / 2 // v^2 = 2as
    }

    fun part2(input: List<String>): Int {
        val line = input.single()
        val targetXRange = Regex("""x=(-?\d+)..(-?\d+)""").find(line)!!.destructured.let { (x1, x2) ->
            IntRange(x1.toInt(), x2.toInt())
        }
        val targetYRange = Regex("""y=(-?\d+)..(-?\d+)""").find(line)!!.destructured.let { (y1, y2) ->
            IntRange(y1.toInt(), y2.toInt())
        }

        var numberOfVelocities = 0
        for (startYVelocity in targetYRange.first until abs(targetYRange.first)) {
            for (startXVelocity in 0..targetXRange.last) {
                var yVelocity = startYVelocity
                var xVelocity = startXVelocity
                var yPosition = 0
                var xPosition = 0
                for (step in 0 until abs(startYVelocity) * 2 + ceil(sqrt(2.0 * abs(targetYRange.first))).toInt()) {
                    xPosition += xVelocity
                    yPosition += yVelocity
                    xVelocity = maxOf(0, xVelocity - 1)
                    yVelocity--
                    if (xPosition in targetXRange && yPosition in targetYRange) {
                        numberOfVelocities++
                        break
                    }
                }
            }
        }
        return numberOfVelocities
    }

    val testInput = readInput("inputs/Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("inputs/Day17")
    println(part1(input))
    println(part2(input))
}
