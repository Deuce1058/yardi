package com.yardi.ejb.helpdesk;

import com.yardi.shared.helpdesk.ResetPwdRequest;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.ejb.model.Reset_Password;
import com.yardi.ejb.model.Update_Temp_Password;
import com.yardi.ejb.util.Utils;
import com.yardi.shared.userServices.PasswordAuthentication;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

/**
 * Password reset controller bean controls the password reset process.<p>
 * Two main functions are performed. findUserDetails() finds the user profile and returns details about the status of the user profile for the helpdesk to review.
 * resetPwd() sets the temporary password assigned by the helpdesk.  
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class PwdResetCtrlBean implements PwdResetCtrl {
	private String feedback;
	@EJB PasswordPolicy passwordPolicyBean;
	private Pwd_Policy pwd_Policy = null;
	private Reset_Password reset_Password;
	private ResetPwdRequest resetPwdRequest;
	@Resource UserTransaction tx;
	@EJB UniqueTokens uniqueTokensBean; 
	@EJB UserProfile userProfileBean;
	@EJB Utils utilsBean;

    /**
     * Default constructor. 
     */
    public PwdResetCtrlBean() {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.PwdResetCtrlBean() 0000 ");
    }

    /**
     * Find user profile details.<p>
     * The help desk will review the following user profile details returned in the reset password request: user profile active flag, disabled timestamp, 
     * password expiration timestamp, last login timestamp, number of failed password attempts,<p>
     * 
     * {@link com.yardi.ejb.UserProfileBean#findUserProfileForPwdReset(String) userProfileBean.findUserProfileForPwdReset()} returns user profile details for
     * the helpdesk in entity {@link com.yardi.ejb.model.Reset_Password Reset_Password}. Next the Reset_Password entity is mapped to the
     * {@link com.yardi.shared.helpdesk.ResetPwdRequest ResetPwdRequest}. The ResetPwdRequest is then returned.<p>
     * 
     *  Feedback provided:<br>
     *  YRD0000 process completed normally<br>
     *  YRD000D No such user name,<br>
     *  YRD001F placeholder for java.lang.Exception.getMessage()
     */
    public ResetPwdRequest findUserDetails() {
    	System.out.println(
    			  "com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() 0003 "
    			+ "\n    "
    			+ resetPwdRequest.toString()
    			);
    	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
        try {
			tx.begin();
			txStatus(tx);
			reset_Password = userProfileBean.findUserProfileForPwdReset(resetPwdRequest.getUpUserid());
	    	System.out.println(
	    			  "com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() 0012 "
	    			+ "\n    "
	    			+ reset_Password.toString()
	    			);
			
			if (!(reset_Password==null)) {
			    ObjectMapper mapper = new ObjectMapper(); 
			    mapper.readerForUpdating(resetPwdRequest).readValue(mapper.writeValueAsString(reset_Password));
			    String s = mapper.writeValueAsString(reset_Password);
		    	System.out.println(
		    			  "com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() 0010 "
		    			+ "\n    "
		    			+ resetPwdRequest.toString()
		    			+ "\n    "
		    			+ "mapper.writeValueAsString(reset_Password)="
		    			+ mapper.writeValueAsString(reset_Password)
		    			+ "\n    "
		    			+ "s="
		    			+ s
		    			);
				resetPwdRequest.setPwdHistory(uniqueTokensBean.findTokensWithCount(resetPwdRequest.getUpUserid()));
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
				String m[] = feedback.split("=");
				resetPwdRequest.setMsgID(m[0]);
				resetPwdRequest.setMsgDescription(m[1]);
		    	System.out.println(
		    			  "com.yardi.ejb.helpdesk.PwdResetCtrlBean.findUserDetails() 0011 "
		    			+ "\n    "
		    			+ resetPwdRequest.toString()
		    			);
			} else {
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D;
				String m[] = feedback.split("=");
				resetPwdRequest.setMsgID(m[0]);
				resetPwdRequest.setMsgDescription(m[1]);
			}
				
			tx.commit();
			txStatus(tx);
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
	 * Return feedback from the most recent operation that provides feedback
	 * @return feedback from the most recent operation that provides feedback
	 */
    public String getFeedback() {
    	return feedback;
    }
	
   /**
    * Returns the password policy obtained from
    * {@link com.yardi.ejb.PasswordPolicyBean#getPwdPolicy() com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()}
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
	 * Post construct callback
	 */	
	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.postConstructCallback() 0001 ");
    }
	
	/**
	 * Remove bean
	 */
	@Remove
	public void remove() {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.remove() 000B ");
		userProfileBean.removeBean();
	}

	/**
	 * Set the temporary password assigned by the helpdesk in the user profile.<p>
	 * A new {@link com.yardi.ejb.model.Update_Temp_Password Update_Temp_Password} entity is merged into the persistence context. Contents of the Update_Temp_Password 
	 * entity are:<br>
	 * <ul>
	 *   <li>User id from the password reset request</li>
	 *   <li>hashed temporary password assigned by the helpdesk</li>
	 *   <li>password expiration timestamp calculated from LocalDateTime.now() plus the number of minutes before the password expires as defined in password policy  
	 *   {@link com.yardi.ejb.model.Pwd_Policy#ppTempPwdTtl ppTempPwdTtl}</li>
	 *   <li>disabled timestamp set to null</li>
	 *   <li>number of password attempts set to zero</li>
	 * </ul>
	 *  
	 * Feedback provided:<br>
	 * YRD0000 Process completed normally<p>
	 */
	public ResetPwdRequest resetPwd() {
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.resetPwd() 0009 ");
	    feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;

	    try {
		    PasswordAuthentication pwdAuthentication = new PasswordAuthentication(); 
		    resetPwdRequest.setNewPassword(pwdAuthentication.hash(resetPwdRequest.getNewPassword().toCharArray())); 
		    tx.begin();
		    txStatus(tx);
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
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.resetPwd() exception 000A "
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
		txStatus(tx);
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
	
	/**
	 * Inject the {@link com.yardi.shared.helpdesk.ResetPwdRequest reset password request}  
	 */
	public  void setResetPwdRequest(ResetPwdRequest resetPwdRequest) {
		this.resetPwdRequest = resetPwdRequest;
		System.out.println(
				  "com.yardi.ejb.helpdesk.PwdResetCtrlBean.setResetPwdRequest() 0008 "
				+ "\n    "
				+ this.resetPwdRequest.toString()
				);
	}
	
	/**
	 * Log the transaction status
	 */
	private void txStatus(UserTransaction tx) {
    	System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.txStatus() 000C ");

    	if (tx==null) {
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.txStatus() tx is null 000F ");
			return;
		}
	
		String status = null;
		
		try {
			switch(tx.getStatus()) {
			case jakarta.transaction.Status.STATUS_ACTIVE:
				status = "active";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTED:
				status = "committed";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTING:
				status = "committing";
				break;
			case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
				status = "marked rollback";
				break;
			case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
				status = "no transaction";
				break;
			case jakarta.transaction.Status.STATUS_PREPARED:
				status = "prepared";
				break;
			case jakarta.transaction.Status.STATUS_PREPARING:
				status = "prepairing";
				break;
			case jakarta.transaction.Status.STATUS_ROLLEDBACK:
				status = "rolled back";
				break;
			case jakarta.transaction.Status.STATUS_ROLLING_BACK:
				status = "rolling back";
				break;
			case jakarta.transaction.Status.STATUS_UNKNOWN:
				status = "unknown";
				break;
			default:
				status = "undefined";
			}
		} catch (SystemException e) {
			System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.txStatus() SystemException 000D ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.helpdesk.PwdResetCtrlBean.txStatus() 000E "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
}
