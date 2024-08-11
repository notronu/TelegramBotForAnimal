package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов бота.
 */
@RestController
public class BotController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Bot is running";
    }
}
