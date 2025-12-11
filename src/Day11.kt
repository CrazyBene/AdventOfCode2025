fun main() = Day11.run(RunMode.BOTH)

object Day11 : BasicDay(separateTestFiles = true) {
    override val expectedTestValuePart1 = 5L
    override val expectedTestValuePart2 = 2L

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val connections = parseInput(input)

        countPaths(connections, "you", "out")
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val connections = parseInput(input)

        val svrToDac = countPaths(connections, "svr", "dac")
        val dacToFft = countPaths(connections, "dac", "fft")
        val fftToOut = countPaths(connections, "fft", "out")

        val svrToFft = countPaths(connections, "svr", "fft")
        val fftToDac = countPaths(connections, "fft", "dac")
        val dacToOut = countPaths(connections, "dac", "out")

        (svrToDac * dacToFft * fftToOut) + (svrToFft * fftToDac * dacToOut)

//        countPaths2InOne(connections, "svr", "out")
    }

    fun parseInput(input: List<String>) =
        input.associate { line ->
            line
                .split(':')
                .let { (name, outputsString) -> name to outputsString.trim().split(' ') }
        }

    fun countPaths(
        connections: Map<String, List<String>>,
        start: String,
        target: String,
    ): Long {
        val cache = mutableMapOf<String, Long>()

        fun inner(currentNode: String): Long {
            if (currentNode == target) {
                return 1
            }

            return connections.getOrElse(currentNode) { emptyList() }.sumOf { cache.getOrPut(it) { inner(it) } }
        }

        return inner(start)
    }

    fun countPaths2InOne(
        connections: Map<String, List<String>>,
        start: String,
        target: String,
    ): Long {
        val cache = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

        fun inner(
            currentNode: String,
            dacSeen: Boolean,
            fftSeen: Boolean,
        ): Long {
            if (currentNode == target && dacSeen && fftSeen) {
                return 1
            }

            return connections.getOrElse(currentNode) { emptyList() }.sumOf {
                val newDacSeen = dacSeen || currentNode == "dac"
                val newFftSeen = fftSeen || currentNode == "fft"
                cache.getOrPut(Triple(it, newDacSeen, newFftSeen)) { inner(it, newDacSeen, newFftSeen) }
            }
        }

        return inner(start, dacSeen = false, fftSeen = false)
    }
}
