package ru.red;

import org.bson.Document;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Bot extends TelegramLongPollingBot {

    public Bot(DefaultBotOptions options) {
        super(options);
        System.out.println("бот стартанул");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if (callbackQuery.getData().contains("getExif")) {
                StringBuffer exifs = ImgAnalizer.analize((String)
                        DBO.getInstance().getSinglePhoto(
                                callbackQuery.getData().split(":")[1]
                        ).get("fullURL"));
                if (exifs != null) sendMsg(callbackQuery.getMessage().getChatId(), exifs.toString(), getExifKeyboard());
            } else if (callbackQuery.getData().contains("hide")) {
                DBO.getInstance().updateAfterHide(callbackQuery.getData().split(":")[1]);
                delMsg(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
                Document photo = DBO.getInstance().getExistSinglePhoto();
                if (photo != null)
                    sendPhoto(callbackQuery.getMessage().getChatId(), (String) photo.get("desc"), (String) photo.get("fullURL"), (String) photo.get("_id"));
                else sendMsg(callbackQuery.getMessage().getChatId(), "Пусто");
            } else if (callbackQuery.getData().contains("save")) {
                DBO.getInstance().updateAfterSave(callbackQuery.getData().split(":")[1]);
                delMsg(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
                Document photo = DBO.getInstance().getExistSinglePhoto();
                if (photo != null)
                    sendPhoto(callbackQuery.getMessage().getChatId(), (String) photo.get("desc"), (String) photo.get("fullURL"), (String) photo.get("_id"));
                else sendMsg(callbackQuery.getMessage().getChatId(), "Пусто");
            } else if (callbackQuery.getData().equals("X")) {
                delMsg(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
            } else {

            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message getedMsg = update.getMessage();
            switch (getedMsg.getText()) {
                case "\uD83D\uDE80Чекать фото":
                    Document photo = DBO.getInstance().getExistSinglePhoto();
                    if (photo != null)
                        sendPhoto(getedMsg.getChatId(), (String) photo.get("desc"), (String) photo.get("fullURL"), (String) photo.get("_id"));
                    else sendMsg(getedMsg.getChatId(), "Пусто");
                    break;
                case "\uD83D\uDCBEСохраненные фото":
                    sendMsg(getedMsg.getChatId(), "СФ");
                    break;
                case "Контейнер":
                    sendMsg(getedMsg.getChatId(), "СФ");
                    break;
                case "\uD83D\uDCBEИнформация":
                    Locale loc = new Locale("ru");
                    NumberFormat digFormatter = NumberFormat.getInstance(loc);
                    Long checkedCount = DBO2.getInstance().getTotalCountSinglePhoto(1);
                    Long noValidCount = DBO2.getInstance().getTotalCountSinglePhoto("notexist");
                    String parserInfo = "Всего записей в БД: " + digFormatter.format(DBO2.getInstance().getTotalCountSinglePhoto());
                    parserInfo += "\nПроверенно: " + digFormatter.format(checkedCount);
                    parserInfo += "\nВалидных: " + digFormatter.format((checkedCount - noValidCount));
                    parserInfo += " (" + String.valueOf((double) (checkedCount - noValidCount) / (checkedCount) * 100.0).substring(0, 3) + " %) ";
                    parserInfo += "\nНе валидных: " + digFormatter.format(noValidCount);
                    parserInfo += "\nНе просмотренных: " + digFormatter.format(DBO2.getInstance().getTotalCountSinglePhoto("exist"));
                    parserInfo += "\nСохраненных: " + digFormatter.format(DBO2.getInstance().getTotalCountSinglePhoto("saved"));
                    parserInfo += "\nСкрытых: " + digFormatter.format(DBO2.getInstance().getTotalCountSinglePhoto("hide"));
                    sendMsg(getedMsg.getChatId(), parserInfo);
                    break;
                default:
                    sendMsg(getedMsg.getChatId(), "Неизвестная команда");
                    break;
            }
        }
    }


    @Override
    public String getBaseUrl() {
        return super.getBaseUrl();
    }

    private void delMsg(Long chatID, Integer msgID) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage
                .setChatId(chatID)
                .setMessageId(msgID);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Long chatID, String msgText) {
        SendMessage msg = new SendMessage();
        msg.setReplyMarkup(getDownKeyboard())
                .setText(msgText)
                .setChatId(chatID);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Long chatID, String msgText, ReplyKeyboard replyKeyboard) {
        SendMessage msg = new SendMessage();
        msg
                .setText(msgText)
                .setReplyMarkup(replyKeyboard)
                .setChatId(chatID);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(Long chatID, String msgText, String photoURL, String sID) {
        System.out.println(photoURL);
        SendPhoto msg = new SendPhoto();
        msg.setReplyMarkup(getDownKeyboard())
                .setPhoto(photoURL)
                .setChatId(chatID)
                .setCaption(msgText)
                .setReplyMarkup(getNewPhotoKeyboard(sID, true));
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getNewPhotoKeyboard(String sID, Boolean hasExif) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardRow = new LinkedList<>();
        InlineKeyboardButton exifBtn = new InlineKeyboardButton();
        exifBtn
                .setText("EXIF")
                .setCallbackData("getExif:" + sID);
        InlineKeyboardButton hideBtn = new InlineKeyboardButton();
        hideBtn
                .setText("HIDE")
                .setCallbackData("hide:" + sID);
        InlineKeyboardButton saveBtn = new InlineKeyboardButton();
        saveBtn
                .setText("SAVE")
                .setCallbackData("save:" + sID);
        InlineKeyboardButton xBtn = new InlineKeyboardButton();
        xBtn
                .setText("X")
                .setCallbackData("X");
        if (hasExif) keyboardRow.add(exifBtn);
        keyboardRow.add(hideBtn);
        keyboardRow.add(saveBtn);
        keyboardRow.add(xBtn);
        List<List<InlineKeyboardButton>> keyboardTotal = new LinkedList<>();
        keyboardTotal.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(keyboardTotal);
        return inlineKeyboardMarkup;


    }

    private InlineKeyboardMarkup getExifKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardRow = new LinkedList<>();
        InlineKeyboardButton xBtn = new InlineKeyboardButton();
        xBtn
                .setText("X")
                .setCallbackData("X");
        keyboardRow.add(xBtn);
        List<List<InlineKeyboardButton>> keyboardTotal = new LinkedList<>();
        keyboardTotal.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(keyboardTotal);
        return inlineKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getDownKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("\uD83D\uDE80Чекать фото"));
        row2.add(new KeyboardButton("\uD83D\uDCBEИзбранные"));
        row3.add(new KeyboardButton("\uD83D\uDCBEИнформация"));
        LinkedList<KeyboardRow> keyboardRows = new LinkedList<>();
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return "testparser22bot";
    }

    @Override
    public String getBotToken() {
        return "553052258:AAE_1yn62JAMbcNmxs-YKwGYqSc0iX5eOa4";
    }
}
