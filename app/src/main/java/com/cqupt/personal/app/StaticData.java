package com.cqupt.personal.app;

import com.avos.avoscloud.AVObject;
import com.cqupt.personal.app.Fragment.FriendFragment;
import com.cqupt.personal.app.Fragment.MessageFragment;

import java.util.List;

public class StaticData {
    //用于判断自定义view是否是第一次绘制
    public static boolean flag = true;
    //修改个人中心菜单的字体颜色
    public static boolean blue = false;
    public static boolean white = false;
    public static boolean blank = false;

    public static AVObject todoFolder = new AVObject("TodoFolder");// 构建对象

    public static FriendFragment friendFragment;
    public static  AVObject avObject;
    public static List<AVObject> parseObjects;

    public static MessageFragment messageFragment;
}
