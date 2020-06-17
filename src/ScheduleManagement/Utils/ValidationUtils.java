package ScheduleManagement.Utils;

import javafx.scene.control.TextInputControl;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ValidationUtils
{
    public enum PatternType
    {
        Date(Pattern.compile("^(0[1-9]|1[0-2])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$")),
        Time(Pattern.compile("^([1-9]|0[1-9]|1[0-2])[:]([0-5][0-9])[ ]([AP]M)$")),
        NotEmpty(Pattern.compile("^(?!\\s*$).+")),
        Optional(Pattern.compile("[\\s\\S]*"));

        private Pattern pattern;

        private PatternType(Pattern pattern)
        {
            this.pattern = pattern;
        }

        public Pattern getPattern()
        {
            return pattern;
        }
    }

    // Validation styles
    public static final String validStyle = "-fx-border-style: solid centered; -fx-border-width: 3px; -fx-border-radius: 0 100px 100px 0; -fx-border-color: -green;";
    public static final String invalidStyle = "-fx-border-style: solid centered; -fx-border-width: 3px; -fx-border-radius: 0 100px 100px 0; -fx-border-color: -red;";

    public static void addValidationListener(TextInputControl textField, PatternType patternType)
    {
        addValidationListener(textField, patternType.getPattern());
    }

    public static void addValidationListener(TextInputControl textField, String regex)
    {
        addValidationListener(textField, Pattern.compile(regex));
    }

    public static void addValidationListener(TextInputControl textField, Pattern pattern)
    {
        textField.textProperty()
                 .addListener(((observable, oldValue, newValue) ->
                 {
                     textField.setStyle(pattern.matcher(newValue)
                                               .matches() ? validStyle : invalidStyle);
                 }));
    }

    // TODO: This is kinda hackish
    public static boolean isTextFieldValid(TextInputControl textField)
    {
        return textField.getStyle().equals(validStyle);
    }
}
