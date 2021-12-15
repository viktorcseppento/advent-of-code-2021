data class Point(val x: Int, val y: Int)

data class Position(val row: Int, val col: Int)

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