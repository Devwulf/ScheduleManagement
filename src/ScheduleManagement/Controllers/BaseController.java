package ScheduleManagement.Controllers;

import javafx.stage.Stage;

public abstract class BaseController
{
    public Stage stage;

    // For accessing the stage during initialization because
    // it cannot be accessed on the normal initialize
    public void lateInitialize() { }
}
