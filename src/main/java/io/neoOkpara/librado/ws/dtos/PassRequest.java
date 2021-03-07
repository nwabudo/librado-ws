package io.neoOkpara.librado.ws.dtos;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PassRequest {
	
	@NotNull
	private String email;
	@NotNull
	private String password;
}
