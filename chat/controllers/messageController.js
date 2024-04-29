const messageService = require("../services/messageService");
const chatService = require('../services/chatService');

class messageController {
    constructor(messageService, chatService){
        this.messageService = messageService;
        //this.chatService = chatService;
    }

    async deleteMessage(request, response){
        try {
            const message_id = request.query.message_id;
            console.log(message_id);

            if (!message_id){
                return response.status(500).json({message: "ERROR, invalid parameter: cannot get \'message_id\' "});
            }

            if (!(await messageService.findMessage(message_id))){
                return response.status(500).json({message: `ERROR, message with id: \'${message_id}\' does not exist`});
            }

            const message = await messageService.deleteMessage(message_id);
            return response.status(200).json(message);

        }catch(error){
            return response.status(500).json({message: error.message});
        }
    };

    async editMessage(request, response){
        try{
        const {message_id, text} = request.body;
        
        if (!message_id || !text){
            return response.status(500).json({message: "ERROR, invalid request body, fields \'message_id\', \'text\' are required"});
        }

        const updatedMessage = await messageService.editMessage(message_id, text);
        return response.status(201).json(updatedMessage);

        }catch(error){
            return response.status(500).json({message: error.message});

        }
    }

    async sendMessage(request, response){
        try{
            const {chat_id, sender_id, text} = request.body;

            if (!chat_id || !sender_id || !text){
                return responce.status(500).json({message: "ERROR, invalid request body, fields \'chat_id\', \'sender_id\', \'text\' are required"});
            }
            
            if (!(await chatService.getChatById(chat_id))){
                return response.status(500).json({message: `ERROR, chat with id: ${chat_id} does not exist`});
            }
            
            const message = await messageService.createMessage(chat_id, sender_id, text);
            return response.status(201).json(message);
        
        }catch(error){
            return response.status(500).json({message: error.message});

        }
    }

}

module.exports = new messageController(messageService);