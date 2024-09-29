//package com.Batman.service;
//
//import java.util.Map;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.Batman.dto.User;
//import com.Batman.dto.UserPrincipal;
//import com.Batman.feignclinet.UserFeignClinet;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class UserService implements UserDetailsService {
//	
//	private final UserFeignClinet userFeignClinet;
//
//	@Override
//	public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
//		log.info("Entering loadUserByUsername...");
//		User user = userFeignClinet.getUserByMail(Map.of("userMail", userMail)).orElseThrow(() -> {
//			log.error("USER NOT FOUND...");
//			return new UsernameNotFoundException("USER_NOT_FOUND");
//		});
//		UserPrincipal userPrincipal = new UserPrincipal(user);
//		log.info("Leaving loadUserByUsername... ");
//		return userPrincipal;
//	}
//
//}
