package io.neoOkpara.librado.ws.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.neoOkpara.librado.ws.dtos.ApiResponse;
import io.neoOkpara.librado.ws.dtos.PassRequest;
import io.neoOkpara.librado.ws.dtos.UserDTO;
import io.neoOkpara.librado.ws.respository.UserRepository;
import io.neoOkpara.librado.ws.service.AuthService;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("api/v1/auth")
public class AuthController {

	private final UserRepository userRepo;

	private final AuthService authService;

	public AuthController(AuthService authService, UserRepository userRepo) {
		this.authService = authService;
		this.userRepo = userRepo;
	}
	
	@PostMapping("login")
	public ResponseEntity<?> sigInMethod(final HttpServletRequest request, @Valid @RequestBody PassRequest loginRequest){
		return new ResponseEntity<>(authService.authenticate(loginRequest), HttpStatus.OK);
	}

	@PostMapping("signup")
	public ResponseEntity<ApiResponse> signUpMethod(final HttpServletRequest request,
			 @Valid @RequestBody UserDTO userDTO) throws Exception {
		
		if (this.userRepo.existsByEmail(userDTO.getEmail())) {
			return new ResponseEntity<>(new ApiResponse(400, "Email already exists"), HttpStatus.BAD_REQUEST);
		}
		ApiResponse resp = authService.createNewUser(userDTO);
		log.info("New User Created");
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
