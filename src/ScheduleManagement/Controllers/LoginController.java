package ScheduleManagement.Controllers;

import ScheduleManagement.Transitions.LabelTransitioner;
import ScheduleManagement.Transitions.PaneTransitioner;
import ScheduleManagement.Utils.Vector;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class LoginController extends BaseController
{
    @FXML private Label logoIcon;
    @FXML private Label usernameIcon;
    @FXML private Label passwordIcon;
    @FXML private Label passwordIcon2;
    @FXML private Label resetPasswordButton;

    @FXML private HBox confirmPasswordField;

    @FXML private Pane loginUnderline;
    private PaneTransitioner loginUnderlineSizeTransitioner;
    private PaneTransitioner loginUnderlineColorTransitioner;
    @FXML private Pane signupUnderline;
    private PaneTransitioner signupUnderlineSizeTransitioner;
    private PaneTransitioner signupUnderlineColorTransitioner;

    @FXML private Label loginSelectButton;
    private LabelTransitioner loginSelectTransitioner;
    @FXML private Label signupSelectButton;
    private LabelTransitioner signupSelectTransitioner;

    private boolean isLogin = true;

    @FXML
    public void initialize()
    {
        // Cannot access stage from here, because this runs on
        // FXMLLoader.load, where stage is not set yet.

        logoIcon.setText("\uf274");
        usernameIcon.setText("\uf007");
        passwordIcon.setText("\uf084");
        passwordIcon2.setText("\uf084");

        // This is so setManaged for these nodes is also set to false
        // when setVisible is set to false. If isManaged is false, the
        // nodes don't get calculated with the layout, making it seem
        // like they don't exist.
        confirmPasswordField.managedProperty()
                            .bind(confirmPasswordField.visibleProperty());
        resetPasswordButton.managedProperty()
                           .bind(resetPasswordButton.visibleProperty());

        handleLoginSelectButton();
    }

    @Override
    public void lateInitialize()
    {
        Parent root = stage.getScene()
                           .getRoot();
    }

    @FXML
    public void handleLoginSelectButton()
    {
        if (signupSelectTransitioner == null)
            signupSelectTransitioner = new LabelTransitioner(signupSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        if (loginSelectTransitioner == null)
            loginSelectTransitioner = new LabelTransitioner(loginSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        signupSelectTransitioner.transition(0);
        loginSelectTransitioner.transition(1);

        if (signupUnderlineSizeTransitioner == null)
            signupUnderlineSizeTransitioner = new PaneTransitioner(signupUnderline, new Vector(-1, 1), new Vector(-1, 3));

        if (loginUnderlineSizeTransitioner == null)
            loginUnderlineSizeTransitioner = new PaneTransitioner(loginUnderline, new Vector(-1, 1), new Vector(-1, 3));

        signupUnderlineSizeTransitioner.transition(0);
        loginUnderlineSizeTransitioner.transition(1);

        if (signupUnderlineColorTransitioner == null)
            signupUnderlineColorTransitioner = new PaneTransitioner(signupUnderline, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        if (loginUnderlineColorTransitioner == null)
            loginUnderlineColorTransitioner = new PaneTransitioner(loginUnderline, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        signupUnderlineColorTransitioner.transition(0);
        loginUnderlineColorTransitioner.transition(1);

        confirmPasswordField.setVisible(false);
        resetPasswordButton.setVisible(true);

        isLogin = true;
    }

    @FXML
    public void handleSignupSelectButton()
    {
        if (signupSelectTransitioner == null)
            signupSelectTransitioner = new LabelTransitioner(signupSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        if (loginSelectTransitioner == null)
            loginSelectTransitioner = new LabelTransitioner(loginSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        signupSelectTransitioner.transition(1);
        loginSelectTransitioner.transition(0);

        if (signupUnderlineSizeTransitioner == null)
            signupUnderlineSizeTransitioner = new PaneTransitioner(signupUnderline, new Vector(-1, 1), new Vector(-1, 3));

        if (loginUnderlineSizeTransitioner == null)
            loginUnderlineSizeTransitioner = new PaneTransitioner(loginUnderline, new Vector(-1, 1), new Vector(-1, 3));

        signupUnderlineSizeTransitioner.transition(1);
        loginUnderlineSizeTransitioner.transition(0);

        if (signupUnderlineColorTransitioner == null)
            signupUnderlineColorTransitioner = new PaneTransitioner(signupUnderline, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        if (loginUnderlineColorTransitioner == null)
            loginUnderlineColorTransitioner = new PaneTransitioner(loginUnderline, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        signupUnderlineColorTransitioner.transition(1);
        loginUnderlineColorTransitioner.transition(0);

        confirmPasswordField.setVisible(true);
        resetPasswordButton.setVisible(false);

        isLogin = false;
    }

    @FXML
    public void handleHoverEnterSignupSelectButton()
    {
        if (!isLogin)
            return;

        if (signupSelectTransitioner == null)
            signupSelectTransitioner = new LabelTransitioner(signupSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        signupSelectTransitioner.transition(1);
    }

    @FXML
    public void handleHoverExitSignupSelectButton()
    {
        if (!isLogin || signupSelectTransitioner == null)
            return;

        signupSelectTransitioner.transition(0);
    }

    @FXML
    public void handleHoverEnterLoginSelectButton()
    {
        if (isLogin)
            return;

        if (loginSelectTransitioner == null)
            loginSelectTransitioner = new LabelTransitioner(loginSelectButton, Color.web("#95A5A6"), Color.web("#ECF0F1"));

        loginSelectTransitioner.transition(1);
    }

    @FXML
    public void handleHoverExitLoginSelectButton()
    {
        if (isLogin || loginSelectTransitioner == null)
            return;

        loginSelectTransitioner.transition(0);
    }
}
