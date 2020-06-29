package org.brainfork.Payroll;

import java.math.BigDecimal;

@SuppressWarnings("WeakerAccess")
class ConfigData {
    public BigDecimal basicIncome = new BigDecimal(2600);
    public BigDecimal retirementTax = new BigDecimal("0.0976");
    public BigDecimal annuityTax = new BigDecimal("0.015");
    public BigDecimal sicknessTax = new BigDecimal("0.0245");
    public BigDecimal healthTax = new BigDecimal("0.09");
    public BigDecimal healthRefund = new BigDecimal("0.0775");
    public BigDecimal obtainingCost = new BigDecimal(300);
    public BigDecimal taxRelief = new BigDecimal("43.76");
    public BigDecimal tax = new BigDecimal("0.17");
    public BigDecimal RepairBounty = new BigDecimal("4.7");
    public BigDecimal PPPBounty = new BigDecimal("2.35");
}
