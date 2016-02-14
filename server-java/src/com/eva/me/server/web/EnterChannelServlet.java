package com.eva.me.server.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eva.me.server.model.Channel;
import com.eva.me.server.model.User;
import com.eva.me.server.utils.ServiceUtils;
import com.eva.me.server.utils.StringUtils;
import com.eva.me.server.web.common.FreemarkerBaseServlet;

public class EnterChannelServlet extends FreemarkerBaseServlet {
	private static final long serialVersionUID = 348660245631638687L;
    private static final Logger LOG = LoggerFactory.getLogger(EnterChannelServlet.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("action - enter channel");
	    
        Map data = new HashMap();
        
	    String udid = request.getParameter("udid");
	    if (null == udid) {
	        responseError(response, "udid is required!");
	        return;
	    }
	    data.put("udid", udid);
	    
	    String channelName = request.getParameter("channel_name");
        if (StringUtils.isTrimedEmpty(channelName)) {
            responseError(response, "channel_name is required!");
            return;
        }
        
        channelName = channelName.trim().toLowerCase();
        
        if (!ServiceUtils.isValidAliasOrTag(channelName)) {
            responseError(response, "Invalid channel_name - " + channelName);
            return;
        }
        
        Channel existedChannel = talkService.getChannelByName(channelName);
        if (existedChannel != null) {
            talkService.enterChannel(channelName, udid);
            
            User user = talkService.getUserByUdid(udid);
            data.put("user", user);
            
            data.put("channel", existedChannel);
            data.put("channelEntered", true);
            data.put("serverTime", System.currentTimeMillis());
            processTemplate(response, "chatting.html", data);
            
        } else {
            talkService.createChannel(channelName, udid);
            response.sendRedirect("/main?udid=" + udid + "&channelCreated=true");
        }

		
	}

	
}
