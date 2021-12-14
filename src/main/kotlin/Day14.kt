data class ElementPair(val first: Char, val second: Char)

data class PairInsertion(val elementPair: ElementPair, val insertion: Char) {
    companion object {
        private val regex = Regex("""([A-Z])([A-Z]) -> ([A-Z])""")
        fun parse(line: String): PairInsertion? {
            return regex.matchEntire(line)?.let {
                val (first, second, insertion) = it.destructured
                return PairInsertion(ElementPair(first.single(), second.single()), insertion.single())
            }
        }
    }
}

fun main() {
    fun getMap(
        elementPair: ElementPair,
        pairInsertions: List<PairInsertion>,
        step: Int,
        map: MutableMap<Pair<ElementPair, Int>, Map<Char, Long>>,
    ): Map<Char, Long> {
        return map.getOrPut(Pair(elementPair, step)) getOrPut@{
            val insertion =
                pairInsertions.singleOrNull { it.elementPair == elementPair } ?: return@getOrPut mutableMapOf()
            if (step == 1)
                return@getOrPut mutableMapOf(insertion.insertion to 1)

            val firstMap =
                getMap(ElementPair(elementPair.first, insertion.insertion), pairInsertions, step - 1, map)
            val secondMap =
                getMap(ElementPair(insertion.insertion, elementPair.second), pairInsertions, step - 1, map)

            val newMap = mutableMapOf<Char, Long>(insertion.insertion to 1)

            (firstMap.keys + secondMap.keys).forEach {
                newMap[it] = newMap.getOrDefault(it, 0) + firstMap.getOrDefault(it, 0) + secondMap.getOrDefault(it, 0)
            }
            return@getOrPut newMap
        }
    }


    fun part1(input: List<String>): Long {
        val template = input.first()
        val pairInsertions = input.mapNotNull { PairInsertion.parse(it) }

        val endMap = mutableMapOf<Char, Long>()
        template.forEach {
            endMap[it] = endMap.getOrPut(it) { 0L } + 1
        }

        val map = mutableMapOf<Pair<ElementPair, Int>, Map<Char, Long>>()

        template.windowed(2).forEach {
            val partialMap = getMap(ElementPair(it[0], it[1]), pairInsertions, 10, map)

            partialMap.forEach { (char, value) ->
                endMap[char] = endMap.getOrPut(char) { 0L } + value
            }
        }

        return endMap.maxOf { it.value } - endMap.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val pairInsertions = input.mapNotNull { PairInsertion.parse(it) }

        val endMap = mutableMapOf<Char, Long>()
        template.forEach {
            endMap[it] = endMap.getOrPut(it) { 0L } + 1
        }

        val map = mutableMapOf<Pair<ElementPair, Int>, Map<Char, Long>>()

        template.windowed(2).forEach {
            val partialMap = getMap(ElementPair(it[0], it[1]), pairInsertions, 40, map)

            partialMap.forEach { (char, value) ->
                endMap[char] = endMap.getOrPut(char) { 0L } + value
            }
        }

        return endMap.maxOf { it.value } - endMap.minOf { it.value }
    }

    val testInput = readInput("inputs/Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("inputs/Day14")
    println(part1(input))
    println(part2(input))
}