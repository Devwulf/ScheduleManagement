package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.*;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Exceptions.IllegalFormInput;
import ScheduleManagement.Managers.LoginManager;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Icons;
import ScheduleManagement.Utils.TimestampHelper;
import ScheduleManagement.Utils.ValidationUtils;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AppointmentsController extends SwitchableController
{
    @FXML private VBox appointmentListRoot;

    @FXML private Button refreshAppointmentsButton;
    @FXML private Label addAppointmentIcon;

    /* Edit Modal */

    @FXML private AnchorPane editModalRoot;

    @FXML private Label closeEditModalButton;

    @FXML private TextField editTitleField;
    @FXML private TextField editDescriptionField;
    @FXML private TextField editStartDateField;
    @FXML private TextField editStartTimeField;
    @FXML private TextField editEndDateField;
    @FXML private TextField editEndTimeField;
    @FXML private TextField editUrlField;
    @FXML private TextField editLocationField;
    @FXML private TextField editContactField;

    @FXML private ComboBox<Customer> editCustomerCombo;
    @FXML private ComboBox<User> editUserCombo;
    @FXML private ComboBox<String> editTypeCombo;

    @FXML private Button editAppointmentSubmit;

    /* Add Modal */

    @FXML private AnchorPane addModalRoot;

    @FXML private Label closeAddModalButton;

    @FXML private TextField addTitleField;
    @FXML private TextField addDescriptionField;
    @FXML private TextField addStartDateField;
    @FXML private TextField addStartTimeField;
    @FXML private TextField addEndDateField;
    @FXML private TextField addEndTimeField;
    @FXML private TextField addUrlField;
    @FXML private TextField addLocationField;
    @FXML private TextField addContactField;

    @FXML private ComboBox<Customer> addCustomerCombo;
    @FXML private ComboBox<User> addUserCombo;
    @FXML private ComboBox<String> addTypeCombo;

    private DBContext context;

    private Map<Integer, Map<String, Animator>> appointmentsAnimations = new HashMap<>();

    @FXML
    public void initialize()
    {
        context = DBContext.getInstance();

        refreshAppointmentsButton.setText(Icons.redo);
        addAppointmentIcon.setText(Icons.plus);
        closeEditModalButton.setText(Icons.times);
        closeAddModalButton.setText(Icons.times);

        ValidationUtils.addValidationListener(editTitleField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(editDescriptionField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(editUrlField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(editLocationField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(editContactField, ValidationUtils.PatternType.NotEmpty);

        ValidationUtils.addValidationListener(editStartDateField, ValidationUtils.PatternType.Date);
        ValidationUtils.addValidationListener(editStartTimeField, ValidationUtils.PatternType.Time);
        ValidationUtils.addValidationListener(editStartTimeField, ValidationUtils.businessHoursValidator);

        ValidationUtils.addValidationListener(editEndDateField, ValidationUtils.PatternType.Date);
        ValidationUtils.addValidationListener(editEndTimeField, ValidationUtils.PatternType.Time);
        ValidationUtils.addValidationListener(editEndTimeField, ValidationUtils.businessHoursValidator);

        ValidationUtils.addValidationListener(addTitleField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(addDescriptionField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(addUrlField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(addLocationField, ValidationUtils.PatternType.NotEmpty);
        ValidationUtils.addValidationListener(addContactField, ValidationUtils.PatternType.NotEmpty);

        ValidationUtils.addValidationListener(addStartDateField, ValidationUtils.PatternType.Date);
        ValidationUtils.addValidationListener(addStartTimeField, ValidationUtils.PatternType.Time);
        ValidationUtils.addValidationListener(addStartTimeField, ValidationUtils.businessHoursValidator);

        ValidationUtils.addValidationListener(addEndDateField, ValidationUtils.PatternType.Date);
        ValidationUtils.addValidationListener(addEndTimeField, ValidationUtils.PatternType.Time);
        ValidationUtils.addValidationListener(addEndTimeField, ValidationUtils.businessHoursValidator);

        List<Customer> customers = context.Customers.readEntity();
        List<User> users = context.Users.readEntity();
        editCustomerCombo.getItems()
                         .addAll(customers);
        editUserCombo.getItems()
                     .addAll(users);
        editTypeCombo.getItems()
                     .addAll("Presentation", "Scrum");
        addCustomerCombo.getItems()
                        .addAll(customers);
        addUserCombo.getItems()
                    .addAll(users);
        addTypeCombo.getItems()
                    .addAll("Presentation", "Scrum");

        handleRefreshAppointmentList();
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

    @FXML
    private void handleOpenEditModal(Appointment appointment, Customer customer, User user)
    {
        editModalRoot.setVisible(true);

        editTitleField.setText(appointment.getTitle());
        editDescriptionField.setText(appointment.getDescription());

        Timestamp start = appointment.getStartTime();
        Timestamp end = appointment.getEndTime();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

        editStartDateField.setText(dateFormatter.format(start));
        editStartTimeField.setText(timeFormatter.format(start));
        editEndDateField.setText(dateFormatter.format(end));
        editEndTimeField.setText(timeFormatter.format(end));
        editUrlField.setText(appointment.getUrl());
        editLocationField.setText(appointment.getLocation());
        editContactField.setText(appointment.getContact());

        editCustomerCombo.setValue(customer);
        editUserCombo.setValue(user);
        editTypeCombo.setValue(appointment.getType());

        // Makes it so clicking this button will submit the edit appointment modal
        editAppointmentSubmit.setOnAction(event -> handleEditAppointmentSubmit(appointment));
    }

    @FXML
    private void handleCloseEditModal()
    {
        editModalRoot.setVisible(false);
    }

    private void handleEditAppointmentSubmit(Appointment appointment)
    {
        try
        {
            // Show error if appointment is outside business hours
            if ((ValidationUtils.PatternType.Time.getPattern().matcher(editStartTimeField.getText()).matches() &&
                !ValidationUtils.businessHoursValidator.test(editStartTimeField.getText())) ||
                (ValidationUtils.PatternType.Time.getPattern().matcher(editEndTimeField.getText()).matches() &&
                !ValidationUtils.businessHoursValidator.test(editEndTimeField.getText())))
            {
                ViewManager.getInstance().showErrorPopup("The appointment time given is outside business hours. (9 AM - 5 PM)");
                throw new IllegalFormInput("The appointment time given is outside business hours. (9 AM - 5 PM)");
            }
        }
        catch (IllegalFormInput ex)
        {
            ex.printStackTrace();
            return;
        }

        // Validate input
        if (!ValidationUtils.isTextFieldValid(editTitleField) ||
            !ValidationUtils.isTextFieldValid(editDescriptionField) ||
            !ValidationUtils.isTextFieldValid(editStartDateField) ||
            !ValidationUtils.isTextFieldValid(editStartTimeField) ||
            !ValidationUtils.isTextFieldValid(editEndDateField) ||
            !ValidationUtils.isTextFieldValid(editEndTimeField) ||
            !ValidationUtils.isTextFieldValid(editUrlField) ||
            !ValidationUtils.isTextFieldValid(editLocationField) ||
            !ValidationUtils.isTextFieldValid(editContactField) ||
            editCustomerCombo.getValue() == null ||
            editUserCombo.getValue() == null ||
            editTypeCombo.getValue() == null)
        {
            ViewManager.getInstance()
                       .showErrorPopup("One or more inputs are invalid!");
            return;
        }

        // Convert times to UTC first
        Timestamp startTime = TimestampHelper.convertToUTC(editStartDateField.getText() + " " + editStartTimeField.getText(), "MM/dd/yyyy h:mm a");
        Timestamp endTime = TimestampHelper.convertToUTC(editEndDateField.getText() + " " + editEndTimeField.getText(), "MM/dd/yyyy h:mm a");

        // Check if times overlap over a current appointment
        LocalDate day = startTime.toLocalDateTime()
                                 .toLocalDate();
        List<Appointment> appointments = context.Appointments.readEntity();

        List<Appointment> dayAppointments = appointments.stream()
                                                        // This filters all the appointments of the same day as the current appointment to be edited
                                                        .filter(appt -> appt.getStartTime()
                                                                                          .toLocalDateTime()
                                                                                          .toLocalDate()
                                                                                          .equals(day))
                                                        .collect(Collectors.toList());
        try
        {
            for (Appointment appt : dayAppointments)
            {
                Timestamp apptStart = appt.getStartTime();
                Timestamp apptEnd = appt.getEndTime();
                if (TimestampHelper.isTimeOverlapping(startTime, endTime, apptStart, apptEnd))
                {
                    ViewManager.getInstance()
                               .showErrorPopup("The time period for this appointment overlaps another appointment!");
                    throw new IllegalFormInput("The time period for this appointment overlaps another appointment!");
                }
            }
        }
        catch (IllegalFormInput ex)
        {
            ex.printStackTrace();
            return;
        }

        Timestamp now = TimestampHelper.nowUTC();
        User currentUser = LoginManager.getInstance()
                                       .getCurrentUser();
        boolean appointmentHasChanged = false;

        if (appointment.getCustomerId() != editCustomerCombo.getValue()
                                                            .getCustomerId() ||
                appointment.getUserId() != editUserCombo.getValue()
                                                        .getUserId() ||
                !appointment.getTitle()
                            .equals(editTitleField.getText()) ||
                !appointment.getDescription()
                            .equals(editDescriptionField.getText()) ||
                !appointment.getType()
                            .equals(editTypeCombo.getValue()) ||
                !appointment.getUrl()
                            .equals(editUrlField.getText()) ||
                !appointment.getLocation()
                            .equals(editLocationField.getText()) ||
                !appointment.getContact()
                            .equals(editContactField.getText()) ||
                !appointment.getStartTime()
                            .equals(startTime) ||
                !appointment.getEndTime()
                            .equals(endTime))
            appointmentHasChanged = true;

        appointment.setCustomerId(editCustomerCombo.getValue()
                                                   .getCustomerId());
        appointment.setUserId(editUserCombo.getValue()
                                           .getUserId());
        appointment.setTitle(editTitleField.getText());
        appointment.setDescription(editDescriptionField.getText());
        appointment.setType(editTypeCombo.getValue());
        appointment.setUrl(editUrlField.getText());
        appointment.setLocation(editLocationField.getText());
        appointment.setContact(editContactField.getText());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setDateCreated(now);
        appointment.setCreatedBy(currentUser.getUsername());
        appointment.setDateModified(now);
        appointment.setModifiedBy(currentUser.getUsername());

        if (appointmentHasChanged)
            context.Appointments.updateEntity(appointment);

        handleRefreshAppointmentList();
        handleCloseEditModal();
    }

    @FXML
    private void handleOpenAddModal()
    {
        // Reset all fields first
        addTitleField.setText("");
        addDescriptionField.setText("");
        addStartDateField.setText("");
        addStartTimeField.setText("");
        addEndDateField.setText("");
        addEndTimeField.setText("");
        addUrlField.setText("");
        addLocationField.setText("");
        addContactField.setText("");

        addCustomerCombo.setValue(null);
        addUserCombo.setValue(null);
        addTypeCombo.setValue(null);

        addModalRoot.setVisible(true);
    }

    @FXML
    private void handleCloseAddModal()
    {
        addModalRoot.setVisible(false);
    }

    @FXML
    private void handleAddAppointmentSubmit()
    {
        try
        {
            // Show error if appointment is outside business hours
            if ((ValidationUtils.PatternType.Time.getPattern().matcher(addStartTimeField.getText()).matches() &&
                !ValidationUtils.businessHoursValidator.test(addStartTimeField.getText())) ||
                (ValidationUtils.PatternType.Time.getPattern().matcher(addEndTimeField.getText()).matches() &&
                !ValidationUtils.businessHoursValidator.test(addEndTimeField.getText())))
            {
                ViewManager.getInstance().showErrorPopup("The appointment time given is outside business hours. (9 AM - 5 PM)");
                throw new IllegalFormInput("The appointment time given is outside business hours. (9 AM - 5 PM)");
            }
        }
        catch (IllegalFormInput ex)
        {
            ex.printStackTrace();
            return;
        }

        // Validate all input first
        if (!ValidationUtils.isTextFieldValid(addTitleField) ||
            !ValidationUtils.isTextFieldValid(addDescriptionField) ||
            !ValidationUtils.isTextFieldValid(addStartDateField) ||
            !ValidationUtils.isTextFieldValid(addStartTimeField) ||
            !ValidationUtils.isTextFieldValid(addEndDateField) ||
            !ValidationUtils.isTextFieldValid(addEndTimeField) ||
            !ValidationUtils.isTextFieldValid(addUrlField) ||
            !ValidationUtils.isTextFieldValid(addLocationField) ||
            !ValidationUtils.isTextFieldValid(addContactField) ||
            addCustomerCombo.getValue() == null ||
            addUserCombo.getValue() == null ||
            addTypeCombo.getValue() == null)
        {
            // TODO: Error message internationalization
            ViewManager.getInstance()
                       .showErrorPopup("One or more inputs are invalid!");
            return;
        }

        // Convert times to UTC first
        Timestamp startTime = TimestampHelper.convertToUTC(addStartDateField.getText() + " " + addStartTimeField.getText(), "MM/dd/yyyy h:mm a");
        Timestamp endTime = TimestampHelper.convertToUTC(addEndDateField.getText() + " " + addEndTimeField.getText(), "MM/dd/yyyy h:mm a");

        // Check if times overlap over a current appointment
        LocalDate day = startTime.toLocalDateTime()
                                 .toLocalDate();
        List<Appointment> appointments = context.Appointments.readEntity();

        List<Appointment> dayAppointments = appointments.stream()
                                                        // This filters all the appointments of the same day as the current appointment to be added
                                                        .filter(appointment -> appointment.getStartTime()
                                                                                          .toLocalDateTime()
                                                                                          .toLocalDate()
                                                                                          .equals(day))
                                                        .collect(Collectors.toList());
        try
        {
            for (Appointment appt : dayAppointments)
            {
                Timestamp apptStart = appt.getStartTime();
                Timestamp apptEnd = appt.getEndTime();
                if (TimestampHelper.isTimeOverlapping(startTime, endTime, apptStart, apptEnd))
                {
                    ViewManager.getInstance()
                               .showErrorPopup("The time period for this appointment overlaps another appointment!");
                    throw new IllegalFormInput("The time period for this appointment overlaps another appointment!");
                }
            }
        }
        catch (IllegalFormInput ex)
        {
            ex.printStackTrace();
            return;
        }

        Timestamp now = TimestampHelper.nowUTC();
        User currentUser = LoginManager.getInstance()
                                       .getCurrentUser();

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(0);
        appointment.setCustomerId(addCustomerCombo.getValue()
                                                  .getCustomerId());
        appointment.setUserId(addUserCombo.getValue()
                                          .getUserId());
        appointment.setTitle(addTitleField.getText());
        appointment.setDescription(addDescriptionField.getText());
        appointment.setType(addTypeCombo.getValue());
        appointment.setUrl(addUrlField.getText());
        appointment.setLocation(addLocationField.getText());
        appointment.setContact(addContactField.getText());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setDateCreated(now);
        appointment.setCreatedBy(currentUser.getUsername());
        appointment.setDateModified(now);
        appointment.setModifiedBy(currentUser.getUsername());

        context.Appointments.createEntity(appointment);

        handleRefreshAppointmentList();
        handleCloseAddModal();
    }

    @FXML
    private void handleRefreshAppointmentList()
    {
        List<Appointment> appointments = context.Appointments.readEntity();
        appointmentListRoot.getChildren()
                           .clear();
        for (Appointment appointment : appointments)
        {
            // Convert start and end times from UTC to local first
            Timestamp start = TimestampHelper.convertToLocal(appointment.getStartTime());
            Timestamp end = TimestampHelper.convertToLocal(appointment.getEndTime());
            appointment.setStartTime(start);
            appointment.setEndTime(end);

            appointmentListRoot.getChildren()
                               .add(getAppointmentNode(appointment));
        }
    }

    @FXML
    private void handleDeleteAppointment(Appointment appointment)
    {
        ViewManager.getInstance()
                   // This runs the given function, which deletes the given appointment from the database, when "Confirm" is clicked in the popup
                   .showConfirmPopup("Are you sure you want to delete the appointment '" + appointment.getTitle() + "'?", () ->
                   {
                       context.Appointments.deleteEntity(new NameValuePair("appointmentId", appointment.getAppointmentId()));
                       handleRefreshAppointmentList();
                   });
    }

    // Returns a procedurally generated UI appointment item
    // with the given appointment details
    private Node getAppointmentNode(Appointment appointment)
    {
        List<Customer> customers = context.Customers.readEntity(new NameValuePair("customerId", appointment.getCustomerId()));

        // If no customer is associated with this appointment, delete this appointment
        if (customers.size() <= 0)
        {
            context.Appointments.deleteEntity(new NameValuePair("appointmentId", appointment.getAppointmentId()));
            return null;
        }

        Customer customer = customers.get(0);

        List<User> users = context.Users.readEntity(new NameValuePair("userId", appointment.getUserId()));
        User appointmentUser = users.size() > 0 ? users.get(0) : null;

        Label appointmentIcon = new Label(Icons.calendarCheck);
        appointmentIcon.setPrefHeight(88);
        appointmentIcon.setAlignment(Pos.CENTER_LEFT);
        appointmentIcon.setStyle("-fx-text-fill: -black; -fx-font-size: 60px;");
        appointmentIcon.getStyleClass()
                       .add("icon");

        HBox appointmentIconRoot = new HBox(appointmentIcon);
        appointmentIconRoot.setAlignment(Pos.TOP_LEFT);
        appointmentIconRoot.setPadding(new Insets(6, 0, 0, 0));

        Label appointmentLabel = new Label("Appointment");
        appointmentLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        appointmentLabel.getStyleClass()
                        .add("text-regular");

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

        VBox appointmentRoot = new VBox(appointmentLabel, appointmentName, appointmentTime);
        appointmentRoot.setAlignment(Pos.CENTER_LEFT);
        appointmentRoot.setPrefHeight(68);
        //appointmentRoot.setStyle("-fx-background-color: -green;");

        Label customerNameLabel = new Label("Customer");
        customerNameLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        customerNameLabel.getStyleClass()
                         .add("text-regular");

        Label customerName = new Label(customer.getCustomerName());
        customerName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        customerName.getStyleClass()
                    .add("text-medium");

        VBox customerNameRoot = new VBox(customerNameLabel, customerName);
        customerNameRoot.setAlignment(Pos.CENTER_LEFT);
        customerNameRoot.setPrefHeight(68);
        //customerNameRoot.setStyle("-fx-background-color: -green;");

        Label userLabel = new Label("User");
        userLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        userLabel.getStyleClass()
                 .add("text-regular");

        Label userName = new Label(appointmentUser != null ? appointmentUser.getUsername() : "None");
        userName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        userName.getStyleClass()
                .add("text-medium");

        VBox userRoot = new VBox(userLabel, userName);
        userRoot.setAlignment(Pos.TOP_LEFT);
        userRoot.setPrefHeight(68);
        userRoot.managedProperty()
                .bind(userRoot.visibleProperty());
        userRoot.setVisible(false);
        //userRoot.setStyle("-fx-background-color: -green;");

        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        descriptionLabel.getStyleClass()
                        .add("text-regular");

        Label descriptionName = new Label(appointment.getDescription());
        descriptionName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        descriptionName.getStyleClass()
                       .add("text-medium");

        VBox descriptionRoot = new VBox(descriptionLabel, descriptionName);
        descriptionRoot.setAlignment(Pos.TOP_LEFT);
        descriptionRoot.setPrefHeight(68);
        descriptionRoot.managedProperty()
                       .bind(descriptionRoot.visibleProperty());
        descriptionRoot.setVisible(false);
        //descriptionRoot.setStyle("-fx-background-color: -green;");

        Label typeLabel = new Label("Type");
        typeLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        typeLabel.getStyleClass()
                 .add("text-regular");

        Label typeName = new Label(appointment.getType());
        typeName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        typeName.getStyleClass()
                .add("text-medium");

        VBox typeRoot = new VBox(typeLabel, typeName);
        typeRoot.setAlignment(Pos.TOP_LEFT);
        typeRoot.setPrefHeight(68);
        typeRoot.managedProperty()
                .bind(typeRoot.visibleProperty());
        typeRoot.setVisible(false);
        //typeRoot.setStyle("-fx-background-color: -green;");

        Label urlLabel = new Label("URL");
        urlLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        urlLabel.getStyleClass()
                .add("text-regular");

        Label urlName = new Label(appointment.getUrl());
        urlName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        urlName.getStyleClass()
               .add("text-medium");

        VBox urlRoot = new VBox(urlLabel, urlName);
        urlRoot.setAlignment(Pos.TOP_LEFT);
        urlRoot.managedProperty()
               .bind(urlRoot.visibleProperty());
        urlRoot.setVisible(false);
        //urlRoot.setStyle("-fx-background-color: -green;");

        Label locationLabel = new Label("Location");
        locationLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        locationLabel.getStyleClass()
                     .add("text-regular");

        Label locationName = new Label(appointment.getLocation());
        locationName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        locationName.getStyleClass()
                    .add("text-medium");

        VBox locationRoot = new VBox(locationLabel, locationName);
        locationRoot.setAlignment(Pos.TOP_LEFT);
        locationRoot.setPrefHeight(68);
        locationRoot.managedProperty()
                    .bind(locationRoot.visibleProperty());
        locationRoot.setVisible(false);
        //locationRoot.setStyle("-fx-background-color: -green;");

        Label contactLabel = new Label("Contact");
        contactLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        contactLabel.getStyleClass()
                    .add("text-regular");

        Label contactName = new Label(appointment.getContact());
        contactName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        contactName.getStyleClass()
                   .add("text-medium");

        VBox contactRoot = new VBox(contactLabel, contactName);
        contactRoot.setAlignment(Pos.TOP_LEFT);
        contactRoot.setPrefHeight(68);
        contactRoot.managedProperty()
                   .bind(contactRoot.visibleProperty());
        contactRoot.setVisible(false);
        //contactRoot.setStyle("-fx-background-color: -green;");

        FlowPane appointmentDetailsFlow = new FlowPane(appointmentRoot, customerNameRoot, userRoot, descriptionRoot, typeRoot, urlRoot, locationRoot, contactRoot);
        appointmentDetailsFlow.setAlignment(Pos.TOP_LEFT);
        appointmentDetailsFlow.setHgap(20);
        appointmentDetailsFlow.setPadding(new Insets(16, 0, 0, 0));
        //customerDetailsFlow.setStyle("-fx-background-color: -green;");

        HBox appointmentDetails = new HBox(appointmentIconRoot, appointmentDetailsFlow);
        appointmentDetails.setAlignment(Pos.TOP_LEFT);
        appointmentDetails.setSpacing(20);

        HBox.setHgrow(appointmentDetailsFlow, Priority.ALWAYS);

        Label editButtonIconLabel = new Label(Icons.pencil);
        editButtonIconLabel.setStyle("-fx-font-size: 14px;");
        editButtonIconLabel.getStyleClass()
                           .add("icon");

        HBox editButtonIcon = new HBox(editButtonIconLabel);
        editButtonIcon.setAlignment(Pos.CENTER_RIGHT);
        editButtonIcon.setPrefWidth(34);
        editButtonIcon.getStyleClass()
                      .addAll("button-sm", "button-left", "button-regular-alt");

        Label editButtonText = new Label("Edit");
        editButtonText.getStyleClass()
                      .add("text-bold");

        HBox editButtonLabel = new HBox(editButtonText);
        editButtonLabel.setAlignment(Pos.CENTER);
        editButtonLabel.setPadding(new Insets(0, 8, 0, 0));
        editButtonLabel.getStyleClass()
                       .addAll("button-sm", "button-right", "button-regular-alt");

        HBox editButton = new HBox(editButtonIcon, editButtonLabel);
        editButton.setAlignment(Pos.CENTER_RIGHT);
        editButton.setPrefWidth(110);
        editButton.setCursor(Cursor.HAND);
        editButton.getStyleClass()
                  .add("drop-shadow-small");

        HBox.setHgrow(editButtonLabel, Priority.ALWAYS);

        Label deleteButtonIconLabel = new Label(Icons.times);
        deleteButtonIconLabel.setStyle("-fx-font-size: 14px;");
        deleteButtonIconLabel.getStyleClass()
                             .add("icon");

        HBox deleteButtonIcon = new HBox(deleteButtonIconLabel);
        deleteButtonIcon.setAlignment(Pos.CENTER_RIGHT);
        deleteButtonIcon.setPrefWidth(34);
        deleteButtonIcon.getStyleClass()
                        .addAll("button-sm", "button-left", "button-danger");

        Label deleteButtonText = new Label("Delete");
        deleteButtonText.getStyleClass()
                        .add("text-bold");

        HBox deleteButtonLabel = new HBox(deleteButtonText);
        deleteButtonLabel.setAlignment(Pos.CENTER);
        deleteButtonLabel.setPadding(new Insets(0, 8, 0, 0));
        deleteButtonLabel.getStyleClass()
                         .addAll("button-sm", "button-right", "button-danger");

        HBox deleteButton = new HBox(deleteButtonIcon, deleteButtonLabel);
        deleteButton.setAlignment(Pos.CENTER_RIGHT);
        deleteButton.setPrefWidth(110);
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.getStyleClass()
                    .add("drop-shadow-small");

        HBox.setHgrow(deleteButtonLabel, Priority.ALWAYS);

        VBox appointmentControls = new VBox(editButton, deleteButton);
        appointmentControls.setAlignment(Pos.TOP_RIGHT);
        appointmentControls.setSpacing(8);
        appointmentControls.setPadding(new Insets(10, 0, 0, 0));

        HBox subRoot = new HBox(appointmentDetails, appointmentControls);

        HBox.setHgrow(appointmentDetails, Priority.ALWAYS);

        Label closeAppointmentLabel = new Label(Icons.chevronUp);
        closeAppointmentLabel.setStyle("-fx-font-size: 20px;");
        closeAppointmentLabel.getStyleClass()
                             .add("icon");

        HBox closeAppointmentItem = new HBox(closeAppointmentLabel);
        closeAppointmentItem.setAlignment(Pos.BOTTOM_CENTER);
        closeAppointmentItem.setPadding(new Insets(0, 0, 16, 0));
        closeAppointmentItem.setCursor(Cursor.HAND);
        closeAppointmentItem.managedProperty()
                            .bind(closeAppointmentItem.visibleProperty());
        closeAppointmentItem.setVisible(false);

        VBox root = new VBox(subRoot, closeAppointmentItem);
        root.setPrefHeight(100);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(0, 25, 0, 25));
        root.setCursor(Cursor.HAND);
        root.setPickOnBounds(false);
        root.getStyleClass()
            .addAll("customer", "drop-shadow");

        VBox.setVgrow(closeAppointmentItem, Priority.ALWAYS);

        // Makes it so clicking this button will open the edit modal
        editButton.setOnMouseClicked(event ->
        {
            handleOpenEditModal(appointment, customer, appointmentUser);
        });

        // Makes it so clicking this button will show the delete appointment popup
        deleteButton.setOnMouseClicked(event ->
        {
            handleDeleteAppointment(appointment);
        });

        // When the appointment list item is clicked, makes the list item unclickable
        // and animates the opening of the list item
        EventHandler<? super MouseEvent> rootOnClicked = event ->
        {
            root.setOnMouseClicked(null);
            root.setCursor(Cursor.DEFAULT);
            setupAppointmentAnimations(appointment.getCustomerId(), root);

            // Makes the chevron to close the appointment list item as visible
            // after the opening animation for this list item finishes
            openAppointmentDetailed(appointment.getCustomerId(), userRoot, descriptionRoot, typeRoot, urlRoot, locationRoot, contactRoot, () ->
            {
                closeAppointmentItem.setVisible(true);
            });
        };

        root.setOnMouseClicked(rootOnClicked);

        // When the chevron to close the appointment list item is clicked,
        // animates the closing of the list item
        closeAppointmentItem.setOnMouseClicked(event ->
        {
            setupAppointmentAnimations(appointment.getCustomerId(), root);

            // Makes the appointment list item to be clickable again
            // after the closing animation of the list item finishes
            closeAppointmentDetailed(appointment.getCustomerId(), userRoot, descriptionRoot, typeRoot, urlRoot, locationRoot, contactRoot, () ->
            {
                closeAppointmentItem.setVisible(false);
                root.setOnMouseClicked(rootOnClicked);
                root.setCursor(Cursor.HAND);

                cleanUpAnimations(appointment.getCustomerId());
            });
        });

        return root;
    }

    private void setupAppointmentAnimations(int appointmentId, VBox root)
    {
        boolean hasKey = appointmentsAnimations.containsKey(appointmentId);

        if (hasKey)
        {
            // Hashmap is already set up
            if (!appointmentsAnimations.get(appointmentId)
                                       .isEmpty())
                return;
        }

        Map<String, Animator> animators = hasKey ? appointmentsAnimations.get(appointmentId) : new HashMap<>();

        HBox subRoot = (HBox) root.getChildren()
                                  .get(0);
        HBox appointmentDetails = (HBox) subRoot.getChildren()
                                                .get(0);
        FlowPane appointmentDetailsFlow = (FlowPane) appointmentDetails.getChildren()
                                                                       .get(1);

        VBox appointmentName = (VBox) appointmentDetailsFlow.getChildren()
                                                            .get(0);
        VBox customerName = (VBox) appointmentDetailsFlow.getChildren()
                                                         .get(1);
        VBox userName = (VBox) appointmentDetailsFlow.getChildren()
                                                     .get(2);
        VBox description = (VBox) appointmentDetailsFlow.getChildren()
                                                        .get(3);
        VBox type = (VBox) appointmentDetailsFlow.getChildren()
                                                 .get(4);
        VBox url = (VBox) appointmentDetailsFlow.getChildren()
                                                .get(5);
        VBox location = (VBox) appointmentDetailsFlow.getChildren()
                                                     .get(6);
        VBox contact = (VBox) appointmentDetailsFlow.getChildren()
                                                    .get(7);

        double maxWidth = appointmentDetailsFlow.getWidth() - 10;
        double halfMaxWidth = (maxWidth - 21) / 2;

        userName.setPrefWidth(halfMaxWidth);
        description.setPrefWidth(maxWidth);

        type.setPrefWidth(halfMaxWidth);
        url.setPrefWidth(halfMaxWidth);
        location.setPrefWidth(halfMaxWidth);
        contact.setPrefWidth(halfMaxWidth);

        Animator rootAnimator = new Animator();
        rootAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(root.prefHeightProperty(), 100, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(root.prefHeightProperty(), 408, Interpolator.EASE_OUT))
        );
        animators.put("root", rootAnimator);

        Animator appointmentNameAnimator = new Animator();
        appointmentNameAnimator.addAnimation("width",
                new KeyFrame(Animator.Zero, new KeyValue(appointmentName.prefWidthProperty(), appointmentName.getWidth(), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(appointmentName.prefWidthProperty(), maxWidth, Interpolator.EASE_OUT))
        );
        animators.put("appointmentName", appointmentNameAnimator);

        Animator customerNameAnimator = new Animator();
        customerNameAnimator.addAnimation("width",
                new KeyFrame(Animator.Zero, new KeyValue(customerName.prefWidthProperty(), customerName.getWidth(), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(customerName.prefWidthProperty(), halfMaxWidth, Interpolator.EASE_OUT))
        );
        animators.put("customerName", customerNameAnimator);

        Animator userNameAnimator = new Animator();
        userNameAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(userName.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(userName.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("userName", userNameAnimator);

        Animator descriptionAnimator = new Animator();
        descriptionAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(description.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(description.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("description", descriptionAnimator);

        Animator typeAnimator = new Animator();
        typeAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(type.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(type.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("type", typeAnimator);

        Animator urlAnimator = new Animator();
        urlAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(url.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(url.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("url", urlAnimator);

        Animator locationAnimator = new Animator();
        locationAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(location.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(location.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("location", locationAnimator);

        Animator contactAnimator = new Animator();
        contactAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(contact.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(contact.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("contact", contactAnimator);

        if (!hasKey)
            appointmentsAnimations.put(appointmentId, animators);
    }

    private void cleanUpAnimations(int customerId)
    {
        Map<String, Animator> animators = new HashMap<>();
        if (!appointmentsAnimations.containsKey(customerId))
            return;

        animators = appointmentsAnimations.get(customerId);
        animators.clear();
    }

    private void openAppointmentDetailed(int appointmentId, VBox userNameRoot, VBox descriptionRoot, VBox typeRoot, VBox urlRoot, VBox locationRoot, VBox contactRoot, Runnable runAfter)
    {
        Map<String, Animator> animators = appointmentsAnimations.get(appointmentId);
        if (animators == null)
            throw new NoSuchElementException("Could not find the animations for the appointment of id: " + appointmentId);

        Animator rootAnimator = animators.get("root");
        Animator appointmentNameAnimator = animators.get("appointmentName");
        Animator customerNameAnimator = animators.get("customerName");
        Animator userNameAnimator = animators.get("userName");
        Animator descriptionAnimator = animators.get("description");
        Animator typeAnimator = animators.get("type");
        Animator urlAnimator = animators.get("url");
        Animator locationAnimator = animators.get("location");
        Animator contactAnimator = animators.get("contact");

        // When the animation list item finishes opening up,
        // plays all the animations of the children of the list item
        rootAnimator.play("height", event ->
        {
            userNameRoot.setOpacity(0);
            userNameRoot.setVisible(true);
            descriptionRoot.setOpacity(0);
            descriptionRoot.setVisible(true);
            typeRoot.setOpacity(0);
            typeRoot.setVisible(true);
            urlRoot.setOpacity(0);
            urlRoot.setVisible(true);
            locationRoot.setOpacity(0);
            locationRoot.setVisible(true);
            contactRoot.setOpacity(0);
            contactRoot.setVisible(true);
            userNameAnimator.play("opacity");
            descriptionAnimator.play("opacity");
            typeAnimator.play("opacity");
            urlAnimator.play("opacity");
            locationAnimator.play("opacity");
            contactAnimator.play("opacity");
            runAfter.run();
        });

        appointmentNameAnimator.play("width");
        customerNameAnimator.play("width");
    }

    private void closeAppointmentDetailed(int appointmentId, VBox userNameRoot, VBox descriptionRoot, VBox typeRoot, VBox urlRoot, VBox locationRoot, VBox contactRoot, Runnable runAfter)
    {
        Map<String, Animator> animators = appointmentsAnimations.get(appointmentId);
        if (animators == null)
            throw new NoSuchElementException("Could not find the animations for the appointment of id: " + appointmentId);

        Animator rootAnimator = animators.get("root");
        Animator appointmentNameAnimator = animators.get("appointmentName");
        Animator customerNameAnimator = animators.get("customerName");
        Animator userNameAnimator = animators.get("userName");
        Animator descriptionAnimator = animators.get("description");
        Animator typeAnimator = animators.get("type");
        Animator urlAnimator = animators.get("url");
        Animator locationAnimator = animators.get("location");
        Animator contactAnimator = animators.get("contact");

        // When the username finishes animating, makes the username invisible
        // and plays the closing animation of the appointment list item
        userNameAnimator.playReverse("opacity", event ->
        {
            userNameRoot.setVisible(false);
            rootAnimator.playReverse("height");
            runAfter.run();
        });

        // When the appointment description finishes playing, makes it invisible
        descriptionAnimator.playReverse("opacity", event ->
        {
            descriptionRoot.setVisible(false);
        });

        // When the appointment type finishes playing, makes it invisible
        typeAnimator.playReverse("opacity", event ->
        {
            typeRoot.setVisible(false);
        });

        // When the appointment url finishes playing, makes it invisible
        urlAnimator.playReverse("opacity", event ->
        {
            urlRoot.setVisible(false);
        });

        // When the appointment location finishes playing, makes it invisible
        locationAnimator.playReverse("opacity", event ->
        {
            locationRoot.setVisible(false);
        });

        // When the appointment contact finishes playing, makes it invisible
        contactAnimator.playReverse("opacity", event ->
        {
            contactRoot.setVisible(false);
        });

        customerNameAnimator.playReverse("width");
        appointmentNameAnimator.playReverse("width");
    }
}
