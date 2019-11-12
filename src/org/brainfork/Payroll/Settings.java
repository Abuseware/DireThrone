package org.brainfork.Payroll;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        RetirementTax.setValue(Config.getRetirementTax() * 100);
        AnnuityTax.setValue(Config.getAnnuityTax() * 100);
        SicknessTax.setValue(Config.getSicknessTax() * 100);
        MedicalTax.setValue(Config.getHealthTax() * 100);
        MedicalTaxRefund.setValue(Config.getHealthRefund() * 100);
        ObtainingCost.setValue(Config.getObtainingCost());
        TaxRelief.setValue(Config.getTaxRelief());
        Tax.setValue(Config.getTax() * 100);
        RepairBounty.setValue(Config.getRepairBounty());
        PppBounty.setValue(Config.getPPPBounty());


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
        Config.setRetirementTax((double) RetirementTax.getValue() / 100.0);
        Config.setAnnuityTax((double) AnnuityTax.getValue() / 100.0);
        Config.setSicknessTax((double) SicknessTax.getValue() / 100.0);
        Config.setHealthTax((double) MedicalTax.getValue() / 100.0);
        Config.setHealthRefund((double) MedicalTaxRefund.getValue() / 100.0);
        Config.setObtainingCost((double) ObtainingCost.getValue());
        Config.setTaxRelief((double) TaxRelief.getValue());
        Config.setTax((double) Tax.getValue() / 100.0);
        Config.setRepairBounty((double) RepairBounty.getValue());
        Config.setPPPBounty((double) PppBounty.getValue());
        Config.save();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
