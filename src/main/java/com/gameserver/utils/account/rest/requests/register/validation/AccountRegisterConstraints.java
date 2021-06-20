package com.gameserver.utils.account.rest.requests.register.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

public class AccountRegisterConstraints {

    @Documented
    @Constraint(validatedBy = UsernameValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UsernameConstraint {
        String message() default "Unknown username constraint issue";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Constraint(validatedBy = EmailValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EmailConstraint {
        String message() default "Unknown email constraint issue";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Constraint(validatedBy = PasswordValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordConstraint {
        String message() default "Unknown password constraint issue";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Constraint(validatedBy = BirthdayValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BirthdayConstraint {
        String message() default "Unknown birthday constraint issue";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Constraint(validatedBy = KeyValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface KeyConstraint {
        String message() default "Unknown key constraint issue";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
