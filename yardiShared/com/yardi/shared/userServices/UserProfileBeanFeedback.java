package com.yardi.shared.userServices;

/**
 * An immutable value object that encapsulates the outcome of a {@link com.yardi.ejb.UserProfileBean UserProfileBean} operation.
 *
 * <p>Each instance carries two pieces of information:
 * <ul>
 *   <li><b>success</b> – whether the operation completed without error</li>
 *   <li><b>feedback</b> – a message string, optionally structured as {@code "msgid=description"}</li>
 * </ul>
 *
 * <p>When the feedback string contains an {@code '='} delimiter, the message ID and human-readable
 * description can be extracted independently via {@link #getMsgid()} and {@link #getMsgDescription()}.
 * If no delimiter is present, both methods return the raw feedback string.
 *
 * <p>Instances are created exclusively through the static factory methods {@link #ok(String)} and {@link #fail(String)}.
 */
public final class UserProfileBeanFeedback implements OperationResult {
	/**
	 * success/fail flag indicates whether the operation succeeded or failed 
	 */
    private final boolean success;
    /**
     * Raw feedback from the operation
     */
    private final String feedback;  

    /**
     * Constructs a UserProfileBeanFeedback object using the success flag and feedback message. Called by either {@link #ok(String)} and {@link #fail(String)}. 
     * @param success success flag
     * @param feedback feedback message
     */
   private UserProfileBeanFeedback(boolean success, String feedback) {
	   System.out.println("com.yardi.shared.userServices.UserProfileBeanFeedback.UserProfileBeanFeedback() "
			   + "\n    "
			   + success
			   + "\n    "
			   + feedback
			   );
       this.success = success;
       this.feedback = feedback;
    }

   /**
    * Return the raw feedback string
    * @return raw feedback string 
    */
    public String  getFeedback() { 
		return feedback; 
	}

    /**
     * Return the message description which is the String on the right side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present  
     * @return message description
     */
    public String getMsgDescription() {
    	int i = feedback.indexOf("=");
    	
    	if (i<0) {
    		return feedback;
    	}
    	
    	return feedback.substring(i+1); 
    }

    /**
     * Return the message ID which is the string on the left side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present.
     * @return message ID
     */
	public String getMsgid() {
    	int i = feedback.indexOf("=");
    	
    	if (i<0) {
    		return feedback;
    	}
    	
    	return feedback.substring(0, i); 
    }

	/**
	 * Return the success flag which indicates whether the operation succeeded or failed
	 * @return success flag
	 */
    public boolean isSuccess() { 
    	return success; 
    }
  
    /**
     * Factory method to construct a UserProfileBeanFeedback object with the success flag set to false and the given feedback.  
     * @param feedback message string describing why the operation failed 
     * @return UserProfileBeanFeedback object
     */
    public static UserProfileBeanFeedback fail(String feedback) {
    	System.out.println("com.yardi.shared.userServices.UserProfileBeanFeedback.fail() 0000 "
    			+ "\n    "
    			+ feedback
    			);
        return new UserProfileBeanFeedback(false, feedback);
    }

    /**
     * Factory method to construct a UserProfileBeanFeedback object with the success flag set to true and the given feedback.
     * @param feedback message string that indicates the operation was a success
     * @return UserProfileBeanFeedback object
     */
    public static UserProfileBeanFeedback ok(String feedback) {
    	System.out.println("com.yardi.shared.userServices.UserProfileBeanFeedback.ok() 0001 "
    			+ "\n    "
    			+ feedback
    			);
        return new UserProfileBeanFeedback(true, feedback);
    }
}
