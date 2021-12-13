data class Position(val row: Int, val col: Int)

fun main() {
    fun getAdjacentPositions(position: Position, mapHeight: Int, mapWidth: Int): List<Position> {
        val adjacentPositions = mutableListOf<Position>()

        if (position.row != 0)
            adjacentPositions.add(Position(position.row - 1, position.col))
        if (position.row != mapHeight - 1)
            adjacentPositions.add(Position(position.row + 1, position.col))
        if (position.col != 0)
            adjacentPositions.add(Position(position.row, position.col - 1))
        if (position.col != mapWidth - 1)
            adjacentPositions.add(Position(position.row, position.col + 1))

        return adjacentPositions
    }

    fun getLowPoints(heightMap: List<List<Int>>): List<Position> {
        val mapWidth = heightMap[0].size
        val mapHeight = heightMap.size

        val lowPoints = mutableListOf<Position>()

        for (i in 0 until mapHeight) {
            for (j in 0 until mapWidth) {
                val adjacentHeights = mutableListOf<Int>()
                if (i != 0)
                    adjacentHeights.add(heightMap[i - 1][j])
                if (i != mapHeight - 1)
                    adjacentHeights.add(heightMap[i + 1][j])
                if (j != 0)
                    adjacentHeights.add(heightMap[i][j - 1])
                if (j != mapWidth - 1)
                    adjacentHeights.add(heightMap[i][j + 1])

                if (adjacentHeights.all { heightMap[i][j] < it }) lowPoints.add(Position(i, j))
            }
        }

        return lowPoints
    }

    fun part1(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() } }

        return getLowPoints(heightMap).sumOf { heightMap[it.row][it.col] + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { line -> line.map { it.digitToInt() } }

        val mapWidth = heightMap[0].size
        val mapHeight = heightMap.size

        val lowPoints = getLowPoints(heightMap)

        val basins = mutableListOf<Set<Position>>()
        lowPoints.forEach { lowPoint ->
            // DFS
            val basin = mutableSetOf<Position>()
            val discovered = mutableListOf(lowPoint)
            while (discovered.isNotEmpty()) {
                val position = discovered.removeLast()
                basin.add(position)
                discovered.addAll(getAdjacentPositions(position, mapHeight, mapWidth)
                    .filter { heightMap[it.row][it.col] < 9 }
                    .filter { !basin.contains(it) })
            }
            basins.add(basin)
        }

        return basins.sortedByDescending { it.size }.take(3).fold(1) { acc, basin -> acc * basin.size }
    }

    val testInput = readInput("inputs/Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("inputs/Day09")
    println(part1(input))
    println(part2(input))
}