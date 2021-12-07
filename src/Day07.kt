import kotlin.math.absoluteValue

fun main() {
    fun minFuel(positions: List<Int>, cost: (Int, Int) -> Int): Int {
        return if (positions.isEmpty())
            0
        else
            (positions.minOrNull()!!..positions.maxOrNull()!!).minOf { commonPos ->
                positions.sumOf { pos -> cost(commonPos, pos) }
            }
    }

    fun part1(input: List<String>): Int {
        return minFuel(input.first().split(',').toIntList()) { common, pos ->
            (pos - common).absoluteValue
        }
    }

    fun part2(input: List<String>): Int {
        return minFuel(input.first().split(',').toIntList()) { common, pos ->
            (pos - common).absoluteValue.let { (it + 1) * it / 2 }
        }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
