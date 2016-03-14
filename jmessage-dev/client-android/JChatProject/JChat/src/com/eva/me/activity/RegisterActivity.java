package com.eva.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.eva.me.R;
import com.eva.me.controller.RegisterController;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
import com.eva.me.view.RegisterView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class RegisterActivity extends BaseActivity {

	private RegisterView mRegisterView = null;
    private RegisterController mRegisterController;
	private Context mContext = RegisterActivity.this;
	private static final String TAG = RegisterActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		mRegisterView = (RegisterView) findViewById(R.id.regist_view);
		mRegisterView.initModule();
		mRegisterController = new RegisterController(mRegisterView,this);
        mRegisterView.setListener(mRegisterController);
		mRegisterView.setListeners(mRegisterController);
	}

	//注册成功
	public void onRegistSuccess(){
//		setDefaultSignature();
        Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(this, FixProfileActivity.class);
        startActivity(intent);
	}

	@Deprecated
	private void setDefaultSignature() {
		UserInfo myUserInfo = JMessageClient.getMyInfo();
		myUserInfo.setSignature(mContext.getString(R.string.default_user_signature));
		JMessageClient.updateMyInfo(UserInfo.Field.signature, myUserInfo, new BasicCallback() {
			@Override
			public void gotResult(final int status, final String desc) {
				if (status == 0) {
					Logger.i(TAG, "Set Default User Signature Success...");
				} else {
					HandleResponseCode.onHandle(mContext, status, false);
				}
			}
		});
	}

	@Override
    protected void onDestroy() {
        mRegisterController.dismissDialog();
		Log.i("RegisterActivity", "onDestroy!");
		super.onDestroy();
    }
}
