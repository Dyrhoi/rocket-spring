package github.dyrhoi.rocket.util;

public class Environment {
    public static final boolean isDeployed = (System.getenv("DEPLOYED") != null);
}
