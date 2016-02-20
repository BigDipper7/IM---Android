package com.eva.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import com.eva.me.R;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.controller.MainController;
import com.eva.me.tools.FileHelper;
import com.eva.me.tools.Logger;
import com.eva.me.tools.SharePreferenceManager;
import com.eva.me.view.MainView;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private MainController mMainController;
    private MainView mMainView;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.v(TAG, "OnCreate() at:"+this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainView = (MainView) findViewById(R.id.main_view);
        mMainView.initModule();//init MainView and it child views instance
        mMainController = new MainController(mMainView, this);//in this step, it will set the viewpager

        mMainView.setOnClickListener(mMainController);
        mMainView.setOnPageChangeListener(mMainController);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Logger.v(TAG, "OnPause() at:"+this);
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Logger.v(TAG, "OnResume() at:"+this);

        JPushInterface.onResume(this);
        //第一次登录需要设置昵称
        boolean flag = SharePreferenceManager.getCachedFixProfileFlag();
        Logger.v(TAG, "OnResume() flag:"+flag);
        Logger.v(TAG, "OnResume() JMessageClient.getMyInfo():"+JMessageClient.getMyInfo());

        if (JMessageClient.getMyInfo() == null) {
            Intent intent = new Intent();
            if (null != SharePreferenceManager.getCachedUsername()) {
                intent.putExtra("userName", SharePreferenceManager.getCachedUsername());
                intent.putExtra("avatarFilePath", SharePreferenceManager.getCachedAvatarPath());
                intent.setClass(this, ReloginActivity.class);
            } else {
                intent.setClass(this, LoginActivity.class);
            }

            Logger.v(TAG, "OnResume() intent:"+intent);

            startActivity(intent);
            finish();
        } else if (TextUtils.isEmpty(JMessageClient.getMyInfo().getNickname()) && flag) {
            Intent intent = new Intent();
            intent.setClass(this, FixProfileActivity.class);

            Logger.v(TAG, "OnResume() intent:" + intent);

            startActivity(intent);
            finish();
        }
        mMainController.sortConvList();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.v(TAG, "OnDestory() at:" + this);
    }

    public FragmentManager getSupportFragmentManger() {
        // TODO Auto-generated method stub
        return getSupportFragmentManager();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.v(TAG, "onActivityResult() at:" + this);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == JChatDemoApplication.REQUEST_CODE_TAKE_PHOTO) {
            String path = mMainController.getPhotoPath();
            File file = new File(path);
            if (file.isFile()) {
                mUri = Uri.fromFile(file);
                //拍照后直接进行裁剪
                mMainController.cropRawPhoto(mUri);
            }
        } else if (requestCode == JChatDemoApplication.REQUEST_CODE_SELECT_PICTURE) {
            if (data != null) {
                Uri selectedImg = data.getData();
                if (selectedImg != null) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getContentResolver()
                            .query(selectedImg, filePathColumn, null, null, null);
                    if (null == cursor) {
                        String path = selectedImg.getPath();
                        File file = new File(path);
                        if (file.isFile()) {
                            copyAndCrop(file);
                            return;
                        } else {
                            Toast.makeText(this, this.getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (!cursor.moveToFirst()) {
                        Toast.makeText(this, this.getString(R.string.picture_not_found),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columnIndex);
                    if (path != null) {
                        File file = new File(path);
                        if (!file.isFile()) {
                            Toast.makeText(this, this.getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            cursor.close();
                        } else {
                            //如果是选择本地图片进行头像设置，复制到临时文件，并进行裁剪
                            copyAndCrop(file);
                            cursor.close();
                        }
                    }
                }
            }
        } else if (requestCode == JChatDemoApplication.REQUEST_CODE_CROP_PICTURE) {
            mMainController.uploadUserAvatar(mUri.getPath());
        } else if (resultCode == JChatDemoApplication.RESULT_CODE_ME_INFO) {
            String newName = data.getStringExtra("newName");
            if (!TextUtils.isEmpty(newName)) {
                mMainController.refreshNickname(newName);
            }
        }
    }

    /**
     * 复制后裁剪文件
     * @param file 要复制的文件
     */
    private void copyAndCrop(final File file) {
        FileHelper.getInstance().copyAndCrop(file, this, new FileHelper.CopyFileCallback() {
            @Override
            public void copyCallback(Uri uri) {
                mUri = uri;
                mMainController.cropRawPhoto(mUri);
            }
        });
    }

}
