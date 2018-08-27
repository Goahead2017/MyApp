package com.cqupt.personal.app.Tools;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 将从系统相机拍摄得到的图片Uri转换成Url
 */

public final class CameraUriToUrl {

    public static  String getAbsoluteImagePath(Activity activity, Uri contentUri) {

        //如果是对媒体文件，在android开机的时候回去扫描，然后把路径添加到数据库中。
        //由打印的contentUri可以看到：2种结构。正常的是：content://那么这种就要去数据库读取path。
        //另外一种是Uri是 file:///那么这种是 Uri.fromFile(File file);得到的
        System.out.println(contentUri);

        String[] projection = { MediaStore.Images.Media.DATA };
        String urlpath;
        CursorLoader loader = new CursorLoader(activity,contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        try
        {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            urlpath =cursor.getString(column_index);
            //如果是正常的查询到数据库。然后返回结构
            return urlpath;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            // TODO: handle exception
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }

        //如果是文件。Uri.fromFile(File file)生成的uri。那么下面这个方法可以得到结果
        urlpath = contentUri.getPath();
        return urlpath;
    }

}
