const mongoose = require("mongoose");

const messageSchema = new mongoose.Schema({
    chat_id: {
        type: mongoose.Schema.Types.ObjectId, ref: 'Chat',
        required: true
    },
    sender_id: {
        type: String,
        required: true
    },
    text: {
        type: String,
        minlength: 1,
        maxlength: 10000
    },
    created_at: {
        type: Date,
    }
});

module.exports = mongoose.model("Message", messageSchema);