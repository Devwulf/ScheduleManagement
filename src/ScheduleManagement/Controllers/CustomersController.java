package ScheduleManagement.Controllers;

import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomersController extends SwitchableController
{
    @FXML private Label logoIcon;
    @FXML private Label calendarIcon;
    @FXML private Label customersIcon;
    @FXML private Label appointmentsIcon;
    @FXML private Label reportsIcon;
    @FXML private Label settingsIcon;

    @FXML private Label logoText;

    @FXML
    public void initialize()
    {
        logoIcon.setText(Icons.calendarCheck);
        calendarIcon.setText(Icons.calendar);
        customersIcon.setText(Icons.users);
        appointmentsIcon.setText(Icons.calendarPlus);
        reportsIcon.setText(Icons.chartLine);
        settingsIcon.setText(Icons.cog);

        initializeSelectionPanePosition(1);
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
    public void handleCalendarSelect()
    {
        animateSelectionPane(0, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Calendar);
        });
    }

    @FXML
    public void handleAppointmentsSelect()
    {
        animateSelectionPane(2, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Appointments);
        });
    }

    @FXML
    public void handleReportsSelect()
    {
        animateSelectionPane(3, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Reports);
        });
    }

    @FXML
    public void handleSettingsSelect()
    {
        animateSelectionPane(4, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Settings);
        });
    }
}
