package com.eva.me.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eva.me.server.api.GetRecentChatsServlet;
import com.eva.me.server.api.GetRecentMessagesServlet;
import com.eva.me.server.api.ShowedMessageServlet;
import com.eva.me.server.api.TalkServlet;
import com.eva.me.server.api.UnreadMessageServlet;
import com.eva.me.server.api.UserInfoServlet;
import com.eva.me.server.web.AllChannelListServlet;
import com.eva.me.server.web.ChannelUserListServlet;
import com.eva.me.server.web.ChattingServlet;
import com.eva.me.server.web.EnterChannelServlet;
import com.eva.me.server.web.ExitChannelServlet;
import com.eva.me.server.web.MainServlet;
import com.eva.me.server.web.NewChannelServlet;
import com.eva.me.server.web.RootServlet;
import com.eva.me.server.web.UserChangeNameServlet;
import com.eva.me.server.web.UserRegisterServlet;

public class JettyServer {
    private static final Logger LOG = LoggerFactory.getLogger(JettyServer.class);
    
	public static void main(String[] args) throws Exception {
		int port = 10010;
		
		if (args.length >= 1) {
			String sPort = args[0];
			try {
				port = Integer.parseInt(sPort);
			} catch (NumberFormatException e) {
				LOG.info("Invalid port arg - " + sPort + ". User default value - " + port);
			}
		}
		
		Server server = new Server(port);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // channel
        context.addServlet(new ServletHolder(new AllChannelListServlet()), "/channel");
        context.addServlet(new ServletHolder(new NewChannelServlet()), "/channel/new");
        context.addServlet(new ServletHolder(new EnterChannelServlet()), "/channel/enter");
        context.addServlet(new ServletHolder(new ExitChannelServlet()), "/channel/exit");
        context.addServlet(new ServletHolder(new ChannelUserListServlet()), "/channel/users");
        
        // user
        context.addServlet(new ServletHolder(new RootServlet()), "/");
        context.addServlet(new ServletHolder(new MainServlet()), "/main");
        context.addServlet(new ServletHolder(new UserRegisterServlet()), "/user/register");
        context.addServlet(new ServletHolder(new UserChangeNameServlet()), "/user/changeName");
        
        // chatting
        context.addServlet(new ServletHolder(new ChattingServlet()), "/chatting");
        
        // ajax & api
        context.addServlet(new ServletHolder(new UserInfoServlet()), "/api/user");
        context.addServlet(new ServletHolder(new ShowedMessageServlet()), "/api/showedMessage");
        context.addServlet(new ServletHolder(new GetRecentMessagesServlet()), "/api/getRecentMessages");
        context.addServlet(new ServletHolder(new GetRecentChatsServlet()), "/api/getRecentChats");
        context.addServlet(new ServletHolder(new TalkServlet()), "/api/talk");
        context.addServlet(new ServletHolder(new UnreadMessageServlet()), "/api/unreadMessage");
        
        server.start();
		server.join();
		LOG.info("Jetty Server started with port:" + port);
		LOG.info("Push Talk Server is started. Version:" + Config.VERSION);
		
	}
}
