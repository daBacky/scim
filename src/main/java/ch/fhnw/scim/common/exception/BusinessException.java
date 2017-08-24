package ch.fhnw.scim.common.exception;

public class BusinessException extends GenericException {


	public BusinessException(int errCode) {
		super(errCode);
	}

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(int errCode, String msg) {
		super(errCode, msg);
	}

	public BusinessException(Throwable t) {
		super(t);
	}

	public BusinessException(int errCode, Throwable t) {
		super(errCode, t);
	}

	public BusinessException(String msg, Throwable t) {
		super(msg, t);
	}

	public BusinessException(int errCode, String msg, Throwable t) {
		super(errCode, msg, t);
	}
}