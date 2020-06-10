package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Managers.LanguageManager;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import ScheduleManagement.Utils.LanguageKeys;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class SwitchableController extends BaseController
{
    @FXML protected Label logoIcon;
    @FXML protected Label calendarIcon;
    @FXML protected Label customersIcon;
    @FXML protected Label appointmentsIcon;
    @FXML protected Label reportsIcon;
    @FXML protected Label settingsIcon;

    @FXML protected Label logoText;

    @FXML protected Pane selectionPane;

    private int currentSelection = 0;
    private Timeline timeline = null;

    @Override
    public void lateInitialize()
    {
        logoIcon.setText(Icons.calendarCheck);
        calendarIcon.setText(Icons.calendar);
        customersIcon.setText(Icons.users);
        appointmentsIcon.setText(Icons.calendarPlus);
        reportsIcon.setText(Icons.chartLine);
        settingsIcon.setText(Icons.cog);

        LanguageManager langManager = LanguageManager.getInstance();
        logoText.textProperty().bind(langManager.createStringBinding(LanguageKeys.logo));
    }

    // TODO: This is temporary, replace it with something more robust
    // selection is from 0 - 4, 0 is calendar, 4 is settings
    protected void animateSelectionPane(int selection, EventHandler<ActionEvent> value)
    {
        if (timeline != null)
            return;

        timeline = new Timeline(
                new KeyFrame(Animator.Zero, new KeyValue(selectionPane.layoutYProperty(), 62 * currentSelection + 9, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(selectionPane.layoutYProperty(), 62 * selection + 9, Interpolator.EASE_OUT))
        );

        timeline.setOnFinished(event -> {
            value.handle(event);
            timeline = null;
        });
        timeline.play();
    }

    protected void initializeSelectionPanePosition(int selection)
    {
        currentSelection = selection;

        selectionPane.setManaged(false);
        selectionPane.relocate(0, 62 * selection + 9);
    }

    @FXML
    public void handleCalendarSelect()
    {
        if (currentSelection == 0)
            return;

        animateSelectionPane(0, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Calendar);
        });
    }

    @FXML
    public void handleCustomersSelect()
    {
        if (currentSelection == 1)
            return;

        animateSelectionPane(1, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Customers);
        });
    }

    @FXML
    public void handleAppointmentsSelect()
    {
        if (currentSelection == 2)
            return;

        animateSelectionPane(2, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Appointments);
        });
    }

    @FXML
    public void handleReportsSelect()
    {
        if (currentSelection == 3)
            return;

        animateSelectionPane(3, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Reports);
        });
    }

    @FXML
    public void handleSettingsSelect()
    {
        if (currentSelection == 4)
            return;

        animateSelectionPane(4, event ->
        {
            ViewManager.getInstance()
                       .loadView(ViewManager.ViewNames.Settings);
        });
    }
}
