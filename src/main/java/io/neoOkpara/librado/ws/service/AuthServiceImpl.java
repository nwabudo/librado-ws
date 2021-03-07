package io.neoOkpara.librado.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.neoOkpara.librado.security.UserPrincipal;
import io.neoOkpara.librado.ws.dtos.ApiResponse;
import io.neoOkpara.librado.ws.dtos.ErrorMessages;
import io.neoOkpara.librado.ws.dtos.LoginResponse;
import io.neoOkpara.librado.ws.dtos.PassRequest;
import io.neoOkpara.librado.ws.dtos.UserDTO;
import io.neoOkpara.librado.ws.entity.Gender;
import io.neoOkpara.librado.ws.entity.User;
import io.neoOkpara.librado.ws.exceptions.UserServiceException;
import io.neoOkpara.librado.ws.respository.UserRepository;
import io.neoOkpara.librado.ws.shared.JwtUtils;
import io.neoOkpara.librado.ws.shared.UtilsMethods;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	BCryptPasswordEncoder bCryptPassword;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public ApiResponse createNewUser(UserDTO userDTO) {
		try {
			User user = new User();
			BeanUtils.copyProperties(userDTO, user);
			//user = mapper.map(userDTO, User.class);
			
			Gender gender = (userDTO.getGender().toUpperCase().startsWith("M")) ? Gender.MALE : Gender.FEMALE;
			user.setGender(gender);

			int num = 15;
			String publicUserId = UtilsMethods.generateRandomId(num);
			while (userRepo.existsByUserId(publicUserId)) {
				num++;
				publicUserId = UtilsMethods.generateRandomId(num);
			}
			user.setUserId(publicUserId);
			user.setEncryptedPassword(bCryptPassword.encode(userDTO.getPassword()));

			User storedUserDetails = userRepo.save(user);
			if (storedUserDetails != null) {
				log.info("Saved User to Database");
				return new ApiResponse(200, "User Registration Completed Successfully!!");
			}
		} catch (ConstraintViolationException ex) {
			String errorLog = String.format("%s Causes by : %s \"/ %s",
					ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage(), ex.getMessage(),
					ex.getConstraintViolations());
			log.error(errorLog);
			throw new UserServiceException(errorLog);
		} catch (Exception ex) {
			String message = String.format("%s Causes by : %s", ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage(),
					ex.getMessage());
			log.error(message);
			throw new UserServiceException(message);
		}
		return new ApiResponse(400, "Error in Creating new User");
	}

	@Override
	public LoginResponse authenticate(PassRequest loginRequestDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String accessToken = jwtUtils.generateJwtToken(authentication);

			User val = getCurrentUser().orElseThrow(() -> new UsernameNotFoundException("No user Currently Logged In"));
			List<String> roles = authentication.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return new LoginResponse(accessToken, loginRequestDto.getEmail(), val.getName(), val.getUserId(), roles);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			String errorLogin = String.format("%s: %s", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage(),
					ex.getMessage());
			throw new UserServiceException(errorLogin);
		}
	}

	@Override
	public Optional<User> getCurrentUser() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return userPrincipal.getUser();
	}

}
