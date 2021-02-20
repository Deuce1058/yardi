package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Local;

@Local
public interface PwdCompositionRules {
	boolean enforce(final String password, final String userName, final String userToken, final Vector<Unique_Tokens> userTokens);
	void removeBean();
}
