const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');

exports.dbConnect = async () => {
  const mongoServer = await MongoMemoryServer.create();
  const uri = await mongoServer.getUri();
  await mongoose.connect(uri);
};

exports.dbClear = async () => {
    const collections = mongoose.connection.collections;
    for (const key in collections) {
        const collection = collections[key];
        await collection.deleteMany();
    }
};