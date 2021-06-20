package com.gameserver.utils.account.rest.requests.discord.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

public class AccountDiscordConstraints {

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
    @Constraint(validatedBy = DiscordValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DiscordConstraint {
        String message() default "Unknown discord constraint issue";
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
