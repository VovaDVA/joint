const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const notificationsService = require('../services/notificationsService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("Creating a notification", () => {
    test("Сreation of the notification must be correct", async () => {
        let id = "66f219137586c3b69cab6b0a";
        let type = "like";
        let content = "user liked your post";
        let is_read = false;

        const notification = await notificationsService.createNotification(id, type, content, is_read);
        
        expect(notification).toHaveProperty("user_id");
        expect(notification).toHaveProperty("type");
        expect(notification).toHaveProperty("content");
        expect(notification).toHaveProperty("is_read");

        await dbClear();
    });

    test("If the message is read, then the field must be true", async () => {
        let id = "66f219137586c3b69cab6b0a";
        let type = "like";
        let content = "user liked your post";
        let is_read = true;

        const notification = await notificationsService.createNotification(id, type, content, is_read);

        expect(notification).toHaveProperty("is_read");
        expect(notification.is_read).toBeTruthy();
        await dbClear();
    });

});

describe("Getting notification", () => {
    test("Getting notification by id must be correct", async () => {
        let id = "66f219137586c3b69cab6b0a";

        const savedNotification = await notificationsService.createNotification(id, "like", "some content", false);
        const notification = await notificationsService.getNotificationById(savedNotification._id)

        expect(notification).toBeTruthy();
        expect(notification.user_id).toStrictEqual(id);
        //console.log(notification);
        await dbClear();
    });

    test("If the notification does not exist, it should not be found", async () => {
        let id = "66f219137586c3b69cab6b0a";

        const notification = await notificationsService.getNotificationById(id);

        expect(notification).toBeFalsy();
    });
});

describe("Getting user notification", () => {
    test("Getting user notification should be correct", async () => {
        let target_id = "66f219137586c3b69cab6b0a";
        let another_id = "661f8097e7b772cececf523b";

        await notificationsService.createNotification(target_id, "like", "some content", false);
        await notificationsService.createNotification(target_id, "comment", "some content", false);
        await notificationsService.createNotification(another_id, "follow", "some content", false);

        const notificationList = await notificationsService.getUserNotifications(target_id);
        
        expect(notificationList).toBeTruthy();
        expect(notificationList.length).toBe(2);

        for(let notification of notificationList){
            expect(notification.user_id).toStrictEqual(target_id);
        }

    });
});



