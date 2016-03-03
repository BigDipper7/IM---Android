package com.eva.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eva.me.R;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
import com.eva.me.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by Ken on 2015/2/26.
 */
public class ContactsListAdapter extends BaseAdapter{

    private static final String TAG = ContactsListAdapter.class.getSimpleName();

//    private List<String> mList;
    private List<UserInfo> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mDensityDpi;

    public ContactsListAdapter(Context context, List<UserInfo> mData, int densityDpi){
        this.mContext = context;
//        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.mDensityDpi = densityDpi;
    }

    public void setmData(List<UserInfo> mNewData) {
        this.mData = mNewData;
        notifyDataSetChanged();
    }

    public void addUserInfo(UserInfo mNewUserInfo) {
        if (mData == null) {
            mData = new ArrayList<UserInfo>();
        }

        mData.add(mNewUserInfo);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public UserInfo getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Logger.d(TAG, "get view at : "+position);

        final ViewHolder viewHolder;

        if (convertView == null) {
            Logger.d(TAG, "convertView is null");
            convertView = mInflater.inflate(R.layout.list_view_contact_item, null);

            viewHolder = new ViewHolder();
            viewHolder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.headIcon = (CircleImageView) convertView.findViewById(R.id.contact_avatar);

            if (mDensityDpi <= 160) {
                viewHolder.userName.setEms(6);
            }else if (mDensityDpi <= 240) {
                viewHolder.userName.setEms(8);
            }else {
                viewHolder.userName.setEms(10);
            }

            convertView.setTag(viewHolder);
        } else {
            Logger.d(TAG, "convertView is not null");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //init current item view
        UserInfo userInfo = mData.get(position);
        Logger.d(TAG, "[userinfo] current user info is "+userInfo);
        viewHolder.userName.setText(TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getUserName() : userInfo.getNickname());

        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int status, String desc, Bitmap bitmap) {
                    if (status == 0) {
                        viewHolder.headIcon.setImageBitmap(bitmap);
                    }else {
                        viewHolder.headIcon.setImageResource(R.drawable.head_icon);
                        HandleResponseCode.onHandle(mContext, status, false);
                    }
                }
            });
        }else {
            viewHolder.headIcon.setImageResource(R.drawable.head_icon);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView alpha;
        CircleImageView headIcon;
        TextView userName;
    }
}
