package ScheduleManagement.Controllers;

import ScheduleManagement.Animation.Animator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class SwitchableController extends BaseController
{
    @FXML protected Pane selectionPane;

    private int currentSelection = 0;
    private Timeline timeline = null;

    // TODO: This is temporary, replace it with something more robust
    // selection is from 0 - 4, 0 is calendar, 4 is settings
    protected void animateSelectionPane(int selection, EventHandler<ActionEvent> value)
    {
        if (timeline != null)
            return;

        timeline = new Timeline(
                new KeyFrame(Animator.Zero, new KeyValue(selectionPane.layoutYProperty(), 62 * currentSelection + 9, Interpolator.EASE_OUT)),
                new KeyFrame(Animator.Fast, new KeyValue(selectionPane.layoutYProperty(), 62 * selection + 9, Interpolator.EASE_OUT))
        );

        timeline.setOnFinished(event -> {
            value.handle(event);
            timeline = null;
        });
        timeline.play();
    }

    protected void initializeSelectionPanePosition(int selection)
    {
        currentSelection = selection;

        selectionPane.setManaged(false);
        selectionPane.relocate(0, 62 * selection + 9);
    }
}
