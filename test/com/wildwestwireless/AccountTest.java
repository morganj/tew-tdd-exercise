package com.wildwestwireless;

import static com.wildwestwireless.PlanType.GOLD;
import static com.wildwestwireless.PlanType.SILVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.Collection;
import java.util.stream.Collectors;
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
    Collection<DynamicTest> assertBasicMonthlyBillForAccountWithPlan() {
        return Stream.of(
            new TestCase("Gold plan with 1 line", 49.95, GOLD, 1),
            new TestCase("Gold plan with 2 lines", 64.45, GOLD, 2),
            new TestCase("Silver plan with 1 line", 29.95, SILVER, 1),
            new TestCase("Silver plan with 3 lines", 72.95, SILVER, 3),
            new TestCase("Gold plan, minutes threshold - 1", 49.95, GOLD, 1, 999),
            new TestCase("Gold plan, at minutes threshold", 49.95, GOLD, 1, 1000),
            new TestCase("Gold plan, minutes threshold + 1", 50.40, GOLD, 1, 1001),
            new TestCase("Gold plan, minutes threshold + N", 54.45, GOLD, 1, 1010),
            new TestCase("Silver plan, minutes threshold - 1", 29.95, SILVER, 1, 499),
            new TestCase("Silver plan, at minutes threshold", 29.95, SILVER, 1, 500),
            new TestCase("Silver plan, minutes threshold + N", 40.75, SILVER, 1, 520),
            new TestCase("Gold plan, family discount threshold - 1", 64.45, GOLD, 2),
            new TestCase("Gold plan, at family discount threshold", 78.95, GOLD, 3),
            new TestCase("Gold plan, family discount threshold + 1", 83.95, GOLD, 4),
            new TestCase("Silver plan, family discount threshold + 1", 77.95, SILVER, 4),
            new TestCase("Silver plan, family discount threshold + N", 82.95, SILVER, 5)
        )
        .map(e -> dynamicTest(e.getTestDescription(), () -> assertEquals(e.getExpected(), getBillTotalForTestCase(e))))
        .collect(Collectors.toList());
    }
}
