package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.*;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Managers.LoginManager;
import ScheduleManagement.Utils.Icons;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CustomersController extends SwitchableController
{

    @FXML private VBox customerListRoot;

    private DBContext context;

    // Temporary solution
    // Stores all animations associated with a customer item,
    // so animations can also be played in reverse while keeping their values
    private Map<Integer, Map<String, Animator>> customersAnimations = new HashMap<>();

    @FXML
    public void initialize()
    {
        context = DBContext.getInstance();

        // TODO: Uncomment these afterwards
        // Since my database system cannot do joins, we'll have to grab
        // all the appointments associated to this user first, then grab
        // all the customers in the database (big problem), then grab the
        // subset of those customers with appointments with the current user.
        // This can be solved by either implementing joins or by filling in
        // the Customer field in the Appointment object
        /*
        User user = LoginManager.getInstance()
                                .getCurrentUser();
        List<Appointment> appointments = context.Appointments.readEntity(new NameValuePair("userId", user.getUserId()));
        List<Customer> allCustomers = context.Customers.readEntity();
        List<Customer> customers = allCustomers.stream()
                                               .filter(customer -> appointments.stream()
                                                                               .anyMatch(appointment -> customer.getCustomerId() == appointment.getCustomerId()))
                                               .collect(Collectors.toList());

         */

        List<Customer> customers = context.Customers.readEntity();
        for (Customer customer : customers)
            customerListRoot.getChildren()
                            .add(getCustomerNode(customer));

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

    // Returns a procedurally generated UI customer item
    // with the given customer details
    private Node getCustomerNode(Customer customer)
    {
        // Grab the appointment associated with this customer
        // However, a customer can have many appointments with different users
        List<Appointment> appointments = context.Appointments.readEntity(new NameValuePair("customerId", customer.getCustomerId()));

        // Maybe sort the appointments by upcoming dates?
        appointments.sort((a1, a2) ->
        {
            return a1.getStartTime()
                     .compareTo(a2.getStartTime());
        });

        Appointment upcomingAppointment = appointments.size() > 0 ? appointments.get(0) : null;

        // A customer should always have an address anyways (database doesn't allow for a NULL address id)
        Address customerAddress = context.Addresses.readEntity(new NameValuePair("addressId", customer.getAddressId()))
                                                   .get(0);
        City customerCity = context.Cities.readEntity(new NameValuePair("cityId", customerAddress.getCityId()))
                                          .get(0);
        Country customerCountry = context.Countries.readEntity(new NameValuePair("countryId", customerCity.getCountryId()))
                                                   .get(0);

        // TODO: Eventually support uploaded profile pics as the customer picture here
        Label customerIcon = new Label(Icons.userCircle);
        customerIcon.setPrefHeight(88);
        customerIcon.setAlignment(Pos.CENTER_LEFT);
        customerIcon.setStyle("-fx-text-fill: -black; -fx-font-size: 60px;");
        customerIcon.getStyleClass()
                    .add("icon");

        HBox customerIconRoot = new HBox(customerIcon);
        customerIconRoot.setAlignment(Pos.TOP_LEFT);
        customerIconRoot.setPadding(new Insets(6, 0, 0, 0));

        Label customerNameLabel = new Label("Name");
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

        String appLabel = String.format("Appointment %s", appointments.size() > 1 ? "(+" + (appointments.size() - 1) + ")" : "");
        Label appointmentLabel = new Label(appLabel);
        appointmentLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        appointmentLabel.getStyleClass()
                        .add("text-regular");

        Label appointmentName = new Label(upcomingAppointment != null ? upcomingAppointment.getTitle() : "No appointments");
        appointmentName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        appointmentName.getStyleClass()
                       .add("text-medium");

        // I hope customers don't have appointments that spans between
        // two different days over midnight, but just in case...
        // TODO: Show through some way if the appointment spans multiple days

        Label appointmentTime = new Label();
        appointmentTime.setStyle("-fx-text-fill: -black; -fx-font-size: 12px;");
        appointmentTime.getStyleClass()
                       .add("text-regular");

        if (upcomingAppointment != null)
        {
            String startTime = new SimpleDateFormat("h:mm aa").format(upcomingAppointment.getStartTime());
            String endTime = new SimpleDateFormat(" - h:mm aa, EEE, MMM. d, yyyy").format(upcomingAppointment.getEndTime());
            appointmentTime.setText(startTime + endTime);
        }
        else
        {
            appointmentTime.setVisible(false);
            appointmentTime.setManaged(false);
        }

        VBox appointmentRoot = new VBox(appointmentLabel, appointmentName, appointmentTime);
        appointmentRoot.setAlignment(Pos.CENTER_LEFT);
        appointmentRoot.setPrefHeight(68);

        Label addressLabel = new Label("Address");
        addressLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        addressLabel.getStyleClass()
                    .add("text-regular");

        Label addressName = new Label(customerAddress.getAddress() +
                (customerAddress.getAddress2()
                                .isEmpty() ? "" : ", " + customerAddress.getAddress2()));
        addressName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        addressName.getStyleClass()
                   .add("text-medium");

        Label addressCityName = new Label(customerCity.getCity() + ", " + customerCountry.getCountry() + " " + customerAddress.getPostalCode());
        addressCityName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        addressCityName.getStyleClass()
                       .add("text-medium");

        VBox addressRoot = new VBox(addressLabel, addressName, addressCityName);
        addressRoot.setAlignment(Pos.TOP_LEFT);
        addressRoot.setPrefHeight(68);
        addressRoot.managedProperty()
                   .bind(addressRoot.visibleProperty());
        addressRoot.setVisible(false);

        Label phoneLabel = new Label("Phone");
        phoneLabel.setStyle("-fx-text-fill: -black; -fx-font-size: 14px;");
        phoneLabel.getStyleClass()
                  .add("text-regular");

        Label phoneName = new Label(customerAddress.getPhoneNumber());
        phoneName.setStyle("-fx-text-fill: -black; -fx-font-size: 20px;");
        phoneName.getStyleClass()
                 .add("text-medium");

        VBox phoneRoot = new VBox(phoneLabel, phoneName);
        phoneRoot.setAlignment(Pos.TOP_LEFT);
        phoneRoot.managedProperty()
                 .bind(phoneRoot.visibleProperty());
        phoneRoot.setVisible(false);

        FlowPane customerDetailsFlow = new FlowPane(customerNameRoot, appointmentRoot, addressRoot, phoneRoot);
        customerDetailsFlow.setAlignment(Pos.TOP_LEFT);
        customerDetailsFlow.setHgap(20);
        customerDetailsFlow.setPadding(new Insets(16, 0, 0, 0));
        //customerDetailsFlow.setStyle("-fx-background-color: -green;");

        FlowPane.setMargin(addressRoot, new Insets(16, 0, 0, 0));
        FlowPane.setMargin(phoneRoot, new Insets(16, 0, 0, 0));

        HBox customerDetails = new HBox(customerIconRoot, customerDetailsFlow);
        customerDetails.setAlignment(Pos.TOP_LEFT);
        customerDetails.setSpacing(20);

        HBox.setHgrow(customerDetailsFlow, Priority.ALWAYS);

        Label editButtonIconLabel = new Label(Icons.pencil);
        editButtonIconLabel.setStyle("-fx-font-size: 14px;");
        editButtonIconLabel.getStyleClass()
                           .add("icon");

        HBox editButtonIcon = new HBox(editButtonIconLabel);
        editButtonIcon.setAlignment(Pos.CENTER_RIGHT);
        editButtonIcon.setPrefWidth(34);
        editButtonIcon.getStyleClass()
                      .add("button-sm");
        editButtonIcon.getStyleClass()
                      .add("button-left");
        editButtonIcon.getStyleClass()
                      .add("button-regular-alt");

        Label editButtonText = new Label("Edit");
        editButtonText.getStyleClass()
                      .add("text-bold");

        HBox editButtonLabel = new HBox(editButtonText);
        editButtonLabel.setAlignment(Pos.CENTER);
        editButtonLabel.setPadding(new Insets(0, 8, 0, 0));
        editButtonLabel.getStyleClass()
                       .add("button-sm");
        editButtonLabel.getStyleClass()
                       .add("button-right");
        editButtonLabel.getStyleClass()
                       .add("button-regular-alt");

        HBox editButton = new HBox(editButtonIcon, editButtonLabel);
        editButton.setAlignment(Pos.CENTER_RIGHT);
        editButton.setPrefWidth(110);
        editButton.setCursor(Cursor.HAND);
        editButton.getStyleClass()
                  .add("drop-shadow-small");

        editButton.setOnMouseClicked(event ->
        {
            // TODO: Open the customer edit modal
        });

        HBox.setHgrow(editButtonLabel, Priority.ALWAYS);

        Label deleteButtonIconLabel = new Label(Icons.times);
        deleteButtonIconLabel.setStyle("-fx-font-size: 14px;");
        deleteButtonIconLabel.getStyleClass()
                             .add("icon");

        HBox deleteButtonIcon = new HBox(deleteButtonIconLabel);
        deleteButtonIcon.setAlignment(Pos.CENTER_RIGHT);
        deleteButtonIcon.setPrefWidth(34);
        deleteButtonIcon.getStyleClass()
                        .add("button-sm");
        deleteButtonIcon.getStyleClass()
                        .add("button-left");
        deleteButtonIcon.getStyleClass()
                        .add("button-danger");

        Label deleteButtonText = new Label("Delete");
        deleteButtonText.getStyleClass()
                        .add("text-bold");

        HBox deleteButtonLabel = new HBox(deleteButtonText);
        deleteButtonLabel.setAlignment(Pos.CENTER);
        deleteButtonLabel.setPadding(new Insets(0, 8, 0, 0));
        deleteButtonLabel.getStyleClass()
                         .add("button-sm");
        deleteButtonLabel.getStyleClass()
                         .add("button-right");
        deleteButtonLabel.getStyleClass()
                         .add("button-danger");

        HBox deleteButton = new HBox(deleteButtonIcon, deleteButtonLabel);
        deleteButton.setAlignment(Pos.CENTER_RIGHT);
        deleteButton.setPrefWidth(110);
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.getStyleClass()
                    .add("drop-shadow-small");

        deleteButton.setOnMouseClicked(event ->
        {
            // TODO: Open the customer delete modal
        });

        HBox.setHgrow(deleteButtonLabel, Priority.ALWAYS);

        VBox customerControls = new VBox(editButton, deleteButton);
        customerControls.setAlignment(Pos.TOP_RIGHT);
        customerControls.setSpacing(8);
        customerControls.setPadding(new Insets(10, 0, 0, 0));

        HBox subRoot = new HBox(customerDetails, customerControls);

        HBox.setHgrow(customerDetails, Priority.ALWAYS);

        Label closeCustomerLabel = new Label(Icons.chevronUp);
        closeCustomerLabel.setStyle("-fx-font-size: 20px;");
        closeCustomerLabel.getStyleClass()
                          .add("icon");

        HBox closeCustomerItem = new HBox(closeCustomerLabel);
        closeCustomerItem.setAlignment(Pos.BOTTOM_CENTER);
        closeCustomerItem.setPadding(new Insets(0, 0, 16, 0));
        closeCustomerItem.setCursor(Cursor.HAND);
        closeCustomerItem.managedProperty()
                         .bind(closeCustomerItem.visibleProperty());
        closeCustomerItem.setVisible(false);

        VBox root = new VBox(subRoot, closeCustomerItem);
        root.setPrefHeight(100);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(0, 25, 0, 25));
        root.setCursor(Cursor.HAND);
        root.getStyleClass()
            .add("customer");
        root.getStyleClass()
            .add("drop-shadow");

        VBox.setVgrow(closeCustomerItem, Priority.ALWAYS);

        EventHandler<? super MouseEvent> rootOnClicked = event ->
        {
            root.setOnMouseClicked(null);
            root.setCursor(Cursor.DEFAULT);
            setupCustomerAnimations(customer.getCustomerId(), root);

            openCustomerDetailed(customer.getCustomerId(), addressRoot, phoneRoot, () ->
            {
                closeCustomerItem.setVisible(true);
            });
        };

        root.setOnMouseClicked(rootOnClicked);

        closeCustomerItem.setOnMouseClicked(event ->
        {
            setupCustomerAnimations(customer.getCustomerId(), root);

            closeCustomerDetailed(customer.getCustomerId(), addressRoot, phoneRoot, () ->
            {
                closeCustomerItem.setVisible(false);
                root.setOnMouseClicked(rootOnClicked);
                root.setCursor(Cursor.HAND);

                cleanUpAnimations(customer.getCustomerId());
            });
        });

        return root;
    }

    private void setupCustomerAnimations(int customerId, VBox root)
    {
        boolean hasKey = customersAnimations.containsKey(customerId);

        if (hasKey)
        {
            if (!customersAnimations.get(customerId)
                                    .isEmpty())
                return;
        }

        Map<String, Animator> animators = hasKey ? customersAnimations.get(customerId) : new HashMap<>();

        HBox subRoot = (HBox) root.getChildren()
                                  .get(0);
        HBox customerDetails = (HBox) subRoot.getChildren()
                                             .get(0);
        FlowPane customerDetailsFlow = (FlowPane) customerDetails.getChildren()
                                                                 .get(1);
        VBox customerName = (VBox) customerDetailsFlow.getChildren()
                                                      .get(0);
        VBox appointmentName = (VBox) customerDetailsFlow.getChildren()
                                                         .get(1);
        VBox addressName = (VBox) customerDetailsFlow.getChildren()
                                                     .get(2);
        VBox phoneName = (VBox) customerDetailsFlow.getChildren()
                                                   .get(3);

        double maxWidth = customerDetailsFlow.getWidth();

        addressName.setPrefWidth(maxWidth);
        phoneName.setPrefWidth(maxWidth);

        Animator rootAnimator = new Animator();
        rootAnimator.addAnimation("width",
                new KeyFrame(Animator.Zero, new KeyValue(root.prefHeightProperty(), 100, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(root.prefHeightProperty(), 340, Interpolator.EASE_OUT))
        );
        animators.put("root", rootAnimator);

        Animator customerNameAnimator = new Animator();
        customerNameAnimator.addAnimation("width",
                new KeyFrame(Animator.Zero, new KeyValue(customerName.prefWidthProperty(), customerName.getWidth(), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(customerName.prefWidthProperty(), maxWidth, Interpolator.EASE_OUT))
        );
        animators.put("customerName", customerNameAnimator);

        Animator appointmentNameAnimator = new Animator();
        appointmentNameAnimator.addAnimation("width",
                new KeyFrame(Animator.Zero, new KeyValue(appointmentName.prefWidthProperty(), appointmentName.getWidth(), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(appointmentName.prefWidthProperty(), maxWidth, Interpolator.EASE_OUT))
        );
        animators.put("appointmentName", appointmentNameAnimator);

        Animator addressNameAnimator = new Animator();
        addressNameAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(addressName.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(addressName.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("addressName", addressNameAnimator);

        Animator phoneNameAnimator = new Animator();
        phoneNameAnimator.addAnimation("opacity",
                new KeyFrame(Animator.Zero, new KeyValue(phoneName.opacityProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(phoneName.opacityProperty(), 1, Interpolator.EASE_OUT))
        );
        animators.put("phoneName", phoneNameAnimator);

        if (!hasKey)
            customersAnimations.put(customerId, animators);
    }

    private void cleanUpAnimations(int customerId)
    {
        Map<String, Animator> animators = new HashMap<>();
        if (!customersAnimations.containsKey(customerId))
            return;

        animators = customersAnimations.get(customerId);
        animators.clear();
    }

    private void openCustomerDetailed(int customerId, VBox addressRoot, VBox phoneRoot, Runnable runAfter)
    {
        Map<String, Animator> animators = customersAnimations.get(customerId);
        if (animators == null)
            throw new NoSuchElementException("Could not find the animations for the customer of id: " + customerId);

        Animator rootAnimator = animators.get("root");
        Animator customerNameAnimator = animators.get("customerName");
        Animator appointmentNameAnimator = animators.get("appointmentName");
        Animator addressNameAnimator = animators.get("addressName");
        Animator phoneNameAnimator = animators.get("phoneName");

        rootAnimator.play("width", event ->
        {
            addressRoot.setOpacity(0);
            addressRoot.setVisible(true);
            phoneRoot.setOpacity(0);
            phoneRoot.setVisible(true);
            addressNameAnimator.play("opacity");
            phoneNameAnimator.play("opacity");
            runAfter.run();
        });

        customerNameAnimator.play("width");
        appointmentNameAnimator.play("width");
    }

    private void closeCustomerDetailed(int customerId, VBox addressRoot, VBox phoneRoot, Runnable runAfter)
    {
        Map<String, Animator> animators = customersAnimations.get(customerId);
        if (animators == null)
            throw new NoSuchElementException("Could not find the animations for the customer of id: " + customerId);

        Animator rootAnimator = animators.get("root");
        Animator customerNameAnimator = animators.get("customerName");
        Animator appointmentNameAnimator = animators.get("appointmentName");
        Animator addressNameAnimator = animators.get("addressName");
        Animator phoneNameAnimator = animators.get("phoneName");

        addressNameAnimator.playReverse("opacity", event ->
        {
            addressRoot.setVisible(false);
            rootAnimator.playReverse("width");
            runAfter.run();
        });

        phoneNameAnimator.playReverse("opacity", event ->
        {
            phoneRoot.setVisible(false);
        });

        customerNameAnimator.playReverse("width");
        appointmentNameAnimator.playReverse("width");
    }
}
