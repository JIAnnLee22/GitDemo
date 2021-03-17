package com.gvs.m.ns01.utils

import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket

class TCPServer {
	
	//server单例
	companion object {
		@JvmStatic
		val instance by lazy { TCPServer() }
	}
	
	//Socket实例
	private var mSocket: Socket? = null
	
	//Socket线程
	private var mSocketThread: SocketThread? = null
	
	//监听接口
	private lateinit var callback: BoundCallback
	
	//监听状态
	private val isBound: Boolean
		get() {
			var flag = false
			if (mSocket != null) {
				flag = mSocket!!.isBound
			}
			return flag
		}
	
	fun bind(port: Int, callback: BoundCallback) {
		this.callback = callback
		mSocketThread = SocketThread(port)
		mSocketThread?.start()
	}
	
	private inner class SocketThread(val port: Int) : Thread() {
		override fun run() {
			super.run()
			try {
				if (mSocket != null) {
					mSocket?.close()
					mSocket = null
				}
				val serverSocket = ServerSocket(port)
				
				//在监听的时候不断监听端口的数据请求
				while (true) {
					dealMsg(serverSocket.accept())
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
		
		private fun dealMsg(socket: Socket) {
			Thread {
				run {
					try {
						val ips = socket.getInputStream()
						val dis = DataInputStream(ips)
						sleep(100)
						val len = dis.available()
						val data = ByteArray(len)
						dis.read(data)
						callback.callback(data)
						dis.close()
						ips.close()
						//socket.shutdownInput()
						//socket.close()
					} catch (e: Exception) {
						e.printStackTrace()
					}
				}
			}.start()
		}
	}
	
	interface BoundCallback {
		fun callback(data: ByteArray)
	}
	
}

fun main() {
	TCPServer.instance.bind(5005, object : TCPServer.BoundCallback {
		override fun callback(data: ByteArray) {
			for (byte in data) {
				byte.toInt().also {
					print((it and 0xff).toString(16))
					print(" ")
				}
			}
			println()
		}
		
	})
}