fun main() {
    fun getNeighbourPositions(position: Position, mapHeight: Int, mapWidth: Int): List<Position> {
        val positions = mutableListOf<Position>()
        if (position.row != 0) {
            positions.add(Position(position.row - 1, position.col))
            if (position.col != 0)
                positions.add(Position(position.row - 1, position.col - 1))
            if (position.col != mapWidth - 1)
                positions.add(Position(position.row - 1, position.col + 1))
        }

        if (position.row != mapHeight - 1) {
            positions.add(Position(position.row + 1, position.col))
            if (position.col != 0)
                positions.add(Position(position.row + 1, position.col - 1))
            if (position.col != mapWidth - 1)
                positions.add(Position(position.row + 1, position.col + 1))
        }

        if (position.col != 0)
            positions.add(Position(position.row, position.col - 1))

        if (position.col != mapWidth - 1)
            positions.add(Position(position.row, position.col + 1))

        return positions
    }

    fun doStep(map: List<MutableList<Int>>): Set<Position> {
        val toFlashPositions = mutableListOf<Position>()

        val mapWidth = map[0].size
        val mapHeight = map.size

        for (i in 0 until mapHeight) {
            for (j in 0 until mapWidth) {
                if (++map[i][j] > 9)
                    toFlashPositions.add(Position(i, j))
            }
        }

        val flashedPositions = mutableSetOf<Position>()
        while (toFlashPositions.isNotEmpty()) {
            val position = toFlashPositions.removeFirst()
            val neighbors = getNeighbourPositions(position, mapHeight, mapWidth)
            neighbors.forEach {
                if (++map[it.row][it.col] > 9 && !toFlashPositions.contains(it) && !flashedPositions.contains(it))
                    toFlashPositions.add(it)
            }
            flashedPositions.add(position)
        }

        return flashedPositions
    }

    fun part1(input: List<String>): Int {
        val map = input.map { line -> line.map { it.digitToInt() }.toMutableList() }

        var flashes = 0
        for (step in 0 until 100) {
            val flashedPositions = doStep(map)

            flashedPositions.forEach {
                flashes++
                map[it.row][it.col] = 0
            }
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val map = input.map { line -> line.map { it.digitToInt() }.toMutableList() }

        val mapWidth = map[0].size
        val mapHeight = map.size

        var step = 0
        while (true) {
            step++

            val flashedPositions = doStep(map)
            flashedPositions.forEach {
                map[it.row][it.col] = 0
            }

            if (flashedPositions.size == mapHeight * mapWidth)
                return step
        }
    }

    val testInput = readInput("inputs/Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("inputs/Day11")
    println(part1(input))
    println(part2(input))
}
