package org.brainfork.Payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;

class MainController {

    static final short RETIREMENT_TAX = 0;
    static final short ANNUITY_TAX = 1;
    static final short SICKNESS_TAX = 2;
    static final short MEDICAL_REFUND = 3;
    static final short MEDICAL_TAX = 4;
    static final short TAX = 5;
    static final short SALARY = 6;

    private final ConfigStorageInterface config = JsonConfig.getInstance();

    BigDecimal calculateBounty(int repairs, int ppps, int returnedRepairs, int returnedPpps) {
        BigDecimal ret;
        ret = config.getRepairBounty().multiply(BigDecimal.valueOf(repairs));
        ret = ret.add(config.getPPPBounty().multiply(BigDecimal.valueOf(ppps)));
        ret = ret.add(config.getRepairBounty().multiply(BigDecimal.valueOf(returnedRepairs)));
        ret = ret.add(config.getPPPBounty().multiply(BigDecimal.valueOf(returnedPpps)));
        return ret;
    }

    BigDecimal calculateOvertimeBonus(BigDecimal overtime50, BigDecimal overtime100) {
        BigDecimal ov50 = config.getBasicIncome().divide(BigDecimal.valueOf(21 * 8), RoundingMode.DOWN);
        BigDecimal ov100 = ov50.multiply(overtime100);
        ov50 = ov50.multiply(overtime50.divide(BigDecimal.valueOf(2), RoundingMode.DOWN));
        return (ov50.add(ov100));
    }

    BigDecimal calculateDeduction(BigDecimal bounty, int daysSick, int daysLeave) {
        /*return -(daysSick * (Math.round(config.getBasicIncome() / 21 * 100.0) / 100.0 * 0.2))
                + (daysLeave * (Math.round(bounty / 21 * 100.0) / 100.0));*/
        BigDecimal sick = BigDecimal.valueOf(daysSick);
        sick = sick.multiply(config.getBasicIncome().divide(BigDecimal.valueOf(21), RoundingMode.DOWN));
        sick = sick.multiply(new BigDecimal("0.2"));
        sick = sick.negate();
        BigDecimal leave = BigDecimal.valueOf(daysLeave);
        leave = leave.multiply(bounty.divide(BigDecimal.valueOf(21), RoundingMode.DOWN));
        return sick.add(leave);
    }

    BigDecimal[] calculatePayout(BigDecimal overtime, BigDecimal bounty, BigDecimal deduction) {
        BigDecimal[] payments = new BigDecimal[7];

        final BigDecimal income = config.getBasicIncome().add(overtime).add(bounty).add(deduction);

        final BigDecimal retirement = income.multiply(config.getRetirementTax()).setScale(2, RoundingMode.HALF_DOWN); //Math.round((income * config.getRetirementTax()) * 100.0) / 100.0;
        final BigDecimal annuity = income.multiply(config.getAnnuityTax()).setScale(2, RoundingMode.HALF_DOWN); //Math.round((income * config.getAnnuityTax()) * 100.0) / 100.0;
        final BigDecimal sickness = income.multiply(config.getSicknessTax()).setScale(2, RoundingMode.HALF_DOWN); //Math.round((income * config.getSicknessTax()) * 100.0) / 100.0;

        final BigDecimal healthBase = income.subtract(retirement).subtract(annuity).subtract(sickness).setScale(2, RoundingMode.HALF_DOWN); //Math.round((income - retirement - annuity - sickness) * 100.0) / 100.0;
        final BigDecimal health = healthBase.multiply(config.getHealthTax()).setScale(2, RoundingMode.HALF_DOWN); //Math.round((healthBase * config.getHealthTax()) * 100.0) / 100.0;
        final BigDecimal healthRefund = healthBase.multiply(config.getHealthRefund()).setScale(2, RoundingMode.HALF_DOWN); //Math.round((healthBase * config.getHealthRefund()) * 100.0) / 100.0;
        final BigDecimal healthTax = health.subtract(healthRefund).setScale(2, RoundingMode.HALF_DOWN); //health - healthRefund;

        final BigDecimal taxBase = healthBase.subtract(config.getObtainingCost()).setScale(2, RoundingMode.HALF_DOWN); //Math.round(healthBase - config.getObtainingCost());
        final BigDecimal tax = taxBase.multiply(config.getTax()).setScale(2, RoundingMode.HALF_DOWN); //taxBase * config.getTax();
        final BigDecimal taxFinal = tax.subtract(healthRefund.add(config.getTaxRelief())).setScale(2, RoundingMode.HALF_DOWN); //Math.round(tax - (healthRefund + config.getTaxRelief()));
        final BigDecimal salary = healthBase.subtract(tax).setScale(2, RoundingMode.HALF_DOWN); //healthBase - tax;

        payments[RETIREMENT_TAX] = retirement;
        payments[ANNUITY_TAX] = annuity;
        payments[SICKNESS_TAX] = sickness;
        payments[MEDICAL_REFUND] = healthRefund;
        payments[MEDICAL_TAX] = healthTax;
        payments[TAX] = taxFinal;
        payments[SALARY] = salary;

        return payments;
    }
}
