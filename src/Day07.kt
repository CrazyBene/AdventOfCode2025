fun main() = Day07.run(RunMode.BOTH)

object Day07 : BasicDay() {
    override val expectedTestValuePart1 = 21
    override val expectedTestValuePart2 = 40L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val grid = parseToGrid(input)
        val startPosition =
            Position(
                grid
                    .first()
                    .withIndex()
                    .find { it.value == Cell.START }!!
                    .index,
                0,
            )

        var currentBeamPositions = setOf(startPosition)
        var numberOfSplits = 0
        repeat(grid.count() - 2) {
            currentBeamPositions =
                currentBeamPositions
                    .flatMap { beamPosition ->
                        when (grid[Position(beamPosition.x, beamPosition.y + 1)]) {
                            Cell.FREE -> {
                                listOf(Position(beamPosition.x, beamPosition.y + 1))
                            }

                            Cell.SPLITTER -> {
                                numberOfSplits++
                                listOf(Position(beamPosition.x - 1, beamPosition.y + 1), Position(beamPosition.x + 1, beamPosition.y + 1))
                            }

                            Cell.START -> {
                                error("No beam should ever reach the start again.")
                            }
                        }
                    }.toSet()
        }

        numberOfSplits
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val grid = parseToGrid(input)
        val startPosition =
            Position(
                grid
                    .first()
                    .withIndex()
                    .find { it.value == Cell.START }!!
                    .index,
                0,
            )

        fun numberOfTimelines(startPosition: Position): Long {
            val cache = mutableMapOf<Position, Long>()

            fun inner(currentPosition: Position): Long {
                if (currentPosition.y == grid.count() - 1) {
                    return 1
                }

                return when (grid[Position(currentPosition.x, currentPosition.y + 1)]) {
                    Cell.FREE -> {
                        cache.getOrPut(currentPosition) {
                            inner(Position(currentPosition.x, currentPosition.y + 1))
                        }
                    }

                    Cell.SPLITTER -> {
                        cache.getOrPut(currentPosition) {
                            inner(Position(currentPosition.x - 1, currentPosition.y + 1)) +
                                inner(Position(currentPosition.x + 1, currentPosition.y + 1))
                        }
                    }

                    Cell.START -> {
                        error("No beam should ever reach the start again.")
                    }
                }
            }

            return inner(startPosition)
        }

        numberOfTimelines(startPosition)
    }

    fun parseToGrid(input: List<String>) = input.map { line -> line.map { Cell.fromChar(it) } }

    data class Position(
        val x: Int,
        val y: Int,
    )

    enum class Cell {
        FREE,
        START,
        SPLITTER,
        ;

        companion object {
            fun fromChar(char: Char): Cell =
                when (char) {
                    '.' -> FREE
                    'S' -> START
                    '^' -> SPLITTER
                    else -> error("Could not parse cell $char.")
                }
        }
    }

    operator fun List<List<Cell>>.get(position: Position): Cell = this[position.y][position.x]
}
