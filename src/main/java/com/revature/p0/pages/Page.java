package com.revature.p0.pages;

/**
 * The Page class provides a template for this program's various different pages (I.e. the "LaunchPage").
 */
public abstract class Page {

    protected String pageID;

    public Page(String pageID) {
        this.pageID = pageID;
    }

    public String getPageID() { return pageID; }

    /**
     * Provide page contents and functionality.
     */
    public abstract void loadPage();

}
