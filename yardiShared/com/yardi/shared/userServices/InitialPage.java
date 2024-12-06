package com.yardi.shared.userServices;


/**
 * Key/value representation of the list of the users initial pages based on the group(s) that the user belongs to. 
 * This will be stored in a Vector so that when converted to JSON it becomes an of array objects with this structure:
 * [ {"page":"page1", "url":"url1"}, {"page":"page2", "url":"url2"}, {"page":"page3", "url":"url3"} ]
 *
 * @author Jim 	
 */
public class InitialPage {
	/**
	 * The name of the page
	 */
	private String page = "";
	
	/**
	 * The URL of the page
	 */
	private String url = "";

	/**
	 * Constructor using all fields
	 * @param page the name of the page
	 * @param url the URL of the page
	 */
	public InitialPage(String page, String url) {
		this.page = page;
		this.url = url;
	}
	
	/**
	 * Return the page name
	 * @return page name
	 */
	public String getPage() {
		return page;
	}
	
	/**
	 * Return the URL of the page
	 * @return URL of the page
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set page name to the given String
	 * @param page name value to set
	 */
	public void setPage(String page) {
		this.page = page;
	}
	
	/**
	 * Set page's URL to the given String
	 * @param url value to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "InitialPage [page=" + page + ", url=" + url + "]";
	}
}
