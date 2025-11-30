package utils

private const val RESET_CONSOLE_COLOR_ASCII = "\u001B[0m"

enum class ConsoleColor(
    val ascii: String,
) {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
}

/**
 * Prints the given [message] and the line separator to the standard output stream.
 * @param message The message to print
 * @param consoleColor The color in which to print the message
 */
fun println(
    message: Any,
    consoleColor: ConsoleColor,
) {
    println("${consoleColor.ascii}$message$RESET_CONSOLE_COLOR_ASCII")
}
