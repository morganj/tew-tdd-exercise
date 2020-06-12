package com.wildwestwireless;

public enum PlanType {
    GOLD("Gold", 49.95, 0.45, 14.5, 1000 ),
    SILVER("Silver", 29.95, 0.54, 21.5, 500);

    public final String label;
    public final double baseRate;
    public final double excessMinuteRate;
    public final double additionalLineRate;
    public final double includedMinutes;

    PlanType(String label, double baseRate, double excessMinuteRate, double additionalLineRate, double includedMinutes) {
        this.label = label;
        this.baseRate = baseRate;
        this.excessMinuteRate = excessMinuteRate;
        this.additionalLineRate = additionalLineRate;
        this.includedMinutes = includedMinutes;
    }
}

