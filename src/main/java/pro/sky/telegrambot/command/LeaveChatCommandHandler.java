package pro.sky.telegrambot.command;


import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Попрощаться с клиентом".
 */
public class LeaveChatCommandHandler implements CommandHandler {
    /**
     * Попрощаться с клиентом и завершить беседу.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.leaveChat(chatId);
    }
}
