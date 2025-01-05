package com.Batman.helper;

import static java.util.Objects.isNull;

import java.time.Duration;
import java.util.Optional;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.http.HttpCookie;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieHelper {

	private CookieHelper() {
	}

	private static final String COOKIE_DOMAIN = "localhost";
	private static final Boolean HTTP_ONLY = Boolean.TRUE;
	private static final Boolean SECURE = Boolean.FALSE;

	public static Optional<String> retrieve(MultiValueMap<String, HttpCookie> cookies, @NonNull String name) {
		if (isNull(cookies)) {
			return Optional.empty();
		}
		Optional<HttpCookie> cookie = Optional.ofNullable(cookies.getFirst(name));
		if (cookie.isPresent()) {
			return Optional.ofNullable(cookie.get().getValue());
		} else {
			return Optional.empty();
		}
	}

	public static String generateCookie(@NonNull String name, @NonNull String value, @NonNull Duration maxAge) {
		log.info("Entering Cookie Helper generateCookie ... ");
		// Build cookie instance
		Cookie cookie = new Cookie(name, value);
		if (!"localhost".equals(COOKIE_DOMAIN)) { // https://stackoverflow.com/a/1188145
			cookie.setDomain(COOKIE_DOMAIN);
		}
		cookie.setHttpOnly(HTTP_ONLY);
		cookie.setSecure(SECURE);
		cookie.setMaxAge((int) maxAge.toSeconds());
		cookie.setPath("/");
		// Generate cookie string
		Rfc6265CookieProcessor processor = new Rfc6265CookieProcessor();
		String header = processor.generateHeader(cookie, null);
		log.info("Leaving Cookie Helper generateCookie ... ");
		return header;
	}

	public static String generateExpiredCookie(@NonNull String name) {
		return generateCookie(name, "-", Duration.ZERO);
	}

}