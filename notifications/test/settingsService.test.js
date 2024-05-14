const mongoose = require('mongoose');
const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const settingsService = require('../services/settingsService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("Settings creation", () => {
    test("Settings creation must be correct", async () => {
        let settings = await settingsService.createSettings({
            "user_id": "661e407c2fbfe93fd6620a73",
			"email_notifications": true, 
			"push_notifications": true, 
			"in_app_notifications": true
        });
        
        expect(settings).toBeTruthy();
        expect(settings).toHaveProperty("user_id");
        expect(settings).toHaveProperty("email_notifications");
        expect(settings).toHaveProperty("push_notifications");
        expect(settings).toHaveProperty("in_app_notifications");

        expect(1).toBe(1);
        await dbClear();
    });

    // test("Settings can be found by id", async () => {
    //     let settings = await settingsService.getSettingsById("661e407c2fbfe93fd6620a73");

    //     expect(settings).toBeTruthy();
    // });

   // test()
});


