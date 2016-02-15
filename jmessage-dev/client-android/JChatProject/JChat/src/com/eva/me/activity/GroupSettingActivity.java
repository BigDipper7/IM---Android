package com.eva.me.activity;

import android.os.Bundle;
import com.eva.me.R;
import com.eva.me.controller.GroupSettingController;
import com.eva.me.view.GroupSettingView;

public class GroupSettingActivity extends BaseActivity {
	
	private GroupSettingView mGroupSettingView;
	private GroupSettingController mGroupSettingController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_group_setting);
		
		int which = getIntent().getIntExtra(ChatDetailActivity.START_FOR_WHICH, 0);
		mGroupSettingView = (GroupSettingView) findViewById(R.id.group_setting_view);
		mGroupSettingView.initModule();
		mGroupSettingController = new GroupSettingController(mGroupSettingView, this, which);
		mGroupSettingView.setListeners(mGroupSettingController);
	}

}
