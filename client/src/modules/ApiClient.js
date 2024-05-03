import { getToken, getUser } from "./auth";

class ApiClient {
    constructor() {
        if (!ApiClient.instance) {
            this.baseUrl = ''; // https://ssou.ru
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

    async handleResponse(response, callback) {
        if (!response.ok) {
            throw new Error('Ошибка получения данных');
        }

        const result = await response.json();
        callback(result);
        return result;
    }

    async handleError(error) {
        console.error(error);
        return null;
    }

    async request(method, url, callback, body = null) {
        try {
            const response = await fetch(this.baseUrl + url, {
                method: method,
                headers: this.getHeaders(url),
                body: body
            });

            return this.handleResponse(response, callback);

        } catch (error) {
            return this.handleError(error);
        }
    }

    async get(url, callback) {
        await this.request('get', url, callback);
    }

    async post(url, data, callback) {
        await this.request('post', url, callback, JSON.stringify(data));
    }

    async delete(url, data, callback) {
        await this.request('delete', url, callback, JSON.stringify(data));
    }

    auth = {
        register: (data, callback) => this.post('/auth/register', data, callback),
        login: (data, callback) => this.post('/auth/login', data, callback),
        getAll: (callback) => this.get('/auth/get-all', callback),
        getUserById: (callback, userId) => this.get('/auth/user/get?userId=' + userId, callback),
        // Two Factor Auth
        enableTwoFactorAuth: (data, callback) => this.post('/auth/two-factor/enable', data, callback),
        disableTwoFactorAuth: (data, callback) => this.post('/auth/two-factor/disable', data, callback),
        // Password Reset
        sendPasswordResetCode: (data, callback) => this.post('/auth/request-reset-password?email=' + data.email, data, callback),
        confirmPasswordReset: (data, callback) => this.post('/auth/confirm-reset-password', data, callback),
        // Password Change
        changePassword: (data, callback) => this.post('/auth/change-password', data, callback),
        confirmChangePassword: (data, callback) => this.post('/auth/confirm-change-password', data, callback),
        // Delete
        delete: (callback) => this.delete('/auth/delete', callback),
        confirmDelete: (data, callback) => this.delete('/auth/confirm-delete', data, callback),
    }

    chat = {
        getUserChats: (callback) => this.get('/chat/getUserChats?user_id=' + getUser().userId, callback),
        createChat: (data, callback) => this.post('/chat/createChat', data, callback),
    }

    message = {
        editMessage: (data, callback) => this.post('/message/editMessage', data, callback),
    }

    content = {
        getAllPosts: (callback) => this.get('/post/getAllPosts', callback),
        getPostsByAuthor: (callback) => this.get('/post/getPostsByAuthor?author_id=' + getUser().userId, callback),
        createPost: (data, callback) => this.post('/post/createPost', data, callback),
    }
}


const apiClient = new ApiClient();
export default apiClient;
