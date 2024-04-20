const notificationsService = require('../services/notificationsService');

class notificationsController {
	constructor(notificationsService) {
    	this.notificationsService = notificationsService;
	}
	
	async createNotification(req, res) {
	  try {
	    const { user_id, type, content, created_at, is_read } = req.body;
	    const notification = await notificationsService.createNotification(user_id, type, content, created_at, is_read);
	    res.status(201).json(notification);
	  } catch (error) {
	    res.status(500).json({ message: error.message });
	  }
	}
	
	async getNotification(req, res) {
	  try {
	    const notificationId = req.params.id;
	    const notification = await notificationsService.getNotificationById(notificationId);
	    if (!notification) {
	      return res.status(404).json({ message: 'Notification not found' });
	    }

	    return res.status(201).json(notification);
	  } catch (error) {
	    res.status(500).json({ message: error.message });
	  }
	}

	async getUserNotifications(req, res){
		try {
            const user_id = req.query.user_id;
            const notifications = await notificationsService.getUserNotifications(user_id);
            return res.status(201).json(notifications);

        }catch(error){
            return res.status(500).json({message: error.message});
        }
    }
	
}


module.exports = new notificationsController();