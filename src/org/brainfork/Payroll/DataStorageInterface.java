package org.brainfork.Payroll;

import java.util.Map;

public interface DataStorageInterface {
    Map<Integer, StorageData> getMonth(int year, int month);

    StorageData getDay(int year, int month, int day);

    void setDay(int year, int month, int day, StorageData data);
}
