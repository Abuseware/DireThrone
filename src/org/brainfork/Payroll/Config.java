package org.brainfork.Payroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

class Config {

    private static ConfigData data;
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            data = objectMapper.readValue(new File("config.json"), ConfigData.class);
        } catch (IOException e) {
            System.err.println("Nie można załadować konfiguracji: " + e.getLocalizedMessage());
            data = new ConfigData();
        }
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    static double getBasicIncome() {
        return data.basicIncome;
    }

    static void setBasicIncome(double basicIncome) {
        data.basicIncome = basicIncome;
    }

    static double getRetirementTax() {
        return data.retirementTax;
    }

    static void setRetirementTax(double retirementTax) {
        data.retirementTax = retirementTax;
    }

    static double getAnnuityTax() {
        return data.annuityTax;
    }

    static void setAnnuityTax(double annuityTax) {
        data.annuityTax = annuityTax;
    }

    static double getSicknessTax() {
        return data.sicknessTax;
    }

    static void setSicknessTax(double sicknessTax) {
        data.sicknessTax = sicknessTax;
    }

    static double getHealthTax() {
        return data.healthTax;
    }

    static void setHealthTax(double healthTax) {
        data.healthTax = healthTax;
    }

    static double getHealthRefund() {
        return data.healthRefund;
    }

    static void setHealthRefund(double healthRefund) {
        data.healthRefund = healthRefund;
    }

    static double getObtainingCost() {
        return data.obtainingCost;
    }

    static void setObtainingCost(double obtainingCost) {
        data.obtainingCost = obtainingCost;
    }

    static double getTaxRelief() {
        return data.taxRelief;
    }

    static void setTaxRelief(double taxRelief) {
        data.taxRelief = taxRelief;
    }

    static double getTax() {
        return data.tax;
    }

    static void setTax(double tax) {
        data.tax = tax;
    }

    static double getRepairBounty() {
        return data.RepairBounty;
    }

    static void setRepairBounty(double repairBounty) {
        data.RepairBounty = repairBounty;
    }

    static double getPPPBounty() {
        return data.PPPBounty;
    }

    static void setPPPBounty(double PPPBounty) {
        data.PPPBounty = PPPBounty;
    }

    static boolean save() {
        try {
            objectMapper.writeValue(new File("config.json"), data);
        } catch (IOException e) {
            System.err.println("Nie można zapisać konfiguracji: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
