package br.com.dbc.dbcarapi.validators;

import br.com.dbc.dbcarapi.config.PlacaCarro;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlacaCarroValidator implements ConstraintValidator<PlacaCarro, String> {

    private Pattern padrao = Pattern.compile("[A-Z]{3}[0-9][0-9A-Z][0-9]{2}");
    //^[a-zA-Z]{3}\-\d{4}$

    @Override
    public void initialize(PlacaCarro constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if(value == null || "".equals(value)){
            return true;
        }
        Matcher matcher = padrao.matcher(value);
        return matcher.matches();
    }
}
