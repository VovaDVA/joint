const Chat = require('../models/chat');
const Message = require('../models/message');

class chatService {
    constructor(Chat){
        this.Chat = Chat;
    }
// Функция для создания и записи нового чата в базу данных 
    async createChat(members){
        let today = new Date();
        const chat = new Chat({
            "members": members,
            "created_at": today,
            "last_message_at": null
        });
        await chat.save();
        return chat;
    }

    async getMessages(chatId){
        const chat = await this.getChatById(chatId);
        //console.log(chat._id);
        return await Message.find({ 
            chat_id: chat._id 
        });
    }

    async getChatById(chatId){
        return await Chat.findById(chatId);
    }

    async getChatByPrts(members){
        console.log(await Chat.findOne({"members": {$all: members}}));
        return await Chat.findOne({"members": {$all: members}});
    }

    async getUserChats(user_id){
        return await Chat.find({"members": user_id
    });
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