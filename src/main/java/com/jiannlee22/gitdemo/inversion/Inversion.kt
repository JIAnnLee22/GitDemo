package com.jiannlee22.gitdemo

fun main() {
    val customer = Customer()
    customer.shop(HeYuan())
    customer.shop(DongYuan())
}

interface Sell{
    fun sell()
}

class Customer{
    fun shop(sell:Sell){
        sell.sell()
    }
}

class HeYuan:Sell{
    override fun sell() {
        println("shop heyuan")
    }
}

class DongYuan:Sell{
    override fun sell() {
        println("shop dongyuan")
    }
}
