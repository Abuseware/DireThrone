package org.brainfork.Payroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

class JsonConfig implements ConfigStorageInterface {

    private static JsonConfig config = null;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ConfigData data;

    private JsonConfig() {
        try {
            data = objectMapper.readValue(new File("config.json"), ConfigData.class);
        } catch (IOException e) {
            System.err.println("Nie można załadować konfiguracji: " + e.getLocalizedMessage());
            data = new ConfigData();
        }
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public synchronized static JsonConfig getInstance() {
        if (config == null) config = new JsonConfig();
        return config;
    }

    public BigDecimal getBasicIncome() {
        return data.basicIncome;
    }

    public void setBasicIncome(BigDecimal basicIncome) {
        data.basicIncome = basicIncome;
    }

    public BigDecimal getRetirementTax() {
        return data.retirementTax;
    }

    public void setRetirementTax(BigDecimal retirementTax) {
        data.retirementTax = retirementTax;
    }

    public BigDecimal getAnnuityTax() {
        return data.annuityTax;
    }

    public void setAnnuityTax(BigDecimal annuityTax) {
        data.annuityTax = annuityTax;
    }

    public BigDecimal getSicknessTax() {
        return data.sicknessTax;
    }

    public void setSicknessTax(BigDecimal sicknessTax) {
        data.sicknessTax = sicknessTax;
    }

    public BigDecimal getHealthTax() {
        return data.healthTax;
    }

    public void setHealthTax(BigDecimal healthTax) {
        data.healthTax = healthTax;
    }

    public BigDecimal getHealthRefund() {
        return data.healthRefund;
    }

    public void setHealthRefund(BigDecimal healthRefund) {
        data.healthRefund = healthRefund;
    }

    public BigDecimal getObtainingCost() {
        return data.obtainingCost;
    }

    public void setObtainingCost(BigDecimal obtainingCost) {
        data.obtainingCost = obtainingCost;
    }

    public BigDecimal getTaxRelief() {
        return data.taxRelief;
    }

    public void setTaxRelief(BigDecimal taxRelief) {
        data.taxRelief = taxRelief;
    }

    public BigDecimal getTax() {
        return data.tax;
    }

    public void setTax(BigDecimal tax) {
        data.tax = tax;
    }

    public BigDecimal getRepairBounty() {
        return data.RepairBounty;
    }

    public void setRepairBounty(BigDecimal repairBounty) {
        data.RepairBounty = repairBounty;
    }

    public BigDecimal getPPPBounty() {
        return data.PPPBounty;
    }

    public void setPPPBounty(BigDecimal PPPBounty) {
        data.PPPBounty = PPPBounty;
    }

    public boolean save() {
        try {
            objectMapper.writeValue(new File("config.json"), data);
        } catch (IOException e) {
            System.err.println("Nie można zapisać konfiguracji: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
