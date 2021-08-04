package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;

/**
 * The LoginPage class provides a single instance, singleton, user login service.
 */
public class LoginPage extends Page{

    private static LoginPage loginPageInstance = null;

    private LoginPage() { pageID = PageIDList.loginPageID; }

    public static LoginPage getInstance() {
        if(loginPageInstance == null) {
            loginPageInstance = new LoginPage();
        }
        return loginPageInstance;
    }

    @Override
    public void loadPage() {



    }

}
