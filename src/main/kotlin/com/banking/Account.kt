package com.banking

class Account(private val printer: StatementPrinter, private val dateProvider: DateProvider) {

    fun interface StatementPrinter {
        operator fun invoke(input: String)

        data class StatementItem(val date: String, val amount: Int, val balance: Int)
    }

    fun interface DateProvider {
        operator fun invoke(): String
    }

    private var balance = 0
    private val statements: MutableList<String> = mutableListOf("DATE       | AMOUNT  | BALANCE")

    fun deposit(amount: Int) {
        balance += amount
        statements.add(createStatementElement(amount))
    }

    private fun createStatementElement(amount: Int) = "${dateProvider()} | $amount.00 | $balance.00"

    fun withdraw(amount: Int) {
        if (balance < amount)
            throw InsufficientBalanceException()
        balance -= amount
        statements.add(createStatementElement(-amount))
    }

    fun printStatement() {
        statements.forEach { printer.invoke(it) }
    }

}


fun main() {
}