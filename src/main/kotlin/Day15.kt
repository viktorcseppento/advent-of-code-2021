import java.util.*

fun main() {
    // Alt functions use primitives, they are faster
    fun getAdjacentPositionsAlt(position: Int, mapHeight: Int, mapWidth: Int): List<Int> {
        val adjacentPositions = mutableListOf<Int>()

        if (position >= mapWidth)
            adjacentPositions.add(position - mapWidth)
        if (position < (mapHeight - 1) * mapWidth)
            adjacentPositions.add(position + mapWidth)
        if (position % mapWidth != 0)
            adjacentPositions.add(position - 1)
        if (position % mapWidth != mapWidth - 1)
            adjacentPositions.add(position + 1)

        return adjacentPositions
    }

    fun dijkstraAlt(riskLevels: IntArray, height: Int, width: Int, startPosition: Int): Map<Int, Int> {
        val distanceMap = mutableMapOf<Int, Int>()
        val unvisited =
            PriorityQueue<Int>(height * width) { o1, o2 -> distanceMap[o1]!! - distanceMap[o2]!! }
        for (i in 0 until height) {
            for (j in 0 until width) {
                val position = i * width + j
                if (position == startPosition)
                    distanceMap[position] = 0
                else
                    distanceMap[position] = Int.MAX_VALUE
                unvisited.add(position)
            }
        }

        while (unvisited.isNotEmpty()) {
            val position = unvisited.poll()
            val adjacentPositions = getAdjacentPositionsAlt(position, height, width)
            adjacentPositions.forEach { adjacent ->
                val newDistance = distanceMap[position]!! + riskLevels[adjacent]
                if (newDistance < distanceMap[adjacent]!!) {
                    distanceMap[adjacent] = newDistance
                    unvisited.remove(adjacent)
                    unvisited.add(adjacent)
                }
            }
        }

        return distanceMap
    }

    fun part2Alt(input: List<String>): Int {
        val repetitions = 5
        val tileHeight = input.size
        val tileWidth = input[0].length
        val riskLevels = IntArray(repetitions * tileHeight * repetitions * tileHeight) { 0 }
        for (i in 0 until repetitions * tileHeight) {
            for (j in 0 until repetitions * tileWidth) {
                val value = if (i < tileHeight && j < tileWidth)
                    input[i][j].digitToInt()
                else if (j >= tileWidth) {
                    riskLevels[i * repetitions * tileWidth + j - tileWidth] + 1
                } else {
                    riskLevels[(i - tileHeight) * tileWidth * repetitions + j] + 1
                }
                riskLevels[i * repetitions * tileWidth + j] = if (value <= 9) value else 1
            }
        }

        val startPosition = 0
        val endPosition = (repetitions * tileHeight - 1) * tileWidth * repetitions + repetitions * tileWidth - 1

        val distanceMap = dijkstraAlt(riskLevels, repetitions * tileHeight, repetitions * tileWidth, startPosition)

        return distanceMap[endPosition]!!
    }


    fun dijkstra(riskLevels: Array<IntArray>, startPosition: Position): Map<Position, Int> {
        val height = riskLevels.size
        val width = riskLevels[0].size

        val distanceMap = mutableMapOf<Position, Int>()
        val unvisited =
            PriorityQueue<Position>(height * width) { o1, o2 -> distanceMap[o1]!! - distanceMap[o2]!! }
        for (i in 0 until height) {
            for (j in 0 until width) {
                val position = Position(i, j)
                if (position == startPosition)
                    distanceMap[position] = 0
                else
                    distanceMap[position] = Int.MAX_VALUE
                unvisited.add(position)
            }
        }

        while (unvisited.isNotEmpty()) {
            val position = unvisited.poll()
            val adjacentPositions = getAdjacentPositions(position, height, width)
            adjacentPositions.forEach { adjacent ->
                val newDistance = distanceMap[position]!! + riskLevels[adjacent.row][adjacent.col]
                if (newDistance < distanceMap[adjacent]!!) {
                    distanceMap[adjacent] = newDistance
                    unvisited.remove(adjacent)
                    unvisited.add(adjacent)
                }
            }
        }

        return distanceMap
    }

    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val riskLevels = Array(height) { IntArray(width) { 0 } }
        for (i in 0 until height) {
            for (j in 0 until width) {
                riskLevels[i][j] = input[i][j].digitToInt()
            }
        }

        val startPosition = Position(0, 0)
        val endPosition = Position(height - 1, width - 1)

        val distanceMap = dijkstra(riskLevels, startPosition)

        return distanceMap[endPosition]!!
    }

    fun part2(input: List<String>): Int {
        val repetitions = 5
        val tileHeight = input.size
        val tileWidth = input[0].length
        val riskLevels = Array(repetitions * tileHeight) { IntArray(repetitions * tileWidth) { 0 } }
        for (i in 0 until repetitions * tileHeight) {
            for (j in 0 until repetitions * tileWidth) {
                val value = if (i < tileHeight && j < tileWidth)
                    input[i][j].digitToInt()
                else if (j >= tileWidth) {
                    riskLevels[i][j - tileWidth] + 1
                } else {
                    riskLevels[i - tileHeight][j] + 1
                }
                riskLevels[i][j] = if (value <= 9) value else 1
            }
        }

        val startPosition = Position(0, 0)
        val endPosition = Position(repetitions * tileHeight - 1, repetitions * tileWidth - 1)

        val distanceMap = dijkstra(riskLevels, startPosition)

        return distanceMap[endPosition]!!
    }

    val testInput = readInput("inputs/Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("inputs/Day15")
    println(part1(input))
    println(part2Alt(input))
}
