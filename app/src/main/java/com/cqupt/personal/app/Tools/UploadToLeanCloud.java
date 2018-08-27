package com.cqupt.personal.app.Tools;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.StaticData;

import java.io.FileNotFoundException;

/**
 * 将头像和个性签名上传到LeanCloud并将图片显示出来
 */

public final class UploadToLeanCloud {

    public static void upLoadIcon(final String path, final ImageView headIcon, final FragmentActivity activity){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath("output_image.jpg", path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.d("photo", file.getUrl());//返回一个唯一的 Url 地址
                    AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            AVUser.getCurrentUser().put("headIcon", file.getUrl());
                            AVUser.getCurrentUser().saveInBackground();
                            StaticData.todoFolder.put("headIcon",file.getUrl());
                            StaticData.todoFolder.saveInBackground();// 保存到服务端
                            Glide.with(activity).load(Uri.parse(file.getUrl())).asBitmap().into(headIcon);
                            Toast.makeText(activity, "头像更换成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("photo", "takePhoto: " + path + "\n" + e.toString());
            Toast.makeText(activity, "网络请求失败", Toast.LENGTH_SHORT).show();
        }

    }

    public static void upLoadSignature(final String signature){
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVUser.getCurrentUser().put("signature", signature);
                AVUser.getCurrentUser().saveInBackground();
                StaticData.todoFolder.put("signature",signature);
                StaticData.todoFolder.saveInBackground();// 保存到服务端
            }
        });
    }

}
