package com.yardi.shared.userServices;


/**
 * Key/value representation of the list of the users initial pages based on the group(s) that the user belongs to. 
 * This will be stored in a Vector so that when converted to JSON it becomes an of array objects with this structure:
 * [ {"page":"page1", "url":"url1"}, {"page":"page2", "url":"url2"}, {"page":"page3", "url":"url3"} ]
 *
 * @author Jim 	
 */
public class LoginInitialPage {
	private String page = "";
	private String url = "";

	public LoginInitialPage(String page, String url) {
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

	@Override
	public String toString() {
		return "InitialPage [page=" + page + ", url=" + url + "]";
	}
}
