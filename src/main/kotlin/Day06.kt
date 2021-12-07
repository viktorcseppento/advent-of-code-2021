fun main() {
    val spawnTime = 7
    val firstSpawnTime = 9

    val remDayToDescNumMap = mutableMapOf<Int, Long>()

    fun numberOfDescendants(remainingDaysFromBirth: Int): Long {
        return remDayToDescNumMap.getOrPut(remainingDaysFromBirth) {
            if (remainingDaysFromBirth < firstSpawnTime)
                return 0

            var counter = 0L

            val numberOfSpawning = (remainingDaysFromBirth - 2) / spawnTime
            for (i in 0 until numberOfSpawning) {
                counter += 1 + numberOfDescendants(remainingDaysFromBirth - 2 - (i + 1) * spawnTime)
            }
            counter
        }
    }

    fun part1(input: List<String>): Long {
        return input.first().split(',').toIntList()
            .sumOf { 1 + numberOfDescendants(80 + firstSpawnTime - 1 - it) }
    }

    fun part2(input: List<String>): Long {
        return input.first().split(',').toIntList()
            .sumOf { 1 + numberOfDescendants(256 + firstSpawnTime - 1 - it) }
    }

    val testInput = readInput("inputs/Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("inputs/Day06")
    println(part1(input))
    println(part2(input))
}
