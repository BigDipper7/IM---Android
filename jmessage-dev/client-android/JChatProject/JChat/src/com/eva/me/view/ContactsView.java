package com.eva.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.eva.me.R;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;


public class ContactsView extends LinearLayout{

	private TextView mTitle;
    private Context mContext;

	private ImageButton mSearchBtnIB;
	private ListView mContListView;
	private ExpandableListView mContExpanListView;


	public ContactsView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.mContext = context;
	}
	
	public void initModule(){
        mTitle = (TextView) findViewById(R.id.title_bar_title);
        mTitle.setText(mContext.getString(R.string.actionbar_contact));

		mSearchBtnIB = (ImageButton) findViewById(R.id.search_btn);
		mContListView = (ListView) findViewById(R.id.contact_list_view);
		mContExpanListView = (ExpandableListView) findViewById(R.id.contact_expandable_list_view);
	}


	public void setContLVAdapter(ListAdapter adapter) {
		mContListView.setAdapter(adapter);
	}

	public void setContLVAdapter(ExpandableListAdapter expandableListAdapter) {
		mContExpanListView.setAdapter(expandableListAdapter);
	}

	public void setItemListeners(OnItemClickListener onClickListener) {
		mContListView.setOnItemClickListener(onClickListener);
	}

	public void setLongClickListener(OnItemLongClickListener listener) {
		mContListView.setOnItemLongClickListener(listener);
	}


	public void setListener(OnClickListener onClickListener) {
		mSearchBtnIB.setOnClickListener(onClickListener);
	}


	public void setOnChildClickListener(ExpandableListView.OnChildClickListener onChildClickListener) {
		mContExpanListView.setOnChildClickListener(onChildClickListener);
	}

	public void setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener onGroupExpandListener) {
		mContExpanListView.setOnGroupExpandListener(onGroupExpandListener);
	}

	public void setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener onGroupCollapseListener) {
		mContExpanListView.setOnGroupCollapseListener(onGroupCollapseListener);
	}
}
