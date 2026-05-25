package com.z.money.ui.sync

import com.z.money.data.ChinaLegalCalendar
import com.z.money.data.ChinaLegalCalendarSource
import com.z.money.data.UserSettings
import com.z.money.data.WorkdayMode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LegalCalendarStatusTest {
    @Test
    fun builtInCalendarForCurrentYearIsUsedWithoutRefresh() = runBlocking {
        var refreshed = false
        val status = resolveLegalCalendarStatus(
            settings = UserSettings(
                workdayMode = WorkdayMode.ChinaLegal,
                chinaLegalCalendar = ChinaLegalCalendar(
                    year = 2026,
                    source = ChinaLegalCalendarSource.BuiltIn,
                    extraWorkDates = emptySet(),
                    offDates = emptySet(),
                ),
            ),
            year = 2026,
            refreshCalendar = { refreshed = true },
        )

        assertEquals(builtInStatus(2026), status)
        assertFalse(refreshed)
    }

    @Test
    fun remoteCalendarForCurrentYearIsUsedWithoutRefresh() = runBlocking {
        var refreshed = false
        val status = resolveLegalCalendarStatus(
            settings = UserSettings(
                workdayMode = WorkdayMode.ChinaLegal,
                chinaLegalCalendar = ChinaLegalCalendar(
                    year = 2026,
                    source = ChinaLegalCalendarSource.Remote,
                    extraWorkDates = emptySet(),
                    offDates = emptySet(),
                ),
            ),
            year = 2026,
            refreshCalendar = { refreshed = true },
        )

        assertEquals(syncedStatus(2026), status)
        assertFalse(refreshed)
    }

    @Test
    fun missingCalendarTriggersRefresh() = runBlocking {
        var refreshed = false
        val status = resolveLegalCalendarStatus(
            settings = UserSettings(
                workdayMode = WorkdayMode.ChinaLegal,
                chinaLegalCalendar = null,
            ),
            year = 2027,
            refreshCalendar = { refreshed = true },
        )

        assertEquals(syncedStatus(2027), status)
        assertTrue(refreshed)
    }

    @Test
    fun fixedWeeklyModeDoesNotShowCalendarStatus() = runBlocking {
        var refreshed = false
        val status = resolveLegalCalendarStatus(
            settings = UserSettings(workdayMode = WorkdayMode.FixedWeekly),
            year = 2026,
            refreshCalendar = { refreshed = true },
        )

        assertEquals("", status)
        assertFalse(refreshed)
    }
}
