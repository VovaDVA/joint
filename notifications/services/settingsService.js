const NotificationSettins = require('../models/notification_settings.js');

class settingsService {
	constructor(NotificationSettins) {
    this.NotificationSettins = NotificationSettins;
	}
	
	async createSettings(data) {
		const settings = new NotificationSettins({ 
			"user_id": data.user_id,
			"email_notifications": data.email_notifications, 
			"push_notifications": data.push_notifications, 
			"in_app_notifications": data.in_app_notifications });
		await settings.save();
		return settings;
	}

	async updateSettings(data) {
		const notif = await NotificationSettins.findByIdAndUpdate(data.id, {
			"user_id": data.user_id,
			"email_notifications": data.email_notifications, 
			"push_notifications": data.push_notifications, 
			"in_app_notifications": data.in_app_notifications
		}, {new: true});
		//console.log(notif);
		return notif;
	}

	async getSettingsById(settingsId) {
	  return await NotificationSettins.findById(settingsId);
	}
}
// Другие методы сервиса

module.exports = new settingsService;