const req = require('supertest');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const app = require('../app');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Reaction', () => {
    
    test('create reaction OK - code 201', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const res = await req(app).post('/reaction/createReaction').send({
            "post_id": post_id,
            "user_id": 999
        });

        expect(res.statusCode).toBe(201);
        expect(res.body).toHaveProperty("post_id");
        expect(res.body.post_id).toEqual(post_id);
        expect(res.body).toHaveProperty("user_id");
        expect(res.body.user_id).toEqual(999);

        await dbClear();
    });

    test('error in request parameters for creating reaction', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const res = await req(app).post('/reaction/createReaction').send({
            "ffddas": post_id,
            "user_id": "999"
        });

        expect(res.statusCode).toBe(500);
    });

    test('get reaction by ID OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const reaction = await req(app).post('/reaction/createReaction').send({
            "post_id": post_id,
            "user_id": 999
        });

        let reaction_id = reaction.body._id;
        const res = await req(app).get('/reaction/getReaction').query({
            "id": reaction_id
        });

        expect(res.statusCode).toBe(200);
        expect(res.body._id).toEqual(reaction_id);

        await dbClear();
    });

    test('get reaction by ID Error (not found the reaction) - code 404', async() => {
        const id = '6643904d7097c59c3a4d68bb';
        const res = await req(app).get('/reaction/getReaction').query({
            "id": id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Reaction not found");
    });

    test('get reaction by user OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const reaction = await req(app).post('/reaction/createReaction').send({
            "post_id": post_id,
            "user_id": 999
        });

        const res = await req(app).get('/reaction/getReactionByUser').query({
            "user_id": 999
        });

        expect(res.statusCode).toBe(200);
        expect(res.body.user_id).toEqual(999);
        expect(res.body._id).toEqual(reaction.body._id);

        await dbClear();
    });

    test('get reaction by user Error (not found the reaction) - code 404', async() => {
        const user_id = "123456";
        const res = await req(app).get('/reaction/getReactionByUser').query({
            "user_id": user_id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Reaction not found");
    });

    test('delete reaction OK - code 200', async() => {
        let data = {
            "author_id": 123, 
            "title": "title",
            "content": "Hello"
        }
        const post = await req(app).post('/post/createPost').send(data);

        let post_id = post.body._id;
        const reaction = await req(app).post('/reaction/createReaction').send({
            "post_id": post_id,
            "user_id": 999
        });

        let id = reaction.body.user_id;
        const res = await req(app).post('/reaction/deleteReaction').send({
            "post_id": post_id,
            "user_id": id
        });

        const deleted_reaction = await req(app).get('/reaction/getReaction').query({
            "user_id": id
        });

        expect(res.statusCode).toBe(200);
        expect(deleted_reaction.statusCode).toBe(404);

        await dbClear();
    });

    test('delete reaction Error (not found the reaction) - code 404', async() => {
        const id = "6643904d7097c59c3a4d68bb";
        const res = await req(app).post('/reaction/deleteReaction').send({
            "id": id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Reaction not found");
    });
});