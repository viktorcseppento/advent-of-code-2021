fun main() {
    fun part1(input: List<Int>): Int {
        return (1 until input.size).count { input[it] > input[it - 1] }
    }

    fun part2(input: List<Int>): Int {
        return (3 until input.size).count { input[it] > input[it - 3] }
    }

    val testInput = readInput("inputs/Day01_test").toIntList()
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("inputs/Day01").toIntList()
    println(part1(input))
    println(part2(input))
}
