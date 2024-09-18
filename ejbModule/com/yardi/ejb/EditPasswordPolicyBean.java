package com.yardi.ejb;

import jakarta.annotation.PostConstruct;
//import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;

/**
 * Session Bean implementation of methods for editing password policy  
 */
@Stateless
public class EditPasswordPolicyBean implements EditPasswordPolicy {
	/**
	 * {@link EntityManager EntityManager}
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager em;

	/**
	 * Default constructor 
	 */
	public EditPasswordPolicyBean() {
    	System.out.println("com.yardi.ejb.EditPassworPolicyBean EditPasswordPolicyBean() ");
    }

	/**
	 * Find the password policy by rrn.<p>
	 * 
	 * The password policy is retrieved directly instead of delegating to 
	 * {@link com.yardi.ejb.PasswordPolicyBean#getPwdPolicy() com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()}.
	 * Changes made to the instance returned by <code>getPwdPolicy()</code>will not be persisted because the instance is not managed.
	 * 
	 * @param rrn the rrn of the password policy to find 
	 * @return password policy entity matching the given rrn 
	 */
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
		/* if the password policy was found init password policy entity fields using the values from the database */
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

    /**
     * Return a {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container.<p>
     * 
     * Clients use this method to obtain a container which holds all of the password policy elements that can be modified by the user. 
     * The {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity is found and then mapped to the <code>EditPwdPolicyRequest</code> container 
     * which is then returned.
     * 
     * @return a container which holds all of the password policy elements that can be modified by the user.
     */
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
     * Map a {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity to 
     * {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest}.<p>
     * 
     * <code>EditPwdPolicyRequest</code> is a container which holds all of the password policy elements that can be 
     * modified by the user. <code>EditPwdPolicyRequest</code> serves as the DTO between the browser and the application. 
     * 
     * @param pwdPolicy The password policy returned from the {@link com.yardi.ejb.PasswordPolicyBean#getPwdPolicy() com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()}
     * @return a container which holds all of the password policy elements that can be modified by the user.
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
    		editPwdPolicyRequest.setPwdLifeInDays    (Short.toString(pwdPolicy.getPpDays()));
    		editPwdPolicyRequest.setNbrUnique        (Short.toString(pwdPolicy.getPpNbrUnique()));
    		editPwdPolicyRequest.setMaxSignonAttempts(Short.toString(pwdPolicy.getPpMaxSignonAttempts())); 
    		editPwdPolicyRequest.setPwdMinLen        (Short.toString(pwdPolicy.getPpPwdMinLen()));
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
	 * Construct a new {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity by mapping 
	 * {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container to <code>Pwd_Policy</code> entity.<p> 
	 * 
	 * Several columns in the <code>PWD_POLICY</code> database table may contain null. To map these correctly from <code>EditPwdPolicyRequest</code>, test for 
	 * a String with a value of "<i>null</i>". If the field from <code>EditPwdPolicyRequest</code> is "<i>null</i>" set the corresponding field in 
	 * <code>Pwd_Policy</code> entity to null. Otherwise, the corresponding field in <code>Pwd_Policy</code> entity is mapped to String or Short.
	 * 
	 * @param editPwdPolicyRequest a container that holds all the password policy elements which can be modified by the user. 	  
	 * @return password policy entity
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

	/**
	 * Persist a new {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity.<p>
	 * 
	 * This method accepts a {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container and maps it to a 
	 * <code>Pwd_Policy</code> entity by delegating to {@link com.yardi.ejb.EditPasswordPolicyBean#newPwdPolicy(EditPwdPolicyRequest) 
	 * com.yardi.ejb.EditPasswordPolicyBean.newPwdPolicy(EditPwdPolicyRequest)} 
	 * before persisting the <code>Pwd_Policy</code> entity.
	 * 
	 * @param editPwdPolicyRequest a container which holds all the password policy elements that can be modified
	 * by the user. 
	 */ 
	public void persist(EditPwdPolicyRequest editPwdPolicyRequest) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean persist() 0001 ");
    	Pwd_Policy newPwdPolicy = newPwdPolicy(editPwdPolicyRequest);
    	newPwdPolicy.setPpRrnNull();
    	em.persist(newPwdPolicy);
    }
    
	/**
	 * Post construct callback 
	 */
    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean postConstructCalllback() ");
    }
    
    /**
     * Update all of the password policy elements that can be modified by the user.<p>
     * 
     * This method accepts a {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container and maps it to a 
     * {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity by delegating to 
     * {@link com.yardi.ejb.EditPasswordPolicyBean#newPwdPolicy(EditPwdPolicyRequest) 
	 * com.yardi.ejb.EditPasswordPolicyBean.newPwdPolicy(EditPwdPolicyRequest)} before merging the state to 
	 * the persistence context.
	 * 
	 * @param editPwdPolicyRequest an <code>EditPwdPolicyRequest</code> container which holds all the password policy elements that can be modified
	 * by the user. 
     */
	public void updateAll(EditPwdPolicyRequest editPwdPolicyRequest) {
    	System.out.println("com.yardi.ejb.EditPasswordPolicyBean updateAll() 0000 ");
    	em.merge(newPwdPolicy(editPwdPolicyRequest));
    }
}
