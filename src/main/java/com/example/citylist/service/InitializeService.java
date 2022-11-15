package com.example.citylist.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public interface InitializeService extends ApplicationListener<ContextRefreshedEvent> {

    void initApp();
}
