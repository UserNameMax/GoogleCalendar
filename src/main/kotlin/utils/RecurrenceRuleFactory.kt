package utils

import model.Ending
import model.Recurrence

object RecurrenceRuleFactory {
    fun Recurrence.rule() =
        "RRULE:"
            .let { it + "FREQ=${freq.name};" }
            .let { if (interval != 0) it + "INTERVAL=${interval};" else it }
            .let {
                it + when (ending) {
                    is Ending.Count -> "COUNT=${ending.value};"
                    is Ending.Until -> "UNTIL=${ending.value};"
                }
            }
            .let {
                if (byDay.isNotEmpty()) it + byDay.fold("BYDAY=") { sum, element -> "$sum${element.toName()}," }
                    .trim(',') + ";" else it
            }
            .let {
                if (byMonth.isNotEmpty()) it + byMonth.fold("BYMONTH=") { sum, element -> "$sum$element," }
                    .trim(',') + ";" else it
            }
            .let {
                if (byYearDay.isNotEmpty()) it + byYearDay.fold("BYYEARDAY=") { sum, element -> "$sum$element," }
                    .trim(',') + ";" else it
            }
            .let {
                if (byHour.isNotEmpty()) it + byHour.fold("BYHOUR=") { sum, element -> "$sum$element," }
                    .trim(',') + ";" else it
            }

    private fun Int.toName(): String =
        when (this) {
            1 -> "MO"
            2 -> "TU"
            3 -> "WE"
            4 -> "TH"
            5 -> "FR"
            6 -> "SA"
            7 -> "SU"
            else -> ""
        }
}