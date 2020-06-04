package ScheduleManagement.Transitions;

import javafx.scene.Node;

public abstract class NodeTransitioner<TNode extends Node>
{
    protected TNode node;
    protected int currentTransitionValue;

    public NodeTransitioner(TNode node)
    {
        this.node = node;
    }

    public void transition(int transitionValue)
    {
        preTransition(transitionValue);
    }

    // Runs before the transition, could be used to
    // check requirements or values
    protected void preTransition(int transitionToValue)
    {
        if (transitionToValue == currentTransitionValue)
            return;

        currentTransitionValue = transitionToValue;
        onTransition(transitionToValue);
    }
    protected abstract void onTransition(int transitionToValue);

    public TNode getNode()
    {
        return node;
    }

    public int getCurrentTransitionValue()
    {
        return currentTransitionValue;
    }
}
