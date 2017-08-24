package ch.fhnw.scim.common.helper;

import ch.fhnw.scim.common.exception.BusinessException;
import ch.fhnw.scim.common.model.System;
import ch.fhnw.scim.common.model.User;
import org.springframework.util.StringUtils;

public class ValidateHelper {

    public static void validate(User user) throws BusinessException {
        if (user == null) {
            throw new BusinessException("User is empty");
        }

        if (StringUtils.isEmpty(user.getFirstName())) {
            throw new BusinessException("Name can not be empty");
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            throw new BusinessException("Email can not be empty");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException("Password can not be empty");
        }
    }

    public static void validate(System system) throws BusinessException {
        if (system == null) {
            throw new BusinessException("System is empty");
        }

        if (StringUtils.isEmpty(system.getName())) {
            throw new BusinessException("Name can not be empty");
        }
    }
}
