package com.example.citylist.validation;

import org.springframework.validation.Errors;

import java.util.List;

public interface Validation<T> {

    List<ValidationErrors> validate(T obj);
}
