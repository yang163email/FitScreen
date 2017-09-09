package jv;

import java.io.*;

/*
 * 替换文件夹下文件中含有dp、sp成生成的dimen值（配合GenerateFiles.jar使用）
 *
 * 原理：逐行读取源文件的内容，一边读取一边同时写一个*.tmp的文件。
 * 当读取的行中发现有需要被替换和改写的目标内容‘行’时候，用新的内容‘行’替换之。
 *  最终，删掉源文件，把*.tmp的文件重命名为源文件名字。
 *
 * 注意！替换是基于‘行’，逐行逐行的替换！
 *     本类适用于layout文件夹下xml布局文件，drawable文件下的dp、sp
 *     id命名请不要以数字+dp  或者 数字+sp结尾，出现误伤请自行解决。
 *     在使用本工具之前请将你的代码备份，避免不可挽回的损失！！！
 *
 * 使用方式：cmd下面运行命令:   java -jar xxx.jar 文件夹绝对路径
 * 		注意:文件夹绝对路径格式：1.D:/test/test/...
 *                       2.D:\\test\\test\\...
 *                       请勿使用单反斜杠('\')，否则无法识别
 * */
public class ReplaceDpAndPx {

    public static void main(String[] args) {
        //代码测试：假设有一个test文件夹，test文件夹下含有若干文件.
        //把test目录下所有（如果有）文件中含有"xxdp"、"xxsp"替换成新的"@dimen/xdp_xx"、"@dimen/xsp_xx"。
        String projectPath = "./";
        if(args.length != 0) {
            projectPath = args[0];
//	        Scanner sc= new Scanner(System.in);
//	        projectPath = sc.next();
//	        sc.close();
        }
        new ReplaceDpAndPx(projectPath);
    }

    public ReplaceDpAndPx(String path) {
        // 操作目录。从该目录开始。该文件目录下及其所有子目录的文件都将被替换
        File file = new File(path);
        File[] files = file.listFiles();
        if(files != null && files.length >= 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getName().endsWith(".xml")) //是xml文件则执行操作
                    operationFile(files[i]);
            }
        }
    }

    public void operationFile(File file) {
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // tmpfile为缓存文件，代码运行完毕后此文件将重命名为源文件名字。  
            File tmpfile = new File(file.getAbsolutePath() + ".tmp");

            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpfile));

            String str = null;
            while (true) {
                str = reader.readLine();
                if (str == null)
                    break;

                if (!str.contains("@dimen/xdp_") && str.contains("dp\"") &&
                        (str.charAt(str.indexOf("dp\"") - 1) + "").matches("\\d")) {
                    //如果已经修改过了，跳过，没修改过则修改dp
                    String[] split = str.split("=\"");
                    String dpNum = split[1].replace("dp", "");
                    String result = split[0] + "=\"@dimen/xdp_" + dpNum;
                    writer.write(result + "\n");
                } else if (!str.contains("@dimen/xsp_") && str.contains("sp\"") &&
                        (str.charAt(str.indexOf("sp\"") - 1) + "").matches("\\d")) {
                    //如果已经修改过了，跳过，没修改过则修改sp
                    String[] split = str.split("=\"");
                    String spNum = split[1].replace("sp", "");
                    String result = split[0] + "=\"@dimen/xsp_" + spNum;
                    writer.write(result + "\n");
                } else
                    writer.write(str + "\n");
            }
            is.close();
            writer.close();
            //删除源文件，重命名新文件
            file.delete();
            tmpfile.renameTo(new File(file.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} 