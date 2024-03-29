const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.ObjectId;

const commentSchema = new mongoose.Schema({
  _id: ObjectId,
  post_id: ObjectId,
  author_id: String,
  content: String,
  created_at: Date,
  likes: [String]
});

module.exports = mongoose.model('Comments', commentSchema);