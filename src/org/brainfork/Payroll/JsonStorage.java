package org.brainfork.Payroll;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class JsonStorage implements DataStorageInterface {
    //private static Storage storage = null;

    private static final TypeReference<HashMap<Integer, StorageData>> monthTypeRef = new TypeReference<HashMap<Integer, StorageData>>() {
    };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonStorage() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /*public synchronized static Storage getInstance() {
        if(storage == null) storage = new Storage();
        return storage;
    }*/

    private File openFile(int year, int month) {
        File dir = new File("database/" + year);
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, month + ".json");
    }

    private Map<Integer, StorageData> loadFile(int year, int month) {
        File file = openFile(year, month);
        Map<Integer, StorageData> monthData;
        try {
            monthData = objectMapper.readValue(file, monthTypeRef);
        } catch (IOException e) {
            System.err.println("Nie można załadować pliku: " + file.getPath());
            monthData = new HashMap<>();
        }

        return monthData;
    }

    private boolean saveFile(int year, int month, Map<Integer, StorageData> data) {
        File file = openFile(year, month);
        try {
            objectMapper.writeValue(file, data);
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (IOException e) {
            System.err.println("Nie można zapisać pliku: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public Map<Integer, StorageData> getMonth(int year, int month) {
        return loadFile(year, month);
    }

    public StorageData getDay(int year, int month, int day) {
        Map<Integer, StorageData> monthData = getMonth(year, month);
        if (monthData.containsKey(day))
            return monthData.get(day);
        return new StorageData();
    }

    public void setDay(int year, int month, int day, StorageData data) {
        Map<Integer, StorageData> monthData = loadFile(year, month);
        monthData.put(day, data);
        saveFile(year, month, monthData);
    }
}
