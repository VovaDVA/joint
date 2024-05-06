const request = require("supertest");
const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const app = require("../app");

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("/chat", () => {

    test("Request to create a chat should send the code 201 (POST /createChat)", async () => {
        const responce = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });

        expect(responce.statusCode).toBe(201);
        expect(responce.body).toHaveProperty("members");
        expect(responce.body.members).toStrictEqual([123, 456]);

        //console.log(responce.body);
        dbClear();
    });

    test("If the chat already exist, the request should return the code 400 (POST /createChat)", async () => {
        await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });
        
        const responce = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });
    
        expect(responce.statusCode).toBe(400);
        expect(responce.body.message).toStrictEqual("ERROR, Chat already exist");
        dbClear();
    });

    
    test("The request to receive the chat should return the code 200 (GET /getChat)", async () => {
        const responce = await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });

        const chat = await request(app).get("/chat/getChat").query({
            "id": responce.body._id
        });

        expect(chat.statusCode).toBe(200);
        expect(chat.body._id).toStrictEqual(responce.body._id);
        dbClear();
    });

    test("Request to receive a chat should return a 404 code if chat doesn exist (GET /getChat)", async () => {
        const responce = await request(app).get("/chat/getChat").query({
            "id": "66f219137586c3b69cab6b0a"
        });

        expect(responce.statusCode).toBe(404);
        expect(responce.body.message).toStrictEqual("ERROR, Chat not found");
        dbClear();
    });

    test("A request to receive all user chats should return the code 200", async () => {
        await request(app).post("/chat/createChat").send({
            "members": [123, 456]
        });

        await request(app).post("/chat/createChat").send({
            "members": [123, 777]
        });

        const responce = await request(app).get("/chat/getUserChats").query({
            "user_id": 123
        });

        expect(responce.statusCode).toBe(200);
        dbClear();
    });

});



