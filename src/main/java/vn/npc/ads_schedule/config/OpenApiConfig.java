package vn.npc.ads_schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adsScheduleOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ads Scheduler API")
                        .description("Backend quan ly lich chieu quang cao tren Android Box")
                        .version("v1.0"));
    }
}
