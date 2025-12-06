import utils.split
import utils.transpose

fun main() = Day06.run(RunMode.BOTH)

object Day06 : BasicDay() {
    override val expectedTestValuePart1 = 4277556L
    override val expectedTestValuePart2 = 3263827L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val lines = input.map { line -> line.split("\\s+".toRegex()).filter { it.isNotBlank() } }

        val problems =
            lines.transpose().map {
                val numbers = it.dropLast(1).map { it.toInt() }

                val symbol = MathSymbol.fromChar(it.last().first())

                MathProblem(numbers, symbol)
            }

        problems.sumOf {
            it.calculate()
        }
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val fixedInput = input.map { it + " ".repeat(input.first().length - it.length) }

        val transposed = fixedInput.map { it.map { it } }.transpose().map { it.joinToString("") }

        val chunks =
            transposed.split {
                it.isBlank()
            }

        val problems =
            chunks.map { chunk ->
                val symbol = MathSymbol.fromChar(chunk.first().last())

                val numbers =
                    mutableListOf(chunk.first().dropLast(1)).apply { addAll(chunk.drop(1)) }.toList().map {
                        it.trim().toInt()
                    }

                MathProblem(numbers, symbol)
            }

        problems.sumOf {
            it.calculate()
        }
    }

    enum class MathSymbol {
        PLUS,
        MULTIPLY,
        ;

        companion object {
            fun fromChar(char: Char) =
                when (char) {
                    '+' -> PLUS
                    '*' -> MULTIPLY
                    else -> error("Could not parse symbol $char.")
                }
        }
    }

    data class MathProblem(
        val numbers: List<Int>,
        val mathSymbol: MathSymbol,
    ) {
        fun calculate() =
            when (mathSymbol) {
                MathSymbol.PLUS -> numbers.fold(0L) { acc, cur -> acc + cur }
                MathSymbol.MULTIPLY -> numbers.fold(1L) { acc, cur -> acc * cur }
            }
    }
}
