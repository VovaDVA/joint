/* В этом файле содердатся функции для действий с тестовой базой данных */ 

const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');

// Функция подключения к бд
exports.dbConnect = async () => {
  const mongoServer = await MongoMemoryServer.create();
  const uri = await mongoServer.getUri();
  await mongoose.connect(uri);
};

// Функция очистки базы данных (удаляются все записи)
exports.dbClear = async () => {
    const collections = mongoose.connection.collections;
    for (const key in collections) {
        const collection = collections[key];
        await collection.deleteMany();
    }
}

// Отключение от бд
exports.dbDisconnect = async () => {
  await mongoose.connection.dropDatabase();
  await mongoose.connection.close();
};