package com.yardi.ejb.helpdesk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.ejb.model.Reset_Password;
import com.yardi.ejb.util.Utils;
import com.yardi.shared.helpdesk.ResetPwdRequest;

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
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() NotSupportedException 0002 "
					+ "\n"
					+ e
					);
			e.printStackTrace();
			utilsBean.rollback(tx);
			return null;
		}     
    }
}
