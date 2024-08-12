package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция как взять".
 */
public class InstructionTakeAnimalCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendTakeAnimalInstruction(chatId);
    }
}