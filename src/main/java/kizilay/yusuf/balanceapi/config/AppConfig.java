package kizilay.yusuf.balanceapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
/**
 * Enable'lı annotationlar için {@link kizilay.yusuf.balanceapi.BalanceApiApplication}'i kullanmak iyi bir fikir değil.
 * Testte sorun çıkartıyor. Boş bile olsa ayrı config class daha iyi.
 */
@EnableJpaAuditing
public class AppConfig {
}
