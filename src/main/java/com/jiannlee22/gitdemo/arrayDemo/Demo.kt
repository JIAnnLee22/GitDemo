package com.jiannlee22.gitdemo.arrayDemo

fun main() {
	val a = intArrayOf(3, 3, 3, 2, 5, 3)
	//escape(a)
	for (i in remove(a)) {
		println(i)
	}
}

fun remove(a: IntArray): IntArray {
	val tmp = IntArray(a.size - 1)
	for (i in a) {
		if (i == 2) {
			val index = a.indexOf(i)
			if (index > 0 || index < a.size) {
				if (a[index - 1] == 1) {
					a[index - 1] = 0
				}
			}
			System.arraycopy(a, 0, tmp, 0, index)
			System.arraycopy(a, index + 1, tmp, index, tmp.size - index)
			return remove(tmp)
		} else if (a.indexOf(i) == a.size - 1) {
			return a
		}
	}
	return a
}

//处理协议转义字符
private fun escape(data: IntArray): IntArray {
	val tmp = IntArray(data.size + 1)
	for (i in data.indices) {
		if (data[i] == 3) {
			//val index = data.indexOf(i)
			if (!(i == 0 || i == data.size - 1)) {
				System.arraycopy(data, 0, tmp, 0, i)
				System.arraycopy(data, i, tmp, i + 1, data.size - i)
				tmp[i] = 1
				tmp[i + 1] = 2
				return escape(tmp)
			}
		} else if (data.indexOf(i) == data.size - 1) {
			return data
		}
	}
	return data
}
