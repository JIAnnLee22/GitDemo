package com.jiannlee22.gitdemo

import java.nio.charset.Charset

class mmcallDemo {
}

fun main() {
	val str = "1418"
	//消息转字节流
	val strBytes = str.toByteArray(Charset.forName("GBK"))
	//计算消息字节长度
	val strByteLen = strBytes.size
	val fillStrByte = ByteArray(48)
	System.arraycopy(strBytes, 0, fillStrByte, 0, strByteLen)
	fillStrByte.fill(0xff.toByte(), strByteLen, fillStrByte.size)
	/*
	创建USB协议数据,
	帧头2,地址1,命令1,
	数据长度1,
	文字长度2,ascii码
	消息长度->strByteLen
	校验和1
	*/
	val usbData = ByteArray(64)
	//下标
	var len = 0
	//装载数据
	//帧头x2 0x55 0xaa
	usbData[len++] = 0x55.toByte()
	usbData[len++] = 0xaa.toByte()
	//地址
	usbData[len++] = 0x01.toByte()
	//命令0xd1上传 0xb1发送
	usbData[len++] = 0xb1.toByte()
	//数据长度
	usbData[len++] = (fillStrByte.size + 2).toByte()
	//消息长度ascii码
	//usbData[len++] = ((strByteLen / 10).toChar()).toByte()
	//usbData[len++] = ((strByteLen % 10).toChar()).toByte()
	usbData[len++] = 0x00.toByte()
	usbData[len++] = 0x01.toByte()
	//复制消息数据部分
	System.arraycopy(fillStrByte, 0, usbData, len, fillStrByte.size)
	len += fillStrByte.size
	//校验和
	usbData[len++] = (checkSum(usbData).toInt().inv() - 1).toByte()
	usbData.fill(0xff.toByte(), len, usbData.size)
	
	for (usbDatum in usbData) {
		print((usbDatum.toInt() and 0xff).toString(16))
		print(" ")
	}
	
}




//校验和
private fun checkSum(bytes: ByteArray): Byte {
	var sum = 0
	for (i in bytes.indices) {
		sum += bytes[i].toInt() and 0xff
	}
	return sum.toByte()
}
