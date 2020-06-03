package ScheduleManagement.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class LoginController extends BaseController
{
    @FXML private Label logoIcon;
    @FXML private Label usernameIcon;
    @FXML private Label passwordIcon;

    @FXML
    public void initialize()
    {
        // Cannot access stage from here, because this runs on
        // FXMLLoader.load, where stage is not set yet.

        logoIcon.setText("\uf274");
        usernameIcon.setText("\uf007");
        passwordIcon.setText("\uf084");
    }

    @Override
    public void lateInitialize()
    {
        Parent root = stage.getScene().getRoot();
    }
}
