import kotlin.math.pow
import kotlin.math.sqrt

fun main() = Day08.run(RunMode.BOTH)

object Day08 : BasicDay() {
    override val expectedTestValuePart1 = 40
    override val expectedTestValuePart2 = 25272L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, isTest ->
        val junctionBoxes = parseInput(input)
        val sortedConnections = calculateDistances(junctionBoxes).sortedBy { it.second }.map { it.first }

        val (groups) = calculateGroups(junctionBoxes, sortedConnections.take(if (isTest) 10 else 1000))

        groups
            .map { group -> group.count() }
            .sortedDescending()
            .take(3)
            .fold(1) { acc, count -> acc * count }
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val junctionBoxes = parseInput(input)
        val sortedConnections = calculateDistances(junctionBoxes).sortedBy { it.second }.map { it.first }

        val (_, connection) =
            calculateGroups(junctionBoxes, sortedConnections) { groups ->
                groups.count() == 1
            }

        connection.first.x.toLong() * connection.second.x.toLong()
    }

    fun parseInput(input: List<String>) =
        input
            .map { line ->
                line.split(',').let { it.map { values -> values.toInt() } }
            }.map { (x, y, z) ->
                JunctionBox(x, y, z)
            }

    fun calculateDistances(junctionBoxes: List<JunctionBox>) =
        junctionBoxes
            .dropLast(1)
            .flatMapIndexed { index, first ->
                junctionBoxes.drop(index + 1).map { second ->
                    first to second to first.distanceTo(second)
                }
            }

    data class JunctionBox(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {
        fun distanceTo(other: JunctionBox) =
            sqrt(
                (this.x - other.x).toDouble().pow(2) +
                    (this.y - other.y)
                        .toDouble()
                        .pow(2) + (this.z - other.z).toDouble().pow(2),
            )
    }

    fun calculateGroups(
        junctionBoxes: List<JunctionBox>,
        sortedConnections: List<Pair<JunctionBox, JunctionBox>>,
        shouldTerminateEarly: ((List<Set<JunctionBox>>) -> Boolean)? = null,
    ): Pair<MutableList<Set<JunctionBox>>, Pair<JunctionBox, JunctionBox>> {
        val groups = junctionBoxes.map { setOf(it) }.toMutableList()
        sortedConnections.forEach { connection ->
            val groupOne =
                groups.find { it.contains(connection.first) }
                    ?: error("Could not find group with element ${connection.first}.")
            val groupTwo =
                groups.find { it.contains(connection.second) }
                    ?: error("Could not find group with element ${connection.second}.")

            groups.remove(groupOne)
            groups.remove(groupTwo)
            groups.add(groupOne + groupTwo)

            if (shouldTerminateEarly?.invoke(groups) == true) return groups to connection
        }

        return groups to sortedConnections.last()
    }
}
