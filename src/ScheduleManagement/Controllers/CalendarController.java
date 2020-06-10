package ScheduleManagement.Controllers;

import ScheduleManagement.Utils.Icons;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Random;

public class CalendarController extends SwitchableController
{
    @FXML private GridPane calendarMonthlyDays;

    @FXML
    public void initialize()
    {
        initializeSelectionPanePosition(0);
    }

    @Override
    public void lateInitialize()
    {
        super.lateInitialize();

        setupCalendarMonthly();
    }

    @Override
    public void initializeAnimations()
    {
        super.initializeAnimations();
    }

    private void setupCalendarMonthly()
    {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();
        int difference = dayOfWeek.getValue() % 7;
        LocalDate firstDayOfCalendarView = firstDayOfMonth.minusDays(difference);

        LocalDate dayCounter = firstDayOfCalendarView;
        Random random = new Random();
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                // int dayNum = 7 * i + j + 1;
                boolean isInMonth = dayCounter.getMonth().equals(now.getMonth());

                Node day = getCalendarMonthlyDay(isInMonth, dayCounter.getDayOfMonth(), "", random.nextInt(3), dayCounter.equals(now));
                calendarMonthlyDays.add(day, j, i);
                dayCounter = dayCounter.plusDays(1);
            }
        }
    }

    private Node getCalendarMonthlyDay(boolean isInMonth, int day, String specialEvent, int appointments, boolean isCurrentDay)
    {
        // TODO: Do different nodes for weekly view

        Label dayLabel = new Label(Integer.toString(day));
        dayLabel.setFont(new Font(24));
        String dayLabelStyle = String.format("-fx-text-fill: %s;", isInMonth ? "-black" : "-gray3");
        dayLabel.setStyle(dayLabelStyle);
        dayLabel.getStyleClass()
                .add("text-bold");

        Label specialEventLabel = new Label();
        specialEventLabel.setFont(new Font(10));
        String specialEventLabelStyle = String.format("-fx-text-fill: %s;", isInMonth ? "-black" : "-gray3");
        specialEventLabel.setStyle(specialEventLabelStyle);
        specialEventLabel.getStyleClass()
                         .add("text-bold");

        if (specialEvent.isEmpty())
        {
            specialEventLabel.setText("special event");
            specialEventLabel.setVisible(false);
        }
        else
            specialEventLabel.setText(specialEvent);

        // TODO: Foreach here for all the appointment types to show in the calendar day
        Label notifIcon = new Label(Icons.exclamationCircle);
        notifIcon.setFont(new Font(12));
        String notifIconStyle = String.format("-fx-text-fill: %s;", isInMonth ? "-red" : "-gray3");
        notifIcon.setStyle(notifIconStyle);
        notifIcon.getStyleClass()
                 .add("icon");

        Label notifLabel = new Label(Integer.toString(appointments));
        notifLabel.setFont(new Font(14));
        String notifLabelStyle = String.format("-fx-text-fill: %s;", isInMonth ? "-red" : "-gray3");
        notifLabel.setStyle(notifLabelStyle);
        notifLabel.getStyleClass()
                  .add("text-bold");

        HBox notif = new HBox(notifIcon, notifLabel);
        notif.setAlignment(Pos.CENTER);
        notif.setSpacing(10);

        HBox notifRoot = new HBox(notif);
        notifRoot.setAlignment(Pos.CENTER);
        HBox.setHgrow(notif, Priority.ALWAYS);

        if (appointments <= 0)
            notifRoot.setVisible(false);

        VBox root = new VBox(dayLabel, specialEventLabel, notifRoot);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        String rootStyle = String.format("-fx-background-color: %s; -fx-background-radius: 5px;", isInMonth ? "-white" : "-gray4");
        root.setStyle(rootStyle);
        root.getStyleClass()
            .add("drop-shadow");

        if (isCurrentDay)
            root.getStyleClass().add("green-border");

        VBox.setMargin(dayLabel, new Insets(0, 0, -2, 0));
        VBox.setMargin(specialEventLabel, new Insets(0, 0, 9, 0));

        return root;
    }
}
