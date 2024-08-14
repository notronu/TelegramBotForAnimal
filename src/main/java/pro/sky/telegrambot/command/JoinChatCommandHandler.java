package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Присоединиться к беседе с клиентом".
 */
public class JoinChatCommandHandler implements CommandHandler {
    /**
     * Присоединяется к беседе с клиентом.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.joinChat(chatId);
    }
}