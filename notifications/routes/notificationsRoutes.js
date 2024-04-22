const express = require('express');
const router = express.Router();
const notificationsController = require('../controllers/notificationsController');

router.post('/createNotifications', notificationsController.createNotification);
router.get('/getNotification/:id', notificationsController.getNotification);
router.get('/getUserNotifications', notificationsController.getUserNotifications);


module.exports = router;
