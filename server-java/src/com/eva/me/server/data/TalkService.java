package com.eva.me.server.data;

import java.util.List;
import java.util.Set;

import com.eva.me.server.model.Channel;
import com.eva.me.server.model.Message;
import com.eva.me.server.model.RecentChat;
import com.eva.me.server.model.User;

public interface TalkService {
    
    // --------------- chatting
    
    void putMessage(String udid, String chatting, Message message);
    
    void newRecentChat(String udid, String chatting);
    
    void showedMessage(String udid, String chatting);
    
    void unreadMessage(String udid, String chatting);
    
    Set<Message> getRecentMessages(String chatting);
    
    Set<RecentChat> getRecentChats(String udid);

    // --------------- channel 
    
	List<Channel> getChannelListAll();
	
	Channel getChannelByName(String channelName);
	
	boolean createChannel(String channelName, String udid);
	
	boolean enterChannel(String channelName, String udid);
	
	boolean exitChannel(String channelName, String udid);
	
	// ---------------- user
	
	User getUserByUdid(String udid);
	
	User getUserByName(String name);
	
	boolean registerUser(String udid, String userName);
	
	List<User> getUserListByChannel(String channelName);
	
	
}
