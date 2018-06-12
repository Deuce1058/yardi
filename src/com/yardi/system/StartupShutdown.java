package com.yardi.system;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.yardi.ejb.SessionsTable;

@WebListener
public class StartupShutdown implements ServletContextListener {
	@EJB SessionsTable sessionsBean;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("com.yardi.system StartupShutdown contextInitialized() 0000");
		sessionsBean.clear(); //clear sessions table
	}

}
