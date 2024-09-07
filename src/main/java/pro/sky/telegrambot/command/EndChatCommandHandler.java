package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Прекратить беседу с клиентом".
 */
public class EndChatCommandHandler implements CommandHandler {
    /**
     * Завершает беседу с клиентом.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.endChat(chatId);
    }
}
