package ScheduleManagement.Controllers;

import ScheduleManagement.Utils.Colors;
import ScheduleManagement.Utils.Icons;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class PopupController extends BaseController
{
    @FXML private Label infoIcon;
    @FXML private Label messageText;

    @FXML private Pane confirmCancelPane;
    @FXML private Pane okPane;

    @FXML private Button confirmButton;

    public void showWarningPopup(String message)
    {
        infoIcon.setText(Icons.warningCircle);
        infoIcon.setTextFill(Colors.yellow);

        showOkPopup(message);
    }

    public void showErrorPopup(String message)
    {
        infoIcon.setText(Icons.timesCircle);
        infoIcon.setTextFill(Colors.red);

        showOkPopup(message);
    }

    public void showSuccessPopup(String message)
    {
        infoIcon.setText(Icons.checkCircle);
        infoIcon.setTextFill(Colors.green);

        showOkPopup(message);
    }

    private void showOkPopup(String message)
    {
        messageText.setText(message);

        confirmCancelPane.setVisible(false);
        okPane.setVisible(true);
    }

    public void showConfirmPopup(String message, Runnable onConfirm)
    {
        infoIcon.setText(Icons.warningCircle);
        infoIcon.setTextFill(Colors.yellow);
        messageText.setText(message);

        confirmCancelPane.setVisible(true);
        okPane.setVisible(false);

        confirmButton.addEventHandler(ActionEvent.ACTION, event ->
        {
            onConfirm.run();
            stage.close();
        });
    }

    @FXML
    public void handleOk()
    {
        stage.close();
    }
}
