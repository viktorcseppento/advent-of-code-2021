data class Connection(val start: String, val end: String) {
    companion object {
        fun parse(line: String): Connection {
            return Connection(line.substringBefore('-'), line.substringAfter('-'))
        }
    }
}

data class Path(
    val connections: MutableList<Connection>,
    val smallCaves: MutableList<String>,
)

fun main() {
    fun getConnections(input: List<String>): List<Connection> {
        val connections = input.map { Connection.parse(it) }.toMutableList()

        val newConnections = mutableListOf<Connection>()
        connections.forEach {
            if (it.start != "start" && it.end != "end")
                newConnections.add(Connection(it.end, it.start))
        }
        connections.addAll(newConnections)

        return connections
    }

    fun String.isLowerCase(): Boolean {
        return this.all { it.isLowerCase() }
    }

    fun getPaths(connections: List<Connection>, twiceVisitableCave: String): List<Path> {
        val startedPaths = mutableListOf<Path>()
        val completedPaths = mutableListOf<Path>()

        startedPaths.add(Path(mutableListOf(Connection("start", "start")), mutableListOf("start")))

        while (startedPaths.isNotEmpty()) {
            val path = startedPaths.removeFirst()
            val lastCave = path.connections.last().end
            connections.filter { it.start == lastCave }.forEach { connection ->
                if (connection.end.isLowerCase()) {
                    val count = path.smallCaves.count { it == connection.end }
                    if (count > 1 || (count == 1 && twiceVisitableCave != connection.end))
                        return@forEach
                }

                val newPath = Path(mutableListOf(), mutableListOf())
                path.connections.forEach {
                    newPath.connections.add(it)
                }
                path.smallCaves.forEach {
                    newPath.smallCaves.add(it)
                }
                newPath.connections.add(connection)
                if (connection.end.isLowerCase())
                    newPath.smallCaves.add(connection.end)
                if (connection.end == "end") {
                    completedPaths.add(newPath)
                } else {
                    startedPaths.add(newPath)
                }
            }
        }

        return completedPaths
    }

    fun part1(input: List<String>): Int {
        val connections = getConnections(input)

        return getPaths(connections, "").size
    }

    fun part2(input: List<String>): Int {
        val connections = getConnections(input)

        val twiceVisitableSmallCaves = connections.map { it.start }.distinct() // All caves
            .filter { it.isLowerCase() && it != "start" && it != "end" } // Small caves

        val paths = mutableListOf<Path>()

        paths.addAll(getPaths(connections, ""))
        twiceVisitableSmallCaves.forEach {
            paths.addAll(getPaths(connections, it))
        }

        return paths.distinctBy { it.connections }.size
    }

    val testInput = readInput("inputs/Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("inputs/Day12")
    println(part1(input))
    println(part2(input))
}
