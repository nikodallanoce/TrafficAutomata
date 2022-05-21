package engine.rules;

import engine.roads.Road;

@FunctionalInterface
public interface RulesSet<T extends Road> {
    void apply(T road);
}
