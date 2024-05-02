const mongoose = require('mongoose');
const {dbConnect, dbClear} = require('./dbConnection');
const chatService = require('../services/chatService.js');
const messageService = require('../services/messageService.js');

beforeAll(async () => await dbConnect()); 

describe("Сreating a chat", () => {
    test("Chat need to be created correctly", async () => {
        let members = ["Maxim", "Vladimir"];
        const chat = await chatService.createChat(members);  
        
        expect(chat).toBeTruthy(); 
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual(["Maxim", "Vladimir"]);
    });

    test("Сhat must be found by members", async () => {
        let members = ["Maxim", "Vladimir"]
        const chat = await chatService.getChatByPrts(members);

        expect(chat).toBeTruthy();
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual(["Maxim", "Vladimir"]);
    });

    test("Сhat must be found by ID", async () => {
        let members = ["Maxim", "Vladimir"]
        let chat = await chatService.getChatByPrts(members);
        id = chat._id;

        chat = await chatService.getChatById(id);

        expect(chat).toBeTruthy();
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual(["Maxim", "Vladimir"]); 
        await dbClear();
    });

    test("All chat messages must be received correctly",  async () => {
        let members = ["Sergey", "Kristina"];
        const chat = await chatService.createChat(members);
        let id = chat._id; 
        
        await messageService.createMessage(id, "65f219117586c3b69cab6a0a", "success");
        await messageService.createMessage("65f219117586c3b69cab6a0b", "65f219117586c3b69cab6a0a", "wasted");

        let messages = await chatService.getMessages(id);
        console.log(messages.length);

        expect(messages.length).toBe(1); 
        expect(messages[0].chat_id).toStrictEqual(id);
        expect(messages[0].text).toStrictEqual("success");
    });

    test("Updating the date of the last message should work correctly", async () => {
        let members = ["Sergey", "Kristina"];
        const chat = await chatService.createChat(members);

        let updatedChat = await chatService.updateLastMessageTime(chat._id);
        expect(chat).toBeTruthy();
        expect(updatedChat).toBeTruthy();

    });
    

});
