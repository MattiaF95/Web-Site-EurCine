package eurcine.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoControlCharsValidator implements ConstraintValidator<NoControlChars, String> {

    private boolean allowNewLines;

    @Override
    public void initialize(NoControlChars constraintAnnotation) {
        this.allowNewLines = constraintAnnotation.allowNewLines();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i += 1) {
            char ch = value.charAt(i);
            if (!Character.isISOControl(ch)) {
                continue;
            }

            if (allowNewLines && (ch == '\n' || ch == '\r' || ch == '\t')) {
                continue;
            }

            return false;
        }

        return true;
    }
}
