package com.yardi.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;

/**
 * Session Bean implementation class EditUserProfileBean
 */
@Stateless
public class EditPasswordPolicyBean implements EditPasswordPolicy {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;

	public EditPasswordPolicyBean() {
    	System.out.println("com.yardi.ejb.EditPassworPolicyBean EditPasswordPolicyBean() ");
    }

    private Pwd_Policy find(Long rrn) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean find() 0008  ");
		Pwd_Policy pwdPolicy = null;
		TypedQuery<Pwd_Policy> qry = em.createQuery(
			  "SELECT p from Pwd_Policy p "
			+ "WHERE p.ppRrn = :rrn ",
			Pwd_Policy.class);
		try {
			pwdPolicy = qry
				.setParameter("rrn", rrn)
				.getSingleResult();
		} catch (NoResultException e) {
			///debug
			System.out.println("com.yardi.ejb.EditPasswordPolicyBean find() exception 000A ");
			//debug
			e.printStackTrace();
		}
		if (!(pwdPolicy==null)) {
			pwdPolicy.setPp_upper_rqd(pwdPolicy.getPp_upper_rqd());
			pwdPolicy.setPp_lower_rqd(pwdPolicy.getPp_lower_rqd());
			pwdPolicy.setPp_number_rqd(pwdPolicy.getPp_number_rqd());
			pwdPolicy.setPp_special_rqd(pwdPolicy.getPp_special_rqd());
			pwdPolicy.setPp_cant_contain_id(pwdPolicy.getPp_cant_contain_id());
			pwdPolicy.setPp_cant_contain_pwd(pwdPolicy.getPp_cant_contain_pwd());
		}
		//debug
		System.out.println("com.yardi.ejb.EditPasswordPolicyBean find() 0009 "
				+ "\n "
				+ "  rrn=" + rrn
				+ "\n "
				+ "  pwdPolicy=" + pwdPolicy
				);
		//debug
		return pwdPolicy;
	}

	public EditPwdPolicyRequest getPwd_Policy() {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean getPwd_Policy() 0007 ");
    	Pwd_Policy pwdPolicy = find(1L);
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean getPwd_Policy() 0003 "
    			+ "\n   "
    			+ pwdPolicy
    			);
    	return newEditPwdPolicyRequest(pwdPolicy);
    }

    /**
     * Map the password policy returned from the PasswordPolicyBean to EditPwdPolicyRequest which will be returned to the browser
     * 
     * @param pwdPolicy The password policy returned from the PasswordPolicyBean
     * @return EditPwdPolicyRequest A String representation of Pwd_Policy which will be returned to the browser
     */
    private EditPwdPolicyRequest newEditPwdPolicyRequest(Pwd_Policy pwdPolicy) {
    	//debug
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean newEditPwdPolicyReqest() 0005 ");
    	//debug
    	EditPwdPolicyRequest editPwdPolicyRequest = new EditPwdPolicyRequest();
    	String s[] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
    	editPwdPolicyRequest.setMsgId(s[0]);
    	editPwdPolicyRequest.setMsgDescription(s[1]);

    	if (pwdPolicy == null) {
    		editPwdPolicyRequest.setAction(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_ADD);
    	} else {
    		editPwdPolicyRequest.setAction(com.yardi.shared.rentSurvey.YardiConstants.EDIT_PASSWORD_POLICY_REQUEST_ACTION_UPDATE);
    		editPwdPolicyRequest.setPwdLifeInDays    (new Short(pwdPolicy.getPpDays()).toString());
    		editPwdPolicyRequest.setNbrUnique        (new Short(pwdPolicy.getPpNbrUnique()).toString());
    		editPwdPolicyRequest.setMaxSignonAttempts(new Short(pwdPolicy.getPpMaxSignonAttempts()).toString()); 
    		editPwdPolicyRequest.setPwdMinLen        (new Short(pwdPolicy.getPpPwdMinLen()).toString());
    		editPwdPolicyRequest.setUpperRqd         (pwdPolicy.getPp_upper_rqd());
    		editPwdPolicyRequest.setLowerRqd         (pwdPolicy.getPp_lower_rqd());
    		editPwdPolicyRequest.setNbrRqd           (pwdPolicy.getPp_number_rqd());
    		editPwdPolicyRequest.setSpecialRqd       (pwdPolicy.getPp_special_rqd());

    		if (pwdPolicy.getPpMaxPwdLen()==null) {
    			editPwdPolicyRequest.setMaxPwdLen("null");
    		} else {
    			editPwdPolicyRequest.setMaxPwdLen(pwdPolicy.getPpMaxPwdLen().toString());
    		}

    		if (pwdPolicy.getPpMaxRepeatChar()==null) {
    			editPwdPolicyRequest.setMaxRepeatChar("null");
    		} else {
    			editPwdPolicyRequest.setMaxRepeatChar(pwdPolicy.getPpMaxRepeatChar().toString());
    		}

    		if (pwdPolicy.getPpNbrDigits()==null) {
    			editPwdPolicyRequest.setNbrDigits("null");
    		} else {
    			editPwdPolicyRequest.setNbrDigits(pwdPolicy.getPpNbrDigits().toString());
    		}

    		if (pwdPolicy.getPpNbrUpper()==null) {
    			editPwdPolicyRequest.setNbrUpper("null");
    		} else {
    			editPwdPolicyRequest.setNbrUpper(pwdPolicy.getPpNbrUpper().toString());
    		}

    		if (pwdPolicy.getPpNbrLower()==null) {
    			editPwdPolicyRequest.setNbrLower("null");
    		} else {
    			editPwdPolicyRequest.setNbrLower(pwdPolicy.getPpNbrLower().toString());
    		}

    		if (pwdPolicy.getPpNbrSpecial()==null) {
    			editPwdPolicyRequest.setNbrSpecial("null");
    		} else {
    			editPwdPolicyRequest.setNbrSpecial(pwdPolicy.getPpNbrSpecial().toString());
    		}

    		editPwdPolicyRequest.setCantContainId(pwdPolicy.getPp_cant_contain_id());
    		editPwdPolicyRequest.setCantContainPwd(pwdPolicy.getPp_cant_contain_pwd());
    	}
    	
    	//debug
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean newEditPwdPolicyRequest() 0006 "
    			+ "\n "
    			+ "   editPwdPolicyRequest="
    			+ editPwdPolicyRequest.toString()
    			);
    	//debug
    	return editPwdPolicyRequest;	
    }
    
    /**
     * Construct a new Pwd_Policy request by mapping EditPwdPolicyRequest to Pwd_Policy. Several columns in PWD_POLICY may contain null. To map these correctly from EditPwdPolicyRequest
     * test for a String with a value of "null". If the field from EditPwdPolicyRequest is "null" set the corresponding field in Pwd_Policy to null. Otherwise, the corresponding field
     * in Pwd_policy is mapped to String or Short
	 * @param editPwdPolicyRequest2 
     * 
     * @return Pwd_Policy
     */

    public Pwd_Policy newPwdPolicy(EditPwdPolicyRequest editPwdPolicyRequest) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean newPwdPolicy() 0002 ");
    	Pwd_Policy newPwdPolicy = new Pwd_Policy();
		newPwdPolicy.setPpDays(Short.parseShort(editPwdPolicyRequest.getPwdLifeInDays()));
		newPwdPolicy.setPpNbrUnique(Short.parseShort(editPwdPolicyRequest.getNbrUnique()));
		newPwdPolicy.setPpMaxSignonAttempts(Short.parseShort(editPwdPolicyRequest.getMaxSignonAttempts()));
		newPwdPolicy.setPpPwdMinLen(Short.parseShort(editPwdPolicyRequest.getPwdMinLen()));
		newPwdPolicy.setPp_upper_rqd(editPwdPolicyRequest.getUpperRqd()); 
		newPwdPolicy.setPp_lower_rqd(editPwdPolicyRequest.getLowerRqd());
		newPwdPolicy.setPp_number_rqd(editPwdPolicyRequest.getNbrRqd());
		newPwdPolicy.setPp_special_rqd(editPwdPolicyRequest.getSpecialRqd());
		
		if (editPwdPolicyRequest.getMaxPwdLen().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpMaxPwdLenNull();
		} else {
			newPwdPolicy.setPpMaxPwdLen(Short.parseShort(editPwdPolicyRequest.getMaxPwdLen()));
		}
		
		if (editPwdPolicyRequest.getMaxRepeatChar().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpMaxRepeatCharNull();
		} else {
			newPwdPolicy.setPpMaxRepeatChar(Short.parseShort(editPwdPolicyRequest.getMaxRepeatChar()));
		}
		
		if (editPwdPolicyRequest.getNbrDigits().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpNbrDigitsNull();
		} else {
			newPwdPolicy.setPpNbrDigits(Short.parseShort(editPwdPolicyRequest.getNbrDigits()));
		}
		
		if (editPwdPolicyRequest.getNbrUpper().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpNbrUpperNull();
		} else {
			newPwdPolicy.setPpNbrUpper(Short.parseShort(editPwdPolicyRequest.getNbrUpper()));
		}
		
		if (editPwdPolicyRequest.getNbrLower().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpNbrLowerNull();
		} else {
			newPwdPolicy.setPpNbrLower(Short.parseShort(editPwdPolicyRequest.getNbrLower()));
		}
		
		if (editPwdPolicyRequest.getNbrSpecial().trim().equalsIgnoreCase("null")) {
			newPwdPolicy.setPpNbrSpecialNull();
		} else {
			newPwdPolicy.setPpNbrSpecial(Short.parseShort(editPwdPolicyRequest.getNbrSpecial()));
		}
		
		newPwdPolicy.setPp_cant_contain_id(editPwdPolicyRequest.getCantContainId());
		newPwdPolicy.setPp_cant_contain_pwd(editPwdPolicyRequest.getCantContainPwd());
		newPwdPolicy.setPpRrn(1L);
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean newPwdPolicy() 0004 "
    			+ "\n   "
    			+ newPwdPolicy.toString());
		return newPwdPolicy;
	}
    
    public void persist(EditPwdPolicyRequest editPwdPolicyRequest) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean persist() 0001 ");
    	Pwd_Policy newPwdPolicy = newPwdPolicy(editPwdPolicyRequest);
    	newPwdPolicy.setPpRrnNull();
    	em.persist(newPwdPolicy);
    }
    
    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean postConstructCalllback() ");
    }
    
	public void updateAll(EditPwdPolicyRequest editPwdPolicyRequest) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean updateAll() 0000 ");
    	em.merge(newPwdPolicy(editPwdPolicyRequest));
    }
}
