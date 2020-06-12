package com.wildwestwireless;

import static com.wildwestwireless.PlanType.GOLD;
import static com.wildwestwireless.PlanType.SILVER;
import static com.wildwestwireless.Bill.FAMILY_DISCOUNT_THRESHOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.stream.Stream;

class TestCase {
    private final PlanType planType;
    private final int numberOfLines;
    private final double minutesUsed;
    private final String testDescription;
    private final double expected;

    TestCase(String testDescription, double expected, PlanType planType, int numberOfLines, double minutesUsed){
        this.testDescription = testDescription;
        this.expected = expected;
        this.planType = planType;
        this.numberOfLines = numberOfLines;
        this.minutesUsed = minutesUsed;
    }

    TestCase(String testDescription, double expected, PlanType planType, int numberOfLines){
        this(testDescription, expected, planType, numberOfLines, 0);
    }

    PlanType getPlanType() { return planType; }
    int getNumberOfLines() { return numberOfLines; }
    double getMinutesUsed() { return minutesUsed; }
    String getTestDescription() { return testDescription; }
    double getExpected() { return expected; }
}

public class AccountTest {
    private Double getBillTotalForTestCase(TestCase testCase){
        PlanType planType =testCase.getPlanType();
        int numberOfLines = testCase.getNumberOfLines();
        double minutesUsed = testCase.getMinutesUsed();
        Plan plan = new Plan(planType, numberOfLines);
        Account account = new Account();
        account.setPlan(plan);
        account.setMinutesUsed(minutesUsed);
        return account.getMonthlyBill().calculateTotal();
    }

    @TestFactory
    Stream<DynamicTest> assertBasicMonthlyBillForAccountWithPlan() {
        return Stream.of(
            new TestCase("Gold plan with 1 line", GOLD.baseRate, GOLD, 1),
            new TestCase("Gold plan with 2 lines", 64.45, GOLD, 2),
            new TestCase("Silver plan with 1 line", SILVER.baseRate, SILVER, 1),
            new TestCase("Silver plan with 3 lines", 72.95, SILVER, 3),
            new TestCase("Gold plan, minutes threshold - 1", GOLD.baseRate, GOLD, 1, GOLD.includedMinutes - 1),
            new TestCase("Gold plan, at minutes threshold", GOLD.baseRate, GOLD, 1, GOLD.includedMinutes),
            new TestCase("Gold plan, minutes threshold + 1", 50.40, GOLD, 1, GOLD.includedMinutes + 1),
            new TestCase("Gold plan, minutes threshold + N", 54.45, GOLD, 1, GOLD.includedMinutes + 10),
            new TestCase("Silver plan, minutes threshold - 1", SILVER.baseRate, SILVER, 1, SILVER.includedMinutes - 1),
            new TestCase("Silver plan, at minutes threshold", SILVER.baseRate, SILVER, 1, SILVER.includedMinutes),
            new TestCase("Silver plan, minutes threshold + N", 40.75, SILVER, 1, SILVER.includedMinutes + 20),
            new TestCase("Gold plan, family discount threshold - 1", 64.45, GOLD, FAMILY_DISCOUNT_THRESHOLD - 1),
            new TestCase("Gold plan, at family discount threshold", 78.95, GOLD, FAMILY_DISCOUNT_THRESHOLD),
            new TestCase("Gold plan, family discount threshold + 1", 83.95, GOLD, FAMILY_DISCOUNT_THRESHOLD + 1),
            new TestCase("Silver plan, family discount threshold + 1", 77.95, SILVER, FAMILY_DISCOUNT_THRESHOLD + 1),
            new TestCase("Silver plan, family discount threshold + N", 82.95, SILVER, FAMILY_DISCOUNT_THRESHOLD + 2)
        )
        .map(e -> dynamicTest(e.getTestDescription(), () -> assertEquals(e.getExpected(), getBillTotalForTestCase(e))));
    }
}
