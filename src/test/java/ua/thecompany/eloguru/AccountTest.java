//package ua.ladno.summer;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ua.ladno.summer.model.Account;
//import ua.ladno.summer.model.Course;
//import ua.ladno.summer.model.EnumeratedRole;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AccountTest {
//    private Account account;
//
//    @BeforeEach
//    void setUp() {
//        account = new Account();
//    }
//
//    @Test
//    public void testGetAndSetFullname() {
//        account.setFullname("amigos code");
//        String expected = "amigos code";
//        String actual = account.getFullname();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetEmail() {
//        account.setEmail("superuser@root.com");
//        String expected = "superuser@root.ua";
//        String actual = account.getEmail();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetPwhash() {
//        account.setPwhash("somecrazypasswordhashhere");
//        String expected = "somecrazypasswordhashhere";
//        String actual = account.getPwhash();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetRole() {
//        account.setRole(EnumeratedRole.TEACHER);
//        EnumeratedRole expectedRole = EnumeratedRole.TEACHER;
//        EnumeratedRole actual = account.getRole();
//        assertEquals(expectedRole, actual);
//    }
//
//    @Test
//    public void testGetAndSetPhone() {
//        account.setPhone("+380123456789");
//        String expected = "+380123456789";
//        String actual = account.getPhone();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetCountry() {
//        account.setCountry("Ukraine");
//        String expected = "Ukraine";
//        String actual = account.getCountry();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetProfilePicture() {
//        account.setProfilePicture("link_to_cloudinary");
//        String expected = "link_to_cloudinary";
//        String actual = account.getProfilePicture();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetAndSetCourses() {
//        Course course = new Course();
//        account.getCourses().add(course);
//
//        assertNotNull(account.getCourses());
//        assertEquals(1, account.getCourses().size());
//        assertTrue(account.getCourses().contains(course));
//    }
//}
