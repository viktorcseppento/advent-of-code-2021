fun main() {
    fun Char.isOpening(): Boolean {
        return this in "([{<"
    }

    fun Char.openingPair(): Char {
        return when (this) {
            ')' -> '('
            ']' -> '['
            '}' -> '{'
            '>' -> '<'
            else -> throw IllegalArgumentException()
        }
    }

    fun Char.getCorruptPoint(): Int {
        return when (this) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    fun Char.getIncompletePoint(): Int {
        return when (this) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> 0
        }
    }

    fun part1(input: List<String>): Int {
        val illegalChars = mutableListOf<Char>()
        input.forEach lines@{ line ->
            val charStack = ArrayDeque<Char>()
            line.forEach chars@{
                if (it.isOpening()) {
                    charStack.addLast(it)
                } else {
                    if (charStack.last() == it.openingPair()) {
                        charStack.removeLast()
                    } else {
                        illegalChars.add(it)
                        return@lines
                    }
                }
            }
        }
        return illegalChars.sumOf { it.getCorruptPoint() }
    }

    fun part2(input: List<String>): Long {
        val points = mutableListOf<Long>()
        input.forEach lines@{ line ->
            val charStack = ArrayDeque<Char>()
            line.forEach chars@{
                if (it.isOpening()) {
                    charStack.addLast(it)
                } else {
                    if (charStack.last() == it.openingPair()) {
                        charStack.removeLast()
                    } else {
                        return@lines
                    }
                }
            }

            points.add(charStack.foldRight(0L) { char, acc ->
                5 * acc + char.getIncompletePoint()
            })
        }
        return points.sorted()[points.size / 2]
    }

    val testInput = readInput("inputs/Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("inputs/Day10")
    println(part1(input))
    println(part2(input))
}
