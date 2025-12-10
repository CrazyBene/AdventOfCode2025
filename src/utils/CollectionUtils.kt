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

fun <T> List<T>.combinations(size: Int): Sequence<List<T>> =
    when (size) {
        0 -> {
            emptySequence()
        }

        1 -> {
            asSequence().map { listOf(it) }
        }

        else -> {
            sequence {
                this@combinations.forEachIndexed { index, element ->
                    val head = listOf(element)
                    val tail = this@combinations.subList(index + 1, this@combinations.size)
                    tail.combinations(size - 1).forEach { tailCombination ->
                        yield(head + tailCombination)
                    }
                }
            }
        }
    }

fun <T> List<T>.allCombinations(): Sequence<List<T>> =
    (0..this.count())
        .map {
            this.combinations(
                it,
            )
        }.fold(emptySequence<List<T>>()) { acc, sequence ->
            acc + sequence
        }
