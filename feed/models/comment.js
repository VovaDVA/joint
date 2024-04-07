const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const commentSchema = new mongoose.Schema({
  post_id: {
    type: ObjectId,
    required: true
  },
  author_id: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  created_at: {
    type: Date,
    required: true
  },
  likes: [String]
});

module.exports = mongoose.model('Comment', commentSchema);