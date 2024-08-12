package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция по обустройству дома".
 */
public class InstructionSetupHomeCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendSetupHomeInstruction(chatId);
    }
}