package ept;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;

@Slf4j
@Component
public class ScenarioVariables {
    private final ThreadLocal<Map<String, Object>> scenarioVariables = new ThreadLocal<>();

    public void setVariable(String variableName, Object variableValue) {
        //todo: variableValue can be huge so show it partly

        log.info("Register variable with name: {}, value: {}, thread: {}",
                variableName, Objects.isNull(variableValue) ?
                        "<NULL>" :
                        variableValue.toString(),
                currentThread().threadId());

        this.getScenarioVariables().put(variableName, variableValue);
    }

    public Map<String, Object> getScenarioVariables() {
        if (Objects.isNull(this.scenarioVariables.get())) {
            this.scenarioVariables.set(new HashMap<>());
        }
        return this.scenarioVariables.get();
    }

    public <T> T getVariable(String variableName){
        try {
            String formattedVariable = StringUtils.substringBetween(variableName, "${", "}");
            Object variableObject = Objects.isNull(formattedVariable) ?
                    (T) variableName :
                    (T) scenarioVariables.get().get(formattedVariable);

            if (variableObject instanceof String) {
                String variableValue = getValueFromVariable(variableName);
                log.info("Variable name: {}, value: {}", variableName, variableValue);
                return (T) variableValue;
            } else {
                return (T) variableObject;
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    public List<String> getVariables (List<String> variableNames) {
        List<String> result = new ArrayList<>();
        variableNames.forEach(v -> result.add(getVariable(v)));
        return result;
    }

    public List<List<String>> getVariablesList(List<List<String>> variablesList){
        List<List<String>> result = new ArrayList<>();
        variablesList.forEach(list -> result.add(getVariables(list)));
        return result;
    }

    private String getValueFromVariable(String variableName) {
        return Arrays.asList(variableName.split("(?<=})")).stream().map(vn ->{
            String formattedVariable = StringUtils.substringBetween(vn, "${", "}");
            String variableValue = (String) getScenarioVariables().get(formattedVariable);
            return StringUtils.replace(vn, String.format("${%s}", formattedVariable), variableValue);
        }).collect(Collectors.joining());
    }

    public final <T> List<T> getEmptyListOrVariable(String value){
        try {
            return value.isEmpty()?
                    Collections.emptyList() :
                    getVariable(value);
        } catch (NullPointerException e) {
            return Collections.emptyList();
        }
    }

    public List<Map<String, String>> getVariablesForDataTable(List<Map<String, String>> dataTable) {
        List<Map<String, String>> result = new ArrayList<>();
        dataTable.forEach(row -> {
            Map<String, String> retrievedRow = new HashMap<>();
            row.forEach((k,v) -> retrievedRow.put(k, Objects.isNull(v)? null: getValueFromVariable(v)));
            result.add(retrievedRow);
        });
        return result;
    }

    public void clear() {
        log.info("Clear variables in thread: {}", currentThread().threadId());
        this.getScenarioVariables().clear();
    }
}

