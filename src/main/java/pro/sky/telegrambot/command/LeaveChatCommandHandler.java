package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Попрощаться с клиентом".
 */
public class LeaveChatCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.leaveChat(chatId);
    }
}