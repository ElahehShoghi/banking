package com.banking

class Account {

    fun deposit(amount: Int) {
    }

    fun withdraw(amount: Int) {
        throw InsufficientBalanceException()
    }

}
