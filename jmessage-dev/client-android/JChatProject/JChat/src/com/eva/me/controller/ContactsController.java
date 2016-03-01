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
import com.eva.me.application.JChatDemoApplication;
import com.eva.me.entity.RespResultAllUserList;
import com.eva.me.tools.HandleResponseCode;
import com.eva.me.tools.Logger;
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
	
	public ContactsController(ContactsView mContactsView, ContactsFragment context) {
		this.mContactsView = mContactsView;
		this.mContactsActivity = context;
		this.mContext = context.getActivity();
		initContacts();
	}

    public void initContacts() {
        //初始化用户名列表
        List<String> userNameList = new ArrayList<String>();
		
		initAllUsers();
		initMyGroups();
		
    }

	/**
	 * get all user list, through HTTP REST api
	 */
	private void initAllUsers() {
		if (JChatDemoApplication.mRequestQueue != null) {
			Logger.i(TAG, "Application requestqueue not null");
//			String url = "http://www.baidu.com";
//			StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//				@Override
//				public void onResponse(String response) {
//					Logger.d(TAG, "[Volley] resp:"+response);
//				}
//			}, new Response.ErrorListener() {
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					Logger.e(TAG, "[Volley] errResp:"+error);
//				}
//			});
//			JChatDemoApplication.mRequestQueue.add(stringRequest);

			String apiUri = "https://api.im.jpush.cn/v1/users/?start=0&count=300";
			JsonRequest request = new JsonObjectRequest(Request.Method.GET, apiUri, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Logger.d(TAG, "[Volley] [JSONObject] is: " + response);
					RespResultAllUserList result = resolveResp(response);
					Logger.w(TAG, "[JSON]"+result);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Logger.e(TAG, "[Volley] Error Response: "+error);
				}
			})
			{
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String,String> headers = new HashMap<String, String>();
					headers.put("Authorization","Basic NDNmZGU4NTAzMjlhMjI2YWE1NTM2YzZmOjgyNDk0OWFmYzYzMjg1YTFmYTQ4ZTE4NA==");
					headers.put("Content-Type","application/json");
//					headers.put("","");
					return headers;
				}
//				@Override
//				protected Map<String, String> getParams() throws AuthFailureError {
//					Map<String,String> params = new HashMap<String, String>();
//					params.put("start","0");
//					params.put("count","56");
//					return params;
//				}
			};

			JChatDemoApplication.mRequestQueue.add(request);
		}
	}

	private RespResultAllUserList resolveResp(JSONObject response) {
		try {
			List<String> allUserNameList = new ArrayList<String>();

			int total = response.getInt("total");
			int start = response.getInt("start");
			int count = response.getInt("count");

			//TODO: check 0-length of JSONArray
			JSONArray userInfoList = response.getJSONArray("users");
			for (int i=0;i<userInfoList.length();i++) {
				JSONObject userInfo = userInfoList.getJSONObject(i);
				String currentUserName = userInfo.getString("username");
				allUserNameList.add(currentUserName);
			}

			RespResultAllUserList result = new RespResultAllUserList();
			result.setNumTotal(total);
			result.setNumCount(count);
			result.setNumStart(start);
			result.setAllUserNameList(allUserNameList);

			return result;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get all my groups, through JMessageClient GetGroupIDs
	 */
	private void initMyGroups() {
		JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
			@Override
			public void gotResult(int status, String desc, List<Long> groupIDList) {
				if (status == 0) {
					//get list....
					//List<Long> groupIDList;
					Logger.i(TAG, "[MyGroupIDList] "+groupIDList);
				}else {
					Logger.i(TAG, "[GetGroupIDListCallback] desc = "+desc);
					HandleResponseCode.onHandle(mContext, status, false);
				}

			}
		});
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
