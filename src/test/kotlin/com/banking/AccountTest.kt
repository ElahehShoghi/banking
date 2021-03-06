package com.banking

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AccountTest {
    private lateinit var account: Account

    @BeforeEach
    fun initializeAccount() {
        val dateProvider = mockk<Account.DateProvider>()
        every { dateProvider() } returns "14/07/2022"
        account = Account(mockk(), dateProvider)
    }

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

        @JvmStatic
        fun depositsAndStatementsArguments() = listOf(
            Arguments.of(
                listOf(1000, 3000),
                listOf("14/07/2022 | 1000.00 | 1000.00", "14/07/2022 | 3000.00 | 4000.00")
            ),
            Arguments.of(
                listOf(1000),
                listOf("14/07/2022 | 1000.00 | 1000.00")
            ),
            Arguments.of(
                listOf<Int>(),
                listOf<String>()
            ),
            Arguments.of(
                listOf(1000, 5000, 3000),
                listOf(
                    "14/07/2022 | 1000.00 | 1000.00",
                    "14/07/2022 | 5000.00 | 6000.00",
                    "14/07/2022 | 3000.00 | 9000.00"
                )
            ),
            Arguments.of(
                listOf(1000, 5000, -3000),
                listOf(
                    "14/07/2022 | 1000.00 | 1000.00",
                    "14/07/2022 | 5000.00 | 6000.00",
                    "14/07/2022 | -3000.00 | 3000.00"
                )
            ),
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
    fun `should print account statement after deposit on 13th of July`() {


        val printer = mockk<(input: String) -> Unit>()
        every { printer(any()) } just runs
        val dateProvider = mockk<() -> String>()
        every { dateProvider() } returns "13/07/2022"

        val account = Account(printer, dateProvider)
        account.deposit(1000)
        account.printStatement()

        verify(ordering = Ordering.ORDERED) {
            printer("DATE       | AMOUNT  | BALANCE")
            printer("13/07/2022 | 1000.00 | 1000.00")
        }
        confirmVerified(printer)
    }

    @ParameterizedTest
    @MethodSource("depositsAndStatementsArguments")
    fun `should print account statement after deposits`(deposits: List<Int>, statements: List<String>) {
        val printer = mockk<(input: String) -> Unit>()
        every { printer(any()) } just runs
        val dateProvider = mockk<() -> String>()
        every { dateProvider() } returns "14/07/2022"

        val account = Account(printer, dateProvider)
        deposits.forEach {
            if (it > 0)
                account.deposit(it)
            else account.withdraw(-it)
        }
        account.printStatement()

        verify(ordering = Ordering.ORDERED) {
            printer("DATE       | AMOUNT  | BALANCE")
            statements.forEach { printer(it) }
        }
        confirmVerified(printer)
    }

    @Test
    fun `should print account statement after deposit on different days`() {
        val printer = mockk<(input: String) -> Unit>()
        every { printer(any()) } just runs
        val dateProvider = mockk<() -> String>()
        every { dateProvider() } returns "13/07/2022"

        val account = Account(printer, dateProvider)
        account.deposit(1000)
        every { dateProvider() } returns "15/07/2022"
        account.deposit(2000)

        account.printStatement()

        verify(ordering = Ordering.ORDERED) {
            printer("DATE       | AMOUNT  | BALANCE")
            printer("13/07/2022 | 1000.00 | 1000.00")
            printer("15/07/2022 | 2000.00 | 3000.00")
        }
        confirmVerified(printer)
    }
}