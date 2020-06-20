package ScheduleManagement;

import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Holidays;
import ScheduleManagement.Utils.TimestampHelper;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

public class App extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Forces font antialiasing for all font sizes
        System.setProperty("prism.lcdtext", "false");

        Font.loadFont(getClass().getResource("Resources/Fonts/fa-solid-900.otf")
                                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("Resources/Fonts/Quicksand-Light.ttf")
                                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("Resources/Fonts/Quicksand-Regular.ttf")
                                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("Resources/Fonts/Quicksand-Medium.ttf")
                                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("Resources/Fonts/Quicksand-SemiBold.ttf")
                                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("Resources/Fonts/Quicksand-Bold.ttf")
                                .toExternalForm(), 12);

        URL mainViewPath = getClass().getResource("Views/LoginView.fxml");

        ViewManager viewManager = ViewManager.getInstance();
        viewManager.initialize(primaryStage, mainViewPath, "Scheduler", 1024, 768);

        // Add views
        viewManager.addView(ViewManager.ViewNames.Login, "LoginView");
        viewManager.addView(ViewManager.ViewNames.Calendar, "CalendarView");
        viewManager.addView(ViewManager.ViewNames.Customers, "CustomersView");
        viewManager.addView(ViewManager.ViewNames.Appointments, "AppointmentsView");
        viewManager.addView(ViewManager.ViewNames.Reports, "ReportsView");
        viewManager.addView(ViewManager.ViewNames.Settings, "SettingsView");
        viewManager.addView(ViewManager.ViewNames.Popup, "PopupView");
    }

    public static void main(String[] args)
    {
        System.out.println(LocalTime.now());
        //Locale.setDefault(Locale.FRENCH);
        launch(args);
    }
}
