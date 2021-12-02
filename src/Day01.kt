fun main() {
    fun part1(input: List<String>): Int {
        val nums = input.toIntList()
        return (1 until nums.size).count { nums[it] > nums[it - 1] }
    }

    fun part2(input: List<String>): Int {
        val nums = input.toIntList()
        return (3 until nums.size).count { nums[it] > nums[it - 3] }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
