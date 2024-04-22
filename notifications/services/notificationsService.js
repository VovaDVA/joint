const Notification = require('../models/notifications');

class notificationsService {
	constructor(Notification) {
    	this.Notification = Notification;
	}
	
	async createNotification( user_id, type, content, is_read) {
		let date = new Date();
		const notification = new Notification({ 
			"user_id": user_id,
			"type": type,
			"content": content,
			"created_at": date,
			"is_read": is_read });
		await notification.save();
	  	return notification;
	}
	
	async getNotificationById(notificationId) {
	  return await Notification.findById(notificationId);
	}

	async getUserNotifications(user_id){
        return await Notification.find({"user_id": user_id
    });
    }

}
// Другие методы сервиса

module.exports = new notificationsService;