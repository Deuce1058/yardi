package com.yardi.ejb.userServices;

import com.yardi.ejb.Unique_Tokens;

public enum PasswordRule {

	REUSED_PWD(com.yardi.shared.rentSurvey.YardiConstants.YRD000A) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {
        	
       if (ctx.getPwdPolicy().getPpNbrUnique()==0 || ctx.getUserTokens().isEmpty()) return;
        	
			System.out.println("com.yardi.ejb.userServices.PasswordRule 0000 " 
			      + getMsgId()  
				  +"\n    "
				  + "   userTokens.size()="
				  + ctx.getUserTokens().size()
				  +"\n    "
				  + "userTokens=" 
			      + ctx.getUserTokens().toString()
			      );
			
			for (Unique_Tokens uniqueToken : ctx.getUserTokens()) {
				
				if (ctx.getJargon2Bean().verify(ctx.getNewPassword(), uniqueToken.getUp1Token())) {
					System.out.println("com.yardi.ejb.userServices.PasswordRule  0001 " 
				            + getMsgId() 
							+ "\n    "
							+ "  newPassword="
							+ ctx.getNewPassword()
							+ "\n    "
							+ "  uniqueToken.getUp1Token()="
							+ uniqueToken.getUp1Token()
							);
					throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD000A);
				}
			}
        }
	}, 
	
    MIN_LENGTH(com.yardi.shared.rentSurvey.YardiConstants.YRD0005) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (ctx.getStats().getPwdLength() < ctx.getPwdPolicy().getPpPwdMinLen()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0002 "
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

    N_UPPERCASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0018) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (ctx.getPwdPolicy().getPpNbrUpper()==null) return;

            if (ctx.getStats().getPwdNbrUpper() < ctx.getPwdPolicy().getPpNbrUpper()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0003 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrUpper()="
            			+ ctx.getStats().getPwdNbrUpper()
            			+ "ctx.getPwdPolicy().getPpNbrUpper()="
            			+ ctx.getPwdPolicy().getPpNbrUpper()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrUpper());
            }
        }
    },
    
    UPPERCASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0006) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

            if (!ctx.getPwdPolicy().getPpUpperRqd()) return;

            if (ctx.getStats().getPwdNbrUpper() <= 0) {            	
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0004 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrUpper()="
            			+ ctx.getStats().getPwdNbrUpper()
            			);
            }
        }
    },
    
    N_LOWER_CASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0019) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpNbrLower()==null) return;
    		
    		if (ctx.getStats().getPwdNbrLower() < ctx.getPwdPolicy().getPpNbrLower()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0005 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrLower()="
            			+ ctx.getStats().getPwdNbrLower()
            			+ "ctx.getPwdPolicy().getPpNbrLower()="
            			+ ctx.getPwdPolicy().getPpNbrLower()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrLower());
    		}    		
    	}
    },

    LOWER_CASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0007) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpLowerRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrLower()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0006 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrLower()="
            			+ ctx.getStats().getPwdNbrLower()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0007);
    		}
    	}
    },
    
    N_DIGITS_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0017) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    	
    		if (ctx.getPwdPolicy().getPpNbrDigits()==null) return;
    		
    		if (ctx.getStats().getPwdNbrNbr() < ctx.getPwdPolicy().getPpNbrDigits()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0007 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrNbr()="
            			+ ctx.getStats().getPwdNbrNbr()
            			+ "ctx.getPwdPolicy().getPpNbrDigits()="
            			+ ctx.getPwdPolicy().getPpNbrDigits()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpNbrDigits());
    		}
    	}    	
    }, 
    
    DIGIT_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0008) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpNumberRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrNbr()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0008 "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrNbr()="
            			+ ctx.getStats().getPwdNbrNbr()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0008);
    		}
    	}
    },
    
    N_SPECIAL_CHAR_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD001A) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpNbrSpecial()==null) return;
    		
    		if (ctx.getStats().getPwdNbrSpecial() < ctx.getPwdPolicy().getPpNbrSpecial()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 0009 "
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
    
    SPECIAL_CHAR_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0009) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (!ctx.getPwdPolicy().getPpSpecialRqd()) return;
    		
    		if (ctx.getStats().getPwdNbrSpecial()<=0) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 000A "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrSpecial()="
            			+ ctx.getStats().getPwdNbrSpecial()
            			);
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0009);
    		} 
    	}
    },
    
    MAX_LENGTH(com.yardi.shared.rentSurvey.YardiConstants.YRD0015) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpMaxPwdLen()==null) return;
    		
    		if (ctx.getStats().getPwdLength() > ctx.getPwdPolicy().getPpMaxPwdLen()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 000B "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdLength()="
            			+ ctx.getStats().getPwdLength()
            			+ "ctx.getPwdPolicy().getPpMaxPwdLen()="
            			+ ctx.getPwdPolicy().getPpMaxPwdLen()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpMaxPwdLen());
    		}
    	}
    },
    
    TOO_MANY_REPEAT_CHARS(com.yardi.shared.rentSurvey.YardiConstants.YRD0016) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    			throws PasswordValidationException {
    	
    		if (ctx.getPwdPolicy().getPpMaxRepeatChar()==null) return;
    		
    		if (ctx.getStats().getPwdNbrRepeatedChar() > ctx.getPwdPolicy().getPpMaxRepeatChar()) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 000C "
            			+ getMsgId() 
            			+ "\n    "
            			+ "ctx.getStats().getPwdNbrRepeatedChar()="
            			+ ctx.getStats().getPwdNbrRepeatedChar()
            			+ "ctx.getPwdPolicy().getPpMaxRepeatChar()="
            			+ ctx.getPwdPolicy().getPpMaxRepeatChar()
            			);
            	throwValidation(ctx.getPwdPolicy().getPpMaxRepeatChar());
    		}
    	}    	
    },
    
    PASSWORD_CONTAINS_ID(com.yardi.shared.rentSurvey.YardiConstants.YRD0011) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

        	if (ctx.getUserName()==null || ctx.getNewPassword()==null) {
        		return; //new password does not contain user ID
        	} 

        	
            if (!ctx.getPwdPolicy().isPpCantContainId()) return;

            if (ctx.getNewPassword().contains(ctx.getUserName())) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 000D "
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
    
    PASSWORD_CONTAINS_PWD(com.yardi.shared.rentSurvey.YardiConstants.YRD0010) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {
        	
        	if (!ctx.getPwdPolicy().isPpCantContainPwd()) return;

        	if (ctx.getNewPassword().contains(ctx.getCurrentPassword())) {
            	System.out.println("com.yardi.ejb.userServices.PasswordRule 000E "
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

	private final String msg;

    PasswordRule(String msg) {
        this.msg = msg;
    	System.out.println("com.yardi.ejb.userServices.PasswordRule 000F " + getMsgId());
    }

    public String getMsg() {
        return msg;
    }
    
    public String getMsgId() {
    	String m[] = msg.split("=");
    	return m[0];
    }
    
    private static String resolveMessage(String constant, Object... params) {
    	System.out.println("com.yardi.ejb.userServices.PasswordRule 0010 "
    			+ "\n    "
    			+ "constant="
    			+ constant
    			+ "\n    "
    			+ "params="
    			+ params.toString()
    			);
        int idx = constant.indexOf("=");
        String message = (idx >= 0) ? constant.substring(idx + 1) : constant;

        for (Object param : params) {
            message = message.replaceFirst("%n", String.valueOf(param));
        }

    	System.out.println("com.yardi.ejb.userServices.PasswordRule 0011 "
    			+ "\n    "
    			+ "message="
    			+ message
    			);
        return message;
    }
    
    protected void throwValidation(Object... params) throws PasswordValidationException {
    	System.out.println("com.yardi.ejb.userServices.PasswordRule 0012 ");
        throw new PasswordValidationException(this, resolveMessage(msg, params));
    }
    
    public abstract void validate(PasswordValidationContext ctx)
            throws PasswordValidationException;    
}
