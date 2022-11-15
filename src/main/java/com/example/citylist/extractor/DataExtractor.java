package com.example.citylist.extractor;

import java.io.IOException;
import java.util.List;

public interface DataExtractor<T> {

    List<T> extractData(String from) throws IOException;
}
