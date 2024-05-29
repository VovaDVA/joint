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

    test("Settings can be found by id", async () => {
        let created_settings = await settingsService.createSettings({
            "user_id": "661e407c2fbfe93fd6620a73",
			"email_notifications": true, 
			"push_notifications": true, 
			"in_app_notifications": true
        });
        
        let settings = await settingsService.getSettingsById(created_settings._id);

        expect(settings).toBeTruthy();
        expect(settings._id).toStrictEqual(created_settings._id);
        await dbClear();
    });

    test("Settings must be updated correctly", async () => {
        let created_settings = await settingsService.createSettings({
            "user_id": "661e407c2fbfe93fd6620a73",
			"email_notifications": true, 
			"push_notifications": true, 
			"in_app_notifications": true
        });

        let updated_settings = await settingsService.updateSettings({
            "id": created_settings._id,
            "user_id": "661e407c2fbfe93fd6620a73",
			"email_notifications": false, 
			"push_notifications": false, 
			"in_app_notifications": false
        });

        expect(updated_settings).toBeTruthy();
        console.log(updated_settings);
        //expect(updated_settings.email_notifications).toBe(false);
    });

});


