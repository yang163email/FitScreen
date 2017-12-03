package fitscreen

import java.io.BufferedWriter
import java.io.File
import java.text.DecimalFormat


/**
 *  @author      : 楠GG
 *  @date        : 2017/12/3 20:50
 *  @description : 自动生成values-swxxxdp文件夹以及对应dimens.xml文件
 *            里面的值单位为dp、sp
 *
 *            此为 GenerateFile.java 文件的 kotlin 版本，替换掉部分代码，
 *            具体可以参照源码
 */

class GenerateFileByKt(baseW: Int, addition: String) {
    companion object {
        //生成文件目录
        val DIR = "res"
        //生成dp个数
        val DP_NUM = 600
        //生成sp的个数
        val SP_NUM = 50
    }
    //初始生成个数
    private val types = mutableListOf<Int>()

    init {
        types.add(360)
        if (!types.contains(baseW)) {
            types.add(0, baseW)
            if (baseW != 320) types.add(320)
            if (baseW != 411) types.add(411)
        } else {
            types.add(320)
            types.add(411)
        }
        if ("" != addition) {
            val split = addition.split("_")
            split.forEach {
                try {
                    types.add(it.toInt())
                } catch (e: NumberFormatException) {
                    println("skip invalidate params : w = $it")
                    e.printStackTrace()
                }
            }
        }
    }

    fun generateFile() {
        //保留1位小数
        val df = DecimalFormat("#.0")
        types.forEachIndexed { i, value ->
            val typeDir = File("$DIR/values-sw${types[i]}dp")
            if (!typeDir.exists()) typeDir.mkdirs()

            val file = File(typeDir, "dimens.xml")
            val bw: BufferedWriter? = null
            try {
                val bw = file.bufferedWriter()
                bw.write("""<?xml version="1.0" encoding="utf-8"?>""")
                bw.write("\n<resources>\n")
                //注释内容
                bw.write("\n<!-- the auto generate dimen values with ${value}dp minimum width -->\n")

                /*------------------------------generate dp---------------------------------------*/
                bw.write("\n<!--start generate dp-->\n")
                for (j in 0..DP_NUM) {
                    //生成dp写入语句
                    bw.write("""    <dimen name="xdp_$j">${df.format(value * 1.0 / types[0] * j)}dp</dimen>""")
                    bw.newLine()
                }

                /*------------------------------generate sp---------------------------------------*/
                //注释内容
                bw.write("\n<!--start generate sp-->\n")
                for (k in 0..SP_NUM) {
                    //生成sp写入语句
                    bw.write("""    <dimen name="xsp_$k">${df.format(value * 1.0 / types[0] * k)}sp</dimen>""")
                    bw.newLine()
                }
                bw.write("</resources>")
                bw.flush()
                bw.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                bw?.close()
            }
        }
    }
}

fun main(args: Array<String>) {
    val baseW = 360
    val addition = ""

    GenerateFileByKt(baseW, addition).generateFile()
}
