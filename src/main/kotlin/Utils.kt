import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun Char.hex2Binary(): String {
    return when (this.uppercase()) {
        "0" -> "0000"
        "1" -> "0001"
        "2" -> "0010"
        "3" -> "0011"
        "4" -> "0100"
        "5" -> "0101"
        "6" -> "0110"
        "7" -> "0111"
        "8" -> "1000"
        "9" -> "1001"
        "A" -> "1010"
        "B" -> "1011"
        "C" -> "1100"
        "D" -> "1101"
        "E" -> "1110"
        "F" -> "1111"
        else -> ""
    }
}

fun List<String>.toIntList() = this.map { it.toInt() }