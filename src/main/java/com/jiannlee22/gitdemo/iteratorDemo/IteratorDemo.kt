package com.jiannlee22.gitdemo.iteratorDemo

import java.util.*
import java.util.Arrays

import java.util.ArrayList
import java.util.stream.Collectors







class IteratorDemo {
}

fun main() {
	val bytes = intArrayOf(3, 2, 2, 3, 2, 2, 3, 2, 2).toMutableList()
	
	val li = bytes.listIterator()
	while (li.hasNext()) {
		if (li.next() == 3) {
			li.remove()
			val next = li.next()
			if (next == 1) {
				li.set(3)
			} else if (next == 2) {
				li.set(4)
			} else {
				li.set(3)
				li.add(next)
			}
		}
	}
	for (i in bytes) {
		print(i)
	}
}