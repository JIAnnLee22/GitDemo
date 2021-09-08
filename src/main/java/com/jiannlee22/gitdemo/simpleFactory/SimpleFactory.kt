package com.jiannlee22.gitdemo.simpleFactory

fun main() {
    var car: Product
    val carA: Car = CarAFactory()
    val carB: Car = CarBFactory()
    car = carA.newProduct()
    car.show()
    car.test()
    car = carB.newProduct()
    car.show()
    car.test()
}

interface Car {
    fun newProduct(): Product
}

interface Product {
    fun show()
    fun test(){
        println("not implement")
    }
}

class CarA : Product {
    override fun show() {
        println("CarA show")
    }

    override fun test() {
        println("CarA test")
    }
}

class CarB : Product {
    override fun show() {
        println("CarB show")
    }
}

class CarAFactory : Car {
    override fun newProduct(): Product {
        println("new CarA")
        return CarA()
    }
}

class CarBFactory : Car {
    override fun newProduct(): Product {
        println("new CarB")
        return CarB()
    }
}