package io.neoOkpara.librado.ws.exceptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ErrorMessage {

	private Date timeStamp;
	private String message;

	public ErrorMessage(Date timeStamp, String message) {
		this.timeStamp = timeStamp;
		this.message = message;
	}

}
