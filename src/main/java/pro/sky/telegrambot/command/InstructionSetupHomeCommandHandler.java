package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция по обустройству дома".
 */
public class InstructionSetupHomeCommandHandler implements CommandHandler {
    /**
     * Отправляет инструкцию по обустройству дома.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendSetupHomeInstruction(chatId);
    }
}