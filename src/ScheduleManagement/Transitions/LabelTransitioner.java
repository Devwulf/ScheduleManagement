package ScheduleManagement.Transitions;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LabelTransitioner extends NodeTransitioner<Label>
{
    private enum LabelTransitionType
    {
        TextFill,
        BGFill,
        Fade
    }

    private Object value1, value2;
    private LabelTransitionType type;
    private Timeline timeline;

    // TODO: Possibly allow multiple color states for the text fill
    public LabelTransitioner(Label node, Color startFillValue, Color endFillValue)
    {
        super(node);
        value1 = startFillValue;
        value2 = endFillValue;
        type = LabelTransitionType.TextFill;

        // TODO: Tweak animation durations if needed, refer to this article: https://valhead.com/2016/05/05/how-fast-should-your-ui-animations-be/
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(node.textFillProperty(), startFillValue)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.textFillProperty(), endFillValue))
        );
    }

    @Override
    protected void onTransition(int transitionToValue)
    {
        if (transitionToValue != 0 &&
            transitionToValue != 1)
            return;

        if (timeline.getStatus() == Animation.Status.RUNNING)
            timeline.stop();

        // This means we want to go from 1 to 0
        timeline.setRate(transitionToValue == 0 ? -1 : 1);
        timeline.play();
    }
}
