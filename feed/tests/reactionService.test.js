const mongoose = require('mongoose');
const {dbConnect, dbDisconnect, dbClear} = require('./mongo');
const reactionService = require('../services/reactionService');
const postService = require('../services/postService');

beforeAll(async () => await dbConnect()); 
afterAll(async () => await dbDisconnect());

describe('Reaction', () => {
    test('create reaction for post', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let reaction_data = {
            "post_id": post._id,
            "user_id": 123
        }
        const reaction = await reactionService.createReaction(reaction_data);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post._id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(123);

        await dbClear();
    });

    test('get reaction by ID', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let reaction_data = {
            "post_id": post._id,
            "user_id": 123
        }
        const Reaction = await reactionService.createReaction(reaction_data);

        let reaction_id = Reaction._id;
        const reaction = await reactionService.getReactionById(reaction_id);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post._id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(123);

        await dbClear();
    });

    test('get reaction by user', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let reaction_data = {
            "post_id": post._id,
            "user_id": 123
        }
        const Reaction = await reactionService.createReaction(reaction_data);

        const reaction = await reactionService.getReactionByUser(123);

        expect(reaction).toBeTruthy();
        expect(reaction).toHaveProperty("post_id");
        expect(reaction.post_id).toEqual(post._id);
        expect(reaction).toHaveProperty("user_id");
        expect(reaction.user_id).toEqual(123);

        await dbClear();
    });

    test('delete the reaction', async() => {
        let data = {
            "author_id": 1,
            "title": "title",
            "content": "Hello Joint!"
        };
        const post = await postService.createPost(data);

        let reaction_data = {
            "post_id": post._id,
            "user_id": 123
        }
        const Reaction = await reactionService.createReaction(reaction_data);

        const deleted_reaction = await reactionService.deleteReaction(123);
        const reaction = await reactionService.getReactionById(Reaction._id);

        expect(deleted_reaction).toBeTruthy();
        expect(reaction).toBeFalsy();

        await dbClear();
    });
});