package ScheduleManagement.Utils;

import javafx.scene.control.TextInputControl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.BooleanSupplier;
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

    // Business hours are from 9 AM to 5 PM
    // Creates a new validator that checks if the given value
    // matches the time pattern and is between 9 AM and 5 PM
    public static final Predicate<String> businessHoursValidator = newValue ->
    {
        if (!ValidationUtils.PatternType.Time.getPattern()
                                             .matcher(newValue)
                                             .matches())
            return false;
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("h:mm a")
                                                                    .toFormatter();
        LocalTime time = formatter.parse(newValue, LocalTime::from);
        // In working hours (between 9-5)
        return !time.isBefore(LocalTime.of(9, 0)) &&
                !time.isAfter(LocalTime.of(17, 0));
    };

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
                 // Makes it so when the text field changes, its new value is
                 // checked against the pattern and if it matches, the text field
                 // itself is given a green border
                 .addListener((observable, oldValue, newValue) ->
                 {
                     textField.setStyle(pattern.matcher(newValue)
                                               .matches() ? validStyle : invalidStyle);
                 });
    }

    public static void addValidationListener(TextInputControl textField, Predicate<String> predicate)
    {
        textField.textProperty()
                 // Makes it so when the text field changes, its new value is
                 // checked against the pattern and if it matches, the text field
                 // itself is given a green border
                 .addListener((observable, oldValue, newValue) ->
                 {
                     textField.setStyle(predicate.test(newValue) ? validStyle : invalidStyle);
                 });
    }

    // TODO: This is kinda hackish
    public static boolean isTextFieldValid(TextInputControl textField)
    {
        return textField.getStyle()
                        .equals(validStyle);
    }
}
