data class GameState(val player1: Player, val player2: Player, val nextPlayer: Int) {
    val isDone: Boolean = isDone(21)

    private fun isDone(scoreLimit: Int): Boolean {
        return player1.score >= scoreLimit || player2.score >= scoreLimit
    }

    fun nextState(dieThrow: Int, boardSize: Int): GameState {
        return if (nextPlayer == 0) {
            GameState(player1.move(dieThrow, boardSize), player2, 1)
        } else {
            GameState(player1, player2.move(dieThrow, boardSize), 0)
        }
    }
}

data class Player(val position: Int, val score: Int = 0) {
    fun move(dieThrow: Int, boardSize: Int): Player {
        val newPosition = (position + dieThrow - 1) % boardSize + 1
        return Player(newPosition, score + newPosition)
    }

    companion object {
        private val regex = Regex("""Player \d+ starting position: (\d+)""")
        fun parse(line: String) = regex.matchEntire(line)!!.destructured.let { (position) ->
            Player(position.toInt())
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        fun castDeterministicDie(numberOfCasts: Int, dieSize: Int, num: Int): Int {
            var throwValue = 0
            var localNumberOfCasts = numberOfCasts
            for (i in 0 until num) {
                throwValue += (localNumberOfCasts++) % dieSize + 1
            }
            return throwValue
        }

        val players = input.map(Player::parse).toMutableList()
        val boardSize = 10
        val dieSize = 100
        var numberOfCasts = 0
        var currentPlayer = 0
        val scoreLimit = 1000
        while (players.all { it.score < scoreLimit }) {
            players[currentPlayer] =
                players[currentPlayer].move(castDeterministicDie(numberOfCasts, dieSize, 3), boardSize)
            numberOfCasts += 3
            currentPlayer = (currentPlayer + 1) % 2
        }
        return numberOfCasts * players.single { it.score < scoreLimit }.score
    }

    fun part2(input: List<String>): Long {
        val boardSize = 10

        // State - frequency map
        val map = mutableMapOf<GameState, Long>()
        val players = input.map(Player::parse)
        map[GameState(players[0], players[1], 0)] = 1

        // 27 new states, 7 distinct ones
        // Throws and occurrences
        // 3-1, 4-3, 5-6, 6-7, 7-6, 8-3, 9-1
        val possibleThrowOccurrences = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        var currentEntry = map.entries.firstOrNull()
        while (currentEntry != null) {
            val oldState = currentEntry.key
            val oldStateFrequency = currentEntry.value
            map.remove(oldState)

            possibleThrowOccurrences.forEach { dieThrow ->
                val nextState = oldState.nextState(dieThrow.first, boardSize)
                map[nextState] = map.getOrDefault(nextState, 0L) + oldStateFrequency * dieThrow.second
            }

            currentEntry = map.entries.firstOrNull { !it.key.isDone }
        }

        val firstPlayerWins = map.filter { it.key.nextPlayer == 0 }.values.sum()
        val secondPlayerWins = map.filter { it.key.nextPlayer == 1 }.values.sum()
        return if (firstPlayerWins > secondPlayerWins) firstPlayerWins else secondPlayerWins
    }

    val testInput = readInput("inputs/Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("inputs/Day21")
    println(part1(input))
    println(part2(input))
}
