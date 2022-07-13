package com.banking

class Account {
    private var balance: Int = 0

    fun deposit(amount: Int) {
        this.balance += amount
    }

    fun getBalance(): Int = balance

}
