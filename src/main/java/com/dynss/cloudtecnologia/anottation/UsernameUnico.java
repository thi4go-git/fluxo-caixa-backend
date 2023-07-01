package com.dynss.cloudtecnologia.anottation;


import com.dynss.cloudtecnologia.anottation.impl.UsernameUnicoImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameUnicoImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameUnico {
    String message() default "Já existe uma usuário com o USERNAME Informado!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
