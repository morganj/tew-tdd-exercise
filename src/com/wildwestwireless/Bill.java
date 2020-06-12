package com.wildwestwireless;

import java.math.BigDecimal;
import static java.math.BigDecimal.valueOf;

public class Bill {
    protected static final double FAMILY_DISCOUNT_RATE = 5;
    protected static final int FAMILY_DISCOUNT_THRESHOLD = 3;
    private Plan plan;
    private Double minutesUsed;

    private BigDecimal calculateAdditionalLinesCharge(Plan plan){
        PlanType type = plan.getType();
        int numberOfLines = plan.getNumberOfLines();
        BigDecimal familyDiscountLines = valueOf(numberOfLines > FAMILY_DISCOUNT_THRESHOLD ? numberOfLines - FAMILY_DISCOUNT_THRESHOLD : 0);
        BigDecimal additionalLines = valueOf(numberOfLines - familyDiscountLines.intValue() - 1);
        BigDecimal costPerAddtionalLine = valueOf(type.additionalLineRate);
        return costPerAddtionalLine.multiply(additionalLines).add(familyDiscountLines.multiply(valueOf(FAMILY_DISCOUNT_RATE)));
    }

    private BigDecimal calculateExtraCharges(Plan plan){
        PlanType type = plan.getType();
        BigDecimal additionalLinesCharge = calculateAdditionalLinesCharge(plan);
        BigDecimal includedMinutes = valueOf(type.includedMinutes);
        BigDecimal excessMinutes = valueOf(minutesUsed).subtract(includedMinutes);
        BigDecimal excessMinuteCharge = (excessMinutes.doubleValue() > 0 ? excessMinutes : BigDecimal.ZERO).multiply(valueOf(type.excessMinuteRate));
        return additionalLinesCharge.add(excessMinuteCharge);
    }

    public double calculateTotal(){
        Plan plan = this.plan;
        PlanType type = plan.getType();
        BigDecimal monthlyRate = valueOf(type.baseRate);
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
