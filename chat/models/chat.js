const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
    members: {
        type: [Number],
        required: true
    }, 
    created_at:{ 
        type: Date,
        required: true
    },
    last_message_at: {
        type: Date,
        default: new Date()
        //required: true
    },
    last_message: {
        type: String,
        default: '-',
        //required: true
    }
});

module.exports = mongoose.model("Chat", chatSchema);