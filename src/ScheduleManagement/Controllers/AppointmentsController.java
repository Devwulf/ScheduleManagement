package ScheduleManagement.Controllers;

import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AppointmentsController extends SwitchableController
{
    @FXML
    public void initialize()
    {
        initializeSelectionPanePosition(2);
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
}
