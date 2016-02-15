package com.eva.me.controller;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.eva.me.R;
import com.eva.me.activity.FriendInfoActivity;
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.view.FriendInfoView;

public class FriendInfoController implements OnClickListener {

    private FriendInfoView mFriendInfoView;
    private FriendInfoActivity mContext;


    public FriendInfoController(FriendInfoView view, FriendInfoActivity context) {
        this.mFriendInfoView = view;
        this.mContext = context;
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_info_return_btn:
                Intent intent = new Intent();
                String nickname = mContext.getNickname();
                intent.putExtra(JChatDemoApplication.NICKNAME, nickname);
                mContext.setResult(JChatDemoApplication.RESULT_CODE_FRIEND_INFO, intent);
                mContext.finish();
                break;
            case R.id.friend_send_msg_btn:
                mContext.startChatActivity();
                break;
            case R.id.friend_detail_avatar:
                mContext.startBrowserAvatar();
                break;
            case R.id.name_rl:
//                Intent intent = new Intent();
//                intent.setClass(mContext, EditNoteNameActivity.class);
//                intent.putExtra("noteName", "ddsklf");
//                intent.putExtra("friendDescription", "kjdkjdlkj");
//                mContext.startActivity(intent);
                break;
        }
    }

}
