package com.example.categoriesDemo.security;

import com.example.categoriesDemo.security.repositories.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return UserDetailsImpl.fromUser(
				userRepository.findByUsername(username)
							  .orElseThrow(() ->
									  new UsernameNotFoundException("No user with username" + username + " in " + "DB")));
	}


}
