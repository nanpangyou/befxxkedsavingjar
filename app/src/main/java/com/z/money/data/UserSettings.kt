package com.z.money.data

import com.z.money.domain.SalaryPeriod

data class UserSettings(
    val salaryPeriod: SalaryPeriod = SalaryPeriod.Monthly,
    val salaryAmountYuan: Double = 10_000.0,
    val annualWorkDays: Int = 250,
    val workStartMinutes: Int = 9 * 60,
    val workEndMinutes: Int = 17 * 60,
)
