package ScheduleManagement.Controllers;

import ScheduleManagement.Managers.LanguageManager;
import ScheduleManagement.Managers.LoginManager;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.util.Locale;

// TODO: Implement this!
public class SettingsController extends SwitchableController
{
    @FXML private ComboBox<Locale> languageCombo;

    @FXML
    public void initialize()
    {
        languageCombo.valueProperty()
                     .bindBidirectional(LanguageManager.getInstance()
                                                       .localeProperty());
        languageCombo.getItems()
                     .addAll(LanguageManager.getInstance()
                                            .getSupportedLocales());
        languageCombo.setConverter(new StringConverter<Locale>()
        {
            @Override
            public String toString(Locale object)
            {
                return object.getDisplayLanguage(object);
            }

            @Override
            public Locale fromString(String string)
            {
                return languageCombo.getItems()
                                    .stream()
                                    // Keeps all the locales that has the same display language
                                    // as the given language name
                                    .filter(locale -> locale.getDisplayLanguage()
                                                            .equals(string))
                                    .findFirst()
                                    .orElse(null);
            }
        });

        initializeSelectionPanePosition(4);
    }

    @Override
    public void lateInitialize()
    {
        super.lateInitialize();
    }

    @Override
    public void initializeAnimations()
    {
        super.initializeAnimations();
    }

    @FXML
    private void handleLogoutAction()
    {
        if (LoginManager.getInstance().logout())
            ViewManager.getInstance().loadView(ViewManager.ViewNames.Login);
    }
}
