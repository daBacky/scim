package ch.fhnw.scim.admin.exception;

import ch.fhnw.scim.common.exception.BusinessException;
import ch.fhnw.scim.common.exception.GenericException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class ServiceExceptionHandler {

    @ExceptionHandler(GenericException.class)
    @ResponseBody
    public GenericExceptionWrapper handleGenericException(BusinessException ex) {
        return new GenericExceptionWrapper(ex.getErrCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public GenericExceptionWrapper handleException(Exception ex) {
        return new GenericExceptionWrapper(999, "Unknown Error");
    }

}
