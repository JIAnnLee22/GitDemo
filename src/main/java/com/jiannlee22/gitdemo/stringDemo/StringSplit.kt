package com.jiannlee22.gitdemo.stringDemo

class StringSplit {

}

fun main(){
	val str = "hahahahaha"
	val spilt1 = str.split("\\s+".toRegex()).toTypedArray()
	spilt1.forEach {
		println(it)
	}
}