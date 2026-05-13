package com.z.money.domain

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class SalaryPeriod {
    Monthly,
    Annual,
}

data class SalaryInput(
    val amountCents: Long,
    val period: SalaryPeriod,
) {
    fun annualAmountCents(): Long = when (period) {
        SalaryPeriod.Monthly -> amountCents * MONTHS_PER_YEAR
        SalaryPeriod.Annual -> amountCents
    }

    private companion object {
        const val MONTHS_PER_YEAR = 12
    }
}

data class WorkSchedule(
    val annualWorkDays: Int = 250,
    val workStart: LocalTime = LocalTime.of(9, 0),
    val workEnd: LocalTime = LocalTime.of(17, 0),
    val workDays: Set<DayOfWeek> = DEFAULT_WORK_DAYS,
) {
    init {
        require(annualWorkDays > 0) { "annualWorkDays must be greater than 0" }
        require(workStart < workEnd) { "workStart must be before workEnd" }
        require(workDays.isNotEmpty()) { "workDays must not be empty" }
    }

    fun isWorkday(date: LocalDate): Boolean = date.dayOfWeek in workDays

    fun workdaySeconds(): Long = Duration.between(workStart, workEnd).seconds

    companion object {
        val DEFAULT_WORK_DAYS: Set<DayOfWeek> = setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
        )
    }
}

data class EarningSnapshot(
    val centsPerSecond: Double,
    val workedSecondsToday: Long,
    val earnedCentsToday: Double,
    val maxEarnableCentsToday: Double,
    val status: WorkdayStatus,
) {
    val progress: Double =
        if (maxEarnableCentsToday <= 0.0) 0.0 else earnedCentsToday / maxEarnableCentsToday
}

enum class WorkdayStatus {
    NotWorkday,
    BeforeWork,
    Working,
    AfterWork,
}

object IncomeCalculator {
    fun centsPerSecond(
        salary: SalaryInput,
        schedule: WorkSchedule,
    ): Double {
        val annualWorkSeconds = schedule.annualWorkDays.toLong() * schedule.workdaySeconds()
        return salary.annualAmountCents().toDouble() / annualWorkSeconds
    }

    fun workedSecondsToday(
        now: LocalDateTime,
        schedule: WorkSchedule,
    ): Long {
        if (!schedule.isWorkday(now.toLocalDate())) return 0L

        val start = LocalDateTime.of(now.toLocalDate(), schedule.workStart)
        val end = LocalDateTime.of(now.toLocalDate(), schedule.workEnd)

        return when {
            now <= start -> 0L
            now >= end -> schedule.workdaySeconds()
            else -> minOf(Duration.between(start, now).seconds, schedule.workdaySeconds())
        }
    }

    fun snapshot(
        salary: SalaryInput,
        schedule: WorkSchedule,
        now: LocalDateTime,
    ): EarningSnapshot {
        val centsPerSecond = centsPerSecond(salary, schedule)
        val workedSecondsToday = workedSecondsToday(now, schedule)
        val maxEarnableCentsToday = schedule.workdaySeconds() * centsPerSecond

        return EarningSnapshot(
            centsPerSecond = centsPerSecond,
            workedSecondsToday = workedSecondsToday,
            earnedCentsToday = workedSecondsToday * centsPerSecond,
            maxEarnableCentsToday = maxEarnableCentsToday,
            status = status(now, schedule),
        )
    }

    private fun status(
        now: LocalDateTime,
        schedule: WorkSchedule,
    ): WorkdayStatus {
        if (!schedule.isWorkday(now.toLocalDate())) return WorkdayStatus.NotWorkday

        val time = now.toLocalTime()
        return when {
            time < schedule.workStart -> WorkdayStatus.BeforeWork
            time >= schedule.workEnd -> WorkdayStatus.AfterWork
            else -> WorkdayStatus.Working
        }
    }
}
