package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Присоединиться к беседе с клиентом".
 */
public class JoinChatCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.joinChat(chatId);
    }
}