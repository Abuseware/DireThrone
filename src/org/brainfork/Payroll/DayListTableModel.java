package org.brainfork.Payroll;

import javax.swing.table.DefaultTableModel;

public class DayListTableModel extends DefaultTableModel {
    private int[] lockedColumns = {0};

    @Override
    public boolean isCellEditable(int row, int column){
        for(int i : lockedColumns) if(i == column) return false;
        return super.isCellEditable(row, column);
    }

    public Class getColumnClass(int column) {
        return this.getValueAt(0, column).getClass();
    }
}
