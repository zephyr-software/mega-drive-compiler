package compiler;

import java.util.HashMap;
import java.util.Map;

public class Environment {

  private Environment parentEnvironment = null;

  private Map<String, Object> variableMap = new HashMap<String, Object>();

  private Map<String, Object> functionMap = new HashMap<String, Object>();
  private Map<String, Environment> functionEnvironmentMap = new HashMap<String, Environment>();

  public Environment() {}

  public Environment(Environment parentEnvironment) {

    this.parentEnvironment = parentEnvironment;
  }

  public Environment getParentEnvironment() {

    return parentEnvironment;
  }

  public void setVariable(String name, Object value) {
    Environment variableEnvironment = getVariableEnvironment(name);
    if (variableEnvironment == null) {
      variableEnvironment = this;
    }

    variableEnvironment.variableMap.put(name, value);
  }

  public void setParameterAsLocalVariable(String name, Object value) {
    variableMap.put(name, value);
  }

  public Object getVariable(String name) {

    Object variable = variableMap.get(name);
    if (variable == null && parentEnvironment != null) {

      return parentEnvironment.getVariable(name);
    }

    return variable;
  }

  public void setFunction(String name, Object value, Environment environment) {
    functionMap.put(name, value);
    functionEnvironmentMap.put(name, environment);
  }

  public Object getFunction(String name) {

    Object function = functionMap.get(name);
    if (function == null && parentEnvironment != null) {

      return parentEnvironment.getFunction(name);
    }

    return function;
  }

  public Environment getFunctionEnvironment(String name) {

    Environment env = functionEnvironmentMap.get(name);
    if (env == null && parentEnvironment != null) {

      return parentEnvironment.getFunctionEnvironment(name);
    }

    return env;
  }

  @Override
  public String toString() {

    return "^environment [" + parentEnvironment + ", " + variableMap + "]$";
  }

  private Environment getVariableEnvironment(String name) {
    Environment environment = this;

    do {
      Object variable = environment.variableMap.get(name);
      if (variable != null) {

        return environment;
      }

      environment = environment.parentEnvironment;
    } while (environment != null);

    return null;
  }
}
