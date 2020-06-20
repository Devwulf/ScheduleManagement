package ScheduleManagement.Controllers;

import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.*;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import ScheduleManagement.Utils.TimestampHelper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportsController extends SwitchableController
{
    @FXML private VBox appointmentTypesRoot;
    @FXML private Label presentationTypeCount;
    @FXML private Label scrumTypeCount;

    @FXML private Label previousMonthIcon;
    @FXML private Label nextMonthIcon;
    @FXML private Label currentDateText;

    @FXML private ComboBox<User> userCombo;
    @FXML private VBox userAppointmentList;

    @FXML private VBox customerCityList;

    private DBContext context;
    private LocalDate currentMonth = LocalDate.now();

    @FXML
    public void initialize()
    {
        context = DBContext.getInstance();

        previousMonthIcon.setText(Icons.chevronLeft);
        nextMonthIcon.setText(Icons.chevronRight);

        List<User> users = context.Users.readEntity();
        userCombo.getItems()
                 .addAll(users);

        setupAppointmentTypeCounts(currentMonth);
        setupCustomerCityList();
        initializeSelectionPanePosition(3);
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
        currentMonth = currentMonth.minusMonths(1);
        setupAppointmentTypeCounts(currentMonth);
    }

    @FXML
    private void handleNextDateButton()
    {
        currentMonth = currentMonth.plusMonths(1);
        setupAppointmentTypeCounts(currentMonth);
    }

    @FXML
    private void handleUserComboAction()
    {
        User user = userCombo.getValue();
        setupUserAppointmentList(user);
    }

    private void setupAppointmentTypeCounts(LocalDate month)
    {
        String currentMonth = month.getMonth()
                                   .toString();
        String currentMonthProperCase = currentMonth.substring(0, 1)
                                                    .toUpperCase() +
                currentMonth.substring(1)
                            .toLowerCase();
        currentDateText.setText(String.format("%s, %d", currentMonthProperCase, month.getYear()));

        List<Appointment> appointments = context.Appointments.readEntity();
        Map<String, Long> appointmentTypeCounts = appointments.stream()
                                                              // Keeps only the appointments in the same month
                                                              // and year of the given date
                                                              .filter(appointment ->
                                                              {
                                                                  LocalDate date = appointment.getStartTime()
                                                                                              .toLocalDateTime()
                                                                                              .toLocalDate();
                                                                  return date.getMonth()
                                                                             .equals(month.getMonth()) &&
                                                                          date.getYear() == month.getYear();
                                                              })
                                                              .map(Appointment::getType)
                                                              .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        presentationTypeCount.setText(Long.toString(appointmentTypeCounts.containsKey("Presentation") ? appointmentTypeCounts.get("Presentation") : 0));
        scrumTypeCount.setText(Long.toString(appointmentTypeCounts.containsKey("Scrum") ? appointmentTypeCounts.get("Scrum") : 0));
    }

    private void setupUserAppointmentList(User user)
    {
        userAppointmentList.getChildren()
                           .clear();

        if (user == null)
            return;

        List<Appointment> appointments = context.Appointments.readEntity();
        List<Appointment> userAppointments = appointments.stream()
                                                         // Keeps only the appointments of the given user
                                                         .filter(appointment -> appointment.getUserId() == user.getUserId())
                                                         // Sorts the appointments by their start time
                                                         .sorted((appt1, appt2) -> appt1.getStartTime()
                                                                                        .toLocalDateTime()
                                                                                        .compareTo(appt2.getStartTime()
                                                                                                        .toLocalDateTime()))
                                                         .collect(Collectors.toList());
        for (Appointment appointment : userAppointments)
            userAppointmentList.getChildren()
                               .add(getAppointmentNode(appointment));
    }

    private Node getAppointmentNode(Appointment appointment)
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
        String endTime = new SimpleDateFormat(" - h:mm aa, EEE, MMM. d, yyyy").format(appointment.getEndTime());
        appointmentTime.setText(startTime + endTime);

        VBox appointmentRoot = new VBox(appointmentName, appointmentTime);
        appointmentRoot.setAlignment(Pos.CENTER_LEFT);
        appointmentRoot.setPrefHeight(60);

        return appointmentRoot;
    }

    private void setupCustomerCityList()
    {
        customerCityList.getChildren()
                        .clear();

        List<City> cities = context.Cities.readEntity();
        List<Address> addresses = context.Addresses.readEntity();
        List<Customer> customers = context.Customers.readEntity();

        Map<City, Long> citiesCustomerCount = new HashMap<>();
        for (City city : cities)
        {
            long count = 0;
            List<Address> cityAddresses = addresses.stream()
                                                   // Keeps only the addresses in the current city
                                                   .filter(address -> address.getCityId() == city.getCityId())
                                                   .collect(Collectors.toList());
            for (Address address : cityAddresses)
            {
                // Multiple customers can be associated with one address,
                // even if in our database so far, each customer is associated
                // with their own address
                List<Customer> addressCustomers = customers.stream()
                                                           // Keeps only the customers with the current address
                                                           .filter(customer -> customer.getAddressId() == address.getAddressId())
                                                           .collect(Collectors.toList());
                count += addressCustomers.size();
            }

            citiesCustomerCount.put(city, count);
        }

        // Sorts by city name
        Map<City, Long> sortedCitiesCustomerCount = citiesCustomerCount.entrySet()
                                                                       .stream()
                                                                       // Sorts the cities by their city name
                                                                       .sorted((entry1, entry2) -> entry1.getKey()
                                                                                                         .getCity()
                                                                                                         .compareTo(entry2.getKey()
                                                                                                                          .getCity()))
                                                                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                                               Map.Entry::getValue,
                                                                               // Makes it so when there are collisions on values
                                                                               // associated with the same key, the old value is chosen
                                                                               (oldValue, newValue) -> oldValue,
                                                                               LinkedHashMap::new));
        for (Map.Entry<City, Long> entry : sortedCitiesCustomerCount.entrySet())
            customerCityList.getChildren()
                            .add(getCityNode(entry.getKey(), entry.getValue()));
    }

    private Node getCityNode(City city, long count)
    {
        Label cityName = new Label(city.getCity() + ":");
        cityName.setStyle("-fx-text-fill: -black; -fx-font-size: 18px;");
        cityName.getStyleClass()
                .add("text-semibold");

        Label customerCount = new Label(Long.toString(count));
        customerCount.setStyle("-fx-text-fill: -black; -fx-font-size: 18px;");
        customerCount.getStyleClass()
                     .add("text-semibold");

        HBox root = new HBox(cityName, customerCount);
        root.setSpacing(10);

        return root;
    }
}
