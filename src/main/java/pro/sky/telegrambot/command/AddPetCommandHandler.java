package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.service.VolunteerService;

public class AddPetCommandHandler implements CommandHandler {

    private final VolunteerService volunteerService;

    public AddPetCommandHandler(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Override
    public void handle(BotService botService, long chatId) {
        botService.startPetRegistration(chatId);
    }
}