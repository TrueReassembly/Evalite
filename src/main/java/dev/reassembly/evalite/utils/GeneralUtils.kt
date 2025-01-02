package dev.reassembly.evalite.utils

import dev.reassembly.evalite.evalite
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

object GeneralUtils {

    fun String.toMiniMessage(): Component {
        return MiniMessage.miniMessage().deserialize(this,
            Placeholder.parsed("prefix", evalite.messagesConfig.getString("prefix") ?: "N/A"),
            Placeholder.parsed("server-name", evalite.messagesConfig.getString("social-links.server-name") ?: "N/A"),
            Placeholder.parsed("discord", evalite.messagesConfig.getString("social-links.discord") ?: "N/A"),
            Placeholder.parsed("twitter", evalite.messagesConfig.getString("social-links.twitter") ?: "N/A"),
            Placeholder.parsed("website", evalite.messagesConfig.getString("social-links.website") ?: "N/A"),
            Placeholder.parsed("store", evalite.messagesConfig.getString("social-links.store") ?: "N/A")
        )

    }

    fun String.toTimeSpanMillis() : Long {
        var time = 0L
        var copy = this
        val yearRegex = Regex("^\\d+dy$")
        val monthRegex = Regex("^\\d+mo$")
        val daysRegex = Regex("^\\d+d$")
        val hoursRegex = Regex("^\\d+h$")
        val minutesRegex = Regex("^\\d+m$")
        val secondsRegex = Regex("^\\d+s$")

        while (copy.isNotEmpty()) {
            val yearExtract = yearRegex.find(copy)
            if (yearExtract != null) {
                copy = copy.replace(yearExtract.value, "")
                time += (yearExtract.value.replace("y", "").toInt()) * 31556952000
            }
            val monthExtract = monthRegex.find(copy)
            if (monthExtract != null) {
                copy = copy.replace(monthExtract.value, "")
                time += (monthExtract.value.replace("mo", "").toInt()) * 2629746000
            }

            val daysExtract = daysRegex.find(copy)
            if (daysExtract != null) {
                copy = copy.replace(daysExtract.value, "")
                time += (daysExtract.value.replace("d", "").toInt()) * 86400000
            }

            val hoursExtract = hoursRegex.find(copy)
            if (hoursExtract != null) {
                copy = copy.replace(hoursExtract.value, "")
                time += (hoursExtract.value.replace("h", "").toInt()) * 3600000
            }

            val minutesExtract = minutesRegex.find(copy)
            if (minutesExtract != null) {
                copy = copy.replace(minutesExtract.value, "")
                time += (minutesExtract.value.replace("m", "").toInt()) * 60000
            }

            val secondsExtract = secondsRegex.find(copy)
            if (secondsExtract != null) {
                copy = copy.replace(secondsExtract.value, "")
                time += (secondsExtract.value.replace("s", "").toInt()) * 1000
            }
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

    /**
     * Converts a time in milliseconds to an expanded time string
     *
     * @param expiresAt The time in milliseconds
     * @return The expanded time string
     */
    fun millisToExpanded(timespan: Long?): String {
        if (timespan == null) return "Permanent"
        val seconds = timespan / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = months / 12

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""}"
            months > 0 -> "$months month${if (months > 1) "s" else ""}"
            days > 0 -> "$days day${if (days > 1) "s" else ""}"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""}"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""}"
            else -> "$seconds second${if (seconds > 1) "s" else ""}"
        }
    }

    fun toDashedUUID(uuid: String): String {
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20)
    }
}