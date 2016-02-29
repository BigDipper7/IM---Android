package com.eva.me.controller;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;
import com.eva.me.activity.ContactsFragment;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
import com.eva.me.view.ContactsView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;

public class ContactsController implements OnClickListener {
	private static final String TAG = ContactsController.class.getSimpleName();

	private ContactsView mContactsView;
	private ContactsFragment mContactsActivity;
	private Context mContext;
	
	public ContactsController(ContactsView mContactsView, ContactsFragment context) {
		this.mContactsView = mContactsView;
		this.mContactsActivity = context;
		this.mContext = context.getActivity();
		initContacts();
	}

    public void initContacts() {
        //初始化用户名列表
        List<String> userNameList = new ArrayList<String>();
		
		initAllUsers();
		initMyGroups();
		
    }

	/**
	 * get all user list, through HTTP REST api
	 */
	private void initAllUsers() {
	}

	/**
	 * get all my groups, through JMessageClient GetGroupIDs
	 */
	private void initMyGroups() {
		JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
			@Override
			public void gotResult(int status, String desc, List<Long> groupIDList) {
				if (status == 0) {
					//get list....
					//List<Long> groupIDList;

				}else {
					Logger.i(TAG, "[GetGroupIDListCallback] desc = "+desc);
					HandleResponseCode.onHandle(mContext, status, false);
				}

			}
		});
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		}
		
	}

}
