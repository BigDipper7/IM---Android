package com.eva.me.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.eva.me.R;
import com.eva.me.activity.ChatDetailActivity;
import com.eva.me.activity.ContactsFragment;
import com.eva.me.activity.FriendInfoActivity;
import com.eva.me.activity.MeInfoActivity;
import com.eva.me.adapter.ContactsExpaListAdapter;
import com.eva.me.adapter.ContactsListAdapter;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.tools.Logger;
import com.eva.me.tools.UserContactsUtil;
import com.eva.me.view.ContactsView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

public class ContactsController implements OnClickListener
		, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	private static final String TAG = ContactsController.class.getSimpleName();

	private ContactsView mContactsView;
	private ContactsFragment mContactsActivity;
	private Context mContext;

	private ContactsListAdapter mListAdapter;
	private ContactsExpaListAdapter mExpandableListAdapter;
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
//        List<String> userNameList = new ArrayList<String>();

		UserContactsUtil.initAllUsers(mContactsActivity);
		UserContactsUtil.initMyGroups(mContactsActivity);

    }

	private void initContactsListViewAdapter() {
//		List<String> mData = new ArrayList<String>();
//
//		for (int i = 0; i < 17; i++) {
//			mData.add("ap[pp ["+i+"]");
//		}


//		List<String> headerData = new ArrayList<String>();
//		headerData.add("group1");
//		headerData.add("group3");
//		headerData.add("group2");
//		headerData.add("group5");
//
//		Map<String,List<String>> childData = new HashMap<String, List<String>>();
//		for (String s :
//				headerData) {
//			List<String> te = new ArrayList<String>();
//			te.add(s+"1");
//			te.add(s+"2");
//			te.add(s+"3");
//			childData.put(s,te);
//		}

		List<String> headerData = new ArrayList<String>();
		Map<String,List<Object>> childData = new HashMap<String, List<Object>>();

		headerData.add(mContext.getString(R.string.expandable_list_view_header_groups_name));
		headerData.add(mContext.getString(R.string.expandable_list_view_header_users_name));

		mExpandableListAdapter = new ContactsExpaListAdapter(mContext, headerData, childData);
		mContactsView.setContLVAdapter(mExpandableListAdapter);

		// the following snippet is useless
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

	public void addUIinELVDataset(UserInfo mNewUserInfo) {
		mExpandableListAdapter.refreshChildData(mContext.getString(R.string.expandable_list_view_header_users_name), mNewUserInfo);
	}

	public void addGIinELVDataset(GroupInfo mNewGroupInfo) {
		mExpandableListAdapter.refreshChildData(mContext.getString(R.string.expandable_list_view_header_groups_name), mNewGroupInfo);
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
		String mTargetId = mCurrItemUI.getUserName();
		Logger.i(TAG, "My Current UI is: " + mCurrItemUI);
//		startChatDetailActivity(false, mCurrItemUI.getUserName(), 0);
		Intent intent = new Intent();
		if (mTargetId.equals(JMessageClient.getMyInfo().getUserName())) {
			intent.putExtra(JChatDemoApplication.TARGET_ID, mTargetId);
			Logger.i(TAG, "msg.getFromName() " + mTargetId);
			intent.setClass(mContext, MeInfoActivity.class);
			mContext.startActivity(intent);
		} else {
//			String targetID = userInfo.getUserName();
			intent.putExtra(JChatDemoApplication.TARGET_ID, mTargetId);
//			intent.putExtra(JChatDemoApplication.TARGET_ID, targetID);
			intent.putExtra(JChatDemoApplication.GROUP_ID, 0L);
			intent.setClass(mContext, FriendInfoActivity.class);
			mContactsActivity.startActivityForResult(intent,
					JChatDemoApplication.REQUEST_CODE_FRIEND_INFO);
		}
	}

	/**
	 * just start detailed page with such app
	 * @param isGroup
	 * @param targetID
	 * @param groupID
	 */
	@Deprecated
	public void startChatDetailActivity(boolean isGroup, String targetID, long groupID) {
//		Intent intent = new Intent();
//		intent.putExtra(JChatDemoApplication.IS_GROUP, isGroup);
//		intent.putExtra(JChatDemoApplication.TARGET_ID, targetID);
//		intent.putExtra(JChatDemoApplication.GROUP_ID, groupID);
//		intent.setClass(mContext, ChatDetailActivity.class);
////		startActivityForResult(intent, JChatDemoApplication.REQUEST_CODE_CHAT_DETAIL);
//		mContactsActivity.startActivityForResult(intent, JChatDemoApplication.REQUEST_CODE_CHAT_DETAIL);
//		Intent intent = new Intent();
//		if () {
//			intent.putExtra(JChatDemoApplication.TARGET_ID, mTargetId);
//			Log.i(TAG, "msg.getFromName() " + mTargetId);
//			intent.setClass(mContext, MeInfoActivity.class);
//			mContext.startActivity(intent);
//		} else {
//			String targetID = userInfo.getUserName();
//			intent.putExtra(JChatDemoApplication.TARGET_ID, targetID);
//			intent.putExtra(JChatDemoApplication.GROUP_ID, mGroupId);
//			intent.setClass(mContext, FriendInfoActivity.class);
//			((Activity) mContext).startActivityForResult(intent,
//					JChatDemoApplication.REQUEST_CODE_FRIEND_INFO);
//		}
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
