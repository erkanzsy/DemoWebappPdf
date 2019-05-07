package com.springwebapppdf;

import com.springwebapppdf.model.User;
import com.springwebapppdf.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemowebappApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemowebappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Lev","Tolstoy","Author");
        User user1 = new User("George","Orwell","Author");
        User user2 = new User("Elon","Musk","Adamdır");

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
