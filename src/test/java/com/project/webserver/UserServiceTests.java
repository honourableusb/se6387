package com.project.webserver;

import com.project.webserver.controller.UserController;
import com.project.webserver.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static com.project.webserver.TestUtilities.*;


@SpringBootTest
//@Run( SpringJUnit4ClassRunner.class )
class UserServiceTests {
	static User goodUser;
	static User badUser;

//	@Autowired
	private static UserController controller;

	@Test
	void contextLoads() {
        Assertions.assertNotNull(controller);
	}

	@BeforeAll
	public static void init() {
		System.out.println("startup reached");
		goodUser = generateUserProfile("goodUser", "goodPass", "123",
				"AAA1234", "testEmail@email.com");
		badUser = generateUserProfile("badUser", "badPass", "456",
				"BBB5678", "test@bad.com");
		if (controller == null) {
			controller = new UserController();
		}
		System.out.println("startup completed");
	}

	@Test
	public void healthCheck() {
		ResponseEntity resp = controller.healthCheck();
		checkOK(resp);
	}

	@Test
	public void testAddUser() {
		//test1: add new user
		ResponseEntity resp = createUser(goodUser, controller);
		checkOK(resp);
		checkEntity(resp, userSuccess());
	}

	@Test
	public void testAddUserExisting() {
		//test2: add existing user
		ResponseEntity resp = createUser(goodUser, controller);
		checkOK(resp);
		checkEntity(resp, userExists());
	}

	@Test
	public void testLoginSuccess() {
		ResponseEntity resp = authenticateUser(goodUser);
		checkOK(resp);
		checkEntity(resp, loginSuccess());
	}

	@Test
	public void testLoginFail() {
		ResponseEntity resp = authenticateUser(goodUser, badUser.getPassword());
		checkNotOK(resp);
		checkEntity(resp, loginFailure());
	}

	@Test
	public void testLoginNoUserExists() {
		ResponseEntity resp = authenticateUser(badUser);
		checkNotOK(resp);
		checkEntity(resp, loginFailure());
	}

	@Test
	public void testPasswordChange(){
		//check if user exists
		ResponseEntity resp = getOrCreateUser(goodUser, controller);
		checkOK(resp);
		//get old password
		String oldPassword = goodUser.getPassword();
		resp = authenticateUser(goodUser, goodUser.getPassword());
		checkOK(resp);
		resp = changePassword(goodUser.getEmail(), badUser.getPassword());
		checkOK(resp);
		//check login with new password
		resp = authenticateUser(goodUser, badUser.getPassword());
		checkOK(resp);
		//check login with old password
		resp = authenticateUser(goodUser, oldPassword);
		checkNotOK(resp);
	}

	@Test
	public void testUserNotExists(){
		ResponseEntity resp = controller.getUser(badUser.getUsername());
		checkNotOK(resp);
		Assertions.assertEquals(404, resp.getStatusCode().value());
	}

	@AfterAll
	public static void tearDown() {
		controller.deleteUser(goodUser.getUsername());
		controller.deleteUser(badUser.getUsername());
	}

	private ResponseEntity authenticateUser(User user) {
		return controller.login(user.getUsername(), user.getPassword());
	}

	private ResponseEntity authenticateUser(User user, String pwd) {
		return controller.login(user.getUsername(), pwd);
	}

	private ResponseEntity changePassword(String userEmail, String newPwd) {
		return controller.forgetPassword(userEmail, newPwd);
	}

	private static String userSuccess() {
		return "User added to Firestore successfully!";
	}

	private static String userExists() {
		return "A User already exists with that username. If you wish to change the information for that user, please use another API.";
	}

	private static String loginSuccess() {
		return "Authentication Successful";
	}

	private static String loginFailure() {
		return "Authentication Failed";
	}

}
