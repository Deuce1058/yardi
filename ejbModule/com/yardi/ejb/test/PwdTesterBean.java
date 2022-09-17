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
	@EJB UserGroups userGroupsBean;
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PwdCompositionRules pwdCompositionRulesBean;
	@Resource UserTransaction tx;
	
	public PwdTesterBean() {
		//debug
		System.out.println("com.yardi.ejb.test.PwdTesterBean() ");
		//debug
	}

	public void enforce() {
		//debug
		System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0004 ");
		//debug
		try {
			tx.begin();
			txStatus(tx);
			String s[] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");

			if (!userGroupsBean.find(pwdTestRequest.getUserName()).isEmpty()) {
				//debug
				System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0005 ");
				//debug
				uniqueTokensBean.removeExtraTokens(uniqueTokensBean.findTokens(pwdTestRequest.getUserName()));
				
				if (pwdCompositionRulesBean.enforce(
						pwdTestRequest.getPassword(), 
						pwdTestRequest.getUserName(), 
						userGroupsBean.getLoginUserProfile().getUptoken(), 
						uniqueTokensBean.findTokens(pwdTestRequest.getUserName()))) {
					//debug
					System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0006 ");
					//debug
					pwdTestRequest.setPwdCompositionRulesBeanStatus("TRUE ");
				} else {
					//debug
					System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0007 ");
					//debug
					pwdTestRequest.setPwdCompositionRulesBeanStatus("FALSE ");
					feedback = pwdCompositionRulesBean.getFeedback();
					s = pwdCompositionRulesBean.getFeedback().split("=");
					pwdTestRequest.setMsgID(s[0]);
					pwdTestRequest.setMsgDescription(s[1]);
				}
			} else {
				//debug
				System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() 0008 ");
				//debug
				pwdTestRequest.setPwdCompositionRulesBeanStatus("FALSE ");
				feedback = userGroupsBean.getFeedback();
				s = userGroupsBean.getFeedback().split("=");
				pwdTestRequest.setMsgID(s[0]);
				pwdTestRequest.setMsgDescription(s[1]);
			}

			tx.commit();
			txStatus(tx);
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			//debug
			System.out.println("com.yardi.ejb.test.PwdTesterBean enforce() exception 0003 ");
			//debug
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
		//debug
    	System.out.println("com.yardi.ejb.test.PwdTesterBean postConstructCallback() ");
		//debug
    	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
    }
	
	@Remove
	public void removeBean() {
		//debug
		System.out.println("com.yardi.ejb.test.PwdTesterBean removeBean() 0000 ");
		//debug
		userGroupsBean.removeBean();
		pwdCompositionRulesBean.removeBean();
	} 
			
	public void setPwdTestRequest(PwdTestRequest r) {
		//debug
		System.out.println("com.yardi.ejb.test.PwdTesterBean setPwdTestRequest() 0009 ");
		//debug
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
