package com.jiannlee22.gitdemo.socketChannelDemo

import com.jiannlee22.gitdemo.bytedemo.string
import java.lang.Thread.sleep
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

fun main() {
	Thread {
		run {
			var i = 0
			while (i < 3) {
				sleep(2000)
				client(i)
				i++
			}
		}
	}.start()
}

fun client(num: Int) {
	val selector = Selector.open()
	val channel = SocketChannel.open()
	channel.configureBlocking(false)
	channel.connect(InetSocketAddress("localhost", 8998))
	channel.register(selector, SelectionKey.OP_CONNECT)
	while (true) {
		selector.select()
		val keys = selector.selectedKeys()
		val iter = keys.iterator()
		while (iter.hasNext()) {
			val key = iter.next()
			iter.remove()
			if (key.isConnectable) {
				val channelConnect = key?.channel() as SocketChannel
				if (channelConnect.isConnectionPending) {
					channelConnect.finishConnect()
				}
				channelConnect.configureBlocking(false)
				channelConnect.register(selector, SelectionKey.OP_READ)
				val t = object : Thread() {
					override fun run() {
						super.run()
						val str = "hello server $num"
						channelConnect.write(ByteBuffer.wrap(str.toByteArray(Charsets.UTF_8)))
					}
				}.start()
			} else if (key.isReadable) {
				val buffer = ByteBuffer.allocate(128)
				val dataSize = channel.read(buffer)
				val data = ByteArray(dataSize)
				System.arraycopy(buffer.array(), 0, data, 0, dataSize)
				val msg = String(data).trim()
				println("$num : $msg")
			}
		}
	}
	
}


