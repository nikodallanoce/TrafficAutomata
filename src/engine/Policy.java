package engine;

@FunctionalInterface
public interface Policy {
    public Request apply(Request req);
}
