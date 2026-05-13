package com.z.money.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class IncomeCalculatorTest {
    private val schedule = WorkSchedule(
        annualWorkDays = 250,
        dailyWorkHours = 8.0,
        workStart = LocalTime.of(9, 0),
        workEnd = LocalTime.of(17, 0),
    )

    @Test
    fun monthlySalaryConvertsToCentsPerSecond() {
        val salary = SalaryInput(amountCents = 10_000_00, period = SalaryPeriod.Monthly)

        val centsPerSecond = IncomeCalculator.centsPerSecond(salary, schedule)

        assertEquals(1.6667, centsPerSecond, 0.0001)
    }

    @Test
    fun annualSalaryUsesAnnualAmountDirectly() {
        val salary = SalaryInput(amountCents = 120_000_00, period = SalaryPeriod.Annual)

        val centsPerSecond = IncomeCalculator.centsPerSecond(salary, schedule)

        assertEquals(1.6667, centsPerSecond, 0.0001)
    }

    @Test
    fun beforeWorkEarnsNothingToday() {
        val now = LocalDateTime.of(2026, 5, 13, 8, 30)

        val snapshot = IncomeCalculator.snapshot(defaultSalary, schedule, now)

        assertEquals(0L, snapshot.workedSecondsToday)
        assertEquals(0.0, snapshot.earnedCentsToday, 0.0)
        assertEquals(WorkdayStatus.BeforeWork, snapshot.status)
    }

    @Test
    fun duringWorkCountsElapsedWorkSeconds() {
        val now = LocalDateTime.of(2026, 5, 13, 10, 30)

        val snapshot = IncomeCalculator.snapshot(defaultSalary, schedule, now)

        assertEquals(5_400L, snapshot.workedSecondsToday)
        assertEquals(9_000.0, snapshot.earnedCentsToday, 0.01)
        assertEquals(WorkdayStatus.Working, snapshot.status)
    }

    @Test
    fun afterWorkCapsAtDailyWorkSeconds() {
        val now = LocalDateTime.of(2026, 5, 13, 19, 0)

        val snapshot = IncomeCalculator.snapshot(defaultSalary, schedule, now)

        assertEquals(28_800L, snapshot.workedSecondsToday)
        assertEquals(48_000.0, snapshot.earnedCentsToday, 0.01)
        assertEquals(WorkdayStatus.AfterWork, snapshot.status)
        assertEquals(1.0, snapshot.progress, 0.0)
    }

    @Test
    fun nonWorkdayEarnsNothingToday() {
        val weekendSchedule = schedule.copy(workDays = setOf(DayOfWeek.MONDAY))
        val now = LocalDateTime.of(2026, 5, 13, 10, 30)

        val snapshot = IncomeCalculator.snapshot(defaultSalary, weekendSchedule, now)

        assertEquals(0L, snapshot.workedSecondsToday)
        assertEquals(0.0, snapshot.earnedCentsToday, 0.0)
        assertEquals(WorkdayStatus.NotWorkday, snapshot.status)
    }

    private companion object {
        val defaultSalary = SalaryInput(amountCents = 10_000_00, period = SalaryPeriod.Monthly)
    }
}
