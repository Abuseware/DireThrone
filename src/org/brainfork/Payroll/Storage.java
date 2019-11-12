package org.brainfork.Payroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class Storage {
    private static TypeReference<HashMap<Integer, StorageData>> monthTypeRef = new TypeReference<HashMap<Integer, StorageData>>() {
    };
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    //@SuppressWarnings("unchecked")
    private static Map<Integer, StorageData> loadFile(int year, int month) {
        File dir = new File("database/" + year);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, month + ".json");
        Map<Integer, StorageData> monthData;
        try {
            monthData = objectMapper.readValue(file, monthTypeRef);
        } catch (IOException e) {
            System.err.println("Nie można załadować pliku: " + file.getPath());
            monthData = new HashMap<>();
        }

        return monthData;
    }

    private static boolean saveFile(int year, int month, Map<Integer, StorageData> data) {
        File dir = new File("database/" + year);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, month + ".json");
        try {
            objectMapper.writeValue(file, data);
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (IOException e) {
            System.err.println("Nie można zapisać pliku: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    static Map<Integer, StorageData> getMonth(int year, int month) {
        return loadFile(year, month);
    }

    static StorageData getDay(int year, int month, int day) {
        Map<Integer, StorageData> monthData = getMonth(year, month);
        if (monthData.containsKey(day))
            return monthData.get(day);
        return new StorageData();
    }

    static void setDay(int year, int month, int day, StorageData data) {
        Map<Integer, StorageData> monthData = loadFile(year, month);
        monthData.put(day, data);
        saveFile(year, month, monthData);
    }
}
