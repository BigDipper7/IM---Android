package com.eva.me.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eva.me.R;
import com.eva.me.view.CircleImageView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by phoen_000 on 2016/3/8.
 */
public class ContactsExpaListAdapter extends BaseExpandableListAdapter {
    private List<String> mHeaderData;
    private Map<String,List<String>> mChildData;
    private Context mContext;
    private LayoutInflater mInflater;

    public ContactsExpaListAdapter(Context context, List<String> headerData, Map<String, List<String>> childData) {
        this.mHeaderData = headerData;
        this.mChildData = childData;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

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
        return mChildData.get(mHeaderData.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mHeaderData.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
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
        final HeaderViewHilder headerViewHilder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_group_header_item, null);

            headerViewHilder = new HeaderViewHilder();
            headerViewHilder.groupName = (TextView) convertView.findViewById(R.id.group_name);

            convertView.setTag(headerViewHilder);
        }else {
            headerViewHilder = (HeaderViewHilder) convertView.getTag();
        }

        //init views....
        headerViewHilder.groupName.setText(getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
        childViewHolder.userName.setText(getChild(groupPosition, childPosition));


        return convertView;
    }

    ////======= Core Code Snippet ======


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;//TODO: just test to change it to true what will happen
    }


    private static class HeaderViewHilder {
        TextView groupName;
    }

    private static class ChildViewHolder {
        TextView alpha;
        ImageView headIcon;
        TextView userName;
    }

}
