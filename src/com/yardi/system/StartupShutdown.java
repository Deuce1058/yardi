package com.yardi.system;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.yardi.ejb.SessionsTable;

/**
 * Startup and shutdown tasks 
 */
@WebListener
public class StartupShutdown implements ServletContextListener {
	/**
	 * Injected reference to {@link com.yardi.ejb.SessionsTableBean#SessionsTableBean() com.yardi.ejb.SessionsTableBean}
	 */
	@EJB SessionsTable sessionsBean;
	
	/**
	 * Shutdown tasks
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * Startup tasks. Clear SESSIONS_TABLE and reset SESSIONS_TABLE sequence column 
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("com.yardi.system StartupShutdown contextInitialized() 0000");
		sessionsBean.clear(); //clear sessions table
		sessionsBean.resetSeq(); //reset sequence column
	}

}
