package dev.reassembly.evalite.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object GeneralUtils {

    fun String.toMiniMessage(): Component {
        return MiniMessage.miniMessage().deserialize(this)
    }

    fun String.toTimeSpanMillis() : Long {
        var time = 0L

        val yearRegex = Regex("/\\d*y/gm")
        val monthRegex = Regex("/\\d*mo/gm")
        val daysRegex = Regex("/\\d*d/gm")
        val hoursRegex = Regex("/\\d*h/gm")
        val minutesRegex = Regex("/\\d*m/gm")
        val secondsRegex = Regex("/\\d*s/gm")

        var i = 0;
        while (i < 30) {
            val yearExtract = yearRegex.find(this)
            if (yearExtract != null) {
                this.replace(yearExtract.value, "")
                time += (yearExtract.value.replace("y", "").toInt()) * 31556952000
            }
            val monthExtract = monthRegex.find(this)
            if (monthExtract != null) {
                this.replace(monthExtract.value, "")
                time += (monthExtract.value.replace("mo", "").toInt()) * 2629746000
            }

            val daysExtract = daysRegex.find(this)
            if (daysExtract != null) {
                this.replace(daysExtract.value, "")
                time += (daysExtract.value.replace("d", "").toInt()) * 86400000
            }

            val hoursExtract = hoursRegex.find(this)
            if (hoursExtract != null) {
                this.replace(hoursExtract.value, "")
                time += (hoursExtract.value.replace("h", "").toInt()) * 3600000
            }

            val minutesExtract = minutesRegex.find(this)
            if (minutesExtract != null) {
                this.replace(minutesExtract.value, "")
                time += (minutesExtract.value.replace("m", "").toInt()) * 60000
            }

            val secondsExtract = secondsRegex.find(this)
            if (secondsExtract != null) {
                this.replace(secondsExtract.value, "")
                time += (secondsExtract.value.replace("s", "").toInt()) * 1000
            }

            i++
        }
        return time
    }

    fun String.expandTimeSpan() : String {
        var time = ""
        val timeSplit = this.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)".toRegex())
        var i = 0
        while (i < timeSplit.size) {
            time += when (timeSplit[i + 1]) {
                "y" -> "${timeSplit[i]} year"
                "mo" -> "${timeSplit[i]} month"
                "d" -> "${timeSplit[i]} day"
                "h" -> "${timeSplit[i]} hour"
                "m" -> "${timeSplit[i]} minute"
                "s" -> "${timeSplit[i]} second"
                else -> ""
            }
            if (timeSplit[i].toInt() > 1) time += "s"
            time += if (i != timeSplit.size - 3) ", " else " and "
            i += 2
        }
        return time
    }
}