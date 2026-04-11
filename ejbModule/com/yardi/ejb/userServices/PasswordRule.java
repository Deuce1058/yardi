package com.yardi.ejb.userServices;

import java.util.Arrays;

import com.yardi.ejb.Unique_Tokens;

/**
 * A password validation engine that validates the new password against a set of defined rules. Password policy determines which rules are enforced. Validation halts at the first failed rule and 
 * throws {@link com.yardi.ejb.userServices.PasswordValidationException PasswordValidationException}. All objects needed to validate the new password are stored in container 
 * {@link com.yardi.ejb.userServices.PasswordValidationContext PasswordValidationContext}.   
 */
public enum PasswordRule {

	/**
	 * Ensure passwords are unique. The new password must not match any of the last <i>n</i> used passwords. Password policy defines how many unique passwords are stored in password history.
	 */
	REUSED_PWD(com.yardi.shared.rentSurvey.YardiConstants.YRD000A) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {
        	
			if (ctx.getPwdPolicy().getPpNbrUnique() == 0 || ctx.getUserTokens().isEmpty())
				return;

			System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0000 "
					+ getMsgId() 
					+ "\n    "
					+ "   userTokens.size()=" 
					+ ctx.getUserTokens().size() 
					+ "\n    " 
					+ "userTokens="
					+ ctx.getUserTokens().toString());

