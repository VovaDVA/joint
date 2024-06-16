import { getToken, getUserId } from "./auth";

export const BASE_URL = 'http://82.97.245.142';
const PORT_AUTH = '8080';
const PORT_PROFILE = '8081';
const PORT_CHAT = '3000';
const PORT_FEED = '3001';
// const PORT_NOTIF = '3002';

class ApiClient {
    constructor() {
        if (!ApiClient.instance) {
            this.baseUrl = ''; 
            ApiClient.instance = this;
        }
        return ApiClient.instance;
    }

    getHeaders(url) {
        const token = getToken();
        let headers = { 'Content-Type': 'application/json' };

        if (token || token && url.includes('auth')) {
            headers['Authorization'] = 'Bearer ' + token;
        }
        return headers;
    }

    async handleResponse(response, callback, errorCallback) {
        const result = await response.json();
        console.log(result);

        if (!response.ok) {
            errorCallback(result);
        } else {
            callback(result);
        }
        
        return result;
    }

    async handleError(error) {
        console.log(error)
        return null;
    }

    async request(method, url, callback = () => {}, body = null, errorCallback = () => {}) {
        try {
            const response = await fetch(this.baseUrl + url, {
                method: method,
                headers: this.getHeaders(url),
                body: body
            });

            return this.handleResponse(response, callback, errorCallback);

        } catch (error) {
            return this.handleError(error);
        }
    }

    async get(url, callback, errorCallback) {
        await this.request('get', url, callback, errorCallback);
    }

    async post(url, data, callback, errorCallback) {
        await this.request('post', url, callback, JSON.stringify(data), errorCallback);
    }

    async put(url, data, callback, errorCallback) {
        await this.request('put', url, callback, JSON.stringify(data), errorCallback);
    }

    async delete(url, data, callback, errorCallback) {
        await this.request('delete', url, callback, JSON.stringify(data), errorCallback);
    }

    auth = {
        register: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/register`, data, callback, errorCallback),
        login: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/login`, data, callback, errorCallback),
        verifyCode: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/verify-code`, data, callback, errorCallback),
        getAll: (callback, errorCallback) => this.get(`${BASE_URL}:${PORT_AUTH}/auth/get-all`, callback, errorCallback),
        // getUser: (callback) => this.get('/auth/user', callback),
        getUserById: (callback, userId) => this.get(`${BASE_URL}:${PORT_AUTH}/auth/user/get?userId=` + userId, callback),
        // Two Factor Auth
        enableTwoFactorAuth: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/two-factor/enable`, data, callback, errorCallback),
        disableTwoFactorAuth: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/two-factor/disable`, data, callback, errorCallback),
        // Password Reset
        sendPasswordResetCode: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/request-reset-password?email=` + data.email, data, callback, errorCallback),
        confirmPasswordReset: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/confirm-reset-password`, data, callback, errorCallback),
        // Password Change
        changePassword: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/change-password`, data, callback, errorCallback),
        confirmChangePassword: (data, callback, errorCallback) => this.post(`${BASE_URL}:${PORT_AUTH}/auth/confirm-change-password`, data, callback, errorCallback),
        // Delete
        delete: (data, callback, errorCallback) => this.delete(`${BASE_URL}:${PORT_AUTH}/auth/delete`, data, callback, errorCallback),
        confirmDelete: (data, callback, errorCallback) => this.delete(`${BASE_URL}:${PORT_AUTH}/auth/confirm-delete`, data, callback, errorCallback),
    }

    profile = {
        update: (data, callback, errorCallback) => this.put(`${BASE_URL}:${PORT_PROFILE}/profile/update`, data, callback, errorCallback),
        updateAvatar: (data, callback, errorCallback) => this.put(`${BASE_URL}:${PORT_PROFILE}/profile/update-avatar`, data, callback, errorCallback),
    }

    chat = {
        getUserChats: (callback) => this.get(`${BASE_URL}:${PORT_CHAT}/chat/getUserChats?user_id=` + getUserId(), callback),
        createChat: (data, callback) => this.post(`${BASE_URL}:${PORT_CHAT}/chat/createChat`, data, callback),
    }

    message = {
        editMessage: (data, callback) => this.post(`${BASE_URL}:${PORT_CHAT}/message/editMessage`, data, callback),
    }

    content = {
        getAllPosts: (callback) => this.get(`${BASE_URL}:${PORT_FEED}/post/getAllPosts`, callback),
        getPostsByAuthor: (callback) => this.get(`${BASE_URL}:${PORT_FEED}/post/getPostsByAuthor?author_id=` + getUserId(), callback),
        createPost: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/post/createPost`, data, callback),
        deletePost: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/post/deletePost`, data, callback),
        // Reactions
        react: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/reaction/createReaction`, data, callback),
        deleteReaction: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/reaction/deleteReaction`, data, callback),
        // Comments
        getPostComments: (callback, postId) => this.get(`${BASE_URL}:${PORT_FEED}/comment/getPostComments?postId=${postId}`, callback),
        comment: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/comment/createComment`, data, callback),
        deleteComment: (data, callback) => this.post(`${BASE_URL}:${PORT_FEED}/comment/deleteComment`, data, callback),
    }
}


const apiClient = new ApiClient();
export default apiClient;
