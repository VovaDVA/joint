const mongoose = require('mongoose');

const notificatinsSchema = new mongoose.Schema({
    _id: ObjectId, //(Primary Key),
    user_id: String, //(ID пользователя, для которого предназначено уведомление)
    type: String, //(тип уведомления, например, "like", "comment", "follow", "message" и т. д.)
    content: String, //(содержание уведомления)
    created_at: Date, //(дата создания уведомления)
    is_read: Boolean //(индикатор прочтения уведомления)
});

module.exports = mongoose.model('Notifications', notificatinsSchema);