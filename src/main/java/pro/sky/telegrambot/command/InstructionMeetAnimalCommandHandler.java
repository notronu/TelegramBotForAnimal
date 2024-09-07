package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция по знакомству с животным".
 */
public class InstructionMeetAnimalCommandHandler implements CommandHandler {
    /**
     * Отправляет инструкцию по знакомству с животным.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendMeetAnimalInstruction(chatId);
    }
}
