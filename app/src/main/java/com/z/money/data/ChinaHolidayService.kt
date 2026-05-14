package com.z.money.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

class ChinaHolidayService {
    suspend fun fetchCalendar(year: Int): ChinaLegalCalendar = withContext(Dispatchers.IO) {
        val connection = URL("https://api.jiejiariapi.com/v1/holidays/$year")
            .openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connectTimeout = TIMEOUT_MILLIS
        connection.readTimeout = TIMEOUT_MILLIS

        try {
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                error("Holiday API returned HTTP $responseCode")
            }

            parseCalendar(
                year = year,
                body = connection.inputStream.bufferedReader().use { it.readText() },
            )
        } finally {
            connection.disconnect()
        }
    }

    fun parseCalendar(
        year: Int,
        body: String,
    ): ChinaLegalCalendar {
        val extraWorkDates = mutableSetOf<LocalDate>()
        val offDates = mutableSetOf<LocalDate>()

        holidayEntryRegex.findAll(body).forEach { match ->
            val date = LocalDate.parse(match.groupValues[1])
            val isOffDay = match.groupValues[2].toBoolean()

            if (isOffDay) {
                offDates += date
            } else {
                extraWorkDates += date
            }
        }

        return ChinaLegalCalendar(
            year = year,
            extraWorkDates = extraWorkDates,
            offDates = offDates,
            source = ChinaLegalCalendarSource.Remote,
        )
    }

    private companion object {
        const val TIMEOUT_MILLIS = 8_000
        val holidayEntryRegex =
            Regex("\"(\\d{4}-\\d{2}-\\d{2})\"\\s*:\\s*\\{[^}]*?\"isOffDay\"\\s*:\\s*(true|false)")
    }
}
