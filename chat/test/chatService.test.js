const mongoose = require('mongoose');
const {dbConnect, dbClear} = require('./dbConnection');
const chatService = require('../services/chatService.js');
const messageService = require('../services/messageService.js');

beforeAll(async () => await dbConnect()); 

describe("Actions on chats", () => {
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
        //console.log(messages.length);

        expect(messages.length).toBe(1); 
        expect(messages[0].chat_id).toStrictEqual(id);
        expect(messages[0].text).toStrictEqual("success");
        await dbClear();
    });

    test("Getting all the user's chats should work correctly", async () => {
        chat1_members = ["65f219117586c3b69cab6a01", "65f219117586c3b69cab6a02"];
        chat2_members = ["65f219117586c3b69cab6a01", "65f219117586c3b69cab6a03"];

        await chatService.createChat(chat1_members);
        await chatService.createChat(chat2_members);

        let chats = await chatService.getUserChats("65f219117586c3b69cab6a01");
        // console.log(chats);
        expect(chats.length).toBe(2);
        
        for (let chat of chats){
            expect(chat.members.includes("65f219117586c3b69cab6a01")).toBeTruthy();
        
        }

    });

    test("Updating the date of the last message should work correctly", async () => {
        let members = ["Sergey", "Kristina"];
        const chat = await chatService.createChat(members);

        let updatedChat = await chatService.updateLastMessageTime(chat._id);
        expect(chat).toBeTruthy();
        expect(updatedChat).toBeTruthy();
        await dbClear();
    });
    

});
