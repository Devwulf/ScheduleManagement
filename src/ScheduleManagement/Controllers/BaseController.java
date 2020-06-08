package ScheduleManagement.Controllers;

import javafx.stage.Stage;

public abstract class BaseController
{
    public Stage stage;

    // For accessing the stage during initialization because
    // it cannot be accessed on the normal initialize
    public void lateInitialize() { }

    // A separate helper method for initializing all the
    // needed animation keyframes
    public void initializeAnimations() { }

    // A separate helper method for initializing all the
    // text bindings needed for language localization
    public void initializeLanguage() { }
}
