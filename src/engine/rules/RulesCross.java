package engine.rules;

import engine.roads.Road;
import engine.roads.YCross;

public abstract class RulesCross implements RulesSet<YCross> {
    @Override
    public abstract void apply(YCross road);
}
