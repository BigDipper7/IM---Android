package com.eva.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eva.me.R;


public class ContactsView extends LinearLayout{

	private TextView mTitle;
    private Context mContext;

	private ImageButton mSearchBtnIB;

	public ContactsView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.mContext = context;
	}
	
	public void initModule(){
        mTitle = (TextView) findViewById(R.id.title_bar_title);
        mTitle.setText(mContext.getString(R.string.actionbar_contact));

		mSearchBtnIB = (ImageButton) findViewById(R.id.search_btn);
	}

	public void setListener(OnClickListener onClickListener) {
		mSearchBtnIB.setOnClickListener(onClickListener);
	}
}
