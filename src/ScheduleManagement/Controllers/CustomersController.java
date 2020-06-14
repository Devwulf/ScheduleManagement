package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.Appointment;
import ScheduleManagement.Database.Models.Customer;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Utils.Icons;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.List;

public class CustomersController extends SwitchableController
{
    @FXML private Label customerIcon;

    @FXML private Label editIcon;

    @FXML private Button moreButton;

    @FXML private VBox customerListRoot;

    private DBContext context;

    @FXML
    public void initialize()
    {
        customerIcon.setText(Icons.userCircle);
        editIcon.setText(Icons.pencil);
        moreButton.setText(Icons.ellipsisV);

        context = DBContext.getInstance();

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

        FlowPane customerDetailsFlow = new FlowPane(customerNameRoot, appointmentRoot);
        customerDetailsFlow.setAlignment(Pos.TOP_LEFT);
        customerDetailsFlow.setHgap(20);
        customerDetailsFlow.setVgap(0);
        customerDetailsFlow.setPadding(new Insets(16, 0, 0, 0));

        HBox customerDetails = new HBox(customerIconRoot, customerDetailsFlow);
        customerDetails.setAlignment(Pos.TOP_LEFT);
        customerDetails.setSpacing(20);

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
        editButton.setPrefWidth(94);
        editButton.setCursor(Cursor.HAND);
        editButton.getStyleClass()
                  .add("drop-shadow-small");

        editButton.setOnMouseClicked(event ->
        {
            // TODO: Open the customer edit modal
        });

        HBox.setHgrow(editButtonLabel, Priority.ALWAYS);

        VBox editButtonRoot = new VBox(editButton);
        editButtonRoot.setAlignment(Pos.TOP_RIGHT);

        Button moreButton = new Button(Icons.ellipsisV);
        moreButton.setPrefWidth(36);
        moreButton.setCursor(Cursor.HAND);
        moreButton.setStyle("-fx-font-size: 16px;");
        moreButton.getStyleClass()
                  .add("button-sm");
        moreButton.getStyleClass()
                  .add("button-regular-alt");
        moreButton.getStyleClass()
                  .add("icon");
        moreButton.getStyleClass()
                  .add("drop-shadow-small");

        moreButton.setOnAction(event ->
        {
            // TODO: Open a dropdown with options
        });

        HBox customerControls = new HBox(editButtonRoot, moreButton);
        customerControls.setAlignment(Pos.TOP_RIGHT);
        customerControls.setSpacing(15);
        customerControls.setPadding(new Insets(32, 0, 0, 0));

        HBox root = new HBox(customerDetails, customerControls);
        root.setPrefHeight(100);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(0, 25, 0, 25));
        root.setCursor(Cursor.HAND);
        root.getStyleClass()
            .add("customer");
        root.getStyleClass()
            .add("drop-shadow");

        root.setOnMouseClicked(event ->
        {
            // TODO: Open the customer detailed listing
            openCustomerDetailed(root);

            // TODO: After animation, disable this root's setOnMouseClicked
        });

        HBox.setHgrow(customerDetails, Priority.ALWAYS);

        return root;
    }

    private void openCustomerDetailed(HBox root)
    {
        Animator rootAnimator = new Animator();
        rootAnimator.addAnimation("expand",
                new KeyFrame(Animator.Zero, new KeyValue(root.prefHeightProperty(), 100, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(root.prefHeightProperty(), 312, Interpolator.EASE_OUT))
        );

        rootAnimator.play("expand");

        HBox customerDetails = (HBox) root.getChildren().get(0);
        FlowPane customerDetailsFlow = (FlowPane) customerDetails.getChildren().get(1);
        VBox customerName = (VBox) customerDetailsFlow.getChildren().get(0);
        VBox appointmentName = (VBox) customerDetailsFlow.getChildren().get(1);

        double customerNameWidth = customerName.getWidth();
        double maxWidth = customerDetailsFlow.getWidth();
        Animator customerNameAnimator = new Animator();
        customerNameAnimator.addAnimation("expand",
                new KeyFrame(Animator.Zero, new KeyValue(customerName.prefWidthProperty(), customerNameWidth, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(customerName.prefWidthProperty(), maxWidth, Interpolator.EASE_OUT))
        );

        customerNameAnimator.play("expand");

        double appointmentNameWidth = appointmentName.getWidth();
        Animator appointmentNameAnimator = new Animator();
        appointmentNameAnimator.addAnimation("expand",
                new KeyFrame(Animator.Zero, new KeyValue(appointmentName.prefWidthProperty(), appointmentNameWidth, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(appointmentName.prefWidthProperty(), maxWidth, Interpolator.EASE_OUT))
        );

        appointmentNameAnimator.play("expand");
    }
}
