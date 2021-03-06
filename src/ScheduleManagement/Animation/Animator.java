package ScheduleManagement.Animation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

// A wrapper around Timeline that gives methods to play animations
// both forward and backward in constant rate and holds all the
// animations associated to one Node
public class Animator
{
    public static final Duration Zero = Duration.millis(0);
    public static final Duration SuperSlow = Duration.millis(3000);
    public static final Duration VerySlow = Duration.millis(1000);
    public static final Duration Slow = Duration.millis(500);
    public static final Duration Fast = Duration.millis(200);
    public static final Duration VeryFast = Duration.millis(100);
    public static final Duration SuperFast = Duration.millis(50);

    private Map<String, Timeline> timelines;

    public Animator()
    {
        timelines = new HashMap<>();
    }

    public void addAnimation(String name, KeyFrame... keyFrames)
    {
        if (name.isEmpty())
            throw new IllegalArgumentException("The animation name given cannot be empty.");
        if (keyFrames.length <= 1)
            throw new IllegalArgumentException("The animation keyframes given must have at least 2 keyframes.");

        timelines.put(name, new Timeline(keyFrames));
    }

    public Timeline getAnimation(String animationName)
    {
        return getAnimation(animationName, null);
    }

    public Timeline getAnimation(String animationName, EventHandler<ActionEvent> eventAfterAnimate)
    {
        if (animationName.isEmpty())
            throw new IllegalArgumentException("The animation name given cannot be empty.");

        Timeline timeline = timelines.get(animationName);
        timeline.setRate(1);

        if (eventAfterAnimate != null)
            // Handles the given event after this timeline finishes playing
            timeline.setOnFinished(event ->
            {
                eventAfterAnimate.handle(event);
                timeline.setOnFinished(null);
            });

        return timeline;
    }

    public Timeline getAnimationReversed(String animationName)
    {
        return getAnimationReversed(animationName, null);
    }

    public Timeline getAnimationReversed(String animationName, EventHandler<ActionEvent> eventAfterAnimate)
    {
        if (animationName.isEmpty())
            throw new IllegalArgumentException("The animation name given cannot be empty.");

        Timeline timeline = timelines.get(animationName);
        timeline.setRate(-1);

        if (eventAfterAnimate != null)
            // Handles the given event after this timeline finishes playing in reverse
            timeline.setOnFinished(event ->
            {
                eventAfterAnimate.handle(event);
                timeline.setOnFinished(null);
            });

        return timeline;
    }

    public void play(String animationName)
    {
        play(animationName, null);
    }

    public void play(String animationName, EventHandler<ActionEvent> value)
    {
        if (animationName.isEmpty())
            throw new IllegalArgumentException("The animation name given cannot be empty.");

        Timeline timeline = getAnimation(animationName, value);
        if (timeline.getStatus() == Animation.Status.RUNNING)
            timeline.stop();

        timeline.play();
    }

    public void playReverse(String animationName)
    {
        playReverse(animationName, null);
    }

    public void playReverse(String animationName, EventHandler<ActionEvent> value)
    {
        if (animationName.isEmpty())
            throw new IllegalArgumentException("The animation name given cannot be empty.");

        Timeline timeline = getAnimationReversed(animationName, value);
        if (timeline.getStatus() == Animation.Status.RUNNING)
            timeline.stop();

        timeline.play();
    }
}
