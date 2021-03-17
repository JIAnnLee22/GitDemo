package com.jiannlee22.gitdemo.bytedemo

class ByteDemo {

}

private fun intToByteArray2(num: Int): ByteArray {
	val byteArray = ByteArray(2)
	val lowH = ((num shr 8) and 0xff).toByte()
	val lowL = (num and 0xff).toByte()
	byteArray[0] = lowH
	byteArray[1] = lowL
	return byteArray
}

fun main() {
	val j = intToByteArray2(0x0102)
	println(170.toByte())
	for (byte in j) {
		println(byte.toString().toInt())
	}
}
