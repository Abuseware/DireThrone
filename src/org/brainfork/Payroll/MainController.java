package org.brainfork.Payroll;

class MainController {

    static final short RETIREMENT_TAX = 0;
    static final short ANNUITY_TAX = 1;
    static final short SICKNESS_TAX = 2;
    static final short MEDICAL_REFUND = 3;
    static final short MEDICAL_TAX = 4;
    static final short TAX = 5;
    static final short SALARY = 6;

    static double calculateBounty(int repairs, int ppps, int returnedRepairs, int returnedPpps) {
        return repairs * Config.getRepairBounty()
                + ppps * Config.getPPPBounty()
                - returnedRepairs * Config.getRepairBounty()
                - returnedPpps * Config.getPPPBounty();
    }

    static double calculateOvertimeBonus(double overtime50, double overtime100) {
        return Math.round(
                (
                        (Config.getBasicIncome() / (21 * 8)) * overtime50 / 2
                                + (Config.getBasicIncome() / (21 * 8)) * overtime100
                ) * 100.0
        ) / 100.0;
    }

    static double calculateDeduction(double bounty, int daysSick, int daysLeave) {
        return -(daysSick * (Math.round(Config.getBasicIncome() / 21 * 100.0) / 100.0 * 0.2))
                + (daysLeave * (Math.round(bounty / 21 * 100.0) / 100.0));
    }

    static double[] calculatePayout(double overtime, double bounty, double deduction) {
        double[] payments = new double[7];

        final double income = Config.getBasicIncome() + overtime + bounty + deduction;

        final double retirement = Math.round((income * Config.getRetirementTax()) * 100.0) / 100.0;
        final double annuity = Math.round((income * Config.getAnnuityTax()) * 100.0) / 100.0;
        final double sickness = Math.round((income * Config.getSicknessTax()) * 100.0) / 100.0;

        final double healthBase = Math.round((income - retirement - annuity - sickness) * 100.0) / 100.0;
        final double health = Math.round((healthBase * Config.getHealthTax()) * 100.0) / 100.0;
        final double healthRefund = Math.round((healthBase * Config.getHealthRefund()) * 100.0) / 100.0;
        final double healthTax = health - healthRefund;

        final double taxBase = Math.round(healthBase - Config.getObtainingCost());
        final double tax = taxBase * Config.getTax();
        final double taxFinal = Math.round(tax - (healthRefund + Config.getTaxRelief()));
        final double salary = healthBase - tax;

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
