package com.eva.me.server.api;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eva.me.server.model.Message;
import com.eva.me.server.model.User;
import com.eva.me.server.utils.ServiceUtils;
import com.eva.me.server.web.common.NormalBaseServlet;

public class GetRecentMessagesServlet extends NormalBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static Logger LOG = LoggerFactory.getLogger(GetRecentMessagesServlet.class);
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOG.debug("api - get recent messages");
        
        String udid = request.getParameter("udid");
        if (null == udid) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "udid is required!");
            return;
        }
        LOG.debug("udid - " + udid);
        
        User user = talkService.getUserByUdid(udid);
        if (null == user) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "the udid is not registered!");
            return;
        }
        
        String friend = request.getParameter("friend");
        String channelName = request.getParameter("channel_name");
        
        if (null == friend && null == channelName) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "friend/channel is required!");
            return;
        }
        
        String chatting = null;
        if (null != friend) {
            chatting = ServiceUtils.getChattingChannel(user.getName(), friend);
            
        } else {
            chatting = ServiceUtils.getChattingChannel(channelName);
        }
        
        Set<Message> messageSet = talkService.getRecentMessages(chatting);
        response.getOutputStream().write(gson.toJson(messageSet).getBytes());
        
    }

	
}
