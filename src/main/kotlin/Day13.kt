data class Fold(val axis: Char, val value: Int)

fun main() {
    fun getNewPoints(points: List<Point>, fold: Fold): List<Point> {
        val newPoints = mutableListOf<Point>()
        points.forEach { point ->
            if (fold.axis == 'x') {
                if (point.x > fold.value) {
                    newPoints.add(Point(2 * fold.value - point.x, point.y))
                } else {
                    newPoints.add(point)
                }
            } else {
                if (point.y > fold.value) {
                    newPoints.add(Point(point.x, 2 * fold.value - point.y))
                } else {
                    newPoints.add(point)
                }
            }
        }

        return newPoints.distinct()
    }

    fun part1(input: List<String>): Int {
        val points = input.filter { it.matches(Regex("""(\d)+,(\d)+""")) }
            .map { Point(it.substringBefore(',').toInt(), it.substringAfter(',').toInt()) }

        val firstFold = input.filter { it.startsWith("fold along ") }
            .map { Fold(it.substringBefore('=').last(), it.substringAfter('=').toInt()) }.first()

        return getNewPoints(points, firstFold).size
    }

    fun part2(input: List<String>): Int {
        var points = input.filter { it.matches(Regex("""(\d)+,(\d)+""")) }
            .map { Point(it.substringBefore(',').toInt(), it.substringAfter(',').toInt()) }

        val folds = input.filter { it.startsWith("fold along ") }
            .map { Fold(it.substringBefore('=').last(), it.substringAfter('=').toInt()) }

        folds.forEach { fold ->
            points = getNewPoints(points, fold)
        }

        for (i in 0..points.maxOf { it.y }) {
            for (j in 0..points.maxOf { it.x }) {
                if (points.contains(Point(j, i)))
                    print('#')
                else
                    print(' ')
            }
            println()
        }
        return 0
    }

    val testInput = readInput("inputs/Day13_test")
    check(part1(testInput) == 17)
    part2(testInput)

    val input = readInput("inputs/Day13")
    println(part1(input))
    part2(input)
}
