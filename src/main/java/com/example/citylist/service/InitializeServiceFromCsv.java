package com.example.citylist.service;

import com.example.citylist.extractor.CsvDataExtractor;
import com.example.citylist.extractor.DataExtractor;
import com.example.citylist.model.City;
import com.example.citylist.repositories.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class InitializeServiceFromCsv implements InitializeService {

    @Value("${app-initialization.init-file:initial-city.csv}")
    private String initFile;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeServiceFromCsv.class);
    private final CityRepository cityRepository;
    private final DataExtractor<City> cityDataExtractor;

    public InitializeServiceFromCsv(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        String[] header = new String[]{"id", "name", "photo"};
        cityDataExtractor = new CsvDataExtractor<>(header, City.class);
    }

    @Transactional
    public void initApp() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(initFile);
        ClassPathResource cpr = new ClassPathResource(initFile);
        try {
            if (resource == null) {
                LOGGER.warn("There is not file {} application start without initialization", initFile);
            } else {
                List<City> initCityList = cityDataExtractor.extractData(cpr.getPath());
                int initCityListSize = initCityList.size();
                LOGGER.info("Extract {} cities from file", initCityListSize);
                List<City> cities = cityRepository.saveAll(initCityList)
                        .collectList().block();
                LOGGER.info("{} cities was upload to database", cities == null ? 0 : cities.size());
            }
        } catch (IOException e) {
            LOGGER.error("Exception during initializing ex: {}", e.getMessage());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initApp();
    }
}