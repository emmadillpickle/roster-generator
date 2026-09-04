package com.emmaong.rostermanager.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RosterApplication {
	private static final Path DATA_DIRECTORY = Paths.get("../data");
	
	static {
        try {
            Files.createDirectories(DATA_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

	public static void main(String[] args) {		
		SpringApplication.run(RosterApplication.class, args);
	}

}
