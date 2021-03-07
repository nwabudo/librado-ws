package io.neoOkpara.librado.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.neoOkpara.librado.ws.entity.User;
import io.neoOkpara.librado.ws.respository.UserRepository;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	private UserRepository userRepository;

	public UserPrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User userEntity = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No user Found with email: " + email));
		
		UserPrincipal user = new UserPrincipal(userEntity);
		return user;
	}

}
