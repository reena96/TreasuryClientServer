package com.example.reenamaryputhota.common;
import com.example.reenamaryputhota.common.DailyCash;

interface BalanceService {
    boolean createDatabase();
    List<DailyCash> dailyCash(int year, int month, int day, int noWorkingDays);
}