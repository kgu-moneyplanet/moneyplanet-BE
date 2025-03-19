package com.money.app;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        // .env 파일 로드 확인
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("SECURE_USERNAME", dotenv.get("SECURE_USERNAME"));
        System.setProperty("SECURE_PASSWORD", dotenv.get("SECURE_PASSWORD"));
        System.setProperty("SECURE_ROLE", dotenv.get("SECURE_ROLE"));
        SpringApplication.run(AppApplication.class, args);

    }

}
