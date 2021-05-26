package com.deepesh.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deepesh.configurations.JwtTokenUtil;
import com.deepesh.configurations.MyEncryptor;
import com.deepesh.models.User;
import com.deepesh.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserService() {
	}
	
	public ResponseEntity<Object> storeUser(User user){
		try {
			
			if(user.getEmail().isBlank() || user.getName().isBlank() || user.getPassword().isBlank()) 
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			
			User _user = userRepository.save(new User(user.getName(), user.getEmail(), user.getAddress(), user.getContact(), user.getPassword()));
			
			MyEncryptor enc = new MyEncryptor();
			_user.setPassword(enc.getEncryptor().encrypt(_user.getPassword()));
			
			return new ResponseEntity<>(_user, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<User> getUser(Long id) {
		try {
			if(id == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			
			Optional<User> user = userRepository.findById(id);
			
			if(user.isPresent()) return new ResponseEntity<>(user.get(), HttpStatus.OK);
			
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = new ArrayList<>();
			
			userRepository.findAll().forEach(users::add);;
			
			if(users.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<>(users, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<User> updateUser(User user) {
		try {
			
			Optional<User> _user = userRepository.findById(user.getId());
			
			if(!_user.get().getEmail().equals(user.getEmail())) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			
			_user.get().setName(user.getName());
			_user.get().setAddress(user.getAddress());
			_user.get().setContact(user.getContact());
			MyEncryptor enc = new MyEncryptor();
			_user.get().setPassword(enc.getEncryptor().encrypt(user.getPassword()));
			
			
			userRepository.save(_user.get());
			
			return new ResponseEntity<>(_user.get(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	public ResponseEntity<HttpStatus> deleteUser(Long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> loginUser(User user) {
		try {
			User _user = userRepository.findByEmail(user.getEmail());
			
			if(_user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			
			MyEncryptor enc = new MyEncryptor();
			
			if(user.getPassword().equals(enc.getEncryptor().decrypt(_user.getPassword()))) { 
				
				JwtTokenUtil tokenUtil = new JwtTokenUtil();
				String token = tokenUtil.generateToken(user.getEmail());
				
				String tokenString = "{\"token\":"+"\""+token+"\"," + _user.toString().substring(1, _user.toString().length()-1)+"}";
				
				return new ResponseEntity<>(tokenString, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public boolean verifyToken(String token) {
		try {
			String[] strSplit = token.split(" ");
			
			if(!strSplit[0].equals("Bearer") || strSplit.length != 2) return false;
			
			JwtTokenUtil tokenUtil = new JwtTokenUtil();
			
			String _email = tokenUtil.getUserIdFromToken(strSplit[1]);
			
			User user = userRepository.findByEmail(_email);
			
			if(user == null) return false;
			
			if(!_email.equals(user.getEmail())) return false;
			
			return true;
			
		} catch (NumberFormatException e) {
			System.out.println(e);
			return false;
		}
		
	}
}
