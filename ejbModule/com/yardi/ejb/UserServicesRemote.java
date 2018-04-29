package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

/**
 * Specifies methods implemented by UserServices.
 * 
 * @author Jim
 *
 */
@Remote
public interface UserServicesRemote {
	boolean authenticate();
	boolean chgPwd();
	String getFeedback();
	boolean passwordPolicy(String password);	
}
