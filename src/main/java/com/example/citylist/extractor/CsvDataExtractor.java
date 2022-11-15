package com.example.citylist.extractor;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvDataExtractor<T> implements DataExtractor<T> {
    private final CsvToBean<T> csvToBean;

    public CsvDataExtractor(String[] columns, Class<T> type) {
        ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(type);
        strategy.setColumnMapping(columns);

        csvToBean = new CsvToBean<>();
        csvToBean.setMappingStrategy(strategy);
    }

    @Override
    public List<T> extractData(String from) throws IOException {
        List<T> data;
        try (CSVReader reader = new CSVReader(new FileReader(from))) {
            reader.skip(1);
            csvToBean.setCsvReader(reader);
            data = csvToBean.parse();
        }
        return data;
    }
}