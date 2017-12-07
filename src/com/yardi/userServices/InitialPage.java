package com.yardi.userServices;


/**
 * Key/value representation of the list of the users initial pages based on the group(s) that the user belongs to. 
 * This will be stored in a Vector so that when converted to JSON it becomes an array object with this structure:
 * [ {"page":"key1", "url":"value1"}, {"page":"key2", "url":"value2"}, {"page":"key3", "url":"value3"} ]
 *
 * @author Jim 	
 */
public class InitialPage {
	private String page = "";
	private String url = "";

	public InitialPage(String page, String url) {
		this.page = page;
		this.url = url;
	}
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
