fun main() = Day04.run(RunMode.BOTH)

object Day04 : BasicDay() {
    override val expectedTestValuePart1 = 13
    override val expectedTestValuePart2 = 43

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val room = parseToRoom(input)

        room.getAccessiblePaperRollsPositions().count()
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val room = parseToRoom(input)

        var currentRoom = room

        run {
            repeat(1_000_000) {
                val accessiblePositions = currentRoom.getAccessiblePaperRollsPositions()
                if (accessiblePositions.isEmpty()) return@run

                currentRoom =
                    Room(
                        currentRoom.cells.mapIndexed { y, line ->
                            line.mapIndexed { x, char ->
                                when {
                                    char == Cell.FREE -> Cell.FREE
                                    Position(x, y) in accessiblePositions -> Cell.FREE
                                    else -> Cell.PAPER_ROLL
                                }
                            }
                        },
                    )
            }
            error("Most likely something went wrong. Or not enough iterations to finish.")
        }

        room.countPaperRolls() - currentRoom.countPaperRolls()
    }

    fun parseToRoom(input: List<String>) = Room(input.map { line -> line.map { if (it == '.') Cell.FREE else Cell.PAPER_ROLL } })

    data class Position(
        val x: Int,
        val y: Int,
    )

    enum class Cell {
        FREE,
        PAPER_ROLL,
    }

    data class Room(
        val cells: List<List<Cell>>,
    ) {
        fun countPaperRolls() = cells.sumOf { line -> line.count { it == Cell.PAPER_ROLL } }

        fun cellAt(position: Position): Cell? {
            if (position.y < 0 || position.y >= cells.size || position.x < 0 || position.x >= cells.first().size) {
                return null
            }

            return cells[position.y][position.x]
        }

        fun getAccessiblePaperRollsPositions(): List<Position> {
            return cells.flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, char ->
                    if (char == Cell.FREE) {
                        return@mapIndexedNotNull null
                    }

                    var paperNeighbors = 0
                    (-1..1).map { y ->
                        (-1..1).map { x ->
                            if (y == 0 && x == 0) {
                                return@map
                            }

                            if (cellAt(Position(col + x, row + y)) == Cell.PAPER_ROLL) {
                                paperNeighbors++
                            }
                        }
                    }

                    if (paperNeighbors < 4) Position(col, row) else null
                }
            }
        }
    }
}
