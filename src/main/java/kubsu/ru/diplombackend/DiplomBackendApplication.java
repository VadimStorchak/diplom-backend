package kubsu.ru.diplombackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class DiplomBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomBackendApplication.class, args);
    }
}
