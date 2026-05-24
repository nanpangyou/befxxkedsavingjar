package com.z.money.ui.main

import com.z.money.data.WorkdayMode

val MINUTE_OPTIONS = (0..59).toList()
val WORKDAY_MODE_OPTIONS = listOf(WorkdayMode.ChinaLegal, WorkdayMode.FixedWeekly)

const val MINUTES_PER_HOUR = 60
const val MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR
const val SECONDS_PER_HOUR = 3_600
const val PROJECT_URL = "https://github.com/nanpangyou/BeFxxkedSavingJar"
const val FEEDBACK_URL = "$PROJECT_URL/issues/new"
const val PRIVACY_URL = "$PROJECT_URL/blob/main/PRIVACY.md"
