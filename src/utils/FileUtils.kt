package utils

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * Read file from the input directory and return its lines as a List.
 * @param fileName The name of the file to be read, relative to eh resources root folder
 * @return A list of strings when the file is found.
 *         If the file is not found, it will return null instead.
 */
fun readInputFile(fileName: String): List<String>? {
    val path = Path("input/$fileName")
    return if (path.exists()) path.readText().trim().lines() else null
}
