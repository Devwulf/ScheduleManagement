package ScheduleManagement.Controllers;

import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.Appointment;
import ScheduleManagement.Database.Models.User;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Managers.LoginManager;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Holiday;
import ScheduleManagement.Utils.Holidays;
import ScheduleManagement.Utils.Icons;
import ScheduleManagement.Utils.TimestampHelper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CalendarController extends SwitchableController
{
    @FXML private VBox monthWeekRoot;
    @FXML private TabPane monthWeekTabPane;

    @FXML private GridPane calendarMonthlyDays;
    @FXML private GridPane calendarWeeklyDays;

    @FXML private Label previousMonthIcon;
    @FXML private Label nextMonthIcon;

    @FXML private Label currentDateText;

    @FXML private StackPane selectedDayModal;
    @FXML private Label dayModalTitle;
    @FXML private Label dayModalSubtitle;
    @FXML private HBox appointmentNumRoot;
    @FXML private Label appointmentNumIcon;
    @FXML private Label appointmentNumLabel;
    @FXML private Label closeModalButton;
    @FXML private VBox appointmentListRoot;

    @FXML private ScrollPane backgroundScroll;
    @FXML private GridPane backgroundGrid;
    @FXML private ScrollPane timelineScroll;
    @FXML private GridPane timelineGrid;
    @FXML private ScrollPane mainScroll;

    @FXML private Button zoomInTimelineButton;
    @FXML private Button zoomOutTimelineButton;

    private LocalDate currentDate = LocalDate.now();
    private LocalDate currentDay;
    private double timelineScale = 2;
    private double timelineScrollValue;

    private DBContext context;

    private boolean isMonthly = true;
    private boolean isInitializing = true;

    @FXML
    public void initialize()
    {
        context = DBContext.getInstance();

        previousMonthIcon.setText(Icons.chevronLeft);
        nextMonthIcon.setText(Icons.chevronRight);
        closeModalButton.setText(Icons.times);
        appointmentNumIcon.setText(Icons.exclamationCircle);
        zoomInTimelineButton.setText(Icons.plus);
        zoomOutTimelineButton.setText(Icons.minus);

        monthWeekTabPane.tabMinWidthProperty()
                        .bind(monthWeekTabPane.widthProperty()
                                              .divide(2)
                                              .subtract(25));
        backgroundScroll.hvalueProperty()
                        .bind(mainScroll.hvalueProperty());
        timelineScroll.hvalueProperty()
                      .bind(mainScroll.hvalueProperty());

        setupCalendarMonthly(currentDate);
        initializeSelectionPanePosition(0);

        isInitializing = false;
    }

    // Check if there are appointments within 15 mins of login
    public void checkApptIn15Mins()
    {
        User user = LoginManager.getInstance().getCurrentUser();
        LocalTime nowTime = TimestampHelper.nowUTC()
                                           .toLocalDateTime()
                                           .toLocalTime();

        List<Appointment> userAppointments = context.Appointments.readEntity(new NameValuePair("userId", user.getUserId()));
        userAppointments = userAppointments.stream()
                                           // Makes sure that past appointments are filtered out
                                           .filter(appointment -> appointment.getStartTime()
                                                                             .after(TimestampHelper.nowUTC()))
                                           .collect(Collectors.toList());

        for (Appointment appointment : userAppointments)
        {
            // Appt start time is in UTC
            LocalTime apptStartTimeMinus15 = appointment.getStartTime()
                                                        .toLocalDateTime()
                                                        .toLocalTime()
                                                        .minusMinutes(15);

            if (nowTime.isAfter(apptStartTimeMinus15))
                ViewManager.getInstance()
                           .showWarningPopup("Your appointment '" + appointment.getTitle() + "' will start within 15 mins!");
        }
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
    private void handlePreviousDateButton()
    {
        if (isMonthly)
        {
            currentDate = currentDate.minusMonths(1);
            setupCalendarMonthly(currentDate);
        }
        else
        {
            currentDate = currentDate.minusWeeks(1);
            setupCalendarWeekly(currentDate);
        }
    }

    @FXML
    private void handleNextDateButton()
    {
        if (isMonthly)
        {
            currentDate = currentDate.plusMonths(1);
            setupCalendarMonthly(currentDate);
        }
        else
        {
            currentDate = currentDate.plusWeeks(1);
            setupCalendarWeekly(currentDate);
        }
    }

    @FXML
    private void handleSelectMonthButton()
    {

    }

    // For some reason, the selection of a tab runs
    // before the main initialize method. Probably a bug?
    @FXML
    private void handleMonthlyTab()
    {
        if (isInitializing)
            return;

        isMonthly = true;
        setupCalendarMonthly(currentDate);
    }

    @FXML
    private void handleWeeklyTab()
    {
        if (isInitializing)
            return;

        isMonthly = false;
        setupCalendarWeekly(currentDate);
    }

    private void handleOpenDayModal(LocalDate day, String specialEvent, int appointments)
    {
        // Fill in details about the day in the day modal
        dayModalTitle.setText(day.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dayModalSubtitle.setText(specialEvent);

        dayModalSubtitle.setVisible(!specialEvent.isEmpty());
        dayModalSubtitle.setManaged(!specialEvent.isEmpty());

        if (appointments > 0)
            appointmentNumLabel.setText(Integer.toString(appointments));
        else
        {
            appointmentNumRoot.setVisible(false);
            appointmentNumRoot.setManaged(false);
        }

        currentDay = day;
        setupAppointmentTimeline(currentDay);

        selectedDayModal.setVisible(true);
    }

    @FXML
    private void handleCloseDayModal()
    {
        selectedDayModal.setVisible(false);
    }

    @FXML
    private void handleZoomInTimeline()
    {
        double scrollX = mainScroll.getHvalue();

        zoomInTimelineButton.setDisable(true);
        zoomOutTimelineButton.setDisable(false);

        timelineScale = 4;
        setupAppointmentTimeline(currentDay);

        mainScroll.setHvalue(scrollX);
    }

    @FXML
    private void handleZoomOutTimeline()
    {
        double scrollX = mainScroll.getHvalue();

        zoomInTimelineButton.setDisable(false);
        zoomOutTimelineButton.setDisable(true);

        timelineScale = 2;
        setupAppointmentTimeline(currentDay);

        mainScroll.setHvalue(scrollX);
    }

    // Show all the appointments on the timeline
    private void setupAppointmentTimeline(LocalDate day)
    {
        backgroundGrid.setPrefWidth(1440 * timelineScale);
        timelineGrid.setPrefWidth(1440 * timelineScale);
        appointmentListRoot.setPrefWidth(1440 * timelineScale);

        appointmentListRoot.getChildren()
                           .clear();

        List<Appointment> appointments = context.Appointments.readEntity();
        // Converts the appointment start and end times from UTC to local time
        appointments.forEach(appointment ->
        {
            appointment.setStartTime(TimestampHelper.convertToLocal(appointment.getStartTime()));
            appointment.setEndTime(TimestampHelper.convertToLocal(appointment.getEndTime()));
        });

        List<Appointment> dayAppointments = appointments.stream()
                                                        // Filter to show only the appointments of the given day
                                                        .filter(appointment -> appointment.getStartTime()
                                                                                          .toLocalDateTime()
                                                                                          .toLocalDate()
                                                                                          .equals(day))
                                                        // Sorts by the appointment's start time
                                                        .sorted((appt1, appt2) -> appt1.getStartTime()
                                                                                       .compareTo(appt2.getStartTime()))
                                                        .collect(Collectors.toList());
        if (dayAppointments.size() <= 0)
            return;

        double anchorWidth = 1440 * timelineScale;
        for (Appointment appointment : dayAppointments)
        {
            LocalTime startTime = appointment.getStartTime()
                                             .toLocalDateTime()
                                             .toLocalTime();
            LocalTime endTime = appointment.getEndTime()
                                           .toLocalDateTime()
                                           .toLocalTime();
            double startWidth = ((double) startTime.getHour() + (startTime.getMinute() / 60.0)) * 60 * timelineScale;
            double endWidth = anchorWidth - (((double) endTime.getHour() + (endTime.getMinute() / 60.0)) * 60 * timelineScale);

            Pane apptHeader = new Pane();
            apptHeader.setPrefHeight(7);
            apptHeader.setStyle("-fx-background-color: -red; -fx-background-radius: 5px 5px 0 0;");

            Label apptName = new Label(appointment.getTitle());
            apptName.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
            apptName.getStyleClass()
                    .add("text-medium");

            Label apptTime = new Label();
            apptTime.setStyle("-fx-text-fill: -black; -fx-font-size: 10px;");
            apptTime.getStyleClass()
                    .add("text-regular");

            String apptStartTime = new SimpleDateFormat("h:mm aa").format(appointment.getStartTime());
            String apptEndTime = new SimpleDateFormat(" - h:mm aa, EEE, MMM. d, yyyy").format(appointment.getEndTime());
            apptTime.setText(apptStartTime + apptEndTime);

            VBox apptContent = new VBox(apptName, apptTime);
            apptContent.setAlignment(Pos.CENTER_LEFT);
            apptContent.setPadding(new Insets(0, 7, 0, 7));
            apptContent.setStyle("-fx-background-color: -white; -fx-background-radius: 0 0 5px 5px");

            VBox apptRoot = new VBox(apptHeader, apptContent);
            VBox.setVgrow(apptContent, Priority.ALWAYS);

            AnchorPane apptAnchor = new AnchorPane(apptRoot);
            apptAnchor.setPrefWidth(anchorWidth);
            apptAnchor.setPrefHeight(60);
            apptAnchor.getStyleClass()
                      .add("bottom-line");

            AnchorPane.setLeftAnchor(apptRoot, startWidth);
            AnchorPane.setRightAnchor(apptRoot, endWidth);
            AnchorPane.setTopAnchor(apptRoot, 0.0);
            AnchorPane.setBottomAnchor(apptRoot, 0.0);

            appointmentListRoot.getChildren()
                               .add(apptAnchor);
        }
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
        currentDateText.setText(String.format("%s, %d", currentMonthProperCase, month.getYear()));

        LocalDate dayCounter = firstDayOfCalendarView;

        Deque<Holiday> holidays = new ArrayDeque<>(Holidays.getHolidays(month.getYear(), month.getMonth()));

        List<Appointment> appointments = context.Appointments.readEntity();
        // Converts the appointment start and end times from UTC to local time
        appointments.forEach(appointment ->
        {
            appointment.setStartTime(TimestampHelper.convertToLocal(appointment.getStartTime()));
            appointment.setEndTime(TimestampHelper.convertToLocal(appointment.getEndTime()));
        });

        // Gets all the start times of appointments as LocalDate,
        // filters them to be in the current month, and
        // counts how many appointments are in a given day
        Map<LocalDate, Long> datesInMonth = appointments.stream()
                                                        // Selects only the start time field of the appointment in local date type
                                                        .map(appointment -> appointment.getStartTime()
                                                                                       .toLocalDateTime()
                                                                                       .toLocalDate())
                                                        // Filters and keeps dates in the same month and year as the given date
                                                        .filter(localDate -> localDate.getMonth()
                                                                                      .equals(month.getMonth()) && localDate.getYear() == month.getYear())
                                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                // int dayNum = 7 * i + j + 1;
                boolean isInMonth = dayCounter.getMonth()
                                              .equals(month.getMonth());
                String specialEvent = "";
                int numAppointments = 0;
                if (isInMonth)
                {
                    if (datesInMonth.containsKey(dayCounter))
                        numAppointments = datesInMonth.get(dayCounter)
                                                      .intValue();

                    // We expect the holiday list given to us to be ordered by default
                    Holiday holiday = holidays.peekFirst();
                    if (holiday != null && dayCounter.getDayOfMonth() == holiday.getDate()
                                                                                .getDayOfMonth())
                    {
                        specialEvent = holiday.getName();
                        holidays.pop();
                    }
                }

                Node day = getCalendarMonthlyDay(isInMonth, dayCounter, specialEvent, numAppointments, dayCounter.equals(now));
                calendarMonthlyDays.add(day, j, i);
                dayCounter = dayCounter.plusDays(1);
            }
        }
    }

    private void setupCalendarWeekly(LocalDate week)
    {
        // Clear all days in the current calendar
        calendarWeeklyDays.getChildren()
                          .clear();

        LocalDate now = LocalDate.now();

        DayOfWeek dayOfWeek = week.getDayOfWeek();
        int difference = dayOfWeek.getValue() % 7;
        LocalDate firstDayOfWeek = week.minusDays(difference);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("MMM. d, yyyy")
                                                                    .toFormatter();

        String firstDay = formatter.format(firstDayOfWeek);
        String lastDay = formatter.format(lastDayOfWeek);
        currentDateText.setText(firstDay + " - " + lastDay);

        /**/
        LocalDate dayCounter = firstDayOfWeek;

        Deque<Holiday> holidays = new ArrayDeque<>(Holidays.getHolidaysInWeek(firstDayOfWeek, lastDayOfWeek));

        List<Appointment> appointments = context.Appointments.readEntity();
        // Converts the appointment start and end times from UTC to local time
        appointments.forEach(appointment ->
        {
            appointment.setStartTime(TimestampHelper.convertToLocal(appointment.getStartTime()));
            appointment.setEndTime(TimestampHelper.convertToLocal(appointment.getEndTime()));
        });


        List<Appointment> weekAppointments = appointments.stream()
                                                         // Filters to keep only the appointments in between the first day of the week (inclusive)
                                                         // and the last day of the week (inclusive)
                                                         .filter(appointment -> TimestampHelper.isDateInBetween(appointment.getStartTime()
                                                                                                                           .toLocalDateTime()
                                                                                                                           .toLocalDate(), firstDayOfWeek, lastDayOfWeek))
                                                         .collect(Collectors.toList());
        for (int i = 0; i < 7; i++)
        {
            // int dayNum = 7 * i + j + 1;
            String specialEvent = "";

            LocalDate finalDayCounter = dayCounter;
            List<Appointment> dayAppointments = weekAppointments.stream()
                                                                // Filters to keep only the appointments on the current day
                                                                .filter(appointment -> appointment.getStartTime()
                                                                                                  .toLocalDateTime()
                                                                                                  .toLocalDate()
                                                                                                  .isEqual(finalDayCounter))
                                                                .collect(Collectors.toList());

            // We expect the holiday list given to us to be ordered by default
            Holiday holiday = holidays.peekFirst();
            if (holiday != null && dayCounter.getDayOfMonth() == holiday.getDate()
                                                                        .getDayOfMonth())
            {
                specialEvent = holiday.getName();
                holidays.pop();
            }

            Node day = getCalendarWeeklyDay(dayCounter, specialEvent, dayAppointments, dayCounter.equals(now));
            calendarWeeklyDays.add(day, i, 0);
            dayCounter = dayCounter.plusDays(1);
        }
        /**/
    }

    private Node getCalendarMonthlyDay(boolean isInMonth, LocalDate day, String specialEvent, int appointments, boolean isCurrentDay)
    {
        Label dayLabel = new Label(Integer.toString(day.getDayOfMonth()));
        dayLabel.setFont(new Font(24));
        String dayLabelStyle = String.format("-fx-text-fill: %s; -fx-font-size: 24px;", isInMonth ? "-black" : "-gray3");
        dayLabel.setStyle(dayLabelStyle);
        dayLabel.getStyleClass()
                .add("text-bold");

        Label specialEventLabel = new Label();
        specialEventLabel.setFont(new Font(10));
        String specialEventLabelStyle = String.format("-fx-text-fill: %s; -fx-font-size: 10px;", isInMonth ? "-black" : "-gray3");
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

        Label notifIcon = new Label(Icons.exclamationCircle);
        notifIcon.setFont(new Font(12));
        String notifIconStyle = String.format("-fx-text-fill: %s; -fx-font-size: 12px;", isInMonth ? "-red" : "-gray3");
        notifIcon.setStyle(notifIconStyle);
        notifIcon.getStyleClass()
                 .add("icon");

        Label notifLabel = new Label(Integer.toString(appointments));
        notifLabel.setFont(new Font(14));
        String notifLabelStyle = String.format("-fx-text-fill: %s; -fx-font-size: 14px;", isInMonth ? "-red" : "-gray3");
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
        root.setCursor(Cursor.HAND);

        String rootStyle = String.format("-fx-background-color: %s; -fx-background-radius: 5px;", isInMonth ? "-white" : "-gray4");
        root.setStyle(rootStyle);
        root.getStyleClass()
            .add("drop-shadow");

        if (isCurrentDay)
            root.getStyleClass()
                .add("green-border");

        // Makes it so when the day in the calendar is clicked,
        // opens the modal to show all the details and appointments for that day
        root.setOnMouseClicked(event ->
        {
            handleOpenDayModal(day, specialEvent, appointments);
        });

        VBox.setMargin(dayLabel, new Insets(0, 0, -2, 0));
        VBox.setMargin(specialEventLabel, new Insets(0, 0, 5, 0));

        return root;
    }

    private Node getCalendarWeeklyDay(LocalDate day, String specialEvent, List<Appointment> appointments, boolean isCurrentDay)
    {
        Label dayLabel = new Label(Integer.toString(day.getDayOfMonth()));
        dayLabel.setFont(new Font(24));
        dayLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 24px;");
        dayLabel.getStyleClass()
                .add("text-bold");

        Label specialEventLabel = new Label();
        specialEventLabel.setFont(new Font(10));
        specialEventLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        specialEventLabel.getStyleClass()
                         .add("text-bold");

        if (specialEvent.isEmpty())
        {
            specialEventLabel.setText("special event");
            specialEventLabel.setVisible(false);
        }
        else
            specialEventLabel.setText(specialEvent);

        Label notifIcon = new Label(Icons.exclamationCircle);
        notifIcon.setFont(new Font(12));
        notifIcon.setStyle("-fx-text-fill: -red; -fx-font-size: 12px;");
        notifIcon.getStyleClass()
                 .add("icon");

        Label notifLabel = new Label(Integer.toString(appointments.size()));
        notifLabel.setFont(new Font(14));
        notifLabel.setStyle("-fx-text-fill: -red; -fx-font-size: 14px;");
        notifLabel.getStyleClass()
                  .add("text-bold");

        HBox notifRoot = new HBox(notifIcon, notifLabel);
        notifRoot.setAlignment(Pos.CENTER);
        notifRoot.setSpacing(10);

        if (appointments.size() <= 0)
            notifRoot.setVisible(false);

        VBox appointmentsRoot = new VBox();

        for (Appointment appointment : appointments)
        {
            Label appointmentName = new Label(appointment.getTitle());
            appointmentName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
            appointmentName.getStyleClass()
                           .add("text-medium");

            Label appointmentTime = new Label();
            appointmentTime.setStyle("-fx-text-fill: -black; -fx-font-size: 12px;");
            appointmentTime.getStyleClass()
                           .add("text-regular");

            String startTime = new SimpleDateFormat("h:mm aa").format(appointment.getStartTime());
            String endTime = new SimpleDateFormat(" - h:mm aa").format(appointment.getEndTime());
            appointmentTime.setText(startTime + endTime);

            VBox appointmentRoot = new VBox(appointmentName, appointmentTime);
            appointmentRoot.setAlignment(Pos.CENTER_LEFT);
            appointmentRoot.setPrefHeight(68);

            appointmentsRoot.getChildren()
                            .add(appointmentRoot);
        }

        Button viewButton = new Button("View");
        viewButton.setPrefWidth(100);
        viewButton.setCursor(Cursor.HAND);
        viewButton.getStyleClass()
                  .addAll("button-sm", "button-regular-alt", "drop-shadow-small", "text-bold");
        // Makes it so when the view button on the day is clicked,
        // opens the modal that shows the details and appointments for this day
        viewButton.setOnAction(event ->
        {
            handleOpenDayModal(day, specialEvent, appointments.size());
        });

        VBox root = new VBox(dayLabel, specialEventLabel, notifRoot, appointmentsRoot, viewButton);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: -white; -fx-background-radius: 15px;");
        root.getStyleClass()
            .add("drop-shadow");

        if (isCurrentDay)
            root.getStyleClass()
                .add("green-border");

        VBox.setMargin(dayLabel, new Insets(0, 0, -2, 0));
        VBox.setMargin(specialEventLabel, new Insets(0, 0, 5, 0));
        VBox.setVgrow(appointmentsRoot, Priority.ALWAYS);

        return root;
    }
}
