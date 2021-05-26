package com.deepesh.configurations;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtTokenUtil implements Serializable{
	
	private static final long serialVersionUID = 1009247355228591971L;

	private int VALIDITY_TIME = 5 * 60 * 60;
	
	private String secret = "somesecretstringusedforencryption";
	
	public JwtTokenUtil() {
	}

	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public String generateToken(String str) {
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder().setClaims(claims).setSubject(str).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME*1000))
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}
	
}
