package com.springwebapppdf;

import com.springwebapppdf.model.User;
import com.springwebapppdf.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemowebappApplication implements Runnable{

    public static void main(String[] args) {
        SpringApplication.run(DemowebappApplication.class, args);
    }

    @Override
    public void run() {

    }
}
