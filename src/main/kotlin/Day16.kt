sealed class Packet(val length: Int, val version: Int, val typeId: Int, val value: Long, val subPackets: List<Packet>)

class LiteralPacket(length: Int, version: Int, typeId: Int, value: Long) :
    Packet(length, version, typeId, value, emptyList())

class OperatorPacket(length: Int, version: Int, typeId: Int, value: Long, subPackets: List<Packet>) :
    Packet(length, version, typeId, value, subPackets)

fun main() {
    fun parseNextPacket(binaryString: String, startingPosition: Int): Packet {
        var currentPosition = startingPosition
        val version = binaryString.substring(currentPosition until currentPosition + 3).toInt(2)
        currentPosition += 3
        val typeId = binaryString.substring(currentPosition until currentPosition + 3).toInt(2)
        currentPosition += 3

        // Literal
        if (typeId == 4) {
            var lastGroup = false
            val literalValue = StringBuilder()
            while (!lastGroup) {
                val prefix = binaryString[currentPosition++]
                if (prefix == '0') {
                    lastGroup = true
                }
                literalValue.append(binaryString.substring(currentPosition until currentPosition + 4))
                currentPosition += 4
            }

            val packetLength = currentPosition - startingPosition
            val value = literalValue.toString().toLong(2)

            return LiteralPacket(packetLength, version, typeId, value)
        } else {
            val lengthTypeId = binaryString[currentPosition++]

            val subPackets = mutableListOf<Packet>()
            var parsedLengths = 0
            if (lengthTypeId == '0') {
                val totalSubPacketsLength = binaryString.substring(currentPosition until currentPosition + 15).toInt(2)
                currentPosition += 15
                while (parsedLengths < totalSubPacketsLength) {
                    val nextPacket = parseNextPacket(binaryString, currentPosition + parsedLengths)
                    parsedLengths += nextPacket.length
                    subPackets.add(nextPacket)
                }

            } else if (lengthTypeId == '1') {
                val subPacketsNumber = binaryString.substring(currentPosition until currentPosition + 11).toInt(2)
                currentPosition += 11
                for (i in 0 until subPacketsNumber) {
                    val nextPacket = parseNextPacket(binaryString, currentPosition + parsedLengths)
                    parsedLengths += nextPacket.length
                    subPackets.add(nextPacket)
                }

            } else {
                throw IllegalStateException("Invalid length type id")
            }

            val value = when (typeId) {
                0 -> subPackets.sumOf { it.value }
                1 -> subPackets.fold(1L) { acc, packet -> acc * packet.value }
                2 -> subPackets.minOf { it.value }
                3 -> subPackets.maxOf { it.value }
                5 -> if (subPackets[0].value > subPackets[1].value) 1L else 0L
                6 -> if (subPackets[0].value < subPackets[1].value) 1L else 0L
                7 -> if (subPackets[0].value == subPackets[1].value) 1L else 0L
                else -> {
                    throw IllegalStateException("Invalid type id")
                }
            }

            val packetLength = currentPosition - startingPosition + parsedLengths
            return OperatorPacket(packetLength, version, typeId, value, subPackets)
        }
    }

    fun calcVersionNumbers(packet: Packet): Int {
        var versionNumbers = packet.version
        packet.subPackets.forEach {
            versionNumbers += calcVersionNumbers(it)
        }
        return versionNumbers
    }

    fun part1(input: List<String>): Int {
        val hexLine = input.single()
        val binaryString = buildString { hexLine.forEach { append(it.hex2Binary()) } }
        val packet = parseNextPacket(binaryString, 0)

        return calcVersionNumbers(packet)
    }

    fun part2(input: List<String>): Long {
        val hexLine = input.first()
        val binaryString = buildString { hexLine.forEach { append(it.hex2Binary()) } }
        val packet = parseNextPacket(binaryString, 0)

        return packet.value
    }

    val testInput = readInput("inputs/Day16_test")
    check(part1(testInput) == 20)
    check(part2(testInput) == 1L)

    val input = readInput("inputs/Day16")
    println(part1(input))
    println(part2(input))
}
