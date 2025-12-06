package utils

inline fun <reified T> List<T>.split(splitFunction: (T) -> Boolean): List<List<T>> {
    val chunks = mutableListOf<List<T>>()
    var currentList = mutableListOf<T>()
    this.forEach { line ->
        if (splitFunction(line)) {
            chunks.add(currentList)
            currentList = mutableListOf()
        } else {
            currentList.add(line)
        }
    }
    chunks.add(currentList)

    return chunks
}