			for (Unique_Tokens uniqueToken : ctx.getUserTokens()) {

				if (ctx.getJargon2Bean().verify(ctx.getNewPassword(), uniqueToken.getUp1Token())) {
					System.out.println("com.yardi.ejb.userServices.PasswordRule.validate()  0001 "
							+ getMsgId()
							+ "\n    " 
							+ "  newPassword=" 
							+ ctx.getNewPassword() 
							+ "\n    "
							+ "  uniqueToken.getUp1Rrn()=" 
							+ uniqueToken.getUp1Rrn() 
							+ "\n    "
							+ "  uniqueToken.getUp1Token()=" 
							+ uniqueToken.getUp1Token());
					throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD000A);
				}
			}
		}
	}, 

	/**
	 * Ensure password length is not shorter than the minimum length defined in password policy
	 */
    MIN_LENGTH(com.yardi.shared.rentSurvey.YardiConstants.YRD0005) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (ctx.getStats().getPwdLength() < ctx.getPwdPolicy().getPpPwdMinLen()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0002 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdLength()="
            			+ ctx.getStats().getPwdLength()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpPwdMinLen()="
            			+ctx.getPwdPolicy().getPpPwdMinLen()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpPwdMinLen());
            }
        }
    },

    /**
     * Ensure password contains at least <i>n</i> upper case characters. 
     */
    N_UPPERCASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0018) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (ctx.getPwdPolicy().getPpNbrUpper()==null) return;

            if (ctx.getStats().getPwdNbrUpper() < ctx.getPwdPolicy().getPpNbrUpper()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0003 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrUpper()="
            			+ ctx.getStats().getPwdNbrUpper()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpNbrUpper()="
            			+ ctx.getPwdPolicy().getPpNbrUpper()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrUpper());
            }
        }
    },
    
    /**
     * Ensure password contains at least one upper case character.
     */
    UPPERCASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0006) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (!ctx.getPwdPolicy().getPpUpperRqd()) return;

            if (ctx.getStats().getPwdNbrUpper() <= 0) {            	
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0004 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrUpper()="
            			+ ctx.getStats().getPwdNbrUpper()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0006);
            }
        }
    },

    /**
     * Ensure password contains at least <i>n</i> lower case characters.
     */
    N_LOWER_CASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0019) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpNbrLower()==null) return;
    		
    		if (ctx.getStats().getPwdNbrLower() < ctx.getPwdPolicy().getPpNbrLower()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0005 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrLower()="
            			+ ctx.getStats().getPwdNbrLower()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpNbrLower()="
            			+ ctx.getPwdPolicy().getPpNbrLower()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrLower());
    		}    		
    	}
    },
    
    /**
     * Ensure password has at least one lower case character.
     */
    LOWER_CASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0007) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpLowerRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrLower()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0006 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrLower()="
            			+ ctx.getStats().getPwdNbrLower()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0007);
    		}
    	}
    },
    
    /**
     * Ensure password has at least <i>n</i> digits.
     */
    N_DIGITS_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0017) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    	
    		if (ctx.getPwdPolicy().getPpNbrDigits()==null) return;
    		
    		if (ctx.getStats().getPwdNbrNbr() < ctx.getPwdPolicy().getPpNbrDigits()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0007 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrNbr()="
            			+ ctx.getStats().getPwdNbrNbr()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpNbrDigits()="
            			+ ctx.getPwdPolicy().getPpNbrDigits()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrDigits());
    		}
    	}    	
    }, 

    /**
     * Ensure password has at least one digit.
     */
    DIGIT_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0008) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpNumberRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrNbr()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0008 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrNbr()="
            			+ ctx.getStats().getPwdNbrNbr()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0008);
    		}
    	}
    },

    /**
     * Ensure password has at least <i>n</i> special characters.
     */
    N_SPECIAL_CHAR_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD001A) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpNbrSpecial()==null) return;
    		
    		if (ctx.getStats().getPwdNbrSpecial() < ctx.getPwdPolicy().getPpNbrSpecial()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 0009 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrSpecial()="
            			+ ctx.getStats().getPwdNbrSpecial()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpNbrSpecial()="
            			+ ctx.getPwdPolicy().getPpNbrSpecial()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrSpecial());
    		}
    	}
    },
    
    /**
     * Ensure password has at least one special character.
     */
    SPECIAL_CHAR_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0009) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpSpecialRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrSpecial()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 000A "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrSpecial()="
            			+ ctx.getStats().getPwdNbrSpecial()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0009);
    		} 
    	}
    },
    
    /**
     * Ensure password is not longer than the maximum length defined in password policy.
     */
    MAX_LENGTH(com.yardi.shared.rentSurvey.YardiConstants.YRD0015) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpMaxPwdLen()==null) return;
    		
    		if (ctx.getStats().getPwdLength() > ctx.getPwdPolicy().getPpMaxPwdLen()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 000B "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdLength()="
            			+ ctx.getStats().getPwdLength()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpMaxPwdLen()="
            			+ ctx.getPwdPolicy().getPpMaxPwdLen()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpMaxPwdLen());
    		}
    	}
    },

    /**
     * Ensure the number of repeated characters in the password does not exceed the maximum number of repeated characters defined in password policy.  
     */
    TOO_MANY_REPEAT_CHARS(com.yardi.shared.rentSurvey.YardiConstants.YRD0016) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    	
    		if (ctx.getPwdPolicy().getPpMaxRepeatChar()==null) return;
    		
    		if (ctx.getStats().getPwdNbrRepeatedChar() > ctx.getPwdPolicy().getPpMaxRepeatChar()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 000C "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrRepeatedChar()="
            			+ ctx.getStats().getPwdNbrRepeatedChar()
            			+ "\n    "
            			+ "ctx.getPwdPolicy().getPpMaxRepeatChar()="
            			+ ctx.getPwdPolicy().getPpMaxRepeatChar()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpMaxRepeatChar());
    		}
    	}    	
    },

    /**
     * Ensure the password does not contain the user ID.
     */
    PASSWORD_CONTAINS_ID(com.yardi.shared.rentSurvey.YardiConstants.YRD0011) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

        	if (ctx.getUserName()==null || ctx.getNewPassword()==null) {
        		return; //new password does not contain user ID
        	} 

        	
            if (!ctx.getPwdPolicy().isPpCantContainId()) return;

            if (ctx.getNewPassword().contains(ctx.getUserName())) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 000D "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getNewPassword()="
            			+ ctx.getNewPassword()
            			+ "\n    "
            			+ "ctx.getUserName()="
            			+ ctx.getUserName()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0011);
            }
        }
    },
    
    /**
     * Ensures that the new password is not derived from the current password. Specifically, the new password must not contain the current password as a substring or trivial 
     * modification thereof, preventing incremental changes such as suffixing or prefixing characters.
     */
    PASSWORD_CONTAINS_PWD(com.yardi.shared.rentSurvey.YardiConstants.YRD0010) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {
        	
        	if (!ctx.getPwdPolicy().isPpCantContainPwd()) return;

        	if (ctx.getNewPassword().contains(ctx.getCurrentPassword())) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule.validate() 000E "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getNewPassword()="
            			+ ctx.getNewPassword()
            			+ "\n    "
            			+ "ctx.getCurrentPassword()="
            			+ ctx.getCurrentPassword()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0010);
        	}
        }
    };

	/**
	 * Message template associated with the rule.
	 * Format: "CODE=Message with %n placeholders"
	 */
	private final String msg;

	/**
	 * Creates a password rule with the given message template.
	 *
	 * @param msg the validation message constant
	 */
	PasswordRule(String msg) {
        this.msg = msg;
    	System.out.println("com.yardi.ejb.userServices.PasswordRule.PasswordRule() 000F " + getMsgId());
    }

	/**
	 * Return the validation message constant
	 * @return validation message constant
	 */
    public String getMsg() {
        return msg;
    }
    
    /**
     * Returns the message id. This is the substring of the message constant starting at index 0 and ending at index of "=", exclusively.
     * @return message id
     */
    public String getMsgId() {
    	String m[] = msg.split("=");
    	return m[0];
    }
    
    /**
     * Resolves a message template by replacing %n placeholders with parameters.
     *
     * @param constant the message template
     * @param params values to inject into the template
     * @return resolved message string
     */
    private static String resolveMessage(String constant, Object... params) {
    	System.out.println("com.yardi.ejb.userServices.PasswordRule.resolveMessage() 0010 "
    			+ "\n    "
    			+ "constant="
    			+ constant
    			+ "\n    "
    			+ "params="
    			+ Arrays.toString(params)
    			);
        int idx = constant.indexOf("=");
        String message = (idx >= 0) ? constant.substring(idx + 1) : constant;

        for (Object param : params) {
            message = message.replaceFirst("%n", String.valueOf(param));
        }

    	System.out.println("com.yardi.ejb.userServices.PasswordRule.resolveMessage( 0011 "
    			+ "\n    "
    			+ "message="
    			+ message
    			);
        return message;
    }
    
    /**
     * Causes a {@link com.yardi.ejb.userServices.PasswordValidationException PasswordValidationException} to be thrown while injecting values into the message template 
     * @param params values to inject into the message template 
     * @throws PasswordValidationException
     */
    protected void throwValidation(Object... params) throws PasswordValidationException {
    	System.out.println("com.yardi.ejb.userServices.PasswordRule.throwValidation() 0012 ");
        throw new PasswordValidationException(this, resolveMessage(msg, params));
    }
    
    /**
     * Validates the password against this specific rule.<p>
     *
     * Each enum constant provides its own implementation of this method
     * to enforce a particular password policy constraint.<p>
     *
     * If the validation fails, a {@link com.yardi.ejb.userServices.PasswordValidationException PasswordValidationException}
     * must be thrown using {@link #throwValidation(Object...)}.<p>
     *
     * @param ctx the {@link com.yardi.ejb.userServices.PasswordValidationContext PasswordValidationContext} containing the current password, new password, user name, the hashed 
     * password history, computed statistics, password policy and a reference to {@link com.yardi.ejb.crypto.Jargon2Bean Jargon2Bean}.
     *
     * @throws PasswordValidationException if the password violates this rule
     */
    public abstract void validate(PasswordValidationContext ctx)
            throws PasswordValidationException;    
}
