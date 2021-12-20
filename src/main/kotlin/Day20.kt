fun main() {
    fun padImage(input: List<String>, padding: Int): List<String> {
        val inputWidth = input.first().length
        val padded = mutableListOf<String>()
        repeat(padding) {
            padded.add("0".repeat(inputWidth + 2 * padding))
        }
        input.forEach { line ->
            padded.add(buildString {
                append("0".repeat(padding))
                append(line)
                append("0".repeat(padding))
            })
        }
        repeat(padding) {
            padded.add("0".repeat(inputWidth + 2 * padding))
        }

        return padded
    }

    fun getDigit(image: List<String>, row: Int, col: Int, algorithm: String): Char {
        val binaryString = buildString {
            append(image[row - 1].substring(col - 1..col + 1))
            append(image[row].substring(col - 1..col + 1))
            append(image[row + 1].substring(col - 1..col + 1))
        }
        return algorithm[binaryString.toInt(2)]
    }

    fun transform(input: List<String>, algorithm: String): List<String> {
        val inputWidth = input.first().length
        val inputHeight = input.size

        val output = mutableListOf<String>()
        for (i in 1 until inputHeight - 1) {
            output.add(buildString {
                for (j in 1 until inputWidth - 1) {
                    append(getDigit(input, i, j, algorithm))
                }
            })
        }

        return output
    }

    fun readImage(imageLines: List<String>): List<String> {
        val image = mutableListOf<String>()
        imageLines.forEach { line ->
            image.add(buildString {
                line.forEach { char ->
                    append(if (char == '.') '0' else '1')
                }
            })
        }
        return image
    }

    fun part1(input: List<String>): Int {
        val algorithm = input.first().replace('.', '0').replace('#', '1')
        val inputImage = readImage(input.drop(2))

        val padded = padImage(inputImage, 4)
        val output = transform(transform(padded, algorithm), algorithm)
        return output.fold(0) { acc, line ->
            acc + line.count { it == '1' }
        }
    }

    fun part2(input: List<String>): Int {
        val algorithm = input.first().replace('.', '0').replace('#', '1')
        val inputImage = readImage(input.drop(2))

        var current = padImage(inputImage, 100)
        repeat(50) {
            current = transform(current, algorithm)
        }

        return current.fold(0) { acc, line ->
            acc + line.count { it == '1' }
        }
    }

    val testInput = readInput("inputs/Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("inputs/Day20")
    println(part1(input))
    println(part2(input))
}