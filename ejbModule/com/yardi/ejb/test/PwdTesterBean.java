package com.yardi.ejb.test;

import com.yardi.ejb.UserGroups;
import com.yardi.ejb.PwdCompositionRules;
import com.yardi.ejb.UniqueTokens;
import com.yardi.shared.test.PwdTestRequest;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class PwdTesterBean implements PwdTester {
	private String feedback = "";
	private PwdTestRequest pwdTestRequest = null;
	@EJB private UserGroups userGroupsBean;
	@EJB private UniqueTokens uniqueTokensBean;
	@EJB private PwdCompositionRules pwdCompositionRulesBean;
	@Resource private UserTransaction tx;
	
	public PwdTesterBean() {
		System.out.println("com.yardi.ejb.test.PwdTesterBean() ");
	}

	public void enforce() {
		System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0004 ");
		try {
			tx.begin();
			txStatus(tx);
			String s[] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");

			if (!userGroupsBean.find(pwdTestRequest.getUserName()).isEmpty()) {
				System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0005 ");
				uniqueTokensBean.removeExtraTokens(uniqueTokensBean.findTokens(pwdTestRequest.getUserName()));
				
				if (pwdCompositionRulesBean.enforce(
						pwdTestRequest.getPassword(),
						pwdTestRequest.getNewPassword(),
						pwdTestRequest.getUserName(), 
						uniqueTokensBean.findTokens(pwdTestRequest.getUserName()))) {
					System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0006 ");
					pwdTestRequest.setPwdCompositionRulesBeanStatus("TRUE ");
				} else {
					System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0007 ");
					pwdTestRequest.setPwdCompositionRulesBeanStatus("FALSE ");
					feedback = pwdCompositionRulesBean.getFeedback();
					s = pwdCompositionRulesBean.getFeedback().split("=");
					pwdTestRequest.setMsgID(s[0]);
					pwdTestRequest.setMsgDescription(s[1]);
				}
			} else {
				System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0008 ");
				pwdTestRequest.setPwdCompositionRulesBeanStatus("FALSE ");
				feedback = userGroupsBean.getFeedback();
				s = userGroupsBean.getFeedback().split("=");
				pwdTestRequest.setMsgID(s[0]);
				pwdTestRequest.setMsgDescription(s[1]);
			}

			tx.commit();
			txStatus(tx);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() exception 0003 ");
			e.printStackTrace();
		}
	}
	
	public String getFeedback() {
		return feedback;
	}

	public PwdTestRequest getPwdTestRequest() {
		return pwdTestRequest;
	}

	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.test.PwdTesterBean postConstructCallback() ");
    	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
    }
	
	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.test.PwdTesterBean removeBean() 0000 ");
		userGroupsBean.removeBean();
		pwdCompositionRulesBean.removeBean();
	} 
			
	public void setPwdTestRequest(PwdTestRequest r) {
		System.out.println(
				  "com.yardi.ejb.test.PwdTesterBean setPwdTestRequest() 0009 "
				+ "\n    "
				+ "r="
				+ r
				);
		pwdTestRequest = r;
	}

	private void txStatus(UserTransaction tx) {
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
			System.out.println("com.yardi.ejb.test.PwdTesterBean txStatus() SystemException 0001 ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.test.PwdTesterBean txStatus() 0002 "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
}
