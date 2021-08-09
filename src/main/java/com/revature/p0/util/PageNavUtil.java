package com.revature.p0.util;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.LandPage;
import com.revature.p0.pages.LoginPage;
import com.revature.p0.pages.Page;
import com.revature.p0.pages.RegisterPage;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * The PageNavUtil class provides methods for storing and transitioning between Pages.
 */
public class PageNavUtil {

    private static PageNavUtil pageNavUtil = null;
    private final int historyDequeCapacity = 10;
    private ArrayDeque<Page> pageHistoryDeque; // Hold page navigation history
    private ArrayList<String> pageIDList;
    private Page nextPage;

    private PageNavUtil() {
        pageHistoryDeque = new ArrayDeque<Page>(historyDequeCapacity);
        pageIDList = PageIDList.pageIDList();
    }

    public static PageNavUtil getInstance() {
        if(pageNavUtil == null) { pageNavUtil = new PageNavUtil(); }
        return pageNavUtil;
    }

    public void mountPage(String pageID) {
        switch(pageID) {
            case PageIDList.landPageID:
                nextPage = LandPage.getInstance();
                break;
            case PageIDList.loginPageID:
                nextPage = LoginPage.getInstance();
                break;
            case PageIDList.registerPageID:
                nextPage = RegisterPage.getInstance();
                break;
            default:
                System.out.println("Page not found.");
        }
    }

    public void loadPage() {
        pushPageOntoHistoryDeque(nextPage);
        nextPage.loadPage();
    }

    public void goBack() {
        mountPage(pageHistoryDeque.pop().getPageID());
    }

    /**
     * The pushPageOntoHistoryDeque method updates the page history deque, and adheres to the deque capacity.
     * @param page - page to be pushed onto the deque.
     */
    private void pushPageOntoHistoryDeque(Page page) {
        if(pageHistoryDeque.size() >= historyDequeCapacity) { pageHistoryDeque.pollLast(); }
        pageHistoryDeque.push(page);
    }

}