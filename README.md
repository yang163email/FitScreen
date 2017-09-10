# FitScreen
Android屏幕适配的一种方案，包含自动生成xml文件jar包，自动替换dp、sp的jar包。顺便附带上源码。



------

### 背景说明

最开始使用dp设置控件尺寸，在hornor v8 1440x2560的手机显示正常，到了samsung Galaxy On7 1080x1920的手机上显示爆炸。。。(左hornor v8 1440x2560, 右samsung Galaxy On7 1080x1920)

![2k](https://github.com/yang163email/FitScreen/raw/master/img/2k.jpg)  ![1080p](https://github.com/yang163email/FitScreen/raw/master/img/1080p.jpg)



在尝试了N久还是没得结果的情况下，看到一个GitHub上面有一个shell写的脚本可以生成这些文件，奈何拉下来跑了一下没成功，向author发了封邮件也没有结果，自己看了看源码也搞不明白(没学过shell...而且很慢)。想想之前看了鸿洋大神的一篇博客准备自己来搞事情。

本方案在参考多位前人的适配方案下，自己对`android`屏幕适配做了一些总结。



### 适配方式

最小宽度(values-swxxxdp...)形式。不同于生成不同分辨率对应px值，这个方案通过生成对应的dp、sp值来进行适配。

1. 根据公式dp= 160*屏幕宽度px/dpi，得到的就是上面xxx的值。

   dpi可以通过代码获取

```java
DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
Log.d(TAG," densityDpi is "+displayMetrics.densityDpi);
```

2. 安卓手机dpi，分为 120（ldpi）、160（mdpi）、213、240（hdpi）、280、320（xhdpi）、360、400、	420、480（xxhdpi）、560、640（xxxhdpi）; 
3. 一般为以上值。比如我的honor手机dpi是360，Samsung是411，所以我创建了两个values文件夹，values-sw360dp、values-sw411dp，手机会自动找到与其对应的values文件夹。

如需了解更多，请自行google。



### 适配后

![last-2k](https://github.com/yang163email/FitScreen/raw/master/img/last-2k.jpg) ![last-1080](https://github.com/yang163email/FitScreen/raw/master/img/last-1080.jpg)



### 如何使用

我已经将jar文件(jar包下)以及java文件放入仓库中，提供源码进行查看(src包下)。

1. 生成对应的文件夹
   - 将GenerateFile.jar下载下来放入某个目录下，cmd输入`java -jar GenerateFile.jar xxx w1_w2...` 。
   - xxx为基础宽度，必须为整数。w1 w2为需要其他的宽度值，同样需要整数。
   - 默认基础宽度360，还有另外两个320、411。如果不需要设置，忽略xxx以及w1、w2，直接输`java -jar GenerateFile.jar` 回车即ok。
   - 说明：为什么要基础宽度？举个栗子，UI美眉给你的标注图为750x1334，这个是iOS的标注尺寸，对应Android里面可以看成720x1280，看下图，根据上面的公式计算出来的dp值就是360。如果你将360设置为基础宽度，你就可以正常使用dp值，例如320、411的就会自动进行缩放。

   **values-sw360dp：**

   ```xml
   <dimen name="xdp_0">.0dp</dimen>
   <dimen name="xdp_1">1.0dp</dimen>
   <dimen name="xdp_2">2.0dp</dimen>
   ...
   <dimen name="xsp_1">1.0sp</dimen>
   <dimen name="xsp_2">2.0sp</dimen>
   <dimen name="xsp_3">3.0sp</dimen>
   ```

   **values-sw320dp**

   ```xml
   <dimen name="xdp_0">.0dp</dimen>
   <dimen name="xdp_1">.9dp</dimen>
   <dimen name="xdp_2">1.8dp</dimen>
   ...
   <dimen name="xsp_1">.9sp</dimen>
   <dimen name="xsp_2">1.8sp</dimen>
   <dimen name="xsp_3">2.7sp</dimen>
   ```

   **values-sw411dp**

   ```xml
   <dimen name="xdp_0">.0dp</dimen>
   <dimen name="xdp_1">1.1dp</dimen>
   <dimen name="xdp_2">2.3dp</dimen>
   ...
   <dimen name="xsp_1">1.1sp</dimen>
   <dimen name="xsp_2">2.3sp</dimen>
   <dimen name="xsp_3">3.4sp</dimen>
   ```

   - 以上就是默认宽度360，其他两种所对应的值，现在能看明白干了什么事情吧。

![px_dpi](https://github.com/yang163email/FitScreen/raw/master/img/px_dpi.png)

2. 如果已经使用了dp、sp，要一个一个改?
   - 不需要，本方案已经提供一个自动替换的jar文件ReplaceDpAndSp.jar。
   - 下载下来后，cmd输入`java -jar ReplaceDpAndSp.jar 需要修改的绝对路径`。
   - 需要修改的绝对路径可以是layout、drawable目录，前提是xml文件。
   - 路径格式：1. `D:/test/test/...`   2. `D:\\test\\test\\...` 。路径格式请勿使用单反斜杠`\`，否则无法识别。
   - 只输入`java -jar ReplaceDpAndSp.jar` ，默认会在当前jar包所在路径进行替换操作。
   - 如果不满足你的需求，可以自己修改源码，源码已提供。
   - **注意：**
     - id命名请不要以数字+dp  或者 数字+sp结尾，出现误伤请自行解决。  
     - 在使用本工具之前请将你的代码备份，避免不可挽回的损失！！！
3. 在这里说一下我的图片问题，同样是wrap_content的情况下, honor v8可以正常显示，Samsung测试机还是老样子，至于后来怎么调整到差不多效果，我采用了一个不大好的方法，全部使用dp表示。如有更好的方法，你可以在issue上面提出来，或者直接发邮件给我。
4. 如果在xml上面不能满足你的需求，你也可以在代码中完成适配，只要拿到对应dimen的值。

例如：

```java
CommonUtils.setTextTopImgSize(getContext(), mTvFollowup, R.mipmap.tag_followup, R.dimen.xdp_56, R.dimen.xdp_53);
...
/**
 * 设置TextView顶部图片的大小
 */
public static void setTextTopImgSize(Context context, TextView tv, @DrawableRes int imgId,
                                         @DimenRes int widthId, @DimenRes int heightId) {
    //调整图片大小
    int width = context.getResources().getDimensionPixelSize(widthId);
    int height = context.getResources().getDimensionPixelSize(heightId);
    Drawable drawable1 = context.getResources().getDrawable(imgId);
    drawable1.setBounds(0, 0, width, height);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
    tv.setCompoundDrawables(null, drawable1, null, null);//只放上边
}
```

5. 以上都可以通过代码来进行操作。


### 最后

如果你有什么问题，可以提issue，或者发邮件给我。如果您在使用中觉得不错，请赏我一颗star，感谢！
