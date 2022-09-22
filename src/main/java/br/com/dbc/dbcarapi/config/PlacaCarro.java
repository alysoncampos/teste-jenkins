package br.com.dbc.dbcarapi.config;

import br.com.dbc.dbcarapi.validators.PlacaCarroValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PlacaCarroValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlacaCarro {

    String message() default "Formato de placa inválido";

    Class[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
