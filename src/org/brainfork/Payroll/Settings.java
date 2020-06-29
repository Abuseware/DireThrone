package org.brainfork.Payroll;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Settings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner RetirementTax;
    private JSpinner AnnuityTax;
    private JSpinner SicknessTax;
    private JSpinner MedicalTax;
    private JSpinner MedicalTaxRefund;
    private JSpinner ObtainingCost;
    private JSpinner TaxRelief;
    private JSpinner Tax;
    private JSpinner RepairBounty;
    private JSpinner PppBounty;

    private static final ConfigStorageInterface config = JsonConfig.getInstance();

    Settings() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        RetirementTax.setModel(getPercentSpinner());
        AnnuityTax.setModel(getPercentSpinner());
        SicknessTax.setModel(getPercentSpinner());
        MedicalTax.setModel(getPercentSpinner());
        MedicalTaxRefund.setModel(getPercentSpinner());
        ObtainingCost.setModel(getDoubleSpinner());
        TaxRelief.setModel(getDoubleSpinner());
        Tax.setModel(getPercentSpinner());
        RepairBounty.setModel(getDoubleSpinner());
        PppBounty.setModel(getDoubleSpinner());

        RetirementTax.setValue(config.getRetirementTax().multiply(BigDecimal.valueOf(100)));
        AnnuityTax.setValue(config.getAnnuityTax().multiply(BigDecimal.valueOf(100)));
        SicknessTax.setValue(config.getSicknessTax().multiply(BigDecimal.valueOf(100)));
        MedicalTax.setValue(config.getHealthTax().multiply(BigDecimal.valueOf(100)));
        MedicalTaxRefund.setValue(config.getHealthRefund().multiply(BigDecimal.valueOf(100)));
        ObtainingCost.setValue(config.getObtainingCost());
        TaxRelief.setValue(config.getTaxRelief());
        Tax.setValue(config.getTax().multiply(BigDecimal.valueOf(100)));
        RepairBounty.setValue(config.getRepairBounty());
        PppBounty.setValue(config.getPPPBounty());


        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private SpinnerNumberModel getPercentSpinner() {
        return new SpinnerNumberModel(0.0, 0.0, 100.0, 0.01);
    }

    private SpinnerNumberModel getDoubleSpinner() {
        return new SpinnerNumberModel(0.0, 0.0, null, 0.01);
    }

    private void onOK() {
        config.setRetirementTax(new BigDecimal(RetirementTax.getValue().toString()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        config.setAnnuityTax(new BigDecimal(AnnuityTax.getValue().toString()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        config.setSicknessTax(new BigDecimal(SicknessTax.getValue().toString()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        config.setHealthTax(new BigDecimal(MedicalTax.getValue().toString()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        config.setHealthRefund(new BigDecimal(MedicalTaxRefund.getValue().toString()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        config.setObtainingCost(new BigDecimal(ObtainingCost.getValue().toString()));
        config.setTaxRelief(new BigDecimal(TaxRelief.getValue().toString()));
        config.setTax(new BigDecimal(Tax.getValue().toString()).divide(BigDecimal.valueOf(100.0), RoundingMode.DOWN));
        config.setRepairBounty(new BigDecimal(RepairBounty.getValue().toString()));
        config.setPPPBounty(new BigDecimal(PppBounty.getValue().toString()));
        config.save();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
