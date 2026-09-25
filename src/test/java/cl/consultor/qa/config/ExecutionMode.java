package cl.consultor.qa.config;

public enum ExecutionMode {
    LOCAL;

    static ExecutionMode parse(String value) {
        if ("remote".equalsIgnoreCase(value.trim()))
            throw new ConfigurationException("Remote execution is reserved for a future phase and is not implemented yet. Use executionMode=local.");
        if (!"local".equalsIgnoreCase(value.trim()))
            throw new ConfigurationException("Unsupported execution mode: " + value + ". Supported execution mode: local.");
        return LOCAL;
    }
}
