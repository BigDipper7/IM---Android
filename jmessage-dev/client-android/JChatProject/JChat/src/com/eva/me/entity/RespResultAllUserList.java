package com.eva.me.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by eva on 16-3-1.
 */
public class RespResultAllUserList {

    @SerializedName("total")
    private int numTotal;

    @SerializedName("start")
    private int numStart;

    @SerializedName("count")
    private int numCount;

    @Deprecated
    @SerializedName("users")
    private List<UserInfo> userInfoList;

    private List<String> allUserNameList;

    @Override
    public String toString() {
        String res = "";

        res = "total:%d, start:%d, count:%d \n[%s]";
        
        String temp = "";
        for (String item :
                allUserNameList) {
            temp += item+" , ";
        }

        return String.format(res, numTotal, numStart, numCount, temp);
    }

    public List<String> getAllUserNameList() {
        return allUserNameList;
    }

    public void setAllUserNameList(List<String> allUserNameList) {
        this.allUserNameList = allUserNameList;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }

    public int getNumStart() {
        return numStart;
    }

    public void setNumStart(int numStart) {
        this.numStart = numStart;
    }

    public int getNumCount() {
        return numCount;
    }

    public void setNumCount(int numCount) {
        this.numCount = numCount;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

}
