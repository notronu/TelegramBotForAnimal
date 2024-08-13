package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Прекратить беседу с клиентом".
 */
public class EndChatCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.endChat(chatId);
    }
}
