sealed class SnailfishNumber(var left: SnailfishNumber?, var right: SnailfishNumber?) {
    abstract fun magnitude(): Int

    operator fun plus(that: SnailfishNumber): SnailfishNumber {
        return SnailfishPair(this.copy(), that.copy()).reduce()
    }

    protected fun reduce(): SnailfishNumber {
        val levelLimit = 4

        val identifierNumberMap = getMap("", mutableMapOf())

        var nestedPair: Map.Entry<String, SnailfishNumber>? = null
        var splittingValue: Map.Entry<String, SnailfishNumber>? = null

        for (entry in identifierNumberMap) {
            if (entry.key.length >= levelLimit &&
                entry.value.left is SnailfishRegularValue && entry.value.right is SnailfishRegularValue
            ) {
                nestedPair = entry
                break
            }
            if (splittingValue == null && entry.value is SnailfishRegularValue && (entry.value as SnailfishRegularValue).value >= 10) {
                splittingValue = entry
            }
        }

        // Exploding
        if (nestedPair != null) {
            val nestedId = nestedPair.key
            val parent = identifierNumberMap.getValue(nestedId.substring(0, nestedId.length - 1))

            var leftRegularValue: SnailfishRegularValue? = null
            var rightRegularValue: SnailfishRegularValue? = null
            identifierNumberMap.forEach { (id, number) ->
                if (number is SnailfishRegularValue) {
                    if (id < nestedId)
                        leftRegularValue = number
                    if (rightRegularValue == null && id > nestedId + '1')
                        rightRegularValue = number
                }
            }

            if (nestedId.last() == '0') {
                parent.left = SnailfishRegularValue(0)
            } else {
                parent.right = SnailfishRegularValue(0)
            }
            leftRegularValue?.let { it.value += (nestedPair.value.left as SnailfishRegularValue).value }
            rightRegularValue?.let { it.value += (nestedPair.value.right as SnailfishRegularValue).value }

            return reduce()
        }

        // Splitting
        if (splittingValue != null) {
            val splittingId = splittingValue.key
            val parent = identifierNumberMap.getValue(splittingId.substring(0, splittingId.length - 1))
            val value = (splittingValue.value as SnailfishRegularValue).value
            val newPair = SnailfishPair(SnailfishRegularValue(value / 2), SnailfishRegularValue(value - value / 2))
            if (splittingId.last() == '0') {
                parent.left = newPair
            } else {
                parent.right = newPair
            }
            return reduce()
        }
        return this
    }

    abstract fun copy(): SnailfishNumber

    abstract fun getMap(identifier: String, map: MutableMap<String, SnailfishNumber>): Map<String, SnailfishNumber>

    companion object {
        fun parse(string: String): SnailfishNumber {
            if (string[0] == '[') {
                var level = 0
                string.forEachIndexed { index, c ->
                    if (c == ',' && level == 1) {
                        return SnailfishPair(parse(string.substring(1, index)),
                            parse(string.substring(index + 1, string.length - 1)))
                    } else if (c == '[') {
                        level++
                    } else if (c == ']')
                        level--
                }
            } else {
                return SnailfishRegularValue(string.toInt())
            }
            throw IllegalStateException("Cannot reach here")
        }
    }
}

class SnailfishPair(left: SnailfishNumber, right: SnailfishNumber) : SnailfishNumber(left, right) {
    override fun magnitude(): Int {
        return 3 * left!!.magnitude() + 2 * right!!.magnitude()
    }

    override fun getMap(
        identifier: String,
        map: MutableMap<String, SnailfishNumber>,
    ): Map<String, SnailfishNumber> {
        map[identifier] = this
        this.left!!.getMap(identifier + '0', map)
        this.right!!.getMap(identifier + '1', map)
        return map
    }

    override fun copy(): SnailfishNumber {
        return SnailfishPair(left!!.copy(), right!!.copy())
    }

    override fun toString(): String {
        return "[${left!!},${right!!}]"
    }
}

class SnailfishRegularValue(var value: Int) : SnailfishNumber(null, null) {
    override fun magnitude(): Int {
        return value
    }

    override fun getMap(
        identifier: String,
        map: MutableMap<String, SnailfishNumber>,
    ): Map<String, SnailfishNumber> {
        map[identifier] = this
        return map
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun copy(): SnailfishNumber {
        return SnailfishRegularValue(value)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val nums = input.map(SnailfishNumber::parse)

        return nums.reduce { acc, snailfishNumber -> acc + snailfishNumber }.magnitude()
    }

    fun part2(input: List<String>): Int {
        var largestMagnitude = 0
        val nums = input.map(SnailfishNumber::parse)
        nums.forEach { first ->
            nums.forEach { second ->
                val magnitude = (first + second).magnitude()
                if (magnitude > largestMagnitude)
                    largestMagnitude = magnitude
            }
        }
        return largestMagnitude
    }

    val testInput = readInput("inputs/Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("inputs/Day18")
    println(part1(input))
    println(part2(input))
}
