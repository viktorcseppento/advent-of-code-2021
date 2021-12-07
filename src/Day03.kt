import kotlin.math.pow

enum class LifeSupportComponents {
    OXYGEN, CO2
}

fun main() {
    fun part1(input: List<String>): Int {
        val bitWidth = input.first().length
        val numberOfOnes = IntArray(bitWidth) // Number of ones in each position
        input.forEach {
            it.forEachIndexed { i, digit ->
                if (digit == '1')
                    numberOfOnes[i]++
            }
        }

        val gammaRate = numberOfOnes.map { if (it >= input.size / 2.0) '1' else '0' }
            .joinToString("").toInt(2)

        // Invert bits and mask off higher bits
        val epsilonRate = gammaRate.inv() and (2.0.pow(bitWidth).toInt() - 1)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        fun getLifeSupportComponentRating(
            input: List<String>, neededRating: LifeSupportComponents,
        ): Int {
            val currentList = input.toMutableList()

            currentList.indices.forEach { i ->
                val numberOfOnes = currentList.count { (it[i] == '1') }

                val neededDigit = when (neededRating) {
                    LifeSupportComponents.OXYGEN -> if (numberOfOnes >= currentList.size / 2.0) '1' else '0'
                    LifeSupportComponents.CO2 -> if (numberOfOnes < currentList.size / 2.0) '1' else '0'
                }

                currentList.retainAll { it[i] == neededDigit }

                if (currentList.size == 1)
                    return currentList.first().toInt(2)
            }
            return 0
        }

        val oxygenGeneratorRating = getLifeSupportComponentRating(input, LifeSupportComponents.OXYGEN)
        val co2ScrubberRating = getLifeSupportComponentRating(input, LifeSupportComponents.CO2)

        return oxygenGeneratorRating * co2ScrubberRating
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
