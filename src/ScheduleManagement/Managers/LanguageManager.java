package ScheduleManagement.Managers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class LanguageManager
{
    private final ObjectProperty<Locale> currentLocale;

    public List<Locale> getSupportedLocales()
    {
        List<Locale> locales = new ArrayList<>();

        locales.add(Locale.ENGLISH);
        locales.add(Locale.FRENCH);

        return locales;
    }

    public Locale getLocale()
    {
        return currentLocale.get();
    }

    public ObjectProperty<Locale> localeProperty()
    {
        return currentLocale;
    }

    public void setLocale(Locale locale)
    {
        currentLocale.set(locale);
    }

    public String getTranslation(String key)
    {
        ResourceBundle rb = ResourceBundle.getBundle("appTexts", getLocale());
        return rb.getString(key);
    }

    public StringBinding createStringBinding(String key)
    {
        return Bindings.createStringBinding(() -> getTranslation(key), currentLocale);
    }

    private static LanguageManager instance = null;

    private LanguageManager()
    {
        Locale defaultLocale = Locale.getDefault();
        Locale locale = getSupportedLocales().contains(defaultLocale) ? defaultLocale : Locale.ENGLISH;

        currentLocale = new SimpleObjectProperty<>(locale);
        currentLocale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    public static LanguageManager getInstance()
    {
        if (instance == null)
            instance = new LanguageManager();

        return instance;
    }
}
