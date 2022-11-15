package com.example.citylist.validation;

import com.example.citylist.model.City;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CityDtoValidation implements Validation<City> {

    @Override
    public List<ValidationErrors> validate(City obj) {
        List<ValidationErrors> validationErrors = new ArrayList<>();
        if(Strings.isEmpty(obj.getName())){
            validationErrors.add(new ValidationErrors("Name is required", "name"));
        }
        return validationErrors;
    }
}
