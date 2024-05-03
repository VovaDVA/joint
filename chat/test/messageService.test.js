const mongoose = require('mongoose');
const {dbConnect, dbClear} = require('./dbConnection');
const chatService = require('../services/chatService.js');
const messageService = require('../services/messageService.js');

beforeAll(async () => await dbConnect()); 

describe("Actions on messages", () => {
    test("Creation of messages must be correct", async () => {
        let members = ["Lisa", "Elnara"];
        const chat = await chatService.createChat(members);

        const message = await messageService.createMessage(chat._id, "65f219117586c3b69cab6a0a", "success");

        expect(message).toBeTruthy();
        expect(message).toHaveProperty("chat_id");
        expect(message.chat_id).toStrictEqual(chat._id);
        
        await dbClear();
    });

    test("Messages can be found by id", async () => {
        let members = ["Lisa", "Elnara"];
        const chat = await chatService.createChat(members);
        let message = await messageService.createMessage(chat._id, "65f219117586c3b69cab6a0a", "success");

        const id = message._id;
        message = await messageService.findMessage(id);

        expect(message).toBeTruthy();
        expect(message).toHaveProperty("chat_id");
        expect(message.chat_id).toStrictEqual(chat._id);

        await dbClear();
    });

    test("Deleting messages should work correctly", async () => {
        let chat_id = "66f219137586c3b69cab6b0a";
        let message = await messageService.createMessage(chat_id, "65f219117586c3b69cab6a0a", "success");
        //console.log(message);

        let deletedMessage = await messageService.deleteMessage(message._id);
        expect(deletedMessage).toBeTruthy();

        message = await messageService.findMessage(message._id);
        expect(message).toBeFalsy();
        await dbClear();

    });

    test("Message editing should work correctly", async () => {
        let chat_id = "66f219137586c3b69cab6b0a";
        let message = await messageService.createMessage(chat_id, "65f219117586c3b69cab6a0a", "success");

        let editedMessage = await messageService.editMessage(message._id, "edited text");
        //console.log(editedMessage);
        
        expect(editedMessage).toBeTruthy();
        expect(editedMessage.text).toStrictEqual("edited text");

    });



});


