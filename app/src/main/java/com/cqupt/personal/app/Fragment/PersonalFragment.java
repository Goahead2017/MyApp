package com.cqupt.personal.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.cqupt.personal.app.Activity.MainActivity;
import com.cqupt.personal.app.CallBack.ChangeSignatureCallback;
import com.cqupt.personal.app.CallBack.ChoosePhotoCallback;
import com.cqupt.personal.app.DIYView.HeadIconDialog;
import com.cqupt.personal.app.DIYView.MyFragmentDialog;
import com.cqupt.personal.app.Adapter.ExpandableAdapter;
import com.cqupt.personal.app.R;
import com.cqupt.personal.app.StaticData;
import com.cqupt.personal.app.CallBack.TakePhotoCallback;
import com.cqupt.personal.app.Tools.AlbumUriToUrl;
import com.cqupt.personal.app.Tools.CameraUriToUrl;
import com.cqupt.personal.app.Tools.UploadToLeanCloud;

import java.io.File;
import java.io.IOException;

/**
 * 个人中心界面
 */

public class PersonalFragment extends Fragment implements View.OnClickListener ,ChangeSignatureCallback,ChoosePhotoCallback,TakePhotoCallback{

    private PersonalFragment personalFragment;
    private ImageView headIcon;
    private TextView signature;
    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    private String[]parent = {"个人信息","背景","修改菜单颜色"};
    private String[][] child = {{"个人资料","个性签名"},{"背景1","背景2","背景3","背景4","背景5","恢复"},{"蓝色","黑色","白色","恢复"}};
    private String[]imageUrl = {"http://p5mi59sy0.bkt.clouddn.com/back6.jpg","http://p5mi59sy0.bkt.clouddn.com/back7.jpg",
            "http://p5mi59sy0.bkt.clouddn.com/back8.jpg","http://p5mi59sy0.bkt.clouddn.com/back9.jpg","http://p5mi59sy0.bkt.clouddn.com/back10.jpg",null};

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    //拍摄照片的Uri
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        this.personalFragment = this;

        //初始化头像
        if(AVUser.getCurrentUser().get("headIcon") == null) {
            AVUser.getCurrentUser().put("headIcon", "http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
            StaticData.todoFolder.put("headIcon","http://p5mi59sy0.bkt.clouddn.com/picture2.jpg");
            StaticData.todoFolder.saveInBackground();// 保存到服务端
        }
        Log.d("photo", "onCreateView: " + AVUser.getCurrentUser().get("headIcon"));

        //初始化个性签名
        if(AVUser.getCurrentUser().get("signature") == null) {
            AVUser.getCurrentUser().put("signature", "良药苦口利于病，忠言逆耳利于行");
            StaticData.todoFolder.put("signature","良药苦口利于病，忠言逆耳利于行");
            StaticData.todoFolder.saveInBackground();// 保存到服务端
        }


        //初始化以及设置相应控件的监听事件
        init(view);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
            Log.d("photo", "onCreateView: " + "我执行了");
        }

        return view;
    }

    private void init(View view) {
        headIcon = view.findViewById(R.id.profile_image);
        Glide.with(getActivity()).load(Uri.parse(/*"http://p5mi59sy0.bkt.clouddn.com/picture2.jpg"*/String.valueOf(AVUser.getCurrentUser().get("headIcon")))).asBitmap().into(headIcon);
        headIcon.setOnClickListener(this);

        signature = view.findViewById(R.id.signature);
        this.signature.setText(String.valueOf(AVUser.getCurrentUser().get("signature")));

        //设置菜单
        expandableListView = view.findViewById(R.id.expandable);
        setMenu();

    }

    public void setMenu(){
        //初始化菜单样式
        if(StaticData.blue){
            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_darkblue_24dp));
        }else if(StaticData.white){
            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
        }else if(StaticData.blank){
            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        }else {
            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_blue_24dp));
        }
        adapter = new ExpandableAdapter(parent,child);
        expandableListView.setAdapter(adapter);
        //设置分组项的点击监听事件
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        //设置子选项点击监听事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if(i == 0 && i1 == 1) {
                    String text = "请输入个性签名";
                    MyFragmentDialog myDialog = new MyFragmentDialog(getActivity(),R.style.Dialog,text,personalFragment);
                    myDialog.show();
                    Window dialogWindow = myDialog.getWindow();
                    WindowManager m = getActivity().getWindowManager();
                    Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                    p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
                    p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.9
                    dialogWindow.setAttributes(p);
                }
                if(i == 1) {
                    ((MainActivity) getActivity()).changSkin(imageUrl[i1]);
                }
                if(i == 2) {
                    switch (i1){
                        case 0:
                            StaticData.blue = true;
                            StaticData.blank =false;
                            StaticData.white = false;
                            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_darkblue_24dp));
                            break;
                        case 1:
                            StaticData.blank = true;
                            StaticData.blue =false;
                            StaticData.white = false;
                            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                            break;
                        case 2:
                            StaticData.white = true;
                            StaticData.blue =false;
                            StaticData.blank = false;
                            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                            break;
                        case 3:
                            StaticData.white = false;
                            StaticData.blue =false;
                            StaticData.blank = false;
                            expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.ic_star_border_blue_24dp));
                    }
                    adapter.notifyDataSetChanged();

                }
                return true;
            }
        });
    }

    //设置头像的点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
                HeadIconDialog myDialog = new HeadIconDialog(getActivity(),R.style.Dialog,personalFragment);
                myDialog.show();
                //设置dialog的长宽
                Window dialogWindow = myDialog.getWindow();
                WindowManager m = getActivity().getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
                p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.9
                dialogWindow.setAttributes(p);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == Activity.RESULT_OK) {
                    //将拍摄的照片显示出来
                    if (imageUri != null)
                        UploadToLeanCloud.upLoadIcon(CameraUriToUrl.getAbsoluteImagePath(getActivity(), imageUri), headIcon, getActivity());
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if(uri != null)
                        UploadToLeanCloud.upLoadIcon(AlbumUriToUrl.getRealPathFromUri(getActivity(),uri), headIcon, getActivity());
                }
                break;
                default:
                    break;
        }
    }

    @Override
    public void changSignature(String signature) {
        UploadToLeanCloud.upLoadSignature(signature);
        this.signature.setText(String.valueOf(AVUser.getCurrentUser().get("signature")));
    }

    @Override
    public void choosePhoto() {
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    @Override
    public void takePhoto() {
        //创建File对象，用于存储拍照后的图片
        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将file对象装换成uri对象
        imageUri = Uri.fromFile(outputImage);
        openCamera();
    }

    private void openCamera(){
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

}
