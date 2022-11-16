package com.example.citylist.extractor;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        ClassPathResource classPathResource = new ClassPathResource(from);
        InputStream inputStream = classPathResource.getInputStream();
        List<T> data;
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            reader.skip(1);
            csvToBean.setCsvReader(reader);
            data = csvToBean.parse();
        }
        return data;
    }
}