package io.neoOkpara.librado.ws.service;

import java.util.Optional;

import io.neoOkpara.librado.ws.dtos.ApiResponse;
import io.neoOkpara.librado.ws.dtos.LoginResponse;
import io.neoOkpara.librado.ws.dtos.PassRequest;
import io.neoOkpara.librado.ws.dtos.UserDTO;
import io.neoOkpara.librado.ws.entity.User;

public interface AuthService {
	
	ApiResponse createNewUser(UserDTO userDTO);

	LoginResponse authenticate(PassRequest loginRequestDto);

	Optional<User> getCurrentUser();
}
