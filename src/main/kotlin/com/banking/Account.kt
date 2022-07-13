package com.banking

class Account(private val printer: (input: String) -> Unit) {
    private var balance = 0

    fun deposit(amount: Int) {
        balance += amount
    }

    fun withdraw(amount: Int) {
        if (balance < amount)
            throw InsufficientBalanceException()
        balance -= amount
    }

    fun printStatement() {
        printer("DATE       | AMOUNT  | BALANCE")
    }

}


fun main() {
    val printer: (input: String) -> Unit = { println(it) }
    val account = Account(printer)

    account.printStatement()
}