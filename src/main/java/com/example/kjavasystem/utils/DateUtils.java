package com.example.kjavasystem.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DateUtils {
    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }
}
