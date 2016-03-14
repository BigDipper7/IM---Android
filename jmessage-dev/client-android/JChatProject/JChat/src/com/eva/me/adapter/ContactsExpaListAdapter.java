package com.eva.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.eva.me.R;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
import com.eva.me.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by phoen_000 on 2016/3/8.
 */
public class ContactsExpaListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = ContactsExpaListAdapter.class.getSimpleName();

    private List<String> mHeaderData;
    private Map<String,List<Object>> mChildData;
    private Context mContext;
    private LayoutInflater mInflater;

    public ContactsExpaListAdapter(Context context, List<String> headerData, Map<String, List<Object>> childData) {
        this.mHeaderData = headerData;
        this.mChildData = childData;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    //================= data refresh methods ========

    /**
     * replace the LIST
     * @param headerData
     */
    public void refreshHeaderData(List<String> headerData) {
        this.mHeaderData = headerData;
        notifyDataSetChanged();
    }

    /**
     * replace the MAP
     * @param childData
     */
    public void refreshChildData(Map<String, List<Object>> childData) {
        this.mChildData = childData;
        notifyDataSetChanged();
    }

    /**
     * add new @param childDataItem to the position with headerName
     * @param headerName
     * @param childDataItem
     */
    public void refreshChildData(String headerName, Object childDataItem) {
        List<Object > tempChildData = mChildData.get(headerName);
        if (tempChildData == null) {
            tempChildData = new ArrayList<Object>();
            mChildData.put(headerName,tempChildData);
        }
        tempChildData.add(childDataItem);
        notifyDataSetChanged();
    }

    /**
     * use new @param childData to replace origin data in headerName position
     * @param headerName
     * @param childData
     */
    public void refreshChildData(String headerName, List<Object > childData) {
        mChildData.put(headerName,childData);
        notifyDataSetChanged();
    }



    //================= override methods ============

    @Override
    public int getGroupCount() {
        return mHeaderData == null? 0: mHeaderData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mHeaderData == null)
            return 0;
        if (mChildData == null)
            return 0;
        return mChildData.get(mHeaderData.get(groupPosition)) == null? 0: mChildData.get(mHeaderData.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mHeaderData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(mHeaderData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    ////======= Core Code Snippet ======

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Logger.d(TAG, String.format("[getGroupView] at (int groupPosition %d, boolean isExpanded %b)", groupPosition, isExpanded));

        final HeaderViewHolder headerViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_group_header_item, null);

            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.groupName = (TextView) convertView.findViewById(R.id.group_name);

            convertView.setTag(headerViewHolder);
        }else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }

        //init views....
        headerViewHolder.groupName.setText(getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Logger.d(TAG, String.format("[getChildView] at (int groupPosition %d, int childPosition %d, boolean isLastChild %b)", groupPosition, childPosition, isLastChild));

        final ChildViewHolder childViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_contact_item,null);

            childViewHolder = new ChildViewHolder();
            childViewHolder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            childViewHolder.userName = (TextView) convertView.findViewById(R.id.name);
            childViewHolder.headIcon = (CircleImageView) convertView.findViewById(R.id.contact_avatar);


            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        //init views:
        if (getGroup(groupPosition).equals(mContext.getString(R.string.expandable_list_view_header_users_name))) {
            //if now is in user info list
            UserInfo userInfo = (UserInfo) getChild(groupPosition, childPosition);
//            childViewHolder.userName.setText(currChildViewUI.getNickname() == null? currChildViewUI.getUserName(): currChildViewUI.getNickname());
            Logger.d(TAG, "[userinfo] current user info is " + userInfo);

            childViewHolder.userName.setText(TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getUserName() : userInfo.getNickname());

            if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            childViewHolder.headIcon.setImageBitmap(bitmap);
                        }else {
                            childViewHolder.headIcon.setImageResource(R.drawable.head_icon);
                            HandleResponseCode.onHandle(mContext, status, false);
                        }
                    }
                });
            }else {
                childViewHolder.headIcon.setImageResource(R.drawable.head_icon);
            }

        } else if (getGroup(groupPosition).equals(mContext.getString(R.string.expandable_list_view_header_groups_name))) {
            //if now is in group info list
            GroupInfo groupInfo = (GroupInfo) getChild(groupPosition, childPosition);

            Logger.d(TAG, "[GroupInfo] current group info is " + groupInfo);

            childViewHolder.headIcon.setImageResource(R.drawable.group);
            childViewHolder.userName.setText(groupInfo.getGroupName());
            Logger.d(TAG, "[GroupInfo] title: " + groupInfo.getGroupName());
        }


        return convertView;
    }

    ////======= Core Code Snippet ======


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;//TODO: just test to change it to true what will happen
    }


    private static class HeaderViewHolder {
        TextView groupName;
    }

    private static class ChildViewHolder {
        TextView alpha;
        CircleImageView headIcon;
        TextView userName;
    }

}
