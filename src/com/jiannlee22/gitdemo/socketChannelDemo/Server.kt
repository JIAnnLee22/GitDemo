package com.jiannlee22.gitdemo.socketChannelDemo

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

fun main() {
	Server().ServerThread().start()
}

class Server {
	var serverSocketChannel: ServerSocketChannel? = null
	lateinit var selector: Selector
	
	inner class ServerThread : Thread() {
		override fun run() {
			super.run()
			selector = Selector.open()
			serverSocketChannel = ServerSocketChannel.open()
			serverSocketChannel?.configureBlocking(false)
			serverSocketChannel?.socket()?.bind(InetSocketAddress(5005))
			serverSocketChannel?.register(selector, SelectionKey.OP_ACCEPT)
			while (true) {
				selector.select()
				val keys = selector.selectedKeys()
				val iter = keys.iterator()
				while (iter.hasNext()) {
					val key = iter.next()
					iter.remove()
					if (key.isAcceptable) {//接入
						dealAccept(key)
					} else if (key.isReadable) {//读取
						dealRead(key)
					}
				}
			}
			
		}
		
		private fun dealAccept(key: SelectionKey?) {
			val server = key?.channel() as ServerSocketChannel
			val channel = server.accept()
			channel.configureBlocking(false)
			channel.write(ByteBuffer.wrap(bytedata()))
			channel.register(selector, SelectionKey.OP_READ)
			
		}
		
		fun bytedata() = byteArrayOf(
			0x1b, 0xb, 0x0, 0x1, 0x0,
			0x99.toByte(), 0x99.toByte(), 0x1, 0x3, 0xa, 0x2, 0x0, 0xb1.toByte(), 0xfe.toByte(), 0x1b
		)
		
		private fun dealRead(key: SelectionKey?) {
			val channel = key?.channel() as SocketChannel
			
			val byteBuffer = ByteBuffer.allocate(128)
			val dataSize = channel.read(byteBuffer)
			val byteData = ByteArray(dataSize)
			System.arraycopy(byteBuffer.array(), 0, byteData, 0, dataSize)
			val str = byteData.toString(Charset.forName("GBK"))
			println(byteData.string())
			//send(str)
			//channel.write(ByteBuffer.wrap("已读".toByteArray()))
		}
	}
	
	fun send(msg: String) {
		val keys = selector.keys()
		for (key in keys) {
			if (key is SocketChannel) {
				val channel = key.channel() as SocketChannel
				channel.write(ByteBuffer.wrap(msg.toByteArray(Charsets.UTF_8)))
			}
		}
	}
	
}

