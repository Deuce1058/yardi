package com.yardi.ejb.test;

import com.yardi.shared.test.PwdTestRequest;

import jakarta.ejb.Local;

@Local
public interface PwdTester {
	void enforce();
	String getFeedback();
	PwdTestRequest getPwdTestRequest();
	void removeBean();
	void setPwdTestRequest(PwdTestRequest r);
}
