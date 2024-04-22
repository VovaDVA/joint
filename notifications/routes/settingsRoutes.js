const express = require('express');
const router = express.Router();
const settingsController = require('../controllers/settingsController');

router.post('/createNotificationsSettings', settingsController.createSettings);
router.get('/getNotificationsSettings/:id', settingsController.getSettings);

module.exports = router;