import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import utils.allCombinations

fun main() = Day10.run(RunMode.BOTH)

object Day10 : BasicDay() {
    override val expectedTestValuePart1 = 7L
    override val expectedTestValuePart2 = 33

    private val machineRegex = Regex("""^\[([.#]+)] (.*) \{([\d,]+)}$""")

    override val solvePart1: ((input: List<String>, isTestRun: Boolean) -> Long) = { input, _ ->
        val machines = parseInput(input)

        machines.sumOf { machine ->
            val combinations = machine.buttonWiring.allCombinations()

            combinations
                .first { combination -> checkCombination(combination, machine.indicatorLights) }
                .count()
                .toLong()
        }
    }

    override val solvePart2: ((input: List<String>, isTestRun: Boolean) -> Int) = { input, _ ->
        val machines = parseInput(input)

        machines.sumOf { machine ->
            solveMachineWithZ3(machine.buttonWiring, machine.joltageRequirements)
        }
    }

    fun parseInput(input: List<String>): List<Machine> =
        input.map { line ->
            machineRegex.find(line).let { result ->
                if (result == null) error("Could not parse line $line.")

                val indicatorLights = result.groupValues[1].map { it != '.' }

                val buttonWiring =
                    result.groupValues[2].split(' ').map { group ->
                        group
                            .drop(1)
                            .dropLast(1)
                            .split(',')
                            .map { it.toInt() }
                    }

                val joltageRequirements = result.groupValues[3].split(",").map { it.toInt() }

                Machine(
                    indicatorLights,
                    buttonWiring,
                    joltageRequirements,
                )
            }
        }

    fun checkCombination(
        combination: List<List<Int>>,
        indicatorLights: List<Boolean>,
    ): Boolean {
        val lights =
            (0..<indicatorLights.count()).map { number ->
                combination.count { number in it } % 2 != 0
            }

        return lights == indicatorLights
    }

    fun solveMachineWithZ3(
        buttonWiring: List<List<Int>>,
        joltageRequirements: List<Int>,
    ): Int {
        val ctx = Context()
        val opt = ctx.mkOptimize()
        val presses = ctx.mkIntConst("presses")

        val buttonArray = Array(buttonWiring.count()) { ctx.mkIntConst("button$it") }

        buttonArray.forEach {
            opt.Add(ctx.mkGe(it, ctx.mkInt(0)))
        }

        val countersToButtons =
            buttonWiring
                .flatMapIndexed { buttonIndex, button ->
                    button.map { counter -> counter to buttonArray[buttonIndex] }
                }.groupBy({ it.first }, { it.second })

        countersToButtons.forEach { (counterIdx, affectingButtons) ->
            opt.Add(
                ctx.mkEq(
                    ctx.mkAdd(*affectingButtons.toTypedArray()),
                    ctx.mkInt(joltageRequirements[counterIdx]),
                ),
            )
        }

        opt.Add(ctx.mkEq(presses, ctx.mkAdd(*buttonArray)))
        opt.MkMinimize(presses)

        return when (val status = opt.Check()) {
            Status.SATISFIABLE -> (opt.model.evaluate(presses, false) as IntNum).int
            Status.UNSATISFIABLE -> error("Problem is UNSATISFIABLE (no solution exists).")
            else -> error("Optimization could not be determined ($status).")
        }
    }

    data class Machine(
        val indicatorLights: List<Boolean>,
        val buttonWiring: List<List<Int>>,
        val joltageRequirements: List<Int>,
    )
}
