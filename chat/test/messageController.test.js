const request = require("supertest");
const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const app = require("../app");

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("message/", () => {
    test("Sending a message should return the code 201", async () => {
        const chat = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });
        
        const responce = await request(app).post("/message/sendMessage").send({
            "chat_id": chat.body._id, 
            "sender_id": 123,
            "text": "succes"
        });
        
        expect(responce.statusCode).toBe(201);
        await dbClear();
    });

    test("validation of message creation request parameters", async () => {
        const responce = await request(app).post("/message/sendMessage").send({
            "dsffdnfj": [123, 456]   // Здесь намереннно допущена ошибка в параметрах
        });

        expect(responce.statusCode).toBe(500);
    });


    test("Deleting a message should return code 200", async () => {
        const chat = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });
        
        const message = await request(app).post("/message/sendMessage").send({
            "chat_id": chat.body._id, 
            "sender_id": 123,
            "text": "succes"
        });

        const responce = await request(app).get("/message/deleteMessage").query({
            "message_id": message.body._id
        });

        expect(responce.statusCode).toBe(200);
        await dbClear();
    })

    test("If the message does not exist, then deleting the message should return the code 500", async () => {
        const responce = await request(app).get("/message/deleteMessage").query({
            "message_id": "66f219137586c3b69cab6b0a"
        });

        expect(responce.statusCode).toBe(500);

    });

    test("validation of message deleting request parameters", async () => {
        const responce = await request(app).get("/message/deleteMessage").query({
            "dsffdnfj": 381233980   // Здесь намереннно допущена ошибка в параметрах
        });

        expect(responce.statusCode).toBe(500);
    });


    test("Editing a message should return code 201", async () => {
        const chat = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });
        
        const message = await request(app).post("/message/sendMessage").send({
            "chat_id": chat.body._id, 
            "sender_id": 123,
            "text": "succes"
        });

        const responce = await request(app).post("/message/editMessage").send({
            "message_id": message.body._id,
            "text": "updated text"
        });

        expect(responce.statusCode).toBe(201);
        await dbClear();
    });

    test("An attempt to edit a non-existent message must be accompanied by a 500 code", async () => {
        const responce = await request(app).post("/message/editMessage").send({
            "message_id": "66f219137586c3b69cab6b0a",
            "text": "updated text"
        });

        expect(responce.statusCode).toBe(500);
    });

    test("validation of message creation request parameters", async () => {
        const responce = await request(app).post("/message/editMessage").send({
            "dsffdnfj": "124244212"  // Здесь намереннно допущена ошибка в параметрах
        });

        expect(responce.statusCode).toBe(500);
    });

});
