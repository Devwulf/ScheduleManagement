package ScheduleManagement;

import ScheduleManagement.Managers.ViewManager;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

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

        /*
        VBox root = new VBox(10);
        Label icon = new Label("\uf303");
        icon.setStyle("-fx-font-family: \"Font Awesome 5 Free Solid\"; -fx-font-size: 48px;");

        Label fontLight = new Label("Light");
        fontLight.setStyle("-fx-font-family: \"Quicksand Light\"; -fx-font-size: 48px;");

        Label fontRegular = new Label("Regular");
        fontRegular.setStyle("-fx-font-family: \"Quicksand Regular\"; -fx-font-size: 48px;");

        Label fontMedium = new Label("Medium");
        fontMedium.setStyle("-fx-font-family: \"Quicksand Medium\"; -fx-font-size: 48px;");

        Label fontSemiBold = new Label("SemiBold");
        fontSemiBold.setStyle("-fx-font-family: \"Quicksand SemiBold\"; -fx-font-size: 48px;");

        Label fontBold = new Label("Bold");
        fontBold.setStyle("-fx-font-family: \"Quicksand Bold\"; -fx-font-size: 48px;");

        root.getChildren().add(icon);
        root.getChildren().add(fontLight);
        root.getChildren().add(fontRegular);
        root.getChildren().add(fontMedium);
        root.getChildren().add(fontSemiBold);
        root.getChildren().add(fontBold);
        /**/

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
        launch(args);
    }
}
