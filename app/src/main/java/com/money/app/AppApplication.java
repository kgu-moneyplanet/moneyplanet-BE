package com.money.app;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        // .env 파일 로드 확인
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
        System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
        System.setProperty("SECURE_USERNAME", dotenv.get("SECURE_USERNAME"));
        System.setProperty("SECURE_PASSWORD", dotenv.get("SECURE_PASSWORD"));
        System.setProperty("SECURE_ROLE", dotenv.get("SECURE_ROLE"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        SpringApplication.run(AppApplication.class, args);

    }

}