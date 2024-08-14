package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция как взять".
 */
public class InstructionTakeAnimalCommandHandler implements CommandHandler {
    /**
     * Отправляет инструкцию как взять животное из приюта.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendTakeAnimalInstruction(chatId);
    }
}