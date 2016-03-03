package com.eva.me.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.eva.me.R;
import com.eva.me.activity.ChatDetailActivity;
import com.eva.me.activity.ContactsFragment;
import com.eva.me.adapter.ContactsListAdapter;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.tools.Logger;
import com.eva.me.tools.UserContactsUtil;
import com.eva.me.view.ContactsView;

import cn.jpush.im.android.api.model.UserInfo;

public class ContactsController implements OnClickListener
		, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
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

	/**
	 * Callback method to be invoked when an item in this AdapterView has
	 * been clicked.
	 * <p/>
	 * Implementers can call getItemAtPosition(position) if they need
	 * to access the data associated with the selected item.
	 *
	 * @param parent   The AdapterView where the click happened.
	 * @param view     The view within the AdapterView that was clicked (this
	 *                 will be a view provided by the adapter)
	 * @param position The position of the view in the adapter.
	 * @param id       The row id of the item that was clicked.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserInfo mCurrItemUI = mListAdapter.getItem(position);
		Logger.i(TAG, "My Current UI is: "+mCurrItemUI);
		startChatDetailActivity(false, mCurrItemUI.getUserName(), 0);
	}

	/**
	 * just start detailed page with such app
	 * @param isGroup
	 * @param targetID
	 * @param groupID
	 */
	public void startChatDetailActivity(boolean isGroup, String targetID, long groupID) {
		Intent intent = new Intent();
		intent.putExtra(JChatDemoApplication.IS_GROUP, isGroup);
		intent.putExtra(JChatDemoApplication.TARGET_ID, targetID);
		intent.putExtra(JChatDemoApplication.GROUP_ID, groupID);
		intent.setClass(mContext, ChatDetailActivity.class);
//		startActivityForResult(intent, JChatDemoApplication.REQUEST_CODE_CHAT_DETAIL);
		mContactsActivity.startActivityForResult(intent, JChatDemoApplication.REQUEST_CODE_CHAT_DETAIL);
	}



	/**
	 * Callback method to be invoked when an item in this view has been
	 * clicked and held.
	 * <p/>
	 * Implementers can call getItemAtPosition(position) if they need to access
	 * the data associated with the selected item.
	 *
	 * @param parent   The AbsListView where the click happened
	 * @param view     The view within the AbsListView that was clicked
	 * @param position The position of the view in the list
	 * @param id       The row id of the item that was clicked
	 * @return true if the callback consumed the long click, false otherwise
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(mContext, String.format("LongClick on position:%d id:%l",position,id), Toast.LENGTH_SHORT).show();
		return true;
	}
}
