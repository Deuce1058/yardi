package com.yardi.ejb.userServices;

import com.yardi.ejb.Unique_Tokens;

public enum PasswordRule {

	REUSED_PWD(com.yardi.shared.rentSurvey.YardiConstants.YRD000A) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {
        	
        	if (ctx.getPwdPolicy().getPpNbrUnique()==0 || ctx.getUserTokens().isEmpty()) return;
        	
			for (Unique_Tokens u : ctx.getUserTokens()) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ u
					);
			}
			
			System.out.println("com.yardi.ejb.userServices.PasswordRule YRD000A "
					+ "\n"
					+ "   userTokens.size()="
					+ ctx.getUserTokens().size()
					);

			for (Unique_Tokens uniqueToken : ctx.getUserTokens()) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ uniqueToken
					);
				
				if (ctx.getJargon2Bean().verify(ctx.getNewPassword(), uniqueToken.getUp1Token())) {
					System.out.println("com.yardi.ejb.userServices.PasswordRule YRD000A  "
							+ "\n "
							+ "  newPassword="
							+ ctx.getNewPassword()
							+ "\n "
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
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0006);
            }
        }
    },
    
    N_LOWER_CASE_REQUIRED(com.yardi.shared.rentSurvey.YardiConstants.YRD0019) {
    	@Override
    	public void validate(PasswordValidationContext ctx)
    	        throws PasswordValidationException {
    		
    		if (ctx.getPwdPolicy().getPpNbrLower()==null) return;
    		
    		if (ctx.getStats().getPwdNbrLower() < ctx.getPwdPolicy().getPpNbrLower()) {
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
            	throwValidation(ctx.getPwdPolicy().getPpMaxRepeatChar());
    		}
    	}    	
    },
    
    PASSWORD_CONTAINS_ID(com.yardi.shared.rentSurvey.YardiConstants.YRD0011) {
        @Override
        public void validate(PasswordValidationContext ctx)
                throws PasswordValidationException {

        	if (ctx.getUserName()==null || ctx.getNewPassword()==null) {
    	    	System.out.println("com.yardi.ejb.userServices.PaswordRule ");
        		return; //new password does not contain user ID
        	} 

        	
            if (!ctx.getPwdPolicy().isPpCantContainId()) return;

            if (ctx.getNewPassword().contains(ctx.getUserName())) {
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
            	throwValidation(com.yardi.shared.rentSurvey.YardiConstants.YRD0010);
        	}
        }
    };

	private final String msg;

    PasswordRule(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
    
    public String getMsgId() {
    	String m[] = msg.split("=");
    	return m[0];
    }
    
    private static String resolveMessage(String constant, Object... params) {
        int idx = constant.indexOf("=");
        String message = (idx >= 0) ? constant.substring(idx + 1) : constant;

        for (Object param : params) {
            message = message.replaceFirst("%n", String.valueOf(param));
        }

        return message;
    }
    
    protected void throwValidation(Object... params) throws PasswordValidationException {
        throw new PasswordValidationException(this, resolveMessage(msg, params));
    }
    
    public abstract void validate(PasswordValidationContext ctx)
            throws PasswordValidationException;
    
}
