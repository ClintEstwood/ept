package ept.rest.actions;

import ept.context.AbstractScenarioContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class TestRestAction <T extends AbstractScenarioContext> extends TestAction<T> {

    public TestRestAction(T context) {
        super(context);
    }


}
