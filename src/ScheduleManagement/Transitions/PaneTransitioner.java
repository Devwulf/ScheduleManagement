package ScheduleManagement.Transitions;

import ScheduleManagement.Utils.Vector;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PaneTransitioner extends NodeTransitioner<Pane>
{
    private Timeline widthTimeline = null;
    private Timeline heightTimeline = null;

    private Timeline colorTimeline = null;

    public PaneTransitioner(Pane node, Vector startSize, Vector endSize)
    {
        super(node);

        if (startSize.getX() >= 0 && endSize.getX() >= 0)
        {
            widthTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(node.prefWidthProperty(), startSize.getX())),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.prefWidthProperty(), endSize.getX()))
            );
        }

        if (startSize.getY() >= 0 && endSize.getY() >= 0)
        {
            heightTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(node.prefHeightProperty(), startSize.getY())),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.prefHeightProperty(), endSize.getY()))
            );
        }
    }

    public PaneTransitioner(Pane pane, Color startFillValue, Color endFillValue)
    {
        super(pane);

        colorTimeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(pane.backgroundProperty(), new Background(new BackgroundFill(startFillValue, null, null)))),
                new KeyFrame(Duration.millis(200), new KeyValue(pane.backgroundProperty(), new Background(new BackgroundFill(endFillValue, null, null))))
        );
    }

    @Override
    protected void onTransition(int transitionToValue)
    {
        if (transitionToValue != 0 &&
            transitionToValue != 1)
            return;

        if (widthTimeline != null)
        {
            if (widthTimeline.getStatus() == Animation.Status.RUNNING)
                widthTimeline.stop();

            widthTimeline.setRate(transitionToValue == 0 ? -1 : 1);
            widthTimeline.play();
        }

        if (heightTimeline != null)
        {
            if (heightTimeline.getStatus() == Animation.Status.RUNNING)
                heightTimeline.stop();

            heightTimeline.setRate(transitionToValue == 0 ? -1 : 1);
            heightTimeline.play();
        }

        if (colorTimeline != null)
        {
            if (colorTimeline.getStatus() == Animation.Status.RUNNING)
                colorTimeline.stop();

            colorTimeline.setRate(transitionToValue == 0 ? -1 : 1);
            colorTimeline.play();
        }
    }
}
