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

        // Possible y velocities and which steps are good
        val yVelocityGoodStepsMap = mutableMapOf<Int, Set<Int>>()
        for (startYVelocity in targetYRange.first until abs(targetYRange.first)) {
            var yVelocity = startYVelocity
            var yPosition = 0
            val goodSteps = mutableSetOf<Int>()
            for (step in 0 until abs(startYVelocity) * 2 + ceil(sqrt(2.0 * abs(targetYRange.first))).toInt()) {
                yPosition += yVelocity
                yVelocity--
                if (yPosition in targetYRange) goodSteps.add(step)
            }
            yVelocityGoodStepsMap[startYVelocity] = goodSteps
        }

        val maxXSteps = yVelocityGoodStepsMap.maxOf { mapEntry ->
            mapEntry.value.maxByOrNull { setEntry -> setEntry } ?: 0
        }

        // Possible good x velocities and which steps are good
        val xVelocityGoodStepsMap = mutableMapOf<Int, Set<Int>>()
        for (startXVelocity in 0..targetXRange.last) {
            var xVelocity = startXVelocity
            var xPosition = 0
            val goodSteps = mutableSetOf<Int>()
            for (step in 0..maxXSteps) {
                xPosition += xVelocity
                xVelocity = maxOf(0, xVelocity - 1)
                if (xPosition in targetXRange) goodSteps.add(step)
            }
            xVelocityGoodStepsMap[startXVelocity] = goodSteps
        }
        var numberOfGoodCombinedVelocities = 0

        yVelocityGoodStepsMap.forEach { (_, yStepSet) ->
            xVelocityGoodStepsMap.forEach { (_, xStepSet) ->
                if (yStepSet.intersect(xStepSet).isNotEmpty()) {
                    numberOfGoodCombinedVelocities++
                }
            }
        }

        return numberOfGoodCombinedVelocities
    }

    val testInput = readInput("inputs/Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("inputs/Day17")
    println(part1(input))
    println(part2(input))
}
