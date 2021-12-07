fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("inputs/DayXX_test")
    check(part1(testInput) == 0)
    // check(part2(testInput) == 0)

    val input = readInput("inputs/DayXX")
    println(part1(input))
    // println(part2(input))
}
