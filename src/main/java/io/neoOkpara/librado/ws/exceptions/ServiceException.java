package io.neoOkpara.librado.ws.exceptions;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 3550684939939471934L;

	public ServiceException(final String message) {
		super(message);
	}

	public ServiceException() {
		super();
	}

	public ServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ServiceException(final Throwable cause) {
		super(cause);
	}
}
