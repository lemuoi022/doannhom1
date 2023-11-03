package com.example.danhom1.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintsValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator val = new PasswordValidator(Arrays.asList(new LengthRule(8, 24),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Special,1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule()));
        return val.validate(new PasswordData(password)).isValid();
    }
}
