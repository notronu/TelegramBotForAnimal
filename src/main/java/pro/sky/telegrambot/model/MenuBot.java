package pro.sky.telegrambot.model;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.util.ArrayList;
import java.util.List;
@Service
public class MenuBot {

    public ReplyKeyboardMarkup sendMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
//        keyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Информация о приюте");
        row1.add("Как взять животное из приюта");
        row1.add("Прислать отчет о питомце");
        row1.add("Позвать волонтера");
        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;


    }
    public ReplyKeyboardMarkup sendSubmenu1() {
        ReplyKeyboardMarkup submenuKeyboard = new ReplyKeyboardMarkup();
        submenuKeyboard.setSelective(true);
        submenuKeyboard.setResizeKeyboard(true);
        List<KeyboardRow> submenuKeyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Расписание и адрес приюта");
        row.add("Оформление пропуска и схема проезда");
        row.add("Техника безопасности");
        row.add("Запросить связь");
        row.add("Список животных");
        submenuKeyboardRows.add(row);

        submenuKeyboard.setKeyboard(submenuKeyboardRows);
        return submenuKeyboard;
    }
    public ReplyKeyboardMarkup sendSubmenu2() {
        ReplyKeyboardMarkup submenuKeyboard2 = new ReplyKeyboardMarkup();
        submenuKeyboard2.setSelective(true);
        submenuKeyboard2.setResizeKeyboard(true);
        List<KeyboardRow> submenuKeyboardRows = new ArrayList<>();

        KeyboardRow row01 = new KeyboardRow();
        row01.add("Список животных");
        row01.add("Правила знакомства и усыновления");
        row01.add("Список необходимых документов");
        row01.add("Рекомендации");

        KeyboardRow row02 = new KeyboardRow();
        row02.add("Советы кинолога");
        row02.add("Проверенные кинологи");
        row02.add("Причины отказа");
        row02.add("Запросить связь");
        submenuKeyboardRows.add(row01);
        submenuKeyboardRows.add(row02);

        submenuKeyboard2.setKeyboard(submenuKeyboardRows);
        return submenuKeyboard2;
    }



//
//        // Создание подменю
//        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
//
//        // Кнопки подменю
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//        row2.add(new InlineKeyboardButton().setText("Информация о приюте").setCallbackData("submenu_item1"));
//        //row1.add(new InlineKeyboardButton().setText("Как взять животное из приюта").setCallbackData("submenu_item2"));
//        rows.add(row2);
//
//        keyboardMarkup.setKeyboard(rows);
//
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText("Выберите действие из подменю: " + submenuData);
//        message.setReplyMarkup(keyboardMarkup);
//        return keyboardMarkup;

//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }

}



