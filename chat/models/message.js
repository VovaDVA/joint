const mongoose = require("mongoose");

const messageSchema = new mongoose.Schema({
    chat_id: {
        type: mongoose.Schema.Types.ObjectId, 
        ref: 'Chat',
        required: true
    },
    sender_id: {
        type: Number,
        required: true
    },
    text: {
        type: String,
        minlength: 1,
        maxlength: 10000,
        required: true
    },
    created_at: {
        type: Date,
    },
    edited: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model("Message", messageSchema);