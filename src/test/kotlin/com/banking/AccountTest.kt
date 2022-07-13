package com.banking

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AccountTest {

    private val account = Account()

    companion object {
        @JvmStatic
        fun depositAmountArguments() = listOf(
            Arguments.of(1000),
            Arguments.of(500),
        )
    }

    @MethodSource("depositAmountArguments")
    @ParameterizedTest
    fun `should deposit correctly`(depositAmount: Int) {
        account.deposit(depositAmount)
    }
}