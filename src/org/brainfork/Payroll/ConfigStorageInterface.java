package org.brainfork.Payroll;

import java.math.BigDecimal;

public interface ConfigStorageInterface {
    BigDecimal getBasicIncome();

    void setBasicIncome(BigDecimal basicIncome);

    BigDecimal getRetirementTax();

    void setRetirementTax(BigDecimal retirementTax);

    BigDecimal getAnnuityTax();

    void setAnnuityTax(BigDecimal annuityTax);

    BigDecimal getSicknessTax();

    void setSicknessTax(BigDecimal sicknessTax);

    BigDecimal getHealthTax();

    void setHealthTax(BigDecimal healthTax);

    BigDecimal getHealthRefund();

    void setHealthRefund(BigDecimal healthRefund);

    BigDecimal getObtainingCost();

    void setObtainingCost(BigDecimal obtainingCost);

    BigDecimal getTaxRelief();

    void setTaxRelief(BigDecimal taxRelief);

    BigDecimal getTax();

    void setTax(BigDecimal tax);

    BigDecimal getRepairBounty();

    void setRepairBounty(BigDecimal repairBounty);

    BigDecimal getPPPBounty();

    void setPPPBounty(BigDecimal PPPBounty);

    boolean save();

}
