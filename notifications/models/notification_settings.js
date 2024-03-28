const mongoose = require('mongoose');

const notification_settingsSchema = new mongoose.Schema({
    _id: ObjectId, //(Primary Key)
    user_id: String, //(ID пользователя, для которого установлены настройки уведомлений)
    email_notifications: Boolean, //(флаг, разрешающий/запрещающий уведомления по электронной почте)
    push_notifications: Boolean, //(флаг, разрешающий/запрещающий push-уведомления)
    in_app_notifications: Boolean, //(флаг, разрешающий/запрещающий уведомления в приложении)
});

module.exports = mongoose.model('NotificationSettings', notificatinsSettingsSchema);