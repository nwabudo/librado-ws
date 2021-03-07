package io.neoOkpara.librado.ws.exceptions;

public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3550684939939471934L;

	public RecordNotFoundException(final String message) {
		super(message);
	}

	public RecordNotFoundException() {
		super();
	}
}
