package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class SafetyEquipmentHandler implements CommandHandler {
    /**
     * Отправляет инструкцию по безопасности на территории приюта.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.safetyEquipment(chatId);

    }
}

