const settingsService = require('../services/settingsService');

class settingsController {
	constructor(settingsService) {
    this.settingsService = settingsService;
	}
	
	async createSettings(req, res) {
	  try {
	    const { user_id, email_notifications, push_notifications, in_app_notifications } = req.body;
	    const settings = await settingsService.createSettings(user_id, email_notifications, push_notifications, in_app_notifications);
	    res.status(201).json(settings);
	  } catch (error) {
	    res.status(500).json({ message: error.message });
	  }
	}
	
	async getSettings(req, res) {
	  try {
	    const settingsId = req.params.id;
	    const settings = await settingsService.getSettingsById(settingsId);
	    if (!settings) {
	      return res.status(404).json({ message: 'Notification settings not found' });
	    }
	    return res.status(201).json(settings);
	  } catch (error) {
	    res.status(500).json({ message: error.message });
	  }
	}
}

module.exports = new settingsController();