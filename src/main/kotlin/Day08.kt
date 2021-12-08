data class SegmentsEntry(val patterns: List<String>, val outputs: List<String>) {
    companion object {
        fun parse(line: String): SegmentsEntry = SegmentsEntry(
            line.substringBefore('|').trim().split(' '),
            line.substringAfter('|').trim().split(' '))
    }
}

fun main() {
    val originalNumberSegments = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9
    )


    // Custom segment to original segment
    fun createSegmentMapping(entry: SegmentsEntry): Map<Char, Char> {
        val segmentMapping = mutableMapOf<Char, Char>()
        val sevenSegments = entry.patterns.first { it.length == 3 }.toSet()
        val oneSegments = entry.patterns.first { it.length == 2 }.toSet()
        val fourSegments = entry.patterns.first { it.length == 4 }.toSet()
        val sixLengthSegments = entry.patterns.filter { it.length == 6 }.flatMap { it.toList() }
        val fiveLengthSegments = entry.patterns.filter { it.length == 5 }.flatMap { it.toList() }
        segmentMapping['a'] = sevenSegments.subtract(oneSegments).first()
        segmentMapping['f'] =
            oneSegments.subtract(sixLengthSegments.groupBy { it }.filter { it.value.size == 2 }.keys).first()
        segmentMapping['c'] = oneSegments.minus(segmentMapping['f']!!).first()
        segmentMapping['e'] =
            fiveLengthSegments.groupBy { it }.filter { it.value.size == 1 }.keys.subtract(fourSegments).first()
        segmentMapping['b'] =
            fiveLengthSegments.groupBy { it }.filter { it.value.size == 1 }.keys.minus(segmentMapping['e']!!).first()
        val remaining = ('a'..'g').subtract(segmentMapping.values.toSet())
        segmentMapping['g'] = remaining.subtract(fourSegments).first()
        segmentMapping['d'] = remaining.intersect(fourSegments).first()

        // Reverse map
        return segmentMapping.entries.associateBy({ it.value }, { it.key })
    }


    fun segmentsToNumber(segmentMapping: Map<Char, Char>, segments: String): Int {
        return originalNumberSegments.getValue(
            segments.map { segmentMapping.getValue(it) }
                .sorted()
                .joinToString("")
        )
    }

    fun part1(input: List<String>): Int {
        return input.flatMap { SegmentsEntry.parse(it).outputs }
            .count { it.length in intArrayOf(2, 3, 4, 7) }
    }

    fun part2(input: List<String>): Int {
        val entries = input.map(SegmentsEntry.Companion::parse)

        var sum = 0

        for (entry in entries) {
            val segmentMapping = createSegmentMapping(entry)
            var output = 0
            entry.outputs.forEach {
                output = 10 * output + segmentsToNumber(segmentMapping, it)
            }
            sum += output
        }
        return sum
    }

    val testInput = readInput("inputs/Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("inputs/Day08")
    println(part1(input))
    println(part2(input))
}
