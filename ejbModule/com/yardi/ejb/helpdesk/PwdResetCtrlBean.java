package com.yardi.ejb.helpdesk;

import java.security.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.ejb.model.Reset_Password;
import com.yardi.ejb.model.Update_Temp_Password;
import com.yardi.ejb.util.Utils;
import com.yardi.shared.helpdesk.ResetPwdRequest;
import com.yardi.shared.userServices.PasswordAuthentication;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

/**
 * Session Bean implementation class PwdResetCtrlBean
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class PwdResetCtrlBean implements PwdResetCtrl {
	private ResetPwdRequest resetPwdRequest;
	private Pwd_Policy pwd_Policy = null;
	private String feedback;
	private Reset_Password reset_Password;
	@EJB UserProfile userProfileBean;
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean; 
	@EJB Utils utilsBean;
	@Resource UserTransaction tx;

    /**
     * Default constructor. 
     */
    public PwdResetCtrlBean() {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.PwdResetCtrlBean() 0000 ");
    }

    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.postConstructCallback() 0001 ");
    }    
    
    public ResetPwdRequest findUserDetails() {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() 0003 ");
        feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
        try {
			tx.begin();
			utilsBean.txStatus(tx);
			reset_Password = userProfileBean.findUserProfileForPwdReset(resetPwdRequest.getUpUserid());
			
			if (!(reset_Password==null)) {
			    ObjectMapper mapper = new ObjectMapper(); 
				resetPwdRequest = mapper.readValue(mapper.writeValueAsString(reset_Password), ResetPwdRequest.class);
				resetPwdRequest.setPwdHistory(uniqueTokensBean.findTokensWithCount(resetPwdRequest.getUpUserid()));
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
				String m[] = feedback.split("=");
				resetPwdRequest.setMsgID(m[0]);
				resetPwdRequest.setMsgDescription(m[1]);
			} else {
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D;
				String m[] = feedback.split("=");
				resetPwdRequest.setMsgID(m[0]);
				resetPwdRequest.setMsgDescription(m[1]);
			}
				
			tx.commit();
			utilsBean.txStatus(tx);
			return resetPwdRequest;			
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() exception 0002 "
					+ "\n    "
					+ e
					);
			e.printStackTrace();
			rollback(tx);
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD001F;
			String m[] = feedback.split("=");
			resetPwdRequest.setMsgID(m[0]);
			feedback = e.getMessage();
			resetPwdRequest.setMsgDescription(feedback);
			return resetPwdRequest;
		}     
    }
    
	/**
	 * Attempt to roll back the transaction
	 * @param tx - The transaction to roll back
	 */
	private void rollback(UserTransaction tx) {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.rollback() 0004 ");
		try {
			tx.rollback();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.rollback() 0005 "
					+ "\n"
					+ "   exception="
					+ e
					);	
			e.printStackTrace();
		}
		utilsBean.txStatus(tx);
	}
	
	public com.yardi.shared.helpdesk.ResetPwdRequest resetPwd() {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.resetPwd() 0009 ");
	    feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;

	    try {
		    PasswordAuthentication pwdAuthentication = new PasswordAuthentication(); 
		    resetPwdRequest.setNewPassword(pwdAuthentication.hash(resetPwdRequest.getNewPassword().toCharArray())); 
		    tx.begin();
		    utilsBean.txStatus(tx);
		    pwd_Policy = getPwdPolicy(); 
		    LocalDateTime ldt = LocalDateTime.now();
		    ldt.plusMinutes((long) pwd_Policy.getPpTempPwdTtl());
		    userProfileBean.merge(new Update_Temp_Password(resetPwdRequest.getUpUserid(), resetPwdRequest.getNewPassword(), ldt, null, (short)0)); 
		    String msg[] = feedback.split("="); 
		    resetPwdRequest.setMsgID(msg[0]);
		    resetPwdRequest.setMsgDescription(msg[1]);
		    tx.commit();
		    return resetPwdRequest;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() exception 000A "
					+ "\n    "
					+ e
					);
			e.printStackTrace();
			rollback(tx);
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD001F;
			String m[] = feedback.split("=");
			resetPwdRequest.setMsgID(m[0]);
			feedback = e.getMessage();
			resetPwdRequest.setMsgDescription(feedback);
			return resetPwdRequest;
		}
	}

	/** 
	* Returns the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy(). 
	* @return reference to Pwd_Policy entity 
	*/ 
	private Pwd_Policy getPwdPolicy() { 
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.getPwdPolicy() 0006	");
		
		if (pwd_Policy == null) {
			setPwdPolicy();
		}
		
		return pwd_Policy; 
	}  
	
	/** 
	 * Obtain a reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().<p> 
	 * 
	 * <strong>The following feedback is provided:</strong><br> 
	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>
	 */ 
	private void setPwdPolicy() {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.setPwdPolicy() 0007	");
		pwd_Policy = passwordPolicyBean.getPwdPolicy(); 
		
		if (pwd_Policy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		} 
	}
	
	public  void setResetPwdRequest(ResetPwdRequest resetPwdRequest) {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.setResetPwdRequest() 0008 ");
		this.resetPwdRequest = resetPwdRequest; 
	}
}
