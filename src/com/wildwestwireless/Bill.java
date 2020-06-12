package com.wildwestwireless;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.wildwestwireless.PlanType.GOLD;
import static com.wildwestwireless.PlanType.SILVER;
import static java.math.BigDecimal.valueOf;

public class Bill {
    private static final double FAMILY_DISCOUNT_RATE = 5;
    private static final int FAMILY_DISCOUNT_THRESHOLD = 3;
    private Plan plan;
    private Double minutesUsed;
    private static final Map<PlanType, Double> planToMonthlyRate = new HashMap<PlanType, Double>(){{
        put(GOLD, 49.95);
        put(SILVER, 29.95);
    }};

    private static final Map<PlanType, Double> planToRatePerExcessMinute = new HashMap<PlanType, Double>(){{
        put(GOLD, 0.45);
        put(SILVER, 0.54);
    }};

    private static final Map<PlanType, Double> planToCostPerAdditionalLine = new HashMap<PlanType, Double>(){{
        put(GOLD, 14.5);
        put(SILVER, 21.5);
    }};

    private static final Map<PlanType, Integer> planToIncludedMinutes = new HashMap<PlanType, Integer>(){{
        put(GOLD, 1000);
        put(SILVER, 500);
    }};

    private BigDecimal calculateAdditionalLinesCharge(Plan plan){
        PlanType type = plan.getType();
        int numberOfLines = plan.getNumberOfLines();
        BigDecimal familyDiscountLines = valueOf(numberOfLines > FAMILY_DISCOUNT_THRESHOLD ? numberOfLines - FAMILY_DISCOUNT_THRESHOLD : 0);
        BigDecimal additionalLines = valueOf(numberOfLines - familyDiscountLines.intValue() - 1);
        BigDecimal costPerAddtionalLine = valueOf(planToCostPerAdditionalLine.get(type));
        return costPerAddtionalLine.multiply(additionalLines).add(familyDiscountLines.multiply(valueOf(FAMILY_DISCOUNT_RATE)));
    }

    private BigDecimal calculateExtraCharges(Plan plan){
        PlanType type = plan.getType();
        BigDecimal additionalLinesCharge = calculateAdditionalLinesCharge(plan);
        BigDecimal includedMinutes = valueOf(planToIncludedMinutes.get(type));
        BigDecimal excessMinutes = valueOf(minutesUsed).subtract(includedMinutes);
        BigDecimal excessMinuteCharge = (excessMinutes.doubleValue() > 0 ? excessMinutes : BigDecimal.ZERO).multiply(valueOf(planToRatePerExcessMinute.get(type)));
        return additionalLinesCharge.add(excessMinuteCharge);
    }

    public double calculateTotal(){
        Plan plan = this.plan;
        PlanType type = plan.getType();
        BigDecimal monthlyRate = valueOf(planToMonthlyRate.get(type));
        BigDecimal extraCharges = calculateExtraCharges(plan);
        return monthlyRate.add(extraCharges).doubleValue();
    }

    public void setMinutesUsed(Double minutesUsed) {
        this.minutesUsed = minutesUsed;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
