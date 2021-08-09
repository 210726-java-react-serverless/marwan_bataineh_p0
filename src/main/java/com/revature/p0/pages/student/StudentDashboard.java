package com.revature.p0.pages.student;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;

public class StudentDashboard extends Page {

    private static StudentDashboard studentDashboardInstance = null;

    private StudentDashboard() { super(PageIDList.studentDashboardID); }

    public static StudentDashboard getInstance() {
        if(studentDashboardInstance == null) {
            studentDashboardInstance = new StudentDashboard();
        }
        return studentDashboardInstance;
    }

    @Override
    public void loadPage() {

    }
}
