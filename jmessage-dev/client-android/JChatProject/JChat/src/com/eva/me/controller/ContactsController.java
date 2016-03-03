package com.eva.me.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.eva.me.R;
import com.eva.me.activity.ContactsFragment;
import com.eva.me.adapter.ContactsListAdapter;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.entity.RespResultAllUserList;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
import com.eva.me.tools.UserContactsUtil;
import com.eva.me.view.ContactsView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadAvatarCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class ContactsController implements OnClickListener {
	private static final String TAG = ContactsController.class.getSimpleName();

	private ContactsView mContactsView;
	private ContactsFragment mContactsActivity;
	private Context mContext;

	private ContactsListAdapter mListAdapter;


	
	public ContactsController(ContactsView mContactsView, ContactsFragment context) {
		this.mContactsView = mContactsView;
		this.mContactsActivity = context;
		this.mContext = context.getActivity();
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
		List<String> mData = new ArrayList<String>();

		for (int i = 0; i < 17; i++) {
			mData.add("ap[pp ["+i+"]");
		}

		mListAdapter = new ContactsListAdapter(mContext, mData);
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
