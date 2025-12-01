fun main() = Day01.run(RunMode.BOTH)

object Day01 : BasicDay() {
    override val expectedTestValuePart1 = 3
    override val expectedTestValuePart2 = 6

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val rotations = parseInput(input)

        var dialPosition = 50
        var amountOfZeroes = 0

        rotations.forEach { rotation ->
            dialPosition = (dialPosition + (rotation.direction.sign * rotation.distance)).mod(100)

            if (dialPosition == 0) amountOfZeroes++
        }

        amountOfZeroes
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val rotations = parseInput(input)

        var dialPosition = 50
        var amountOfZeroes = 0

        rotations.forEach { rotation ->
            repeat(rotation.distance) {

                dialPosition = (dialPosition + rotation.direction.sign).mod(100)

                if (dialPosition == 0) amountOfZeroes++
            }
        }

        amountOfZeroes
    }

    fun parseInput(lines: List<String>) =
        lines.map { line ->
            val direction =
                when (line.first()) {
                    'L' -> Direction.L
                    'R' -> Direction.R
                    else -> error("Could not parse direction ${line.first()}.")
                }

            val distance = line.drop(1).toIntOrNull() ?: error("Could not parse distance ${line.drop(1)}.")

            Rotation(direction, distance)
        }

    enum class Direction(
        val sign: Int,
    ) {
        L(-1),
        R(1),
    }

    data class Rotation(
        val direction: Direction,
        val distance: Int,
    )
}
