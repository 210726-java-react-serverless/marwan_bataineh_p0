package com.revature.p0.util;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.LandPage;
import com.revature.p0.pages.LoginPage;
import com.revature.p0.pages.Page;

import java.util.ArrayDeque;
import java.util.HashSet;

/**
 * The PageDriver class provides methods for storing and transitioning between Pages.
 */
public class PageDriver {

    private final int historyDequeCapacity = 10;
    private ArrayDeque<Page> pageHistoryDeque; // Hold page navigation history
    private HashSet<Page> pageBufferedSet; // Store previously loaded pages here; do not load duplicates

    public PageDriver() {
        pageHistoryDeque = new ArrayDeque<Page>(historyDequeCapacity);
        pageBufferedSet = new HashSet<Page>();
    }

    public void launchPage(String pageID) {

        switch(pageID) {
            case PageIDList.landPageID:
                storePageInstanceIntoBuffer(LandPage.getInstance());
                break;
            case PageIDList.loginPageID:
                storePageInstanceIntoBuffer(LoginPage.getInstance());
                break;
            case PageIDList.registerPageID:
                break;
            default:
                System.out.println("Selection not recognized");
                return;
        }

    }

    private void storePageInstanceIntoBuffer(Page page) {

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