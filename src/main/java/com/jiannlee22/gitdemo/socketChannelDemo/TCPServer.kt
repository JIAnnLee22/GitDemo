package com.jiannlee22.gitdemo.socketChannelDemo

import com.jiannlee22.gitdemo.bytedemo.string
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

open class TCPServer {
	
	//server单例
	companion object {
		@JvmStatic
		val instance by lazy { TCPServer() }
	}
	
	//
	lateinit var selector: Selector
	
	//Socket实例
	private var mSocketChannel: SocketChannel? = null
	
	//Socket线程
	private var mSocketThread: SocketThread? = null
	
	//监听接口
	private lateinit var callback: BoundCallback
	
	fun bind(port: Int, callback: BoundCallback) {
		this.callback = callback
		mSocketThread = SocketThread(port)
		mSocketThread?.start()
	}
	
	private inner class SocketThread(val port: Int) : Thread() {
		override fun run() {
			super.run()
			try {
				if (mSocketChannel != null) {
					mSocketChannel?.close()
					mSocketChannel = null
				}
				selector = Selector.open()
				val serverSocketChannel = ServerSocketChannel.open()
				serverSocketChannel.configureBlocking(false)
				serverSocketChannel.bind(InetSocketAddress(5005))
				serverSocketChannel.socket().reuseAddress = true
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
				
				//在监听的时候不断监听端口的数据请求
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
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
		private fun dealAccept(key: SelectionKey?) {
			val server = key?.channel() as ServerSocketChannel
			val channel = server.accept()
			channel.configureBlocking(false)
			//channel.write(ByteBuffer.wrap("hello".toByteArray(Charsets.UTF_8)))
			channel.register(selector,SelectionKey.OP_READ)
		}
		
//		private fun dealSocket(key: SelectionKey) {
//			Thread {
//				run {
//					try {
//						val socketChannel = key.channel() as ServerSocketChannel
//						val ips = socketChannel.socket().getInputStream()
//						val bis = BufferedInputStream(ips)
//						val dis = DataInputStream(bis)
//						//val dis = DataInputStream(ips)
//						sleep(100)
//						val len = dis.available()
//						val data = ByteArray(len)
//						dis.read(data)
//						callback.callback(dealData(data))
//					} catch (e: Exception) {
//						e.printStackTrace()
//					}
//				}
//			}.start()
//		}
		
		private fun dealRead(key: SelectionKey?) {
			val channel = key?.channel() as SocketChannel
			val byteBuffer = ByteBuffer.allocate(128)
			channel.read(byteBuffer)
			val byteData = byteBuffer.array()
			println(byteData.string())
		}
		
		private fun dealData(data: ByteArray): ByteArray {
			if (data.isEmpty()) {
				return ByteArray(0)
			}
			//return data
			return unescape(data)
		}
		
	}
	
	interface BoundCallback {
		fun callback(data: ByteArray)
	}
	
	
}

//处理协议转义字符
fun unescape(data: ByteArray): ByteArray {
	val tmp = ByteArray(data.size - 1)
	for (i in data.indices) {
		if (data[i] == (0x02).toByte()) {
			//val index = data.indexOf(i)
			if (i > 0 || i < data.size) {
				if (data[i - 1] == (0x1a).toByte()) {
					data[i - 1] = (0x1b).toByte()
					System.arraycopy(data, 0, tmp, 0, i)
					System.arraycopy(data, i + 1, tmp, i, tmp.size - i)
					return unescape(tmp)
				}
			}
		} else if (i == data.size - 1) {
			return data
		}
	}
	return data
}

//处理协议转义字符
fun escape(data: ByteArray): ByteArray {
	val tmp = ByteArray(data.size + 1)
	for (i in data.indices) {
		if (data[i] == (0x1b).toByte()) {
			if (!(i == 0 || i == data.size - 1)) {
				System.arraycopy(data, 0, tmp, 0, i)
				System.arraycopy(data, i, tmp, i + 1, data.size - i)
				tmp[i] = (0x1a).toByte()
				tmp[i + 1] = (0x02).toByte()
				return escape(tmp)
			}
		} else if (i == data.size - 1) {
			return data
		}
	}
	return data
}

fun finalData(data: ByteArray) {
	val dataLen = bytes2Int(data[2], data[1])
	val dataType = data[7].toInt() and 0xff
	val usbData: ByteArray
	when (dataType) {
		1 -> {
			println("注册命令")
		}
		2 -> {
			println("心跳命令")
		}
		4 -> {
			println("通信命令")
			usbData = ByteArray(data.size - 15)
			//获取usb协议数据
			System.arraycopy(data, 12, usbData, 0, usbData.size)
			dealUsbData(usbData)
			usbData.string()
		}
	}
	println("dataLen->$dataLen")
}

fun dealUsbData(usbData: ByteArray) {
	val dataAddress = usbData[2]
	val dataCmd = usbData[3].toInt() and 0xff
	val dataLen = usbData[4].toInt() and 0xff
	val infoData: ByteArray
	when (dataCmd) {
		//上传发送的数据
		0xd1 -> {
			println("接收到上传的数据")
			infoData = ByteArray(dataLen)
			System.arraycopy(usbData, 5, infoData, 0, infoData.size)
			dealInfoData(infoData)
		}
		//按照信息机编号控制发送
		0xb1 -> {
		
		}
		//按照信息机地址参数控制发送
		0xb2 -> {
		
		}
		//按照呼叫器编号控制发送
		0xb4 -> {
		
		}
	}
}

fun dealInfoData(info: ByteArray) {
	val numStr = StringBuilder().append(info[0].toChar()).append(info[1].toChar()).toString()
	val infoLen = numStr.toInt()
	val infoTextByte = ByteArray(infoLen)
	System.arraycopy(info, 2, infoTextByte, 0, infoLen)
	val infoStr = String(infoTextByte, Charset.forName("GBK"))
	println("infoLen->$infoLen infoStr->$infoStr")
}

fun ByteArray.string() {
	if (this.isEmpty()) {
		print("")
	} else {
		val strBuilder = StringBuilder()
		strBuilder.append("byteArray->[")
		for (byte in this) {
			strBuilder.append((byte.toInt() and 0xff).toString(16))
				.append(" , ")
		}
		strBuilder.append("]")
		println(strBuilder.toString())
	}
}

fun bytes2Int(byteH: Byte, byteL: Byte) =
	(byteH.toInt() and 0xff) shr 8 or (byteL.toInt() and 0xff)


fun main() {
	TCPServer.instance.bind(5005, object : TCPServer.BoundCallback {
		override fun callback(data: ByteArray) {
			data.string()
			if (data.isNotEmpty()) {
				finalData(data)
			}
		}
	})
}