package com.banking

import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AccountTest {

    private val account = Account(mockk())

    companion object {
        @JvmStatic
        fun amountArguments() = listOf(
            Arguments.of(1000),
            Arguments.of(500),
            Arguments.of(200),
            Arguments.of(300),
        )

        @JvmStatic
        fun depositAndWithdrawArguments() = listOf(
            Arguments.of(listOf(1000), listOf(500)),
            Arguments.of(listOf(1000, 1000), listOf(1500)),
        )

        @JvmStatic
        fun depositAndWithdrawInvalidArguments() = listOf(
            Arguments.of(listOf(1000, 1000), listOf(1500), 700),
            Arguments.of(listOf(1000, 1000), listOf(1500), 800),
        )
    }

    @MethodSource("amountArguments")
    @ParameterizedTest
    fun `should deposit correctly`(depositAmount: Int) {
        account.deposit(depositAmount)
    }

    @MethodSource("amountArguments")
    @ParameterizedTest
    fun `should raise exception when withdrawing from empty account`(withdrawAmount: Int) {
        assertThrows<InsufficientBalanceException> { account.withdraw(withdrawAmount) }
    }

    @ParameterizedTest
    @MethodSource("depositAndWithdrawArguments")
    fun `should be able to withdraw after deposit`(depositList: List<Int>, withdrawList: List<Int>) {
        depositList.forEach { account.deposit(it) }
        withdrawList.forEach { account.withdraw(it) }
    }

    @ParameterizedTest
    @MethodSource("depositAndWithdrawInvalidArguments")
    fun `should raise exception when withdrawing more than balance`(
        depositList: List<Int>,
        withdrawList: List<Int>,
        extraWithdrawAmount: Int
    ) {
        `should be able to withdraw after deposit`(depositList, withdrawList)
        assertThrows<InsufficientBalanceException> {
            account.withdraw(extraWithdrawAmount)
        }
    }

    @Test
    fun `should print account statement`() {
        val printer = mockk<(input: String) -> Unit>()
        every { printer("DATE       | AMOUNT  | BALANCE") } just runs

        val account = Account(printer)
        account.printStatement()

        verify { printer("DATE       | AMOUNT  | BALANCE") }
    }
}