const Message = require('../models/message');
const Chat = require('../models/chat');

class messageService {
    constructor(Message, Chat){
        this.Message = Message;
        // this.Chat = Chat;
    }

    async deleteMessage(message_id){
        const message = Message.findByIdAndDelete(message_id);
        return message;
    }

    async editMessage(message_id, new_text){
        const message = await Message.findByIdAndUpdate(message_id, {"text": new_text, "edited": true}, {new: true});
        return message;
    }

    async findMessage(message_id){
        const message = await Message.findById(message_id);
        return message;
    }

    async createMessage(data){
        let today = new Date();

        const message = new Message({
            "chat_id": data.chat_id,
            "sender_id": data.sender_id,
            "text": data.text,
            "created_at": today
        });

        await Chat.findByIdAndUpdate(data.chat_id, {"last_message": data.text, "last_message_at": today}, {new: true});
        
        await message.save();
        return message;
    }

}

module.exports = new messageService();