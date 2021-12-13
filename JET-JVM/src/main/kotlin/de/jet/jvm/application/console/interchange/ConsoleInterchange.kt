@file:Suppress("unused")

package de.jet.jvm.application.console.interchange

import de.jet.jvm.tool.smart.Producible
import de.jet.jvm.tool.smart.positioning.Address

class ConsoleInterchange(
    override val identity: String,
    override val path: String,
    override val branches: List<ConsoleStructureBranch>,
    override val content: ((parameters: List<String>) -> Unit)? = null,
    val syntaxIssue: ((parameters: List<String>) -> Unit)? = null,
) : ConsoleInterchangeStructure<ConsoleStructureBranch>(identity) {

    fun performInterchange(input: String): Boolean {
        val nearest = getNearestBranchWithParameters(Address.address(input.replace(" ", "/")))

        return run {
            val content = nearest.first.content

            if (content != null) {
                content(nearest.second.split(" "))
                true
            } else
                false

        }

    }

    data class Builder(
        var identity: String,
        var path: String,
        var branches: MutableList<ConsoleStructureBranch> = mutableListOf(),
        var content: ((parameters: List<String>) -> Unit)? = null,
        var syntaxIssue: ((parameters: List<String>) -> Unit)? = null,
    ) : Producible<ConsoleInterchange> {

        operator fun plus(branch: ConsoleStructureBranch) = apply {
            branches.add(branch)
        }

        fun branch(name: String, process: ConsoleStructureBranch.Builder.() -> Unit = { }) = apply {
            branches.add(ConsoleStructureBranch.Builder(name, "$path/$name", mutableListOf(), content).apply(process).produce())
        }

        fun content(content: ((parameters: List<String>) -> Unit)?) = apply {
            this.content = content
        }

        fun onSyntaxIssue(syntaxIssue: ((parameters: List<String>) -> Unit)?) = apply {
            this.syntaxIssue = syntaxIssue
        }

        override fun produce() = ConsoleInterchange(
            identity, path, branches, content
        )

    }

}