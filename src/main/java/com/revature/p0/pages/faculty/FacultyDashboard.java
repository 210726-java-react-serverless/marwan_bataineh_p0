package com.revature.p0.pages.faculty;

import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.UserState;
import com.revature.p0.util.services.UserService;

import java.util.List;

public class FacultyDashboard extends Page {

    private static FacultyDashboard instance = null;

    private FacultyDashboard() {
        super(PageIDList.facultyDashboardID);
    }

    public static FacultyDashboard getInstance() {
        if(instance == null) instance = new FacultyDashboard();
        return instance;
    }

    UserService userService;
    PageNavUtil pageNavUtil;
    ConsoleReaderUtil consoleReaderUtil;

    @Override
    public void loadPage() {
        userService = new UserService();
        pageNavUtil = PageNavUtil.getInstance();
        consoleReaderUtil = ConsoleReaderUtil.getInstance();

        System.out.print("\nHello, " + UserState.getInstance().getUser().getFirstName() + "! What would you like to do?" +
                "\n1) view courses (drop / add / edit)" +
                "\n2) add course" +
                "\n3) logout" +
                "\n4) exit" +
                "\n> ");

        int selection = consoleReaderUtil.getIntOption();
        switch (selection) {
            case 1:
                viewCoursesMenu();
                return;
            case 2:
                addCourse();
                return;
            case 3:
                userService.logout();
                return;
            case 4:
                AppState.sendExitSignal();
                return;
            default:
                System.out.println("\nInvalid input.");
                return;
        }

//        pageNavUtil.mountPage(PageIDList.landPageID);
//        UserState.getInstance().logout();
    }

