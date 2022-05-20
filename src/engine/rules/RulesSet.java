package engine.rules;

import engine.roads.Road;

@FunctionalInterface
public interface RulesSet {
    void apply(Road road);
}
