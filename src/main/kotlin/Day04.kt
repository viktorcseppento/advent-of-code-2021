data class BingoNumber(val num: Int, var checked: Boolean)

class BingoBoard(private val nums: Array<Array<BingoNumber>>) {
    fun checkWin(): Boolean {
        if (nums.any { row -> row.all { it.checked } })
            return true

        repeat(5) { i ->
            if (nums.map { row ->
                    row[i]
                }.all { it.checked })
                return true
        }

        return false
    }

    fun calcScore(lastCalled: Int): Int {
        return lastCalled * nums.flatten().filterNot { it.checked }.sumOf { it.num }
    }

    fun markBoard(calledNum: Int) {
        nums.forEach { row ->
            row.filter { it.num == calledNum }.forEach {
                it.checked = true
            }
        }
    }

    companion object {
        fun parse(block: List<String>): BingoBoard {
            val nums = block.map {
                it.split(" ").filter(String::isNotBlank).map {
                    BingoNumber(it.toInt(), false)
                }.toTypedArray()
            }.toTypedArray()
            return BingoBoard(nums)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val calledNums = input.first().split(',').toIntList()
        val boards = input
            .drop(1)
            .filter { it.isNotBlank() }
            .chunked(5)
            .map(BingoBoard::parse)
        calledNums.forEach { calledNum ->
            boards.forEach {
                it.markBoard(calledNum)
                if (it.checkWin())
                    return it.calcScore(calledNum)
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val calledNums = input.first().split(',').toIntList()
        val boards = input
            .drop(1)
            .filter { it.isNotBlank() }
            .chunked(5)
            .map(BingoBoard::parse)
            .toMutableList()

        calledNums.forEach { calledNum ->
            val toRemove = mutableListOf<BingoBoard>()
            boards.forEach {
                it.markBoard(calledNum)
                if (it.checkWin()) {
                    toRemove.add(it)
                    if (boards.size == 1)
                        return it.calcScore(calledNum)
                }
            }
            boards.removeAll(toRemove)
        }
        return 0
    }

    val testInput = readInput("inputs/Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("inputs/Day04")
    println(part1(input))
    println(part2(input))
}
