const messageService = require("../services/messageService");
const chatService = require('../services/chatService');

class messageController {
    constructor(messageService, chatService){
        this.messageService = messageService;
        //this.chatService = chatService;
    }

    async sendMessage(request, response){
        try{
            const {chat_id, sender_id, text} = request.body;
            
            if (!(await chatService.getChatById(chat_id))){
                return response.status(400).json({message: `Chat with id: ${chat_id} does not exist`});
            }
            
            const message = await messageService.createMessage(chat_id, sender_id, text);
            return response.status(201).json(message);
        
        }catch(error){
            return response.status(500).json({message: error.message});

        }
    }

}

module.exports = new messageController(messageService);