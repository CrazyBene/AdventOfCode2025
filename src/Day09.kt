import utils.polygonFromCorners
import utils.rectangleFromCorners
import kotlin.math.abs

fun main() = Day09.run(RunMode.BOTH)

object Day09 : BasicDay() {
    override val expectedTestValuePart1 = 50L
    override val expectedTestValuePart2 = 24L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val redTilePositions =
            input.map { line ->
                line.split(',').let { (x, y) -> x.toLong() to y.toLong() }
            }

        redTilePositions
            .flatMapIndexed { index, first ->
                redTilePositions.drop(index).map { second ->
                    calculateArea(first, second)
                }
            }.max()
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val redTilePositions =
            input.map { line ->
                line.split(',').let { (x, y) -> x.toLong() to y.toLong() }
            }

        val polygon = polygonFromCorners(redTilePositions)

        val sortedRectangles =
            redTilePositions
                .flatMapIndexed { index, first ->
                    redTilePositions.drop(index).map { second ->
                        Triple(first, second, calculateArea(first, second))
                    }
                }.sortedByDescending { it.component3() }

        sortedRectangles
            .first { (corner1, corner2, area) ->
                val rect = rectangleFromCorners(corner1, corner2)

                polygon.contains(rect)
            }.component3()
    }

    fun calculateArea(
        corner1: Pair<Long, Long>,
        corner2: Pair<Long, Long>,
    ) = (abs(corner1.first - corner2.first) + 1) * (abs(corner1.second - corner2.second) + 1)
}
