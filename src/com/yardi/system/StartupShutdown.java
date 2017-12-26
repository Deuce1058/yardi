package com.yardi.system;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.yardi.ejb.SessionsTableSessionBeanRemote;

@WebListener
public class StartupShutdown implements ServletContextListener {
	@EJB SessionsTableSessionBeanRemote sessionsBean;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		sessionsBean.clear(); //clear sessions table
	}

}
