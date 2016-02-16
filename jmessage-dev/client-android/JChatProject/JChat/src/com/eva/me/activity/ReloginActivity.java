package com.eva.me.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.eva.me.R;
import com.eva.me.controller.ReloginController;
import com.eva.me.tools.BitmapLoader;
import com.eva.me.tools.SharePreferenceManager;
import com.eva.me.view.ReloginView;

public class ReloginActivity extends BaseActivity {

    private ReloginView mReloginView;
    private ReloginController mReloginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_login);

        init();
    }

    /**
     * init method in onCreate
     */
    private void init() {
        mReloginView = (ReloginView) findViewById(R.id.relogin_view);
        mReloginView.initModule();
        fillContent();
        mReloginView.setListeners(mReloginController);
        mReloginView.setListener(mReloginController);
    }

    /**
     * update current activity content, add username and add avatar
     */
    private void fillContent() {
        //get userName and get user avatar from preferences
        String userName = SharePreferenceManager.getCachedUsername();
        String userAvatarPath = SharePreferenceManager.getCachedAvatarPath();

        //set
        Bitmap bitmap = BitmapLoader.getBitmapFromFile(userAvatarPath, mAvatarSize, mAvatarSize);
        if (bitmap != null) {
            mReloginView.showAvatar(bitmap);
        }
        mReloginView.setUserName(userName);
        mReloginController = new ReloginController(mReloginView, this, userName);

        //update
        SharePreferenceManager.setCachedUsername(userName);
        SharePreferenceManager.setCachedAvatarPath(userAvatarPath);
    }


    public void startRelogin() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void startSwitchUser() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("fromSwitch", true);
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }


    public void startRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
