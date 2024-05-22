const mongoose = require("mongoose");
const MONGO_URI = process.env.MONGO_URI || 'mongodb://joint_chat_db:27017/joint_chat';

const connectDB = async () => {
  try {
    await mongoose.connect(MONGO_URI, {
      // dbName: 'joint_chat',
      // user: 'joint_chat_admin',
      // pass: 'p;rXG1Y75E',
    });
    console.log("MongoDB was successfully connected");
  } catch (err) {
    console.error(err.message);
    process.exit(1);
  }
};

module.exports = {MONGO_URI, connectDB};
