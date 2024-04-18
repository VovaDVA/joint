const Chat = require('../models/chat');

class chatService {
    constructor(Chat){
        this.Chat = Chat;
    }
// Функция для создания и записи нового чата в базу данных 
    async createChat(participitans){
        let today = new Date();
        const chat = new Chat({
            "participitans": participitans,
            "created_at": today,
            "last_message_at": null
        });
        await chat.save();
        return chat;
    }

    async getChatById(chatId){
        return await Chat.findById(chatId);
    }

    async getChatByPrts(participitans){
        return await Chat.find({"participitans": participitans});
    }

    // Функция для обновления даты последнего сообщения 
    async updateLastMessageTime(chatId){
        // Устанавливаем дату последнего сообщения, как текущую
        let today = new Date();                         
        const chat = await getChatById(chatId);
        chat.last_message_at = today;
        await chat.save();
        return chat;
    }
}
// Экспорт
module.exports = new chatService(Chat);