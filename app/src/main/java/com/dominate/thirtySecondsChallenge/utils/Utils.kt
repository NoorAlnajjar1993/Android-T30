package com.dominate.thirtySecondsChallenge.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import kotlin.experimental.or


object Utils {
    // UNICODE 0x23 = #
    val UNICODE_TEXT = byteArrayOf(
        0x23, 0x23, 0x23,
        0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
        0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
        0x23, 0x23, 0x23
    )
    private const val hexStr = "0123456789ABCDEF"
    private val binaryArray = arrayOf(
        "0000", "0001", "0010", "0011",
        "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
        "1100", "1101", "1110", "1111"
    )

    fun decodeBitmap(bmp: Bitmap): ByteArray? {
        val bmpWidth = bmp.width
        val bmpHeight = bmp.height
        val list: MutableList<String> = ArrayList() //binaryString list
        var sb: StringBuffer
        var bitLen = bmpWidth / 8
        val zeroCount = bmpWidth % 8
        var zeroStr = ""
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1
            for (i in 0 until 8 - zeroCount) {
                zeroStr = zeroStr + "0"
            }
        }
        for (i in 0 until bmpHeight) {
            sb = StringBuffer()
            for (j in 0 until bmpWidth) {
                val color = bmp.getPixel(j, i)
                val r = color shr 16 and 0xff
                val g = color shr 8 and 0xff
                val b = color and 0xff

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160) sb.append("0") else sb.append("1")
            }
            if (zeroCount > 0) {
                sb.append(zeroStr)
            }
            list.add(sb.toString())
        }
        val bmpHexList = binaryListToHexStringList(list)
        val commandHexString = "1D763000"
        var widthHexString = Integer
            .toHexString(if (bmpWidth % 8 == 0) bmpWidth / 8 else bmpWidth / 8 + 1)
        if (widthHexString.length > 2) {
            Log.e("decodeBitmap error", " width is too large")
            return null
        } else if (widthHexString.length == 1) {
            widthHexString = "0$widthHexString"
        }
        widthHexString = widthHexString + "00"
        var heightHexString = Integer.toHexString(bmpHeight)
        if (heightHexString.length > 2) {
            Log.e("decodeBitmap error", " height is too large")
            return null
        } else if (heightHexString.length == 1) {
            heightHexString = "0$heightHexString"
        }
        heightHexString = heightHexString + "00"
        val commandList: MutableList<String> = ArrayList()
        commandList.add(commandHexString + widthHexString + heightHexString)
        commandList.addAll(bmpHexList)
        return hexList2Byte(commandList)
    }

    fun binaryListToHexStringList(list: List<String>): List<String> {
        val hexList: MutableList<String> = ArrayList()
        for (binaryStr in list) {
            val sb = StringBuffer()
            var i = 0
            while (i < binaryStr.length) {
                val str = binaryStr.substring(i, i + 8)
                val hexString = myBinaryStrToHexString(str)
                sb.append(hexString)
                i += 8
            }
            hexList.add(sb.toString())
        }
        return hexList
    }

    fun myBinaryStrToHexString(binaryStr: String): String {
        var hex = ""
        val f4 = binaryStr.substring(0, 4)
        val b4 = binaryStr.substring(4, 8)
        for (i in binaryArray.indices) {
            if (f4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
        }
        for (i in binaryArray.indices) {
            if (b4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
        }
        return hex
    }

    fun hexList2Byte(list: List<String>): ByteArray {
        val commandList: MutableList<ByteArray?> = ArrayList()
        for (hexStr in list) {
            commandList.add(hexStringToBytes(hexStr))
        }
        return sysCopy(commandList)
    }

    fun hexStringToBytes(hexString: String?): ByteArray? {
        var hexString = hexString
        if (hexString == null || hexString == "") {
            return null
        }
        hexString = hexString.toUpperCase()
        val length = hexString.length / 2
        val hexChars = hexString.toCharArray()
        val d = ByteArray(length)
        for (i in 0 until length) {
            val pos = i * 2
            d[i] = (charToByte(hexChars[pos]) or charToByte(hexChars[pos + 1])) as Byte
        }
        return d
    }

    fun sysCopy(srcArrays: List<ByteArray?>): ByteArray {
        var len = 0
        for (srcArray in srcArrays) {
            len += srcArray!!.size
        }
        val destArray = ByteArray(len)
        var destLen = 0
        for (srcArray in srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray!!.size)
            destLen += srcArray.size
        }
        return destArray
    }

    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    fun getRealPathFromURI(uri:Uri,context:Context):String{
        val returnCursor=context.contentResolver.query(uri,null,null,null,null)
        returnCursor?.moveToFirst()
        val file=File(context.filesDir,UUID.randomUUID().toString())
        try{
            val inputStream:InputStream?=context.contentResolver.openInputStream(uri)
            val outputStream=FileOutputStream(file)
            var read=0
            val maxBufferSize=1*1024*1024
            val bytesAvailable:Int=inputStream?.available()?:0
            val bufferSize=Math.min(bytesAvailable,maxBufferSize)
            val buffers=ByteArray(bufferSize)
            while(inputStream?.read(buffers).also{
                    if(it!=null){
                        read=it
                    }
                }!=-1){
                outputStream.write(buffers,0,read)
            }
            inputStream?.close()
            outputStream.close()
        }catch(e:java.lang.Exception){
        }
        return file.path
    }


    fun getTitlesFromJsonArray(jsonArrayString: String): List<String> {
        val titles = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(jsonArrayString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.optString("title")
                titles.add(title)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return titles
    }

//    fun parseJson(item: String): ParsedJson? {
//        try {
//            val json = JSONObject(item)
//
//            val type = json.getInt("type")
//            val target = json.getString("target")
//            val argumentsArray = json.getJSONArray("arguments")
//            val arguments = mutableListOf<String>()
//
//            for (i in 0 until argumentsArray.length()) {
//                arguments.add(argumentsArray.getString(i))
//            }
//
//            return ParsedJson(type, target, arguments)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//            return null
//        }
//    }


}