package com.z.money.data

import com.z.money.domain.SalaryPeriod
import com.z.money.domain.WorkSchedule
import java.time.DayOfWeek

data class UserSettings(
    val salaryPeriod: SalaryPeriod = SalaryPeriod.Monthly,
    val salaryAmountYuan: Double = 10_000.0,
    val annualWorkDays: Int = 250,
    val workStartMinutes: Int = 9 * 60,
    val workEndMinutes: Int = 17 * 60,
    val workdayMode: WorkdayMode = WorkdayMode.ChinaLegal,
    val workDays: Set<DayOfWeek> = WorkSchedule.DEFAULT_WORK_DAYS,
    val chinaLegalCalendar: ChinaLegalCalendar? = null,
    val themeMode: ThemeMode = ThemeMode.System,
)
