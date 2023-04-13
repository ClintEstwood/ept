package ept.rest.actions;

import ept.context.AbstractScenarioContext;

public abstract class TestAction<T> {
    protected final T context;

    protected TestAction(T context) {
        this.context = context;
    }
}