package ch.fhnw.scim.admin.exception;

public class GenericExceptionWrapper {

    private int errCode = 0;
    private String errMsg = "";

    public GenericExceptionWrapper() {
    }

    public GenericExceptionWrapper(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String toString() {
        return "test";
    }
}