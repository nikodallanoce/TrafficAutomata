package engine.rules;

import engine.Road;

@FunctionalInterface
public interface RulesSet {
    void apply(Road road);
}
