package com.gameserver.utils.account.rest.requests.register.validation;

import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BirthdayValidator implements ConstraintValidator<AccountRegisterConstraints.BirthdayConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    private static final int MIN_AGE = 13;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_REGEX = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";

    @Autowired
    private CommonChecks commonChecks;

    @Override
    public void initialize(AccountRegisterConstraints.BirthdayConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String birthday, ConstraintValidatorContext context) {
        LOGGER.debug("Validating birthday: [{}]", birthday);

        String message;

        if (commonChecks.isMissing(birthday)) {
            message = "Birthday field is empty";
        } else if (!isValidFormat(birthday)) {
            message = "Invalid birthday format";
        } else if (!isValidAge(birthday)) {
            message = MessageFormat.format("Age must be above {0}", MIN_AGE);
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }

    private boolean isValidFormat(String birthday) {
        if (!birthday.matches(DATE_REGEX)) {
            return false;
        }
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT).parse(birthday);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isValidAge(String birthday) {
        LocalDate birthdayDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern(DATE_FORMAT));
        int age = Period.between(birthdayDate, LocalDate.now()).getYears();
        return age >= MIN_AGE;
    }
}
