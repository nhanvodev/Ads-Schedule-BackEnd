package vn.npc.ads_schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdsScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdsScheduleApplication.class, args);
	}

}
