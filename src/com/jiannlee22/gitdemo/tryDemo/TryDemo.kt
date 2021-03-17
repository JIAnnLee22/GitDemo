package com.jiannlee22.gitdemo.tryDemo

import java.lang.Exception

fun main(){
	try{
		object : Thread() {
			override fun run() {
				super.run()
				sleep(2000)
				println("in thread")
				println("out thread")
			}
		}.start()
	}catch (e:Exception){
		e.printStackTrace()
	}
}
