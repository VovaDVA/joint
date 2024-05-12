const mongoose = require('mongoose');
const {dbConnect, dbClear, dbDisconnect} = require('./dbConnection');
const settingsService = require('../services/settingsService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe("Settings creation", () => {
    test("test1", () => {
        expect(1).toBe(1);
    });
});


