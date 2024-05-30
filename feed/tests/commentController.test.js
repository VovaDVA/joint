const req = require('supertest');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const app = require('../app');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Comment controller', () => {
    
    test('create comment OK - code 201', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const res = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        expect(res.statusCode).toBe(201);
        expect(res.body).toHaveProperty("post_id");
        expect(res.body.post_id).toEqual(post_id);
        expect(res.body).toHaveProperty("author_id");
        expect(res.body.author_id).toEqual("999");
        expect(res.body).toHaveProperty("content");
        expect(res.body.content).toEqual("Some words");

        await dbClear();
    });

    test('error in request parameters for creating comment', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const res = await req(app).post('/comment/createComment').send({
            "qwerty": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        expect(res.statusCode).toBe(500);
    });
    
    test('get comment by ID OK - code 200', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const comment = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        let comment_id = comment.body._id;
        const res = await req(app).get('/comment/getComment').query({
            "id": comment_id
        });

        expect(res.statusCode).toBe(200);
        expect(res.body._id).toEqual(comment_id);

        await dbClear();
    });

    test('get comment by ID Error (not found the comment) - code 404', async() => {
        const res = await req(app).get('/comment/getComment').query({
            "id": "66434a746113960ccba51082"
        })

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Comment not found");
    });

    test('new like for comment OK - code 201', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const comment = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        let comment_id = comment.body._id;
        const res = await req(app).post('/comment/newLike').send({
            "comment_id": comment_id,
            "user_id": "999"
        });

        expect(res.statusCode).toBe(201);
        expect(res.body.likes).toEqual(["999"]);
        
        await dbClear();
    });

    test('edit comment OK - code 201', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const comment = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        let comment_id = comment.body._id;
        const res = await req(app).post('/comment/editComment').send({
            "comment_id": comment_id,
            "post_id": post_id,
            "content": "New content"
        });

        expect(res.statusCode).toBe(201);
        expect(res.body.content).toEqual("New content");

        await dbClear();
    });

    test('edit comment Error (not found the comment) - code 404', async() => {
        const id = "66434a746113960ccba51082";
        const res = await req(app).post('/comment/editComment').send({
            "id": id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Comment not found");
    });

    test('delete comment OK - code 200', async() => {
        const post = await req(app).post('/post/createPost').send({
            "author_id": "123", 
            "content": "Hello"
        });

        let post_id = post.body._id;
        const comment = await req(app).post('/comment/createComment').send({
            "post_id": post_id,
            "author_id": "999",
            "content": "Some words"
        });

        let comment_id = comment.body._id;
        const res = await req(app).get('/comment/deleteComment').query({
            "post_id": post_id,
            "id": comment_id
        });

        const deleted_comment = await req(app).get('/comment/getComment').query({
            "id": comment_id
        });
        
        expect(res.statusCode).toBe(200);
        expect(deleted_comment.statusCode).toBe(404);

        await dbClear();
    });

    test('delete comment Error (not found the comment) - code 404', async() => {
        const id = "66434a746113960ccba51082";
        const res = await req(app).get('/comment/deleteComment').send({
            "id": id
        });

        expect(res.statusCode).toBe(404);
        expect(res.body.message).toEqual("Comment not found");
    });
});