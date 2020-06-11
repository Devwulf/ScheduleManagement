package ScheduleManagement.Controllers;

import ScheduleManagement.Utils.Holiday;
import ScheduleManagement.Utils.Holidays;
import ScheduleManagement.Utils.Icons;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.*;
import java.util.stream.Collectors;

public class CalendarController extends SwitchableController
{
    @FXML private GridPane calendarMonthlyDays;

    @FXML private Label previousMonthIcon;
    @FXML private Label nextMonthIcon;

    @FXML private Label currentMonthText;

    @FXML private Label calendarViewIcon;

    private LocalDate currentMonth = LocalDate.now();

    @FXML
    public void initialize()
    {
        previousMonthIcon.setText(Icons.chevronLeft);
        nextMonthIcon.setText(Icons.chevronRight);
        calendarViewIcon.setText(Icons.chevronDown);

        initializeSelectionPanePosition(0);
    }

    @Override
    public void lateInitialize()
    {
        super.lateInitialize();

        setupCalendarMonthly(currentMonth);
    }

    @Override
    public void initializeAnimations()
    {
        super.initializeAnimations();
    }

    @FXML
    public void handlePreviousMonthButton()
    {
        currentMonth = currentMonth.minusMonths(1);
        setupCalendarMonthly(currentMonth);
    }

    @FXML
    public void handleNextMonthButton()
    {
        currentMonth = currentMonth.plusMonths(1);
        setupCalendarMonthly(currentMonth);
    }

    @FXML
    public void handleSelectMonthButton()
    {

    }

    @FXML
    public void handleCalendarViewDropdown()
    {

    }

    private void setupCalendarMonthly(LocalDate month)
    {
        // Clear all days in the current calendar
        calendarMonthlyDays.getChildren()
                           .clear();

        LocalDate now = LocalDate.now();

        LocalDate firstDayOfMonth = month.with(TemporalAdjusters.firstDayOfMonth());
        DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();
        int difference = dayOfWeek.getValue() % 7;
        LocalDate firstDayOfCalendarView = firstDayOfMonth.minusDays(difference);

        String currentMonth = month.getMonth()
                                   .toString();
        String currentMonthProperCase = currentMonth.substring(0, 1)
                                                    .toUpperCase() +
                currentMonth.substring(1)
                            .toLowerCase();
        currentMonthText.setText(String.format("%s, %d", currentMonthProperCase, month.getYear()));

        LocalDate dayCounter = firstDayOfCalendarView;

        Deque<Holiday> holidays = new ArrayDeque<>(Holidays.getHolidays(month.getYear(), month.getMonth()));

        // TODO: Temporary appointment number generator
        Random random = new Random();
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                // int dayNum = 7 * i + j + 1;
                boolean isInMonth = dayCounter.getMonth()
                                              .equals(month.getMonth());
                String specialEvent = "";
                if (isInMonth)
                {
                    // We expect the holiday list given to us to be ordered by default
                    Holiday holiday = holidays.peekFirst();
                    if (holiday != null && dayCounter.getDayOfMonth() == holiday.getDay())
                    {
                        specialEvent = holiday.getName();
                        holidays.pop();
                    }
                }

                Node day = getCalendarMonthlyDay(isInMonth, dayCounter.getDayOfMonth(), specialEvent, random.nextInt(3), dayCounter.equals(now));
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
        root.setPadding(new Insets(8));
        root.setAlignment(Pos.TOP_CENTER);

        String rootStyle = String.format("-fx-background-color: %s; -fx-background-radius: 5px;", isInMonth ? "-white" : "-gray4");
        root.setStyle(rootStyle);
        root.getStyleClass()
            .add("drop-shadow");

        if (isCurrentDay)
            root.getStyleClass()
                .add("green-border");

        VBox.setMargin(dayLabel, new Insets(0, 0, -2, 0));
        VBox.setMargin(specialEventLabel, new Insets(0, 0, 5, 0));

        return root;
    }
}
