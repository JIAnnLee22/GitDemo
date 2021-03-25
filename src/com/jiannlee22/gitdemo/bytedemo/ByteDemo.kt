package com.jiannlee22.gitdemo.bytedemo

class ByteDemo {

}

fun intToByteArray2(num: Int): ByteArray {
	val byteArray = ByteArray(2)
	val lowH = ((num shr 8) and 0xff).toByte()
	val lowL = (num and 0xff).toByte()
	byteArray[0] = lowH
	byteArray[1] = lowL
	return byteArray
}

fun intToByteArray4(n: Int): ByteArray {
	val b = ByteArray(4)
	b[3] = (n and 0xff).toByte()
	b[2] = (n shr 8 and 0xff).toByte()
	b[1] = (n shr 16 and 0xff).toByte()
	b[0] = (n shr 24 and 0xff).toByte()
	return b
}
fun toInt(b: ByteArray): Int {
	var res = 0
	for (i in b.indices) {
		res += b[i].toInt() and 0xff shl (3 - i) * 8
	}
	return res
}

fun byteArray2Int(bytes: ByteArray) {
	val numByte = ByteArray(4)
	System.arraycopy(bytes, 0, bytes, 0, bytes.size)
	numByte[0].toInt() shl 24
}

//bytearray数组输出
fun ByteArray.string() = run {
	val str = StringBuilder()
	str.append("[")
	for (i in this.indices) {
		str.append((this[i].toInt() and 0xff).toString(16))
		if (i < this.size - 1) {
			str.append(" ,")
		} else {
			str.append("]")
		}
	}
	str
}

fun main() {
	val j = intToByteArray2(299.inv())
	println(170.toByte())
	for (byte in j) {
		println((byte.toInt() and 0xff).toString(16))
	}
}
