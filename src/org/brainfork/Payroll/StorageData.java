package org.brainfork.Payroll;

import java.math.BigDecimal;

@SuppressWarnings("WeakerAccess")
class StorageData {
    public int repairs = 0;
    public int ppp = 0;
    public int repairsReturned = 0;
    public int pppReturned = 0;
    public boolean sickness = false;
    public boolean leave = false;
    public BigDecimal overtime50 = new BigDecimal(0);
    public BigDecimal overtime100 = new BigDecimal(0);
}
