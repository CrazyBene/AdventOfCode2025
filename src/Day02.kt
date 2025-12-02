fun main() = Day02.run(RunMode.BOTH)

object Day02 : BasicDay() {
    override val expectedTestValuePart1 = 1227775554L
    override val expectedTestValuePart2 = 4174379265L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val idRanges = parseToRanges(input.first())

        idRanges
            .flatMap { idRange ->
                idRange.filter { it.isPatternRepeatingTwice() }
            }.sum()
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val idRanges = parseToRanges(input.first())

        idRanges
            .flatMap { idRange ->
                idRange.filter { it.isAnyPatternRepeating() }
            }.sum()
    }

    fun parseToRanges(line: String) =
        line.split(',').map { range ->
            range.split('-').map { it.toLong() }.let { (start, end) ->
                start..end
            }
        }

    fun Long.isPatternRepeatingTwice(): Boolean {
        val asString = this.toString()
        val halfLengthFixed = (asString.length + 1) / 2
        return asString.take(halfLengthFixed) == asString.drop(halfLengthFixed)
    }

    fun Long.isAnyPatternRepeating(): Boolean {
        val asString = this.toString()

        for (i in 1..asString.length / 2) {
            if (asString.chunked(i).let { chunks ->
                    chunks.all { it == chunks.first() }
                }
            ) {
                return true
            }
        }

        return false
    }
}
