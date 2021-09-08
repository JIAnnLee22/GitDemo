package com.jiannlee22.gitdemo.bytedemo

import kotlin.experimental.and
import kotlin.jvm.JvmStatic

object ByteJavaDemo {
	@JvmStatic
	fun main(args: Array<String>) {
		val bytes = ByteArray(4)
		for (i in 0..3) {
			bytes[i] = (i + 190 and 0xFF).toByte()
		}
		for (aByte in bytes) {
			println(aByte and 0xff.toByte())
		}
	}
}