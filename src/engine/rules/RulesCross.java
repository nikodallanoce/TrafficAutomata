package engine.rules;

import engine.Road;
import engine.rules.RulesSet;

public abstract class RulesCross implements RulesSet {
    @Override
    public abstract void apply(Road road);
}
