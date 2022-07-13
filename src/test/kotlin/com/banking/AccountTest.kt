package com.banking

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun `should deposit into an account correctly`() {
        val accountTest = Account()
        accountTest.depodit(1000)
        assertEquals(1000, accountTest.balance)
    }
}