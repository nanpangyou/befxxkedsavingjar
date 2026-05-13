package com.z.money.data

import com.z.money.domain.SalaryInput
import com.z.money.domain.WorkSchedule

fun UserSettings.toSalaryInput(): SalaryInput {
    return SalaryInput(
        amountCents = (salaryAmountYuan.coerceAtLeast(0.0) * 100).toLong(),
        period = salaryPeriod,
    )
}

fun UserSettings.toWorkSchedule(): WorkSchedule {
    return WorkSchedule(
        annualWorkDays = annualWorkDays.coerceAtLeast(1),
        dailyWorkHours = dailyWorkHours.coerceAtLeast(0.1),
    )
}
