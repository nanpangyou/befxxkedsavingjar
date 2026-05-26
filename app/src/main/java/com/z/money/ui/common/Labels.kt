package com.z.money.ui.common

import com.z.money.data.WorkdayMode
import com.z.money.data.WorkdaySource
import com.z.money.domain.SalaryPeriod
import com.z.money.domain.WorkdayStatus
import java.time.DayOfWeek

val WorkdayStatus.label: String
    get() = when (this) {
        WorkdayStatus.NotWorkday -> "\u4eca\u5929\u4e0d\u662f\u5de5\u4f5c\u65e5"
        WorkdayStatus.BeforeWork -> "\u8fd8\u6ca1\u5f00\u59cb\u4e0a\u73ed"
        WorkdayStatus.Working -> "\u6b63\u5728\u6253\u5de5"
        WorkdayStatus.AfterWork -> "\u4eca\u5929\u5df2\u7ecf\u4e0b\u73ed"
    }

val WorkdaySource.label: String
    get() = when (this) {
        WorkdaySource.FixedWorkday -> "\u6bcf\u5468\u56fa\u5b9a\u5de5\u4f5c\u65e5"
        WorkdaySource.FixedOffDay -> "\u6bcf\u5468\u56fa\u5b9a\u4f11\u606f\u65e5"
        WorkdaySource.ChinaLegalFallbackWorkday -> "\u4e2d\u56fd\u6cd5\u5b9a\uff08\u672a\u540c\u6b65\uff0c\u56fa\u5b9a\u5de5\u4f5c\u65e5\uff09"
        WorkdaySource.ChinaLegalFallbackOffDay -> "\u4e2d\u56fd\u6cd5\u5b9a\uff08\u672a\u540c\u6b65\uff0c\u56fa\u5b9a\u4f11\u606f\u65e5\uff09"
        WorkdaySource.ChinaLegalRegularWorkday -> "\u4e2d\u56fd\u6cd5\u5b9a\u666e\u901a\u5de5\u4f5c\u65e5"
        WorkdaySource.ChinaLegalRegularOffDay -> "\u4e2d\u56fd\u6cd5\u5b9a\u666e\u901a\u4f11\u606f\u65e5"
        WorkdaySource.ChinaLegalAdjustedWorkday -> "\u8c03\u4f11\u8865\u73ed\u65e5"
        WorkdaySource.ChinaLegalHoliday -> "\u6cd5\u5b9a\u4f11\u606f\u65e5"
    }

val SalaryPeriod.label: String
    get() = when (this) {
        SalaryPeriod.Monthly -> "\u6708\u85aa"
        SalaryPeriod.Annual -> "\u5e74\u85aa"
    }

val WorkdayMode.label: String
    get() = when (this) {
        WorkdayMode.FixedWeekly -> "\u6bcf\u5468\u56fa\u5b9a"
        WorkdayMode.ChinaLegal -> "\u4e2d\u56fd\u6cd5\u5b9a"
    }

val DayOfWeek.shortLabel: String
    get() = when (this) {
        DayOfWeek.MONDAY -> "\u4e00"
        DayOfWeek.TUESDAY -> "\u4e8c"
        DayOfWeek.WEDNESDAY -> "\u4e09"
        DayOfWeek.THURSDAY -> "\u56db"
        DayOfWeek.FRIDAY -> "\u4e94"
        DayOfWeek.SATURDAY -> "\u516d"
        DayOfWeek.SUNDAY -> "\u65e5"
    }
