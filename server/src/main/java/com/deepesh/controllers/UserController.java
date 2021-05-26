package com.deepesh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepesh.models.User;
import com.deepesh.services.UserService;

@CrossOrigin(origins="*")
@RequestMapping(value="/api")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService = new UserService();

	// =================== CRUD ======================
	@PostMapping("/user")
	public ResponseEntity<Object> storeUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
		if(!userService.verifyToken(token)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		return userService.storeUser(user);
	}
	
	@GetMapping("/user/all")
	public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String token){
		if(!userService.verifyToken(token)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		return userService.getAllUsers();
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		if(!userService.verifyToken(token)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		return userService.getUser(id);
	}
	
	@PutMapping("/user")
	public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
		if(!userService.verifyToken(token)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		if(user.getId() == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		return userService.updateUser(user);
	}
	
	@DeleteMapping("/user/delete/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		if(!userService.verifyToken(token)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		return userService.deleteUser(id);
	}
	
	
	// =========================== OTHER =======================
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		if(user.getEmail().isBlank() || user.getEmail().isBlank()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		return userService.loginUser(user);
	}
}
