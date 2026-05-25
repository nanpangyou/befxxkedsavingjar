package com.z.money.ui.main

import com.z.money.data.ChinaLegalCalendar
import com.z.money.data.ThemeMode
import com.z.money.data.UserSettings
import com.z.money.data.WorkdayMode
import com.z.money.data.toSalaryInput
import com.z.money.data.toWorkSchedule
import com.z.money.domain.SalaryPeriod
import java.time.DayOfWeek

data class EarningSettings(
    val salaryPeriod: SalaryPeriod = SalaryPeriod.Monthly,
    val salaryAmount: String = "10000",
    val workStartMinutes: Int = 9 * MINUTES_PER_HOUR,
    val workEndMinutes: Int = 17 * MINUTES_PER_HOUR,
    val workdayMode: WorkdayMode = WorkdayMode.ChinaLegal,
    val workDays: Set<DayOfWeek> = UserSettings().workDays,
    val chinaLegalCalendar: ChinaLegalCalendar? = null,
    val themeMode: ThemeMode = ThemeMode.System,
) {
    val summaryText: String
        get() = "\u5f53\u524d\u4f7f\u7528\uff1a${salaryPeriod.label} ${salaryAmount.ifBlank { "0" }} \u5143\uff0c${workdayMode.label}\uff0c${workStartMinutes.toTimeText()}-${workEndMinutes.toTimeText()}\u3002"

    fun toSalaryInput() = toUserSettings().toSalaryInput()

    fun toWorkSchedule() = toUserSettings().toWorkSchedule()

    fun toUserSettings(): UserSettings {
        return UserSettings(
            salaryPeriod = salaryPeriod,
            salaryAmountYuan = salaryAmount.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
            workStartMinutes = workStartMinutes,
            workEndMinutes = workEndMinutes,
            workdayMode = workdayMode,
            workDays = workDays,
            chinaLegalCalendar = chinaLegalCalendar,
            themeMode = themeMode,
        )
    }

    companion object {
        fun fromUserSettings(settings: UserSettings): EarningSettings {
            return EarningSettings(
                salaryPeriod = settings.salaryPeriod,
                salaryAmount = settings.salaryAmountYuan.toDisplayString(),
                workStartMinutes = settings.workStartMinutes,
                workEndMinutes = settings.workEndMinutes,
                workdayMode = settings.workdayMode,
                workDays = settings.workDays,
                chinaLegalCalendar = settings.chinaLegalCalendar,
                themeMode = settings.themeMode,
            )
        }
    }
}

private fun Double.toDisplayString(): String {
    return if (this % 1.0 == 0.0) {
        toLong().toString()
    } else {
        toString()
    }
}
