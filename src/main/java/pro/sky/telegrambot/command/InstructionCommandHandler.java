package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Инструкция как взять животное из приюта".
 */
public class InstructionCommandHandler implements CommandHandler {
    /**
     * Отправляет меню инструкций.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendInstructionMenu(chatId);
    }
}
