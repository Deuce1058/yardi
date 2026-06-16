package com.yardi.ejb;

/**
 * Common contract for classes that represent the outcome
 * of an operation and expose standardized feedback details.
 */
public interface OperationResult {

	/**
	 * Return the raw feedback string
	 * @return raw feedback string 
	 */
    String getFeedback();

	/**
	 * Return the message description which is the String on the right side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present  
	 * @return message description
	 */
    String getMsgDescription();

    /**
	 * Return the message ID which is the string on the left side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present.
	 * @return message ID
	 */
    String getMsgid();

    /**
	 * Return the success flag which indicates whether the operation succeeded or failed
	 * @return success flag
	 */
    boolean isSuccess();
}