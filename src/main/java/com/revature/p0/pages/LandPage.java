package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;

/**
 * The LandPage class provides the first, or "landing", page for the program which provides basic options such as "login",
 * "register", etc. This is a single instance, singleton, class.
 */
public class LandPage extends Page{

    private static LandPage landPageInstance = null;

    private LandPage() { pageID = PageIDList.landPageID; }

    public static LandPage getInstance() {
        if(landPageInstance == null) {
            landPageInstance = new LandPage();
        }
        return landPageInstance;
    }

    @Override
    public void loadPage() {



    }

}
