package com.eva.me.controller;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import com.eva.me.R;
import com.eva.me.activity.ContactsFragment;
import com.eva.me.adapter.ContactsListAdapter;
import com.eva.me.tools.Logger;
import com.eva.me.tools.UserContactsUtil;
import com.eva.me.view.ContactsView;

import cn.jpush.im.android.api.model.UserInfo;

public class ContactsController implements OnClickListener {
	private static final String TAG = ContactsController.class.getSimpleName();

	private ContactsView mContactsView;
	private ContactsFragment mContactsActivity;
	private Context mContext;

	private ContactsListAdapter mListAdapter;
	private	int mDensityDpi = 0;

	
	public ContactsController(ContactsView mContactsView, ContactsFragment context, int densityDpi) {
		this.mContactsView = mContactsView;
		this.mContactsActivity = context;
		this.mContext = context.getActivity();
		this.mDensityDpi = densityDpi;

		initContactsListViewAdapter();
		initContacts();
	}

	public void initContacts() {
        //初始化用户名列表
        List<String> userNameList = new ArrayList<String>();

		UserContactsUtil.initAllUsers(mContactsActivity);
		UserContactsUtil.initMyGroups(mContext);

    }

	private void initContactsListViewAdapter() {
//		List<String> mData = new ArrayList<String>();
//
//		for (int i = 0; i < 17; i++) {
//			mData.add("ap[pp ["+i+"]");
//		}

		List<UserInfo> mData = new ArrayList<UserInfo>();

		mListAdapter = new ContactsListAdapter(mContext, mData, mDensityDpi);
		mContactsView.setContLVAdapter(mListAdapter);
	}


	public void refreshLVDataset(List<UserInfo> mNewData) {
		mListAdapter.setmData(mNewData);
	}

	public void addUIinLVDataset(UserInfo mNewUserInfo) {
		mListAdapter.addUserInfo(mNewUserInfo);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.search_btn:
				Logger.i(TAG, "Before Looooooog.........");
				initContacts();
				Logger.i(TAG, "End Looooooog.........");
				break;
			default:
				Logger.e(TAG, "Do not know id");
				break;
		}
		
	}

}
