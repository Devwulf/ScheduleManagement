package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.Models.Appointment;
import ScheduleManagement.Database.Models.User;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Exceptions.IllegalFormInput;
import ScheduleManagement.Managers.LanguageManager;
import ScheduleManagement.Managers.LoginManager;
import ScheduleManagement.Managers.ViewManager;
import ScheduleManagement.Utils.Colors;
import ScheduleManagement.Utils.Icons;
import ScheduleManagement.Utils.LanguageKeys;
import ScheduleManagement.Utils.TimestampHelper;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LoginController extends BaseController
{
    private static class LoginKeys extends LanguageKeys
    {
        public static final String loginSelect = "login.label.loginSelect";
        public static final String signupSelect = "login.label.signupSelect";
        public static final String usernameField = "login.textField.usernamePrompt";
        public static final String passwordField = "login.passField.passwordPrompt";
        public static final String confirmPasswordField = "login.passField.confirmPasswordPrompt";
        public static final String loginSubmit = "login.button.loginSubmit";
        public static final String signupSubmit = "login.button.signupSubmit";
        public static final String resetPassword = "login.label.resetPassword";

        public static final String emptyUserPass = "login.message.emptyUsernamePassword";
        public static final String emptyConfirmPass = "login.message.emptyConfirmPassword";
        public static final String successLogin = "login.message.successLogin";
        public static final String successSignup = "login.message.successSignup";
        public static final String incorrectUserPass = "login.message.incorrectUsernamePassword";
        public static final String usernameTaken = "login.message.usernameTaken";
    }

    @FXML private Label logoIcon;
    @FXML private Label usernameIcon;
    @FXML private Label passwordIcon;
    @FXML private Label passwordIcon2;

    @FXML private Label logoText;
    @FXML private Label languageButton;

    @FXML private Label resetPasswordButton;
    private Animator resetPasswordAnimator;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private HBox confirmPasswordBox;
    private Animator confirmPasswordAnimator;

    @FXML private Pane loginUnderline;
    private Animator loginUnderlineAnimator;
    @FXML private Pane signupUnderline;
    private Animator signupUnderlineAnimator;

    @FXML private Label loginSelect;
    private Animator loginSelectAnimator;
    @FXML private Label signupSelect;
    private Animator signupSelectAnimator;

    @FXML private Button submitButton;
    private Animator submitAnimator;

    private DBContext context;
    private BooleanProperty isLogin = new SimpleBooleanProperty(true);

    @FXML
    public void initialize()
    {
        // Cannot access stage from here, because this runs on
        // FXMLLoader.load, where stage is not set yet.

        context = DBContext.getInstance();

        logoIcon.setText(Icons.calendarCheck);
        usernameIcon.setText(Icons.user);
        passwordIcon.setText(Icons.key);
        passwordIcon2.setText(Icons.key);
        languageButton.setText(Icons.language);

        // This is so setManaged for these nodes is also set to false
        // when setVisible is set to false. If isManaged is false, the
        // nodes don't get calculated with the layout, making it seem
        // like they don't exist.
        confirmPasswordBox.managedProperty()
                          .bind(confirmPasswordBox.visibleProperty());
        resetPasswordButton.managedProperty()
                           .bind(resetPasswordButton.visibleProperty());
    }

    @Override
    public void lateInitialize()
    {
        Parent root = stage.getScene()
                           .getRoot();
    }

    @Override
    public void initializeAnimations()
    {
        loginSelectAnimator = new Animator();
        loginSelectAnimator.addAnimation("color",
                new KeyFrame(Animator.Zero, new KeyValue(loginSelect.textFillProperty(), Colors.lighterGray, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(loginSelect.textFillProperty(), Colors.white, Interpolator.EASE_OUT))
        );

        signupSelectAnimator = new Animator();
        signupSelectAnimator.addAnimation("color",
                new KeyFrame(Animator.Zero, new KeyValue(signupSelect.textFillProperty(), Colors.lighterGray, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(signupSelect.textFillProperty(), Colors.white, Interpolator.EASE_OUT))
        );

        loginUnderlineAnimator = new Animator();
        loginUnderlineAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(loginUnderline.prefHeightProperty(), 1, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(loginUnderline.prefHeightProperty(), 3, Interpolator.EASE_OUT))
        );
        loginUnderlineAnimator.addAnimation("color",
                new KeyFrame(Animator.Zero, new KeyValue(loginUnderline.backgroundProperty(), new Background(new BackgroundFill(Colors.lighterGray, null, null)), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(loginUnderline.backgroundProperty(), new Background(new BackgroundFill(Colors.white, null, null)), Interpolator.EASE_OUT))
        );

        signupUnderlineAnimator = new Animator();
        signupUnderlineAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(signupUnderline.prefHeightProperty(), 1, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(signupUnderline.prefHeightProperty(), 3, Interpolator.EASE_OUT))
        );
        signupUnderlineAnimator.addAnimation("color",
                new KeyFrame(Animator.Zero, new KeyValue(signupUnderline.backgroundProperty(), new Background(new BackgroundFill(Colors.lighterGray, null, null)), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(signupUnderline.backgroundProperty(), new Background(new BackgroundFill(Colors.white, null, null)), Interpolator.EASE_OUT))
        );

        confirmPasswordAnimator = new Animator();
        confirmPasswordAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(confirmPasswordBox.prefHeightProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(confirmPasswordBox.prefHeightProperty(), 42, Interpolator.EASE_OUT))
        );

        resetPasswordAnimator = new Animator();
        resetPasswordAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(resetPasswordButton.prefHeightProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(resetPasswordButton.prefHeightProperty(), 17, Interpolator.EASE_OUT))
        );

        submitAnimator = new Animator();
        DropShadow shadow1 = new DropShadow();
        shadow1.setBlurType(BlurType.GAUSSIAN);
        shadow1.setColor(Color.rgb(0, 0, 0, 0.25));
        shadow1.setRadius(2);
        shadow1.setOffsetY(2);
        DropShadow shadow2 = new DropShadow();
        shadow2.setBlurType(BlurType.GAUSSIAN);
        shadow2.setColor(Color.rgb(0, 0, 0, 0.25));
        shadow2.setRadius(4);
        shadow2.setOffsetY(4);
        submitAnimator.addAnimation("hover",
                new KeyFrame(Animator.Zero, new KeyValue(submitButton.effectProperty(), shadow1, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.SuperFast, new KeyValue(submitButton.effectProperty(), shadow2, Interpolator.EASE_OUT))
        );
        CornerRadii radii = new CornerRadii(21);
        submitAnimator.addAnimation("color",
                new KeyFrame(Animator.Zero, new KeyValue(submitButton.backgroundProperty(), new Background(new BackgroundFill(Colors.green, radii, null)), Interpolator.EASE_OUT)),
                new KeyFrame(Animator.SuperFast, new KeyValue(submitButton.backgroundProperty(), new Background(new BackgroundFill(Colors.darkGreen, radii, null)), Interpolator.EASE_OUT))
        );

        handleLoginSelectButton();
    }

    @Override
    public void initializeLanguage()
    {
        LanguageManager langManager = LanguageManager.getInstance();

        logoText.textProperty()
                .bind(langManager.createStringBinding(LoginKeys.logo));
        loginSelect.textProperty()
                   .bind(langManager.createStringBinding(LoginKeys.loginSelect));
        signupSelect.textProperty()
                    .bind(langManager.createStringBinding(LoginKeys.signupSelect));
        usernameField.promptTextProperty()
                     .bind(langManager.createStringBinding(LoginKeys.usernameField));
        passwordField.promptTextProperty()
                     .bind(langManager.createStringBinding(LoginKeys.passwordField));
        confirmPasswordField.promptTextProperty()
                            .bind(langManager.createStringBinding(LoginKeys.confirmPasswordField));

        isLogin.addListener((observable, oldValue, newValue) ->
        {
            // TODO: Does this add new unnecessary listeners for the submitButton?
            if (newValue)
                submitButton.textProperty()
                            .bind(langManager.createStringBinding(LoginKeys.loginSubmit));
            else
                submitButton.textProperty()
                            .bind(langManager.createStringBinding(LoginKeys.signupSubmit));
        });

        //submitButton.textProperty().bind(Bindings.when(isLogin).then(langManager.getTranslation(LoginKeys.loginSubmit)).otherwise(langManager.getTranslation(LoginKeys.signupSubmit)));
        resetPasswordButton.textProperty()
                           .bind(langManager.createStringBinding(LoginKeys.resetPassword));
    }

    @FXML
    private void handleLoginSelectButton()
    {
        resetPasswordButton.setVisible(true);
        signupSelectAnimator.playReverse("color");
        loginSelectAnimator.play("color");

        signupUnderlineAnimator.playReverse("height");
        loginUnderlineAnimator.play("height");

        signupUnderlineAnimator.playReverse("color");
        loginUnderlineAnimator.play("color");

        confirmPasswordAnimator.playReverse("height", event -> confirmPasswordBox.setVisible(false));
        resetPasswordAnimator.play("height");

        isLogin.set(true);
    }

    @FXML
    private void handleSignupSelectButton()
    {
        confirmPasswordBox.setVisible(true);

        signupSelectAnimator.play("color");
        loginSelectAnimator.playReverse("color");

        signupUnderlineAnimator.play("height");
        loginUnderlineAnimator.playReverse("height");

        signupUnderlineAnimator.play("color");
        loginUnderlineAnimator.playReverse("color");

        confirmPasswordAnimator.play("height");
        resetPasswordAnimator.playReverse("height", event -> resetPasswordButton.setVisible(false));

        isLogin.set(false);
    }

    @FXML
    private void handleHoverEnterSignupSelectButton()
    {
        if (!isLogin.get())
            return;

        signupSelectAnimator.play("color");
    }

    @FXML
    private void handleHoverExitSignupSelectButton()
    {
        if (!isLogin.get())
            return;

        signupSelectAnimator.playReverse("color");
    }

    @FXML
    private void handleHoverEnterLoginSelectButton()
    {
        if (isLogin.get())
            return;

        loginSelectAnimator.play("color");
    }

    @FXML
    private void handleHoverExitLoginSelectButton()
    {
        if (isLogin.get())
            return;

        loginSelectAnimator.playReverse("color");
    }

    @FXML
    private void handleHoverEnterSubmitButton()
    {
        submitAnimator.play("hover");
        submitAnimator.play("color");
    }

    @FXML
    private void handleHoverExitSubmitButton()
    {
        submitAnimator.playReverse("hover");
        submitAnimator.playReverse("color");
    }

    // TODO: Show all popups in different languages
    @FXML
    private void handleSubmitButton()
    {
        LanguageManager langManager = LanguageManager.getInstance();

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty())
        {
            // TODO: Show a popup for invalid input
            ViewManager.getInstance()
                       .showErrorPopup(langManager.getTranslation(LoginKeys.emptyUserPass));
            return;
        }

        if (!isLogin.get() && confirmPassword.isEmpty())
        {
            // TODO: Show a popup for invalid confirm password input
            ViewManager.getInstance()
                       .showErrorPopup(langManager.getTranslation(LoginKeys.emptyConfirmPass));
            return;
        }

        if (isLogin.get())
        {
            try
            {
                if (LoginManager.getInstance()
                                .login(username, password))
                {
                    // Load the calendar view
                    CalendarController controller = ViewManager.getInstance()
                                                               .loadView(ViewManager.ViewNames.Calendar);
                    controller.checkApptIn15Mins();
                }
                else
                {
                    // TODO: Show a popup for username/password not found
                    ViewManager.getInstance()
                               .showErrorPopup(langManager.getTranslation(LoginKeys.incorrectUserPass));
                    throw new IllegalFormInput(langManager.getTranslation(LoginKeys.incorrectUserPass));
                }
            }
            catch (IllegalFormInput ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            if (LoginManager.getInstance()
                            .signup(username, password))
            {
                // TODO: Show a popup for successful signup and go to login
                ViewManager.getInstance()
                           .showSuccessPopup(langManager.getTranslation(LoginKeys.successSignup));
            }
            else
            {
                // TODO: Show a popup for username already taken
                ViewManager.getInstance()
                           .showWarningPopup(langManager.getTranslation(LoginKeys.usernameTaken));
            }
        }
    }

    @FXML
    private void handleToggleLanguage()
    {
        LanguageManager langManager = LanguageManager.getInstance();
        if (langManager.getLocale() == Locale.US)
            langManager.setLocale(Locale.FRANCE);
        else
            langManager.setLocale(Locale.US);
    }
}
