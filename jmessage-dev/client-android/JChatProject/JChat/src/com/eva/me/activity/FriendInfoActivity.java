package com.eva.me.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import com.eva.me.R;
import com.eva.me.adapter.ContactsExpaListAdapter;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.controller.FriendInfoController;
import com.eva.me.entity.Event;
import com.eva.me.tools.DialogCreator;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.NativeImageLoader;
import com.eva.me.view.FriendInfoView;

public class FriendInfoActivity extends BaseActivity {

    private FriendInfoView mFriendInfoView;
    private FriendInfoController mFriendInfoController;
    private String mTargetId;
    private long mGroupId;
    private UserInfo mUserInfo;
    private String mNickname;
    private boolean mIsGetAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        mFriendInfoView = (FriendInfoView) findViewById(R.id.friend_info_view);
        mTargetId = getIntent().getStringExtra(JChatDemoApplication.TARGET_ID);
        mGroupId = getIntent().getLongExtra(JChatDemoApplication.GROUP_ID, 0);

        ////===== the following friend info showing is using double-caching
        ////===== get userinfo from exist conversation or groupchat, and present it right now, and then pull
        ////===== online to present all updated userinfo
        Conversation conv;
        conv = JMessageClient.getSingleConversation(mTargetId);
        if (conv == null && mGroupId == 0) {
            //==== this means if you get a conv and it is null also your GroupID is 0, this means this
            //intent did not come from a group chat or a exist single chat, we just can only get all user
            //info online ==== this is just from contact list

            mUserInfo = ContactsExpaListAdapter.curTargetInfo==null?null: (UserInfo) ContactsExpaListAdapter.curTargetInfo;
            //it indicates that this is a conversation that should be created
            //TODO: this may exits some portatial bug for this conversation should not be established
            //conv = Conversation.createSingleConversation(mTargetId);
            //ALERT: fix bug here, can not create one, because it may present all you created conversation
            //last time you login
        }else if (conv == null && mGroupId != 0) {
            //==== this means this conv is null and groupid is not null , it is from exist group conv
            //we can easily get all this conv from exist group member list, and use it to present the userinfo
            //right now , double-caching ====

            conv = JMessageClient.getGroupConversation(mGroupId);
            GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
            mUserInfo = groupInfo.getGroupMemberInfo(mTargetId);
        } else {
            //=== this means this has exist a conv, and we can simply get all this userinfo just from the exist
            //conversation ===

            mUserInfo = (UserInfo) conv.getTargetInfo();
        }
        mFriendInfoView.initModule();
        //先从Conversation里获得UserInfo展示出来
        mFriendInfoView.initInfo(mUserInfo);
        mFriendInfoController = new FriendInfoController(mFriendInfoView, this);
        mFriendInfoView.setListeners(mFriendInfoController);
        //更新一次UserInfo
        final Dialog dialog = DialogCreator.createLoadingDialog(FriendInfoActivity.this,
                FriendInfoActivity.this.getString(R.string.loading));
        dialog.show();
        JMessageClient.getUserInfo(mTargetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(int status, String desc, final UserInfo userInfo) {
                dialog.dismiss();
                if (status == 0) {
                    mUserInfo = userInfo;
                    mNickname = userInfo.getNickname();
                    mFriendInfoView.initInfo(userInfo);
                } else {
                    HandleResponseCode.onHandle(FriendInfoActivity.this, status, false);
                }
            }
        });

    }

    /**
     * 如果是群聊，使用startActivity启动聊天界面，如果是单聊，setResult然后
     * finish掉此界面
     */
    public void startChatActivity() {
        if (mGroupId != 0) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(JChatDemoApplication.TARGET_ID, mTargetId);
            intent.setClass(this, ChatActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra("returnChatActivity", true);
            intent.putExtra(JChatDemoApplication.NICKNAME, mNickname);
            setResult(JChatDemoApplication.RESULT_CODE_FRIEND_INFO, intent);
        }
        Conversation conv = JMessageClient.getSingleConversation(mTargetId);
        //如果会话为空，使用EventBus通知会话列表添加新会话
        if (conv == null) {
            conv = Conversation.createSingleConversation(mTargetId);
            EventBus.getDefault().post(new Event.StringEvent(mTargetId));
        }
        finish();
    }

    public String getNickname() {
        return mNickname;
    }


    //点击头像预览大图
    public void startBrowserAvatar() {
        if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getAvatar())) {
            if (mIsGetAvatar) {
                //如果缓存了图片，直接加载
                Bitmap bitmap = NativeImageLoader.getInstance().getBitmapFromMemCache(mUserInfo.getUserName());
                if (bitmap != null) {
                    Intent intent = new Intent();
                    intent.putExtra("browserAvatar", true);
                    intent.putExtra("avatarPath", mUserInfo.getUserName());
                    intent.setClass(this, BrowserViewPagerActivity.class);
                    startActivity(intent);
                }
            } else {
                final Dialog dialog = DialogCreator.createLoadingDialog(this, this.getString(R.string.loading));
                dialog.show();
                mUserInfo.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            mIsGetAvatar = true;
                            //缓存头像
                            NativeImageLoader.getInstance().updateBitmapFromCache(mUserInfo.getUserName(), bitmap);
                            Intent intent = new Intent();
                            intent.putExtra("browserAvatar", true);
                            intent.putExtra("avatarPath", mUserInfo.getUserName());
                            intent.setClass(FriendInfoActivity.this, BrowserViewPagerActivity.class);
                            startActivity(intent);
                        } else {
                            HandleResponseCode.onHandle(FriendInfoActivity.this, status, false);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }


    //将获得的最新的昵称返回到聊天界面
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(JChatDemoApplication.NICKNAME, mNickname);
        setResult(JChatDemoApplication.RESULT_CODE_FRIEND_INFO, intent);
        finish();
        super.onBackPressed();
    }
}
