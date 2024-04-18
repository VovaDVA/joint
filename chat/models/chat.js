const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
    participitans: {
        type: [String],
        required: true
    }, 
    created_at:{ 
        type: Date,
        required: true
    },
    last_message_at: {
        type: Date,
        required: true
    }
});

module.exports = mongoose.model("Chat", chatSchema);