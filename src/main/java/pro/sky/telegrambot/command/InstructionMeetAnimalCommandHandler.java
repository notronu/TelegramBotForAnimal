package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция по знакомству с животным".
 */
public class InstructionMeetAnimalCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendMeetAnimalInstruction(chatId);
    }
}