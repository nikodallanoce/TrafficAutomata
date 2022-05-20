package engine.rules;

import engine.roads.Road;

public abstract class RulesCross implements RulesSet {
    @Override
    public abstract void apply(Road road);
}
