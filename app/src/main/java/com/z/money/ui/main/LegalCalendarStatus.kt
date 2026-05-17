package com.z.money.ui.main

fun syncStatus(year: Int): String = "\u6b63\u5728\u540c\u6b65 $year"

fun syncedStatus(year: Int): String = "$year \u5df2\u540c\u6b65"

fun builtInStatus(year: Int): String = "$year \u5df2\u4f7f\u7528\u5185\u7f6e\u6cd5\u5b9a\u65e5\u5386"

fun missingCalendarStatus(year: Int): String =
    "\u672a\u627e\u5230 $year \u6cd5\u5b9a\u65e5\u5386\uff0c\u5df2\u4f7f\u7528\u56fa\u5b9a\u5de5\u4f5c\u65e5"

val syncFailedStatus: String =
    "\u540c\u6b65\u5931\u8d25\uff0c\u5df2\u4f7f\u7528\u56fa\u5b9a\u5de5\u4f5c\u65e5"
