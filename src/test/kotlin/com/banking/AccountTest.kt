package com.banking

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {

    private lateinit var account: Account

    @BeforeAll
    fun setup() {
        account = Account()
    }

    @Test
    fun `should deposit into an account correctly`() {
        account.deposit(1000)
        assertEquals(1000, account.getBalance())
    }

    @Test
    fun `should deposit into an account that its balance is more than 0 correctly`() {
        account.deposit(500)
        assertEquals(1500, account.getBalance())
    }
}