package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class InstructionCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendInstructionMenu(chatId);
    }
}