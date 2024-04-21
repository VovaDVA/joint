const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const notificatinsSchema = new mongoose.Schema({
    user_id: String, //(ID пользователя, для которого предназначено уведомление)
    type: String, //(тип уведомления, например, "like", "comment", "follow", "message" и т. д.)
    content: String, //(содержание уведомления)
    created_at:{
        type: Date,
        required: true
    },
    is_read: Boolean //(индикатор прочтения уведомления)
});

module.exports = mongoose.model('Notifications', notificatinsSchema);