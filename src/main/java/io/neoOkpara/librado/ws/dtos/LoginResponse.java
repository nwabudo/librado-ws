package io.neoOkpara.librado.ws.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class LoginResponse {

	private String accessToken;
	private String email;
	private String firstName;
	private String userId;
	private List<String> authorities;

	public LoginResponse(String accessToken, String email, String firstName, String userId, List<String> authorities) {
		this.accessToken = accessToken;
		this.email = email;
		this.firstName = firstName;
		this.userId = userId;
		this.authorities = authorities;
	}

	public LoginResponse() {
		super();
	}
	
}
