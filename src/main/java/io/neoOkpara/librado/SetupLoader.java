package io.neoOkpara.librado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.neoOkpara.librado.ws.entity.Gender;
import io.neoOkpara.librado.ws.entity.User;
import io.neoOkpara.librado.ws.respository.UserRepository;

@Component
public class SetupLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;

		List<User> userList = List.of(

				User.builder().userId("NO123").name("Ifeoma Nwabudo")
						.encryptedPassword(passwordEncoder.encode("password123")).enabled(true).gender(Gender.FEMALE)
						.email("ifeoma@neoOkpara.io").build(),

				User.builder().userId("NO153").name("Emmanuel Nwabudo")
						.encryptedPassword(passwordEncoder.encode("password123")).enabled(true).gender(Gender.FEMALE)
						.email("emmanuel@neoOkpara.io").build());

		for (User emp : userList) {
			if (userRepository.existsByEmail(emp.getEmail()))
				continue;
			userRepository.save(emp);
		}

		alreadySetup = true;
	}

}
