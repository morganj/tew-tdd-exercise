package com.wildwestwireless;

public class Account {
    private double minutesUsed;
    private Plan plan;

    public Bill getMonthlyBill() {
        Bill bill = new Bill();
        bill.setPlan(plan);
        bill.setMinutesUsed(minutesUsed);
        return bill;
    }

    public void setMinutesUsed(double minutesUsed) {
        this.minutesUsed = minutesUsed;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
