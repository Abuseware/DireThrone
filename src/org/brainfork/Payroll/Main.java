package org.brainfork.Payroll;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class Main {
    private JComboBox<String> MonthSelector;
    private JTable DayList;
    private JPanel MainWindow;
    private JSpinner BasicIncome;
    private JTextField RetirementTax;
    private JTextField AnnuityTax;
    private JTextField SicknessTax;
    private JTextField MedicalTaxRefund;
    private JTextField MedicalTax;
    private JTextField ObtainingCost;
    private JTextField TaxRelief;
    private JTextField Tax;
    private JTextField Salary;
    private JTextField Bounty;
    private JComboBox<Integer> YearSelector;
    private JTextField Deduction;
    private JTextField Overtime;
    private static final ConfigStorageInterface config = JsonConfig.getInstance();
    private final NumberFormat nf = NumberFormat.getNumberInstance();
    private final String[] TableHeader = new String[]{"#", "Naprawy", "PPP", "Zwr. naprawy", "Zwr. PPP", "L4", "Urlop", "Nadgodz. 50%", "Nadgodz. 100%"};

    public Main() {

        Calendar calendar = Calendar.getInstance();
        BasicIncome.setModel(new SpinnerNumberModel(0.0, 0.0, null, 1.0));
        BasicIncome.setValue(config.getBasicIncome());

        int currentYear = calendar.get(Calendar.YEAR);
        for (int i = currentYear - 3; i <= currentYear; i++) YearSelector.addItem(i);
        YearSelector.setSelectedIndex(3);

        int currentMonth = calendar.get(Calendar.MONTH);
        for (int i = 0; i <= calendar.getMaximum(Calendar.MONTH); i++) {
            calendar.set(Calendar.MONTH, i);
            MonthSelector.addItem(String.format("%02d. %s", i + 1, calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())));
        }
        MonthSelector.setSelectedIndex(currentMonth);

        updateDayList(currentYear, currentMonth);
        updatePayout();

        Bounty.addActionListener(e -> updatePayout());
        MonthSelector.addActionListener(e -> {
            updateDayList((int) YearSelector.getSelectedItem(), MonthSelector.getSelectedIndex());
            updatePayout();
        });
        YearSelector.addActionListener(e -> {
            updateDayList((int) YearSelector.getSelectedItem(), MonthSelector.getSelectedIndex());
            updatePayout();
        });
        BasicIncome.addChangeListener(e -> {
            config.setBasicIncome(new BigDecimal(BasicIncome.getValue().toString()));
            updatePayout();
        });
        DayList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    DayList.editCellAt(DayList.getSelectedRow(), DayList.getSelectedColumn());
                    e.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Kalkulator wypłaty");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Main main = new Main();
        JFrame frame = new JFrame("Kalkulator wypłaty");
        frame.setContentPane(main.MainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setIconImage(new ImageIcon(Main.class.getResource("/icon.png")).getImage());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                config.save();
                super.windowClosing(e);
            }
        });

        /*if(System.getProperty("os.name").startsWith("Mac")) {
            Desktop desktop = Desktop.getDesktop();
            desktop.setAboutHandler(e -> showAboutWindow(frame));
            desktop.setPreferencesHandler(e -> showSettingsWindow(main));
            desktop.setQuitHandler((e, r) -> System.exit(0));
        } else */
        {
            JMenuItem settingsMenuItem = new JMenuItem("Ustawienia", KeyEvent.VK_U);
            settingsMenuItem.addActionListener(e -> showSettingsWindow(main));

            JMenu mainMenu = new JMenu("Plik");
            mainMenu.setMnemonic(KeyEvent.VK_P);
            mainMenu.add(settingsMenuItem);

            JMenuItem aboutMenuItem = new JMenuItem("O programie...", KeyEvent.VK_O);
            aboutMenuItem.addActionListener(e -> showAboutWindow(frame));

            JMenu helpMenu = new JMenu("Pomoc");
            helpMenu.setMnemonic(KeyEvent.VK_O);
            helpMenu.add(aboutMenuItem);


            JMenuBar menuBar = new JMenuBar();
            menuBar.add(mainMenu);
            menuBar.add(helpMenu);

            frame.setJMenuBar(menuBar);

        }

        frame.pack();
        frame.setVisible(true);
    }

    private static void showSettingsWindow(Main parent) {
        Settings settings = new Settings();
        settings.pack();
        settings.setResizable(false);
        settings.setLocationRelativeTo(null);
        settings.setVisible(true);
        settings.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.updatePayout();
                super.windowClosed(e);
            }
        });
    }

    private static void showAboutWindow(JFrame parent) {
        JOptionPane.showMessageDialog(
                parent,
                new JLabel(
                        "<html>"
                                + "<h1>Payout calculator</h1>"
                                + "<p style=\"text-align: center;\"><b>© 2019-2020 Artur \"Licho\" Kaleta</b></center><p/>"
                                + "<p style=\"text-align: center;\"><a href=\"https://brainfork.org/\">https://brainfork.org</a></p>"
                                + "</html>"
                ),
                "O programie",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void updatePayout() {
        MainController controller = new MainController();
        int repairs = 0;
        int ppps = 0;
        int returnedRepairs = 0;
        int returnedPpps = 0;
        int daysSick = 0;
        int daysLeave = 0;
        BigDecimal overtime50 = BigDecimal.ZERO;
        BigDecimal overtime100 = BigDecimal.ZERO;

        for (int i = 0; i < DayList.getRowCount(); i++) {
            repairs += (int) DayList.getValueAt(i, 1);
            ppps += (int) DayList.getValueAt(i, 2);
            returnedRepairs += (int) DayList.getValueAt(i, 3);
            returnedPpps += (int) DayList.getValueAt(i, 4);
            if ((boolean) DayList.getValueAt(i, 5)) daysSick++;
            if ((boolean) DayList.getValueAt(i, 6)) daysLeave++;
            overtime50 = overtime50.add(new BigDecimal(DayList.getValueAt(i, 7).toString()));
            overtime100 = overtime100.add(new BigDecimal(DayList.getValueAt(i, 8).toString()));
        }

        BigDecimal bounty = controller.calculateBounty(repairs, ppps, returnedRepairs, returnedPpps);
        BigDecimal overtime = controller.calculateOvertimeBonus(overtime50, overtime100);
        BigDecimal deduction = controller.calculateDeduction(bounty, daysSick, daysLeave);
        Bounty.setText(nf.format(bounty));
        Overtime.setText(nf.format(overtime));
        Deduction.setText(nf.format(deduction));

        BigDecimal[] payout = controller.calculatePayout(overtime, bounty, deduction);

        RetirementTax.setText(nf.format(payout[MainController.RETIREMENT_TAX]));
        AnnuityTax.setText(nf.format(payout[MainController.ANNUITY_TAX]));
        SicknessTax.setText(nf.format(payout[MainController.SICKNESS_TAX]));
        MedicalTaxRefund.setText(nf.format(payout[MainController.MEDICAL_REFUND]));
        MedicalTax.setText(nf.format(payout[MainController.MEDICAL_TAX]));
        ObtainingCost.setText(nf.format(config.getObtainingCost()));
        TaxRelief.setText(nf.format(config.getTaxRelief()));
        Tax.setText(nf.format(payout[MainController.TAX]));
        Salary.setText(nf.format(payout[MainController.SALARY]));

    }

    private void updateDayList(int year, int month) {
        DataStorageInterface storage = new JsonStorage();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        DayListTableModel model = new DayListTableModel();
        for(String name : TableHeader) model.addColumn(name);

        Map<Integer, StorageData> results = storage.getMonth(year, month + 1);

        for (int i = 1; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1; i++) {
            Vector<Object> row = new Vector<>();
            StorageData data = results.get(i);
            row.add(String.format("%02d", i));
            row.add(data != null ? data.repairs : 0);
            row.add(data != null ? data.ppp : 0);
            row.add(data != null ? data.repairsReturned : 0);
            row.add(data != null ? data.pppReturned : 0);
            row.add(data != null && data.sickness);
            row.add(data != null && data.leave);
            row.add(data != null ? data.overtime50 : BigDecimal.ZERO);
            row.add(data != null ? data.overtime100 : BigDecimal.ZERO);
            model.addRow(row);
        }
        model.addTableModelListener(e -> {
            updatePayout();
            @SuppressWarnings("unchecked")
            Vector<Object> row = (Vector<Object>) model.getDataVector().elementAt(e.getFirstRow());
            StorageData data = new StorageData();
            data.repairs = (int) row.elementAt(1);
            data.ppp = (int) row.elementAt(2);
            data.repairsReturned = (int) row.elementAt(3);
            data.pppReturned = (int) row.elementAt(4);
            data.sickness = (boolean) row.elementAt(5);
            data.leave = (boolean) row.elementAt(6);
            data.overtime50 = new BigDecimal(row.elementAt(7).toString());
            data.overtime100 = new BigDecimal(row.elementAt(8).toString());
            storage.setDay(
                    (int) YearSelector.getSelectedItem(),
                    MonthSelector.getSelectedIndex() + 1,
                    Integer.parseInt((String) row.elementAt(0)),
                    data
            );
        });

        DayList.setModel(model);

        TableColumn column = DayList.getColumnModel().getColumn(0);
        column.setPreferredWidth(32);
        column.setMaxWidth(32);
        column.setResizable(false);
        column = DayList.getColumnModel().getColumn(5);
        column.setPreferredWidth(32);
        column.setMaxWidth(32);
        column.setResizable(false);
        column = DayList.getColumnModel().getColumn(6);
        column.setPreferredWidth(48);
        column.setMaxWidth(48);
        column.setResizable(false);

    }
}