    private void viewCoursesMenu() {
        System.out.print("\n\n\n\n" +
                "What would you like to do?\n" +
                "1) view all courses\n" +
                "2) search for courses\n" +
                "3) add a course\n" +
                "4) go back\n" +
                "5) logout\n" +
                "6) exit\n" +
                "> ");

        int selection = consoleReaderUtil.getIntOption();

        System.out.println();

        switch(selection) {
            case 1: // ALL COURSES MANAGER --------------------------------------------------------------------------
                List<Course> courseList = userService.getAllCourses();
                for(int i = 0; i < courseList.size(); i++) {
                    System.out.println("[" + i + "]: " + courseList.get(i) + "\n");
                }

                // ~~~~~~~~~~~~~~~~~ POST-ALL PROMPT ~~~~~~~~~~~~~~~~~~~~~~ //
                System.out.print("Choose operation:\n" +
                        "1) remove course\n" +
                        "2) update course\n" +
                        "3) go back\n" +
                        "> ");

                int nextSelection = consoleReaderUtil.getIntOption();
                System.out.println();

                switch(nextSelection) { // ALL OPTIONS
                    case 1: // [ALL] REMOVE COURSE +++++++++++++++++++++++++++++++++++++++++++++
                        System.out.print("Select a course to remove [number]: ");
                        int dropCourse = consoleReaderUtil.getIntOption();

                        if(dropCourse < 0 || dropCourse >= courseList.size()) {
                            System.out.println("\nThat input is not valid/within range.");
                            return;
                        }

                        if(userService.removeCourse(new CourseHeader(courseList.get(dropCourse).getCourseId(),
                                courseList.get(dropCourse).getSection()))) {
                            System.out.println("\nCourse removed successfully!");
                        } else {
                            System.out.println("\nFailed to remove course.");
                        }

                        return;

                    case 2: // [ALL] UPDATE COURSE +++++++++++++++++++++++++++++++++++++++++++++++
                        System.out.print("Select a course to update [number]: ");
                        int updateCourse = consoleReaderUtil.getIntOption();

                        if(updateCourse < 0 || updateCourse >= courseList.size()) {
                            System.out.println("\nThat input is not valid/within range.");
                            return;
                        }

                        updateCoursePrompt(courseList.get(updateCourse));
                        return;
                    case 3: // [ALL] GO BACK +++++++++++++++++++++++++++++++++++++++++++++++++++++
                        return;

                    default: // [ALL] INVALID INPUT ++++++++++++++++++++++++++
                        System.out.println("\nInvalid input.");
                        return;

                }
            case 2: // SEARCH COURSES MANAGER --------------------------------------------------------------------
                System.out.print("Search for a course?\n" +
                        "1) search\n" +
                        "2) go back\n" +
                        "> ");

                int searchSelection = consoleReaderUtil.getIntOption();
                if(searchSelection == 1) { // SEARCH ~~~~~~~~~~~~~~~~~~~~

                    System.out.print("Enter field (i.e. 'cs'): ");
                    String field = consoleReaderUtil.getLine();

                    if (field == null) {
                        System.out.println("\nInvalid input entered.");
                        return;
                    }

                    if (!userService.isFieldValid(field)) {
                        System.out.println("\nEntered field not recognised!");
                        return;
                    }

                    //      LIST COURSES
                    List<Course> courseListByField = userService.getCoursesByField(field);
                    for (int i = 0; i < courseListByField.size(); i++) {
                        System.out.println("[" + i + "]: " + courseListByField.get(i) + "\n");
                    }

                    // ~~~~~~~~~~~~~~~~~~~~~~ POST-SEARCH PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

                    System.out.print("Choose operation:\n" +
                            "1) remove course\n" +
                            "2) update course\n" +
                            "3) go back\n" +
                            "> ");

                    int nextSelection2 = consoleReaderUtil.getIntOption();
                    System.out.println();

                    switch(nextSelection2) { // SEARCH OPTIONS
                        case 1: // [SEARCH] REMOVE COURSE +++++++++++++++++++++++++++++++++++++++++++++
                            System.out.print("Select a course to remove [number]: ");
                            int dropCourse = consoleReaderUtil.getIntOption();

                            if(dropCourse < 0 || dropCourse >= courseListByField.size()) {
                                System.out.println("\nThat input is not valid/within range.");
                                return;
                            }

                            if(userService.removeCourse(new CourseHeader(courseListByField.get(dropCourse).getCourseId(),
                                    courseListByField.get(dropCourse).getSection()))) {
                                System.out.println("\nCourse removed successfully!");
                            } else {
                                System.out.println("\nFailed to remove course.");
                            }

                            return;
                        case 2: // [SEARCH] UPDATE COURSE ++++++++++++++++++++++++++++++++++++++++++++
                            System.out.print("Select a course to update [number]: ");
                            int updateCourse = consoleReaderUtil.getIntOption();

                            if(updateCourse < 0 || updateCourse >= courseListByField.size()) {
                                System.out.println("\nThat input is not valid/within range.");
                                return;
                            }

                            updateCoursePrompt(courseListByField.get(updateCourse));
                            return;
                        case 3: // [SEARCH] GO BACK +++++++++++++++++++++++++++++++++++++++++++++++++++
                            return;

                        default: // [SEARCH] INVALID INPUT +++++++++++++++++++++++
                            System.out.println("\nInvalid input.");
                            return;

                    }
                }
            case 3: // ADD COURSE MANAGER -------------------------------------------------------------------------
                addCourse();
                return;
            case 4: // GO BACK-------------------------------------------------------------------------------------
                return;
            case 5:
                userService.logout();
                return;
            case 6: // EXIT ---------------------------------------------------------------------------------------
                AppState.sendExitSignal();
                return;
            default: // INVALID OPTION ----------------------------------------------------------------------------
                System.out.println("\nInput not recognized.");
                return;
        }
    }

