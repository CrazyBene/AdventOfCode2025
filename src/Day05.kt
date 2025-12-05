import kotlin.math.max
import kotlin.math.min

fun main() = Day05.run(RunMode.BOTH)

object Day05 : BasicDay() {
    override val expectedTestValuePart1 = 3
    override val expectedTestValuePart2 = 14L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val freshIngredientRanges = parseToRanges(input.takeWhile { it.isNotBlank() })

        val availableIngredientIds = input.takeLastWhile { it.isNotBlank() }.map { line -> line.toLong() }

        availableIngredientIds.count { id ->
            freshIngredientRanges.any { it.contains(id) }
        }
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val freshIngredientRanges = parseToRanges(input.takeWhile { it.isNotBlank() })

        val currentRanges = freshIngredientRanges.toMutableList()

        var index = 0
        while (index < currentRanges.size) {
            var currentRange = currentRanges[index]
            val overlappingRange =
                currentRanges
                    .drop(index + 1)
                    .withIndex()
                    .firstOrNull { it.value.isOverlapping(currentRange) }

            if (overlappingRange != null) {
                currentRanges[index] = currentRange.merge(overlappingRange.value)
                currentRanges.removeAt(index + overlappingRange.index + 1)
                continue
            }

            index++
        }

        currentRanges.sumOf { it.last - it.first + 1 }
    }

    fun parseToRanges(input: List<String>) =
        input.map { line ->
            line.split("-").map { it.toLong() }.let { (start, end) ->
                start..end
            }
        }

    fun LongRange.isOverlapping(other: LongRange) = this.first <= other.last && other.first <= this.last

    fun LongRange.merge(other: LongRange) = min(this.first, other.first)..max(this.last, other.last)
}
