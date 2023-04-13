package ept.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import ept.ScenarioVariables;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@Data
@RequiredArgsConstructor
public abstract class AbstractScenarioContext {

    private final ScenarioVariables scenarioVariables;
    public final ObjectMapper objectMapper;
    protected final RequestSpecification spec;

    public abstract String getXsrfTokenName();

    public AbstractScenarioContext setVariable(String variableName, Object variableValue) {
        this.scenarioVariables.setVariable(variableName, variableValue);
        return this;
    }

    public Map<String, Object> getScenarioVariables() {
        return this.scenarioVariables.getScenarioVariables();
    }

    public <T> T getVariable(String variableName) {
        return this.scenarioVariables.getVariable(variableName);
    }

    public List<String> getVariables(List<String> variableNames) {
        return this.scenarioVariables.getVariables(variableNames);
    }

    public final <T> List<T> getEmptyListOrVariable(String value) {
        return this.scenarioVariables.getEmptyListOrVariable(value);
    }

    public void clear() {
        this.scenarioVariables.clear();
    }

    public Object[] parseParameters(Object... parameters) {
        return Stream.of(parameters).map(parameter -> getVariable(String.valueOf(parameter))).toArray();
    }
}
