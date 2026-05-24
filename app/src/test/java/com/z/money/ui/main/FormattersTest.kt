package com.z.money.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {
    @Test
    fun fractionDigitsUseTwoPlacesForOneCentOrMore() {
        assertEquals(2, fractionDigitsForCents(1.0))
        assertEquals(2, fractionDigitsForCents(123.45))
    }

    @Test
    fun fractionDigitsUseFourPlacesForSmallVisibleAmounts() {
        assertEquals(4, fractionDigitsForCents(0.99))
        assertEquals(4, fractionDigitsForCents(0.01))
    }

    @Test
    fun fractionDigitsUseSixPlacesForTinyAmounts() {
        assertEquals(6, fractionDigitsForCents(0.009))
    }

    @Test
    fun timeTextSupportsEndOfDay() {
        assertEquals("00:00", 0.toTimeText())
        assertEquals("09:05", (9 * 60 + 5).toTimeText())
        assertEquals("24:00", (24 * 60).toTimeText())
    }
}
