class SubmarineMovement(val direction: String, val value: Int) {
    companion object {
        private val regex = Regex("""([a-z]+) ([0-9]+)""")
        fun parse(line: String): SubmarineMovement =
            regex.matchEntire(line)!!.destructured.let { (direction, value) ->
                SubmarineMovement(direction, value.toInt())
            }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val movementList = input.map(SubmarineMovement::parse)
        var pos = 0
        var depth = 0
        movementList.forEach {
            when (it.direction) {
                "forward" -> pos += it.value
                "up" -> depth -= it.value
                "down" -> depth += it.value
            }
        }

        return pos * depth
    }

    fun part2(input: List<String>): Int {
        val movementList = input.map(SubmarineMovement::parse)
        var pos = 0
        var depth = 0
        var aim = 0
        movementList.forEach {
            when (it.direction) {
                "forward" -> {
                    pos += it.value
                    depth += aim * it.value
                }
                "up" -> aim -= it.value
                "down" -> aim += it.value
            }
        }

        return pos * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}