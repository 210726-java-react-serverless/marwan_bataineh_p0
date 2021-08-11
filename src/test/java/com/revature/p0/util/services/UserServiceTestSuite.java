package com.revature.p0.util.services;

import com.revature.p0.dao.MongodCourseDAO;
import com.revature.p0.dao.MongodUserDAO;
import com.revature.p0.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTestSuite {

    UserService sut;

    private MongodUserDAO mockUserDao;
    private MongodCourseDAO mockCourseDao;

    @Before
    public void beforeEachTest() {
        mockUserDao = Mockito.mock(MongodUserDAO.class);
        mockCourseDao = Mockito.mock(MongodCourseDAO.class);
        sut = new UserService(mockUserDao, mockCourseDao);
    }

    @After
    public void afterEachTest() {
        sut = null;
    }

    @Test
    public void isUserValid_returnsTrue_givenValidUser() {

        User validUser = new User(0, "Name", "Name", "username@dname.domain", "username", "password");

        boolean actualResult = sut.isUserValid(validUser);

        Assert.assertTrue("Expected user to be considered valid!", actualResult);

    }

    @Test public void isUserValid_returnsFalse_givenUserWithNullOrEmptyNames() {

        User invalidUser1 = new User(0, null, "valid", "valid@email.com", "username", "password");
        User invalidUser2 = new User(0, "", "valid", "valid@email.com", "username", "password");
        User invalidUser3 = new User(0, "     ", "valid", "valid@email.com", "username", "password");
        User invalidUser4 = new User(0, "valid", null, "valid@email.com", "username", "password");
        User invalidUser5 = new User(0, "valid", "", "valid@email.com", "username", "password");
        User invalidUser6 = new User(0, "valid", "     ", "valid@email.com", "username", "password");

        boolean actualResult1 = sut.isUserValid(invalidUser1);
        boolean actualResult2 = sut.isUserValid(invalidUser2);
        boolean actualResult3 = sut.isUserValid(invalidUser3);
        boolean actualResult4 = sut.isUserValid(invalidUser4);
        boolean actualResult5 = sut.isUserValid(invalidUser5);
        boolean actualResult6 = sut.isUserValid(invalidUser6);

        Assert.assertFalse("User first name cannot be null!", actualResult1);
        Assert.assertFalse("User first name cannot be empty!", actualResult2);
        Assert.assertFalse("User first name cannot be only whitespace!", actualResult3);
        Assert.assertFalse("User last name cannot be null!", actualResult4);
        Assert.assertFalse("User last name cannot be empty!", actualResult5);
        Assert.assertFalse("User last name cannot be only whitespace!", actualResult6);

    }

    @Test
    public void isNameValid_returnsTrue_whenGivenValidName() {

        String validName = "Name";

        boolean actualResult = sut.isNameValid(validName);

        Assert.assertTrue(actualResult);

    }

    @Test
    public void isNameValid_returnsFalse_whenGivenInvalidName() {

        String invalidName1 = null;
        String invalidName2 = "";
        String invalidName3 = "   ";
        String invalidName4 = "invalid123";

        boolean actualResult1 = sut.isNameValid(invalidName1);
        boolean actualResult2 = sut.isNameValid(invalidName2);
        boolean actualResult3 = sut.isNameValid(invalidName3);
        boolean actualResult4 = sut.isNameValid(invalidName4);

        Assert.assertFalse("Names cannot be null!", actualResult1);
        Assert.assertFalse("Names cannot be empty!", actualResult2);
        Assert.assertFalse("Names cannot be whitespace", actualResult3);
        Assert.assertFalse("Names cannot contain numbers!", actualResult4);

    }

    @Test
    public void isEmailValid_returnsTrue_whenGivenValidEmail() {

        String validEmail = "valid@email.com";

        boolean actualResult = sut.isEmailValid(validEmail);

        Assert.assertTrue(actualResult);

    }

    @Test
    public void isEmailValid_returnsFalse_whenGivenInvalidEmail() {

        String invalidEmail1 = null;
        String invalidEmail2 = "";
        String invalidEmail3 = "   ";
        String invalidEmail4 = "a@email.com";
        String invalidEmail5 = "@email.com";
        String invalidEmail6 = "abcemail.com";
        String invalidEmail7 = "abc@.com";
        String invalidEmail8 = "abc@emailcom";
        String invalidEmail9 = "abc@email.";

        boolean actualResult1 = sut.isEmailValid(invalidEmail1);
        boolean actualResult2 = sut.isEmailValid(invalidEmail2);
        boolean actualResult3 = sut.isEmailValid(invalidEmail3);
        boolean actualResult4 = sut.isEmailValid(invalidEmail4);
        boolean actualResult5 = sut.isEmailValid(invalidEmail5);
        boolean actualResult6 = sut.isEmailValid(invalidEmail6);
        boolean actualResult7 = sut.isEmailValid(invalidEmail7);
        boolean actualResult8 = sut.isEmailValid(invalidEmail8);
        boolean actualResult9 = sut.isEmailValid(invalidEmail9);

        Assert.assertFalse("Email cannot be null!", actualResult1);
        Assert.assertFalse("Email cannot be empty!", actualResult2);
        Assert.assertFalse("Email cannot be whitespace!", actualResult3);
        Assert.assertFalse("Email username must be at least 3 characters!", actualResult4);
        Assert.assertFalse("Email must have username!", actualResult5);
        Assert.assertFalse("Email must have '@' symbol!", actualResult6);
        Assert.assertFalse("Email must have domain name!", actualResult7);
        Assert.assertFalse("Email must have '.' separator between domain name and domain!", actualResult8);
        Assert.assertFalse("Email must have domain!", actualResult9);

    }

    @Test
    public void isEmailTaken_returnsFalse_whenGivenOriginalEmail() {

        String originalEmail = "original@email.com";
        when(mockUserDao.readByEmail(originalEmail)).thenReturn(null);

        boolean actualResult = sut.isEmailTaken(originalEmail);

        Assert.assertFalse(actualResult);
        verify(mockUserDao).readByEmail(originalEmail);

    }

    @Test
    public void isEmailTaken_returnsTrue_whenGivenDuplicateEmail() {

        String duplicateEmail = "duplicate@email.com";
        when(mockUserDao.readByEmail(duplicateEmail)).thenReturn(any());

        boolean actualResult = sut.isEmailTaken(duplicateEmail);

        Assert.assertTrue(actualResult);
        verify(mockUserDao).readByEmail(duplicateEmail);

    }

    @Test
    public void isUsernameValid_returnsTrue_whenGivenValidUsername() {

        String validUsername = "username";

        boolean actualResult = sut.isUsernameValid(validUsername);

        Assert.assertTrue(actualResult);

    }

    @Test
    public void isUsernameValid_returnsFalse_whenGivenInvalidUsername() {

        String invalidUsername1 = null;
        String invalidUsername2 = "";
        String invalidUsername3 = "   ";
        String invalidUsername4 = "lsd423#@$";

        boolean actualResult1 = sut.isUsernameValid(invalidUsername1);
        boolean actualResult2 = sut.isUsernameValid(invalidUsername2);
        boolean actualResult3 = sut.isUsernameValid(invalidUsername3);
        boolean actualResult4 = sut.isUsernameValid(invalidUsername4);

        Assert.assertFalse("Username cannot be null!", actualResult1);
        Assert.assertFalse("Username cannot be empty!", actualResult2);
        Assert.assertFalse("Username cannot be whitespace!", actualResult3);
        Assert.assertFalse("Username cannot contain special characters!", actualResult4);

    }

    @Test
    public void isPasswordValid_returnsTrue_whenGivenValidPassword() {

        String validPassword = "validPassword";

        boolean actualResult = sut.isPasswordValid(validPassword);

        Assert.assertTrue(actualResult);

    }

    @Test
    public void isPasswordValid_returnsFalse_whenGivenInvalidPassword() {

        String invalidPassword1 = null;
        String invalidPassword2 = "";
        String invalidPassword3 = "   ";
        String invalidPassword4 = "invalid password";
        String invalidPassword5 = "short";

        boolean actualResult1 = sut.isPasswordValid(invalidPassword1);
        boolean actualResult2 = sut.isPasswordValid(invalidPassword2);
        boolean actualResult3 = sut.isPasswordValid(invalidPassword3);
        boolean actualResult4 = sut.isPasswordValid(invalidPassword4);
        boolean actualResult5 = sut.isPasswordValid(invalidPassword5);

        Assert.assertFalse(actualResult1);
        Assert.assertFalse(actualResult2);
        Assert.assertFalse(actualResult3);
        Assert.assertFalse(actualResult4);
        Assert.assertFalse(actualResult5);

    }

    @Test
    public void isUsernameTaken_returnsFalse_whenGivenOriginalUsername() {

        String originalUsername = "original";
        when(mockUserDao.readByUsername(originalUsername)).thenReturn(null);

        boolean actualResult = sut.isUsernameTaken(originalUsername);

        Assert.assertFalse(actualResult);
        verify(mockUserDao).readByUsername(originalUsername);
    }

    @Test
    public void isUsernameTaken_returnsTrue_whenGivenDuplicateUsername() {

        String duplicateUsername = "duplicate";
        when(mockUserDao.readByUsername(duplicateUsername)).thenReturn(any());

        boolean actualResult = sut.isUsernameTaken(duplicateUsername);

        Assert.assertTrue(actualResult);
        verify(mockUserDao).readByUsername(duplicateUsername);

    }

    @Test
    public void register_returnsSuccessfully_whenGivenValidUser() {

        User expectedResult = new User(0, "Valid", "Valid", "valid@email.com", "username", "password");
        User validUser = new User(0, "Valid", "Valid", "valid@email.com", "username", "password");
        when(mockUserDao.create(any())).thenReturn(expectedResult);

        User actualResult = sut.register(validUser);

        Assert.assertEquals(expectedResult, actualResult);
        verify(mockUserDao).create(any());

    }

    @Test
    public void register_returnsNull_whenGivenInvalidUser() {

        User invalidUser = new User(-1, null, "", "", "", "");
        User expectedResult = null;

        User actualResult = sut.register(invalidUser);

        Assert.assertNull("Expected result to be null!", actualResult);
        verify(mockUserDao, times(0)).create(any());

    }

    @Test
    public void register_returnsNull_whenGivenDuplicateUser() {

        User existingUser = new User(0, "name", "name", "valid@email.com", "duplicate", "original");
        User duplicateUser = new User(0, "name", "name", "valid2@email.com", "duplicate", "duplicate");
        when(mockUserDao.readByUsername(duplicateUser.getUsername())).thenReturn(existingUser);

        User actualResult = sut.register(duplicateUser);

        Assert.assertNull("Expected result to be null!", actualResult);
        verify(mockUserDao, times(1)).readByUsername(duplicateUser.getUsername());
        verify(mockUserDao, times(0)).create(duplicateUser);

    }

}