    private void addCourse() {

        System.out.println("\nEnter Course Details below (all records must be filled)");

        String courseId = null;
        while(courseId == null || courseId.trim().equals("")) {
            System.out.print("courseId: ");
            courseId = consoleReaderUtil.getLine();
            if (courseId != null && !courseId.trim().equals("")) break;
            System.out.println("Invalid input!");
        }

        String courseName = null;
        while(courseName == null || courseName.trim().equals("")) {
            System.out.print("courseName: ");
            courseName = consoleReaderUtil.getLine();
            if (courseName != null && !courseName.trim().equals("")) break;
            System.out.println("Invalid input!");
        }

        int section = 0;
        while(section < 1) {
            System.out.print("section: ");
            section = consoleReaderUtil.getIntOption();
            if(section >= 1) break;
            System.out.println("Invalid input!");
        }

        String field = null;
        while(!userService.isFieldValid(field)) {
            System.out.print("field: ");
            field = consoleReaderUtil.getLine();
            if(userService.isFieldValid(field)) break;
            System.out.println("Invalid input!");
        }

        int level = -1;
        while(level < 1) {
            System.out.print("level: ");
            level = consoleReaderUtil.getIntOption();
            if(level >= 1) break;
            System.out.println("Invalid input!");
        }

        int capacity = -1;
        while(capacity < 1) {
            System.out.print("capacity: ");
            capacity = consoleReaderUtil.getIntOption();
            if(capacity >= 1) break;
            System.out.println("Invalid input!");
        }

        int space = -1;
        while(space < 0 || space > capacity) {
            System.out.print("space: ");
            space = consoleReaderUtil.getIntOption();
            if(space >= 0 && space <= capacity) break;
            System.out.println("Invalid input! (space must be > 0 but also < capacity)");
        }

        Course newCourse = new Course(courseId, courseName, section, field, level, space, capacity);

        int selection = -1;
        while(selection < 1 || selection > 2) {
            System.out.println(newCourse);
            System.out.print("\nConfirm add course\n" +
                    "1) confirm\n" +
                    "2) cancel\n" +
                    "> ");
            selection = consoleReaderUtil.getIntOption();
            if(selection < 1 || selection > 2) System.out.println("\nInvalid input.");
        }

        if(selection == 1) {
            userService.addCourse(newCourse);
        } // Fall through
        return;
    }

    private void updateCoursePrompt(Course course) {
        Course newCourse = new Course();
        System.out.println("\nUpdate Course Details (leave blank if you want it unchanged)");
//        System.out.print("courseId: ");
//        String courseId = consoleReaderUtil.getLine();
//        if(courseId != null && !courseId.trim().equals("")) newCourse.setCourseId(courseId);
//        else newCourse.setCourseId(course.getCourseId());

        newCourse.setCourseId(course.getCourseId());

//        System.out.println();

        System.out.print("courseName: ");
        String courseName = consoleReaderUtil.getLine();
        if(courseName != null && !courseName.trim().equals("")) newCourse.setCourseName(courseName);
        else newCourse.setCourseName(course.getCourseName());

        System.out.println();

//        System.out.print("section: ");
//        int section = consoleReaderUtil.getIntOption();
//        if(section > 0) newCourse.setSection(section);
//        else newCourse.setSection(course.getSection());

        newCourse.setSection(course.getSection());

//        System.out.println();

//        System.out.print("field: ");
//        String field = consoleReaderUtil.getLine();
//        if(field != null && !field.trim().equals("")) newCourse.setField(field);
//        else newCourse.setField(course.getField());

        newCourse.setField(course.getField());

//        System.out.println();

//        System.out.print("level: ");
//        int level = consoleReaderUtil.getIntOption();
//        if(level > 0) newCourse.setLevel(level);
//        else newCourse.setLevel(course.getLevel());

        newCourse.setLevel(course.getLevel());

//        System.out.println();

        System.out.print("capacity: ");
        int capacity = consoleReaderUtil.getIntOption();
        if(capacity > 0) newCourse.setCapacity(capacity);
        else newCourse.setCapacity(course.getCapacity());

        System.out.println();

        System.out.print("space: ");
        int space = consoleReaderUtil.getIntOption();
        if(space >= 0) {
            if(space > newCourse.getCapacity()) {
                System.out.println("\nSpace cannot exceed capacity!");
                return;
            }
            newCourse.setSpace(space);
        } else newCourse.setSpace(course.getSpace());

        System.out.println();

        int selection = -1;
        while(selection < 1 || selection > 2) {
            System.out.println(newCourse);
            System.out.print("\nConfirm update\n" +
                    "1) confirm\n" +
                    "2) cancel\n" +
                    "> ");
            selection = consoleReaderUtil.getIntOption();
            if(selection < 1 || selection > 2) System.out.println("\nInvalid input.");
        }

        if(selection == 1) {
            userService.updateCourse(newCourse);
        } // Fall through
        return;
    }
}
