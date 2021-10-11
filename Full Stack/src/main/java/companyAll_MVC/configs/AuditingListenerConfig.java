package companyAll_MVC.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingListenerConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        AuditorAware<String> auditor = new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {

                String uname = "Ali Bilmem";
                //String uname = SecurityContextHolder.getContext().getAuthentication().getName()
                // Security varsa SecurityContextHolder'dan username alınır.

                return Optional.ofNullable(uname);
            }
        };
        return auditor;
    }

}
