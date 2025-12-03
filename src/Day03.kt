import kotlin.math.pow

fun main() = Day03.run(RunMode.BOTH)

object Day03 : BasicDay() {
    override val expectedTestValuePart1 = 357
    override val expectedTestValuePart2 = 3121910778619L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val batterieBanks = parseToBatterieBanks(input)

        batterieBanks.sumOf { bank ->
            val (index, firstDigit) = bank.dropLast(1).maxWithIndex()
            val secondDigit = bank.drop(index + 1).max()

            firstDigit * 10 + secondDigit
        }
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val batterieBanks = parseToBatterieBanks(input)

        batterieBanks.sumOf { bank ->
            (11 downTo 0)
                .fold(bank to 0L) { (restBank, joltage), digitIndex ->
                    val (index, digit) = restBank.dropLast(digitIndex).maxWithIndex()

                    restBank.drop(index + 1) to joltage + 10.0.pow(digitIndex).toLong() * digit
                }.second

//            var restBank = bank
//            var joltage = 0L
//            for (digitIndex in 11 downTo 0) {
//                val (index, digit) = restBank.dropLast(digitIndex).maxWithIndex()
//
//                restBank = restBank.drop(index + 1)
//                joltage += 10.0.pow(digitIndex).toLong() * digit
//            }
//
//            joltage
        }
    }

    fun parseToBatterieBanks(input: List<String>) =
        input.map { line ->
            line.map { it.toString().toInt() }
        }

    fun List<Int>.maxWithIndex() = this.withIndex().maxBy { (_, digit) -> digit }
}
