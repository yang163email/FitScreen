package jv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/*
 *  创建者:   楠GG
 *  创建时间: 2017/9/9 11:09
 *  描述：    自动生成values-swxxxdp文件夹以及对应dimens.xml文件
 *            里面的值单位为dp、sp
 */
public class GenerateFile {
    //生成文件目录
    private static final String DIR = "res";
    //生成dp个数
    private static final int DP_NUM = 600;
    //生成sp的个数
    private static final int SP_NUM = 50;
    //初始生成个数
    private List<Integer> types;

    public GenerateFile(int baseW, String addition) {
        types = new ArrayList<>();
        types.add(360);
        if (!types.contains(baseW)) {
            types.add(0, baseW);
            if(baseW != 320) {
                types.add(320);
            }
            if(baseW != 411) {
                types.add(411);
            }
        } else {
            types.add(320);
            types.add(411);
        }
        if (!"".equals(addition)) {
            String[] split = addition.split("_");
            for (String s : split) {
                try {
                    types.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    System.out.println("skip invalidate params : w = " + s);
                    e.printStackTrace();
                }
            }
        }
    }

    private void generateFile() {
        File dir = new File(DIR);
        if (!dir.exists()) dir.mkdir();

        //保留1位小数
        DecimalFormat df = new DecimalFormat("#.0");

        BufferedWriter bw = null;
        for (int i = 0; i < types.size(); i++) {
            File typeDir = new File(dir, "values-sw" + types.get(i) + "dp");
            if (!typeDir.exists()) typeDir.mkdir();

            File file = new File(typeDir, "dimens.xml");
            try {
                bw = new BufferedWriter(new FileWriter(file));
                bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                bw.write("<resources>\n");
                //注释内容
                bw.write("\n<!-- the auto generate dimen values with " + types.get(i) + "dp minimum width -->\n");

                /*------------------------------generate dp---------------------------------------*/
                bw.write("\n<!--start generate dp-->\n\n");

                for (int j = 0; j <= DP_NUM; j++) {
                    //生成dp写入语句
                    bw.write("    <dimen name=\"xdp_" + j + "\">" +
                            df.format(types.get(i) * 1.0 / types.get(0) * j) + "dp</dimen>");
                    bw.newLine();
                }

                /*------------------------------generate sp-------------------------------------*/
                //注释内容
                bw.write("\n<!--start generate sp-->\n\n");

                for (int k = 1; k <= SP_NUM; k++) {
                    //生成sp写入语句
                    bw.write("    <dimen name=\"xsp_" + k + "\">" +
                            df.format(types.get(i) * 1.0 / types.get(0) * k) + "sp</dimen>");
                    bw.newLine();
                }
                bw.write("</resources>");
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int baseW = 360;
        String addition = "";
        try {
            if (args.length >= 2) {
                baseW = Integer.parseInt(args[0]);
                addition = args[1];
            } else if (args.length >= 1) {
                addition = args[0];
            }
        } catch (NumberFormatException e) {
            System.err
                    .println("right input params : java -jar xxx.jar width w1_w2_w3...");
            e.printStackTrace();
            System.exit(-1);
        }
        new GenerateFile(baseW, addition).generateFile();
    }
}
