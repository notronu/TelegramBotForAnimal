package pro.sky.telegrambot.model;

import pro.sky.telegrambot.command.*;
import pro.sky.telegrambot.service.BotService;

/**
 * Перечисление команд бота с соответствующими действиями.
 */
public enum BotCommand {

    SHELTER_CHOICE("Выбор приюта", new ShelterChoiceCommandHandler()),
    INSTRUCTION("Инструкция как взять животное из приюта", new InstructionCommandHandler()),
    PET_REPORT("Прислать отчет о питомце", new PetReportCommandHandler()),
    CALL_VOLUNTEER("Позвать волонтера", new CallVolunteerCommandHandler()),
    CAT_SHELTER_INFO("Информация о приюте для кошек", new CatShelterInfoCommandHandler()),
    CAT_SHELTER_LOCATION("Местоположение приюта для кошек", new CatShelterLocationCommandHandler()),
    DOG_SHELTER_INFO("Информация о приюте для собак", new DogShelterInfoCommandHandler()),
    DOG_SHELTER_LOCATION("Местоположение приюта для собак", new DogShelterLocationCommandHandler()),
    INSTRUCTION_MEET_ANIMAL("Инструкция по знакомству с животным", new InstructionMeetAnimalCommandHandler()),
    INSTRUCTION_TAKE_ANIMAL("Инструкция как взять", new InstructionTakeAnimalCommandHandler()),
    INSTRUCTION_SETUP_HOME("Инструкция по обустройству дома", new InstructionSetupHomeCommandHandler()),
    RECOMMEND_CYNOLOGISTS("Рекомендации по проверенным кинологам", new RecommendCynologistsCommandHandler()),
    GET_FILE("Получить файл", new GetFileCommandHandler()),
    JOIN_CHAT("Присоедениться к беседе с клиентом", new JoinChatCommandHandler()),
    LEAVE_CHAT("Попрощаться с клиентом", new LeaveChatCommandHandler()),
    REGISTER_VOLUNTEER("Регистрация волонтера", new RegisterVolunteerCommandHandler()),
    VOLUNTEER_ACTIVE("Активен", new VolunteerActiveCommandHandler()),
    DEFAULT("", new DefaultCommandHandler());

    private final String command;
    private final CommandHandler handler;

    BotCommand(String command, CommandHandler handler) {
        this.command = command;
        this.handler = handler;
    }

    public void execute(BotService botService, long chatId) {
        handler.handle(botService, chatId);
    }

    public static BotCommand fromString(String text) {
        for (BotCommand command : BotCommand.values()) {
            if (command.command.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return DEFAULT;
    }
}