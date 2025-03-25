package com.project.webserver;

import com.project.webserver.model.User;
import com.project.webserver.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;

@SpringBootTest
class UserServiceTests {
	static User goodUser;
	static User badUser;
	static String thirdUsername = "user3!";
	static UserService userService;
//	private Assertions Assert;

	@Test
	void contextLoads() {
	}

	@BeforeAll
	public static void init() {
		System.out.println("startup reached");
		userService = new UserService();
		goodUser = generateUserProfile("goodUser", "goodPass", "123",
				"AAA1234", "testEmail@email.com");
		badUser = generateUserProfile("badUser", "badPass", "456",
				"BBB5678", "test@bad.com");
		System.out.println("startup completed");
	}

	@Test
	public void testAddUser() {
		//test1: add new user
		String resp = userService.addUserHandler("goodUser", "goodPass", "123",
				"AAA1234", "testEmail@email.com");
		Assertions.assertTrue(resp.contains("User added to Firestore successfully!"));
		//test2: add existing user
		resp = userService.addUserHandler("goodUser", "goodPass", "123",
				"AAA1234", "testEmail@email.com");
		Assertions.assertTrue(resp.contains("A User already exists with that username."));
		userService.addUserHandler("badUser", "badPass", "456",
				"BBB5678", "test@bad.com");
	}

	@Test
	public void testLogin() {
		//we need users first
		testAddUser();
		//test1 good pass
		String resp = userService.authenticateHandler(goodUser.getUsername(), goodUser.getPassword());
		Assertions.assertTrue(resp.equals("Login Successful"));
		resp = userService.authenticateHandler(goodUser.getUsername(), badUser.getPassword());
		Assertions.assertTrue(resp.equals("Login Failed"));
		resp = userService.authenticateHandler(thirdUsername, goodUser.getPassword());
		Assertions.assertTrue(resp.equals("Login Failed"));

	}

	@Test
	public void testChangePassword() {
		// we need users
		testAddUser();
		//change one of their passwords
		String oldPassword = goodUser.getPassword();
		String resp = userService.updatePasswordHandler(goodUser.getEmail(), badUser.getPassword());
		Assertions.assertTrue(resp.equals("Password updated successfully!"));
		//test login of new password
		resp = userService.authenticateHandler(goodUser.getUsername(), badUser.getPassword());
		Assertions.assertTrue(resp.equals("Login Successful"));
		//test login attempt with old password
		resp = userService.authenticateHandler(goodUser.getUsername(), oldPassword);
		Assertions.assertTrue(resp.equals("Login Failed"));
		//test no account found
		resp = userService.updatePasswordHandler("fake@email.com", "testpwd");
		Assertions.assertTrue(resp.equals("Email not found "));
		//test password to the same password -> is that allwoed??
		resp = userService.updatePasswordHandler(goodUser.getEmail(), goodUser.getPassword());
		Assertions.assertTrue(resp.equals("Password updated successfully!"));
	}

	public static User generateUserProfile(String userName, String password,
                                           String vin, String licensePlate, String email) {
		User resp = new User();
		resp.setUsername(userName);
		resp.setEmail(email);
		resp.setVin(vin);
		resp.setPassword(password);
		resp.setLicesePlate(licensePlate);
		return resp;
	}

	@AfterAll
	public static void tearDown() {
		userService.deleteUser(goodUser);
		userService.deleteUser(badUser);
	}


}
