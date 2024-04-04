const Message = require('../models/message');
//const Chat = require('../models/chat');

class messageService {
    constructor(Message, Chat){
        this.Message = Message;
        //this.Chat = Chat;
    }

    async createMessage(chat_id, sender_id, text){
        let today = await new Date();

        const message = new Message({
            "chat_id": chat_id,
            "sender_id": sender_id,
            "text": text,
            "created_at": today
        });
        
        await message.save();
        return message;
    }

}

module.exports = new messageService();