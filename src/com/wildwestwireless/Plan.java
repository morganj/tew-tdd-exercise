package com.wildwestwireless;

public class Plan {
    private final PlanType planType;
    private final int numberOfLines;

    public Plan(PlanType planType, int numberOfLines) {
        this.planType = planType;
        this.numberOfLines = numberOfLines;
    }

    public PlanType getType() {
        return this.planType;
    }

    public int getNumberOfLines() {
        return this.numberOfLines;
    }
}
