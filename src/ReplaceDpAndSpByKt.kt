package fitscreen

import java.io.File

/**
 *  @author      : 楠GG
 *  @date        : 2017/12/3 21:50
 *  @description : 替换文件夹下文件中含有dp、sp成生成的dimen值（配合GenerateFiles.jar使用）
 *  说明：此为 ReplaceDpAndPx.java 文件的 kotlin版 (sp 写成了 px，原谅 =_=!!)
 *      去掉了之前输入的部分代码，直接导入工程，运行main方法即可；
 *      当然，需要根据你需要替换的文件夹/文件绝对路径 替换 projectPath 字段即可
 */
class ReplaceDpAndSpByKt(path: String) {

    init {
        // 操作目录。从该目录开始。该文件目录下及其所有子目录的文件都将被替换
        operatorXmlFile(File(path))
    }

    private fun operatorXmlFile(file: File) {
        val files = file.listFiles()
        files?.let {
            it.forEach {
                if (it.isFile && it.name.endsWith(".xml")) //是xml文件则执行操作
                    operatorFile(it)
                if (it.isDirectory) {
                    operatorXmlFile(it)
                }
            }
        }
    }

    private fun operatorFile(file: File) {
        val tempFile = File(file.absolutePath + ".tmp")

        file.bufferedReader().useLines { reader ->
            tempFile.bufferedWriter().use { writer ->
                reader.forEach {
                    if ("@dimen/xdp_" !in it && "dp\"" in it &&
                            (it[it.indexOf("dp\"") - 1] + "").matches(Regex("\\d"))) {
                        //如果已经修改过了，跳过，没修改过则修改dp
                        val split = it.split("=\"")
                        val dpNum = split[1].replace("dp", "")
                        val result = split[0] + "=\"@dimen/xdp_" + dpNum
                        writer.write(result + "\n")
                    } else if ("@dimen/xsp_" !in it && "sp\"" in "sp\"" &&
                            (it[(it.indexOf("sp\"")) - 1] + "").matches(Regex("\\d"))) {
                        //如果已经修改过了，跳过，没修改过则修改sp
                        val split = it.split("=\"")
                        val spNum = split[1].replace("sp", "")
                        val result = split[0] + "=\"@dimen/xsp_" + spNum
                        writer.write(result + "\n")
                    } else writer.write(it + "\n")
                }
            }
        }
        //删除源文件，重命名新文件
        file.delete()
        tempFile.renameTo(File(file.absolutePath))
    }
}

fun main(args: Array<String>) {
    //代码测试：假设有一个test文件夹，test文件夹下含有若干文件.
    //把test目录下所有（如果有）文件中含有"xxdp"、"xxsp"替换成新的"@dimen/xdp_xx"、"@dimen/xsp_xx"。
    val projectPath = "./"
    ReplaceDpAndSpByKt(projectPath)
}