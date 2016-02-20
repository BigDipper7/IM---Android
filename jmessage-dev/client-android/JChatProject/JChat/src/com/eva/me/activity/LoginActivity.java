package com.eva.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.eva.me.R;
import com.eva.me.controller.LoginController;
import com.eva.me.tools.Logger;
import com.eva.me.view.LoginView;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private LoginView mLoginView = null;
    private LoginController mLoginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.v(TAG, "OnCreate() at:" + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {
        mLoginView = (LoginView) findViewById(R.id.login_view);
        mLoginView.initModule();
        mLoginController = new LoginController(mLoginView, this);
        mLoginView.setListener(mLoginController);
        mLoginView.setListeners(mLoginController);
        mLoginView.setOnCheckedChangeListener(mLoginController);
        Intent intent = this.getIntent();
        mLoginView.isShowReturnBtn(intent.getBooleanExtra("fromSwitch", false));
    }

    @Override
    protected void onPause() {
        Logger.v(TAG, "onPause() at:" + this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Logger.v(TAG, "onPause() at:" + this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Logger.v(TAG, "onStop() at:" + this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.v(TAG, "onDestroy() at:" + this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public Context getContext() {
        return this;
    }

    public void startMainActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void startRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

}
