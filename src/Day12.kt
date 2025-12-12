import utils.split

fun main() = Day12.run(RunMode.PART1)

object Day12 : BasicDay() {
    // this solution does not work for the example
    // only works for regions >> present size
//    override val expectedTestValuePart1 = 2

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val parts = input.split { it.isBlank() }

        val presents =
            parts.take(6).map { presentPart ->
                val shape = presentPart.drop(1).map { it.map { it == '#' } }

                Present(shape)
            }

        val regions =
            parts.drop(6).flatMap { regionPart ->
                regionPart.map { line ->
                    val (sizeString, quantityString) = line.split(": ")

                    val size = sizeString.split('x').map { it.toInt() }
                    val quantity = quantityString.split(' ').map { it.toInt() }

                    Region(size[0], size[1], quantity)
                }
            }

        // random / guessed
        val enoughSizeFactor = 1.2

        val regionsWithEnoughSize =
            regions.filter { region ->
                region.shapeQuantity.withIndex().sumOf { (index, quantity) ->
                    presents[index].tileCount() * quantity
                } * enoughSizeFactor <= region.size()
            }

        regionsWithEnoughSize.count()
    }

    data class Present(
        val shape: List<List<Boolean>>,
    ) {
        fun tileCount() = shape.flatten().count { it }
    }

    data class Region(
        val width: Int,
        val length: Int,
        val shapeQuantity: List<Int>,
    ) {
        fun size() = width * length
    }
}
