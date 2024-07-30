package pro.sky.telegrambot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("{telegram.bot.volunteer}")
    private String volunteer;

//    @Bean
//    public PetService petService(){
//    return new PetService();
//    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
