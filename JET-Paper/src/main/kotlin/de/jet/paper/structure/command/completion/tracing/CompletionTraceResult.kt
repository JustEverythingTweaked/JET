package de.jet.paper.structure.command.completion.tracing

import de.jet.paper.structure.command.completion.CompletionBranch
import de.jet.paper.structure.command.completion.tracing.CompletionTraceResult.Conclusion.*

data class CompletionTraceResult(
	val waysMatching: List<PossibleTraceWay>, // the possible ways to execute the trace
	val waysIncomplete: List<PossibleTraceWay>, // the possible, but uncompleted ways to execute the trace - more input is needed to complete the trace
	val waysFailed: List<PossibleTraceWay>, // the ways that died during the trace and cannot be used with the query
	val traceBase: CompletionBranch, // the base of the trace
	val executedQuery: List<String>, // the original query, that was used to execute the trace
) {

	val conclusion = if (waysMatching.size == 1) RESULT else if (waysMatching.isEmpty()) NO_RESULT else MULTIPLE_RESULTS

	enum class Conclusion {

		/**
		 * Found the perfect result.
		 * ***ONE POSSIBLE WAY***
		 */
		RESULT,

		/**
		 * Many possible results.
		 * ***MANY POSSIBLE WAYS***
		 */
		MULTIPLE_RESULTS,

		/**
		 * No result, invalid request.
		 * ***NO POSSIBLE WAY***
		 */
		NO_RESULT,


	}

}
