package com.banking

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {

    private lateinit var account: Account

    @BeforeAll
    fun setup() {
        account = Account()
    }

    companion object {
        @JvmStatic
        fun depositAmountArguments() = listOf(
            Arguments.of(1000, 1000),
            Arguments.of(500, 1500),
        )
    }

    @MethodSource("depositAmountArguments")
    @ParameterizedTest
    fun `should deposit correctly`(depositAmount: Int, expectedBalance: Int) {
        account.deposit(depositAmount)
        assertEquals(expectedBalance, account.balance)
    }
}