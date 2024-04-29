const Message = require('../models/message');
//const Chat = require('../models/chat');

class messageService {
    constructor(Message, Chat){
        this.Message = Message;
        //this.Chat = Chat;
    }

    async deleteMessage(message_id){
        const message = Message.findByIdAndDelete(message_id);
        return message;
    }

    async editMessage(message_id, new_text){
        const message = await Message.findByIdAndUpdate(message_id, {"text": new_text}, {new: true});
        return message;
    }

    async findMessage(message_id){
        const message = await Message.findById(message_id);
        return message;
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