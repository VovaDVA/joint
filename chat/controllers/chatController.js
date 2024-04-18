const chatService = require('../services/chatService.js');

class chatController {
    constructor(chatService){
        this.chatService = chatService;
    }

    async createChat(request, response){
        try {
            const {participitans} = request.body;

            if (chatService.getChatByPrts(participitans)){
                return response.status(400).json({message: "Chat already exist"})
            }

            const chat = await chatService.createChat(participitans);
            return response.status(201).json(chat);
        } 
        catch (error) {
            return response.status(500).json({message: error.message});
        }
    }
    
    async getChat(request, response){
        try {
            const chatId = request.params.id;
            const chat = await chatService.getChatById(chatId);
    
            if(!chat){
                return response.status(404).json({message: "Chat not found"});
            }
    
            return response.status(201).json(chat)
    
        }
        catch (error){
            return response.status(500).json({message: error.message});
        }
    
    }
}

module.exports = new chatController();
