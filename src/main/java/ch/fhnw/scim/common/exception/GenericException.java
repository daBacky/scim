package ch.fhnw.scim.common.exception;

public abstract class GenericException extends Exception {

	private int errCode = 0;

	public GenericException() {
		super();
	}

	public GenericException(int errCode) {
		super();
		this.errCode = errCode;
	}

	public GenericException(String msg) {
		super(msg);
	}

	public GenericException(int errCode, String msg) {
		super(msg);
		this.errCode = errCode;
	}

	public GenericException(Throwable t) {
		super(t);
	}

	public GenericException(int errCode, Throwable t) {
		super(t);
		this.errCode = errCode;
	}

	public GenericException(String msg, Throwable t) {
		super(msg, t);
	}

	public GenericException(int errCode, String msg, Throwable t) {
		super(msg, t);
		this.errCode = errCode;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}


}