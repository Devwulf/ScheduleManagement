package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import ScheduleManagement.Utils.Colors;
import ScheduleManagement.Utils.Vector;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LoginController extends BaseController
{
    @FXML private Label logoIcon;
    @FXML private Label usernameIcon;
    @FXML private Label passwordIcon;
    @FXML private Label passwordIcon2;

    @FXML private Label resetPasswordButton;
    private Animator resetPasswordAnimator;

    @FXML private HBox confirmPasswordField;
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
                new KeyFrame(Animator.Zero, new KeyValue(confirmPasswordField.prefHeightProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(confirmPasswordField.prefHeightProperty(), 42, Interpolator.EASE_OUT))
        );

        resetPasswordAnimator = new Animator();
        resetPasswordAnimator.addAnimation("height",
                new KeyFrame(Animator.Zero, new KeyValue(resetPasswordButton.prefHeightProperty(), 0, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(resetPasswordButton.prefHeightProperty(), 17, Interpolator.EASE_OUT))
        );

        submitAnimator = new Animator();
        submitAnimator.addAnimation("text",
                new KeyFrame(Animator.Zero, new KeyValue(submitButton.textProperty(), "Login", Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(submitButton.textProperty(), "Sign Up", Interpolator.EASE_OUT))
        );
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

    @FXML
    public void handleLoginSelectButton()
    {
        resetPasswordButton.setVisible(true);
        signupSelectAnimator.playReverse("color");
        loginSelectAnimator.play("color");

        signupUnderlineAnimator.playReverse("height");
        loginUnderlineAnimator.play("height");

        signupUnderlineAnimator.playReverse("color");
        loginUnderlineAnimator.play("color");

        confirmPasswordAnimator.playReverse("height", event -> confirmPasswordField.setVisible(false));
        resetPasswordAnimator.play("height");

        submitAnimator.playReverse("text");

        isLogin = true;
    }

    @FXML
    public void handleSignupSelectButton()
    {
        confirmPasswordField.setVisible(true);

        signupSelectAnimator.play("color");
        loginSelectAnimator.playReverse("color");

        signupUnderlineAnimator.play("height");
        loginUnderlineAnimator.playReverse("height");

        signupUnderlineAnimator.play("color");
        loginUnderlineAnimator.playReverse("color");

        confirmPasswordAnimator.play("height");
        resetPasswordAnimator.playReverse("height", event -> resetPasswordButton.setVisible(false));

        submitAnimator.play("text");

        isLogin = false;
    }

    @FXML
    public void handleHoverEnterSignupSelectButton()
    {
        if (!isLogin)
            return;

        signupSelectAnimator.play("color");
    }

    @FXML
    public void handleHoverExitSignupSelectButton()
    {
        if (!isLogin)
            return;

        signupSelectAnimator.playReverse("color");
    }

    @FXML
    public void handleHoverEnterLoginSelectButton()
    {
        if (isLogin)
            return;

        loginSelectAnimator.play("color");
    }

    @FXML
    public void handleHoverExitLoginSelectButton()
    {
        if (isLogin)
            return;

        loginSelectAnimator.playReverse("color");
    }

    @FXML
    public void handleHoverEnterSubmitButton()
    {
        submitAnimator.play("hover");
        submitAnimator.play("color");
    }

    @FXML
    public void handleHoverExitSubmitButton()
    {
        submitAnimator.playReverse("hover");
        submitAnimator.playReverse("color");
    }
}
