const chatService = require('../services/chatService.js');

class chatController {
    constructor(chatService){
        this.chatService = chatService;
    }

    async getMessages(request, response){
        try {
            const chat_id = request.query.chat_id;

            if (!chat_id){
                return response.status(500).json({message: "ERROR, invalid parameter: cannot get \'chat_id\' "});
            }

            //console.log(request.query.chat_id);
            const messages = await chatService.getMessages(chat_id);
            return response.status(201).json(messages);
        }
        catch(error){
            return response.status(500).json({message: error.message});
        }
    }

    async createChat(request, response){
        try {
            const {members} = request.body;
            //console.log(members);

            if (await chatService.getChatByPrts(members)){
                return response.status(400).json({message: "ERROR, Chat already exist"})
            }

            const chat = await chatService.createChat(members);
            return response.status(201).json(chat);
        } 
        catch (error) {
            return response.status(500).json({message: error.message});
        }
    }

    async getUserChats(request, response){
        try {
            const user_id = request.query.user_id;

            if (!user_id){
                return response.status(500).json({message: "ERROR, invalid parameter: cannot get \'user_id\' "});
            }

            const chats = await chatService.getUserChats(user_id);
            return response.status(201).json(chats);

        }catch(error){
            return response.status(500).json({message: error.message});
        }
    }

    async getChat(request, response){
        try {
            const chatId = request.query.id;
            //console.log(chatId);
            const chat = await chatService.getChatById(chatId);
    
            if(!chat){
                return response.status(404).json({message: "ERROR, Chat not found"});
            }
    
            return response.status(200).json(chat)
    
        }
        catch (error){
            return response.status(500).json({message: error.message});
        }
    
    }
}

module.exports = new chatController();
