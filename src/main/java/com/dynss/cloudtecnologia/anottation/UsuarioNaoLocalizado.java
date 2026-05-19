package com.dynss.cloudtecnologia.anottation;

import com.dynss.cloudtecnologia.anottation.impl.UsuarioNaoLocalizadoImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UsuarioNaoLocalizadoImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsuarioNaoLocalizado {
    String message() default "(username): Usuário não localizado!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
