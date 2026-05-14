package com.z.money.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ChinaHolidayServiceTest {
    @Test
    fun parsesHolidayEntriesIntoOffDaysAndExtraWorkDays() {
        val body = """
            {
              "2026-01-01": {
                "date": "2026-01-01",
                "name": "元旦",
                "isOffDay": true
              },
              "2026-02-14": {
                "date": "2026-02-14",
                "name": "春节",
                "isOffDay": false
              }
            }
        """.trimIndent()

        val calendar = ChinaHolidayService().parseCalendar(
            year = 2026,
            body = body,
        )

        assertEquals(2026, calendar.year)
        assertEquals(setOf(LocalDate.of(2026, 1, 1)), calendar.offDates)
        assertEquals(setOf(LocalDate.of(2026, 2, 14)), calendar.extraWorkDates)
    }
}
