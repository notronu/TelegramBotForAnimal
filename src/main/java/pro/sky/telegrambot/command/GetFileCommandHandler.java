package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Получить файл".
 */
public class GetFileCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        /**
         * Отправляет файл в чат.
         * @param botService сервис для взаимодействия с ботом
         * @param chatId идентификатор чата
         */
        botService.sendFile(chatId, "src/main/resources/file.doc");
    }
}