const mongoose = require('mongoose');
const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const chatService = require('../services/chatService.js');
const messageService = require('../services/messageService.js');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("Actions on chats", () => {
    test("Chat need to be created correctly", async () => {
        let members = [123, 456];
        const chat = await chatService.createChat(members);  
        
        expect(chat).toBeTruthy(); 
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual([123, 456]);
    });

    test("Сhat must be found by members", async () => {
        let members = [123, 456]
        const chat = await chatService.getChatByPrts(members);

        expect(chat).toBeTruthy();
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual([123, 456]);
    });

    test("Сhat must be found by ID", async () => {
        let members = [123, 456]
        let chat = await chatService.getChatByPrts(members);
        id = chat._id;

        chat = await chatService.getChatById(id);

        expect(chat).toBeTruthy();
        expect(chat).toHaveProperty("members");
        expect(chat["members"]).toEqual([123, 456]); 
        await dbClear();
    });

    test("All chat messages must be received correctly",  async () => {
        let members = [123, 456];
        const chat = await chatService.createChat(members);
        let id = chat._id; 
        
        await messageService.createMessage({
            "chat_id": id, 
            "sender_id": 123, 
            "text": "success"});

        await messageService.createMessage({
            "chat_id": "65f219117586c3b69cab6a0b", 
            "sender_id": 123, 
            "text": "wasted"});

        let messages = await chatService.getMessages(id);
        //console.log(messages.length);

        expect(messages.length).toBe(1); 
        expect(messages[0].chat_id).toStrictEqual(id);
        expect(messages[0].text).toStrictEqual("success");
        await dbClear();
    });

    test("Getting all the user's chats should work correctly", async () => {
        chat1_members = [1322, 8645];
        chat2_members = [1322, 1111];

        await chatService.createChat(chat1_members);
        await chatService.createChat(chat2_members);

        let chats = await chatService.getUserChats(1322);
        // console.log(chats);
        expect(chats.length).toBe(2);
        
        for (let chat of chats){
            expect(chat.members.includes(1322)).toBeTruthy();
        
        }
    });

    test("Updating the date of the last message should work correctly", async () => {
        let members = [123, 456];
        const chat = await chatService.createChat(members);

        let updatedChat = await chatService.updateLastMessageTime(chat._id);
        expect(chat).toBeTruthy();
        expect(updatedChat).toBeTruthy();
        await dbClear();
    });
    

});
