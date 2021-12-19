import kotlin.math.abs
import kotlin.math.sqrt

data class Vector(val x: Int, val y: Int, val z: Int) {
    operator fun minus(that: Vector): Vector = Vector(x - that.x, y - that.y, z - that.z)
    operator fun plus(that: Vector): Vector = Vector(x + that.x, y + that.y, z + that.z)
    operator fun unaryMinus(): Vector {
        return Vector(-x, -y, -z)
    }

    fun manhattanDistance(other: Vector): Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    fun magnitude() = sqrt((x * x + y * y + z * z).toDouble())

    fun rotate(rotation: Int, inverse: Boolean = false): Vector {
        require(rotation in 0 until 24)
        var xyRot = rotation % 4
        var yzRot = if (rotation < 20) rotation / 4 % 4 else 0
        var xzRot = if (rotation < 20) rotation / 16 else 3
        if (inverse) {
            xyRot = (4 - xyRot) % 4
            yzRot = (4 - yzRot) % 4
            xzRot = (4 - xzRot) % 4
        }

        var current = this
        for (i in 0 until if (inverse) xzRot else xyRot)
            current = Vector(-current.y, current.x, current.z)
        for (i in 0 until yzRot)
            current = Vector(current.x, -current.z, current.y)
        for (i in 0 until if (inverse) xyRot else xzRot)
            current = Vector(-current.z, current.y, current.x)

        return current
    }
}

// Class representing a scanner object with rotation and position
// Beacons' coordinates and position are according to the reference plane
data class Scanner(
    val id: Int,
    val beacons: List<Vector>,
    val rotation: Int = 0,
    val position: Vector = Vector(0, 0, 0),
) {
    fun rotate(rotation: Int, inverse: Boolean = false): Scanner {
        require(rotation in 0 until 24)
        return Scanner(id, beacons.map { it.rotate(rotation, !inverse) }, rotation)
    }

    fun translate(translationVector: Vector, inverse: Boolean = false): Scanner {
        return if (inverse) Scanner(id, beacons.map { it + translationVector }, rotation, -translationVector)
        else Scanner(id, beacons.map { it - translationVector }, rotation, translationVector)
    }

    private fun getVectorPairs(): List<Pair<Vector, Vector>> {
        return this.beacons.flatMap { first ->
            this.beacons.mapNotNull { second ->
                if (first != second) Pair(first, second)
                else null
            }
        }
    }

    fun distanceVectors(): List<Vector> {
        val distances = mutableListOf<Vector>()
        beacons.forEach { first ->
            beacons.forEach { second ->
                if (first != second) distances.add(second - first)
            }
        }

        return distances
    }

    // List of pairs in each scanner which distances are equal
    fun commonPairs(other: Scanner): List<Pair<Pair<Vector, Vector>, Pair<Vector, Vector>>> {
        val thisPairs = this.getVectorPairs()
        val otherPairs = other.getVectorPairs()

        val commonPairs = mutableListOf<Pair<Pair<Vector, Vector>, Pair<Vector, Vector>>>()
        thisPairs.forEach { firstPair ->
            otherPairs.forEach { secondPair ->
                if (firstPair.second - firstPair.first == secondPair.second - secondPair.first) {
                    commonPairs.add(Pair(firstPair, secondPair))
                }
            }
        }
        return commonPairs
    }

    companion object {
        private val vectorRegex = Regex("""(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)""")
        fun parseAll(lines: List<String>): List<Scanner> {
            val beacons = mutableListOf<MutableList<Vector>>()

            lines.forEach {
                if (it.startsWith("---")) {
                    beacons.add(mutableListOf())
                } else {
                    val match = vectorRegex.matchEntire(it)
                    if (match != null) {
                        beacons.last().add(match.destructured.let { (x, y, z) ->
                            Vector(x.toInt(), y.toInt(), z.toInt())
                        })
                    }
                }
            }

            return beacons.mapIndexed { index, vectors ->
                Scanner(index, vectors)
            }
        }
    }
}

fun main() {
    val neededCommonPoints = 12

    fun positionScanners(input: List<String>): List<Scanner> {
        // Scanners to check everything
        val remainingScanners = Scanner.parseAll(input).toMutableList()
        // Scanners with known rotation and position
        val knownPlacementScanners = mutableListOf(remainingScanners.first())
        remainingScanners.removeFirst()
        // Scanners which cannot explore other scanners
        val fullyFinishedScanners = mutableListOf<Scanner>()

        while (remainingScanners.isNotEmpty()) {
            val newlyDiscoveredScanners = mutableListOf<Scanner>()
            val toRemoveScanners = mutableListOf<Scanner>()

            knownPlacementScanners.forEach { knownScanner ->
                val firstDistances = knownScanner.distanceVectors()
                remainingScanners.forEach { investigatedScanner ->
                    // If they have many same distances
                    for (rotation in 0 until 24) {
                        // Get rotation
                        val rotatedScanner = investigatedScanner.rotate(rotation)
                        val secondDistances = rotatedScanner.distanceVectors()
                        var commonDistances = 0
                        secondDistances.forEach {
                            if (it in firstDistances) commonDistances++
                        }
                        if (commonDistances >= neededCommonPoints * (neededCommonPoints - 1)) {
                            val commonPairs = knownScanner.commonPairs(rotatedScanner)
                            // Get position, deals with accidental common beacons
                            val translationVectorFrequencies = mutableMapOf<Vector, Int>()
                            commonPairs.forEach {
                                val translationVector = it.second.first - it.first.first
                                translationVectorFrequencies[translationVector] =
                                    translationVectorFrequencies.getOrPut(translationVector) { 0 } + 1
                            }
                            val translationVector = translationVectorFrequencies.maxByOrNull { it.value }!!.key
                            val newScanner = rotatedScanner.translate(translationVector)
                            toRemoveScanners.add(investigatedScanner)
                            newlyDiscoveredScanners.add(newScanner)
                            break
                        }
                    }
                }
                fullyFinishedScanners.add(knownScanner)
            }
            remainingScanners.removeAll(toRemoveScanners)
            knownPlacementScanners.removeAll(fullyFinishedScanners)
            knownPlacementScanners.addAll(newlyDiscoveredScanners)
        }

        fullyFinishedScanners.addAll(knownPlacementScanners)

        return fullyFinishedScanners
    }

    fun part1(input: List<String>): Int {
        val scanners = positionScanners(input)
        return scanners.flatMap { it.beacons }.distinct().size
    }

    fun part2(input: List<String>): Int {
        val scanners = positionScanners(input)
        var maxDistance = 0
        scanners.forEach { first ->
            scanners.forEach { second ->
                val distance = first.position.manhattanDistance(second.position)
                if (distance > maxDistance) maxDistance = distance
            }
        }

        return maxDistance
    }

    val testInput = readInput("inputs/Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("inputs/Day19")
    println(part1(input))
    println(part2(input))
}
