const NotificationSettins = require('../models/notification_settings.js');

class settingsService {
	constructor(NotificationSettins) {
    this.NotificationSettins = NotificationSettins;
	}
	
	async createSettings( user_id, email_notifications, push_notifications, in_app_notifications) {
		const settings = new NotificationSettins({ 
			"user_id": user_id,
			"email_notifications": email_notifications, 
			"push_notifications": push_notifications, 
			"in_app_notifications": in_app_notifications });
		await settings.save();
		return settings;
	}
	
	async getSettingsById(settingsId) {
	  return await NotificationSettins.findById(settingsId);
	}
}
// Другие методы сервиса

module.exports = new settingsService;