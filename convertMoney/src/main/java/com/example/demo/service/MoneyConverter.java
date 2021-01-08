package com.example.demo.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.demo.model.CurrencyRate;
import com.example.demo.model.ExchangeRate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;



@Service
public class MoneyConverter {
    private JsonNode masterNode;
    private HashMap<String, CurrencyRate> mapCurrencyRates;

    // Constructor
    public MoneyConverter() {
        loadExchangeRateFromJSON();
        loadCurrencyCodeFromCSV();
    }

    // Dung HashMap ma khong dung List, ArrayList vi HashMap truy van voi toc do cao
    // Truy van theo key-value
    // HashMap co the duplicate KEY
    // Set thi KEY la unique
    public HashMap<String, CurrencyRate> getMapCurrencyRates() {
        return mapCurrencyRates;
    }

    /**
     * Đọc dữ liệu từ file JSON vào JsonNode masterNode
     * Json co 2 kieu luu du lieu: Key/Value và Array
     */
    private void loadExchangeRateFromJSON() {
        try {
            File file = ResourceUtils.getFile("classpath:static/exchange_rate.json");
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            ObjectMapper objectMapper = new ObjectMapper();
            masterNode = objectMapper.readTree(bufferedReader);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadCurrencyCodeFromCSV() {
        mapCurrencyRates = new HashMap<>();
        try {
            File file = ResourceUtils.getFile("classpath:static/currency.csv");
            CsvMapper mapper = new CsvMapper(); // Dùng để ánh xạ cột trong CSV với từng trường trong POJO
            CsvSchema schema = CsvSchema.emptySchema().withHeader(); // Dòng đầu tiên sử dụng làm Header
            ObjectReader oReader = mapper.readerFor(CurrencyRate.class).with(schema); // Cấu hình bộ đọc CSV phù hợp với kiểu
            Reader reader = new FileReader(file);
            MappingIterator<CurrencyRate> mi = oReader.readValues(reader); // Iterator đọc từng dòng trong file


            while (mi.hasNext()) {
                CurrencyRate currencyRate = mi.next();
                String currencyCode = currencyRate.getCode();

                if (mapCurrencyRates.get(currencyCode) == null) { //Nếu trong HashMap chưa có currencyCode
                    float rate = getExchangeRate(currencyCode); //Lấy ExchangeRate from danh sách build từ JSON
                    if (rate > 0.0f) { //Nếu lấy thành công
                        currencyRate.setRate(rate);
                        System.out.println(currencyRate);
                        mapCurrencyRates.put(currencyCode, currencyRate);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Lấy tỷ giá chuyển đổi 1 USD sang currencyCode
     *
     * @param currencyCode
     * @return
     */
    public List<ExchangeRate> parseExchangeRate() {
        if (masterNode == null) return Collections.emptyList();
        Iterator<Map.Entry<String, JsonNode>> iter = masterNode.fields();

        ArrayList<ExchangeRate> exchageRates = new ArrayList<>();

        while (iter.hasNext()) {
            var node = iter.next();
            //System.out.println(node.getKey() + " = " + node.getValue());
            exchageRates.add(new ExchangeRate(node.getKey(),  (float)node.getValue().doubleValue()));
        }
        return exchageRates;
    }

    public float getExchangeRate(String currencyCode) {
        if (masterNode == null) return 0;
        JsonNode currencyNode = masterNode.get(currencyCode);

        if (currencyNode != null) {
            return currencyNode.floatValue();
        } else {
            return 0.0f;
        }

    }



}
