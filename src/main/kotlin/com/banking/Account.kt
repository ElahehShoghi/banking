package com.banking

class Account {
    var balance: Int = 0

    fun deposit(amount: Int) {
        this.balance += amount
    }

}
