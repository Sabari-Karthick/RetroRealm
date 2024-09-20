package com.Batman.security.jwt;

import java.util.Base64;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.Batman.exceptions.JwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

	@Lazy
	private final UserDetailsService userDetailsService;

	@Value("${jwt.header}")
	private String authorizationHeader;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long validityInMilliseconds;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String usermail, String role) {
		log.info("Entering createToken ...");
		Claims claims = Jwts.claims().setSubject(usermail);
		claims.put("role", role);
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);

		String token = Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
		log.info("Leaving createToken ...");
		return token;
	}

	public boolean validateToken(String token) {
		try {
			log.info("Enter validateToken...");
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claimsJws.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException exception) {
			log.error("Token Validation Failed......" + exception.getMessage());
			throw new JwtAuthenticationException("INVALID_TOKEN", HttpStatus.UNAUTHORIZED);
		} finally {
			log.info("Leaving validateToken...");
		}
	}

	public Authentication getAuthentication(String token) {
		log.info("Entering getAuthentication...");
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsermail(token));
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, "", userDetails.getAuthorities());
		log.info("Leaving validateToken...");
		return usernamePasswordAuthenticationToken;
	}

	public String getUsermail(String token) {
		log.info("Entering JWT getUsermail...");
		String userMail = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		log.info("Leaving JWT getUsermail...");
		return userMail;
	}

	public String resolveToken(HttpServletRequest request) {
		log.info("Entering JWT resolveToken...");
		String bearerToken = request.getHeader(authorizationHeader);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			log.info("Token Found ...");
			return bearerToken.substring(7);
		}
		log.error("Token Not Found ...");
		log.info("Leaving JWT resolveToken...");
		return null;
	}
}
