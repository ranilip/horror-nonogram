package sk.tuke.kpi.kp.nonogram;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.nonogram.consoleUI.ConsoleUI;
import sk.tuke.kpi.kp.nonogram.consoleUI.PlayerSessionService;
import sk.tuke.kpi.kp.nonogram.service.JPA.CommentServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.RatingServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.JPA.ScoreServiceJPA;
import sk.tuke.kpi.kp.nonogram.service.rest.CommentServiceRestClient;
import sk.tuke.kpi.kp.nonogram.service.rest.RatingServiceRestClient;
import sk.tuke.kpi.kp.nonogram.service.rest.ScoreServiceRestClient;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.ScoreService;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.CommentService;
import sk.tuke.kpi.kp.nonogram.service.serviceInterface.RatingService;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.kpi.kp.nonogram.server.*"))

public class SpringClient {

    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class, args);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI consoleUI) {
        return args -> consoleUI.play();
    }

    @Bean
    public ConsoleUI consoleUI(PlayerSessionService sessionService) {
        return new ConsoleUI(sessionService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
}
