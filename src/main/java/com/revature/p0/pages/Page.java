package com.revature.p0.pages;

/**
 * The Page class provides a template for this program's various different pages (I.e. the "LaunchPage").
 */
public abstract class Page {

    protected String pageID;

    public Page(String pageID) { this.pageID = pageID; }

    public String getPageID() { return pageID; }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return pageID.equals(page.getPageID());
    }

    /**
     * Provide page contents and functionality.
     */
    public abstract void loadPage();

}
