import { getToken, getUserId } from "./auth";

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
        register: (data, callback, errorCallback) => this.post('/auth/register', data, callback, errorCallback),
        login: (data, callback, errorCallback) => this.post('/auth/login', data, callback, errorCallback),
        verifyCode: (data, callback, errorCallback) => this.post('/auth/verify-code', data, callback, errorCallback),
        getAll: (callback, errorCallback) => this.get('/auth/get-all', callback, errorCallback),
        // getUser: (callback) => this.get('/auth/user', callback),
        getUserById: (callback, userId) => this.get('/auth/user/get?userId=' + userId, callback),
        // Two Factor Auth
        enableTwoFactorAuth: (data, callback, errorCallback) => this.post('/auth/two-factor/enable', data, callback, errorCallback),
        disableTwoFactorAuth: (data, callback, errorCallback) => this.post('/auth/two-factor/disable', data, callback, errorCallback),
        // Password Reset
        sendPasswordResetCode: (data, callback, errorCallback) => this.post('/auth/request-reset-password?email=' + data.email, data, callback, errorCallback),
        confirmPasswordReset: (data, callback, errorCallback) => this.post('/auth/confirm-reset-password', data, callback, errorCallback),
        // Password Change
        changePassword: (data, callback, errorCallback) => this.post('/auth/change-password', data, callback, errorCallback),
        confirmChangePassword: (data, callback, errorCallback) => this.post('/auth/confirm-change-password', data, callback, errorCallback),
        // Delete
        delete: (data, callback, errorCallback) => this.delete('/auth/delete', data, callback, errorCallback),
        confirmDelete: (data, callback, errorCallback) => this.delete('/auth/confirm-delete', data, callback, errorCallback),
    }

    profile = {
        updateAvatar: (data, callback, errorCallback) => this.put('/profile/update-avatar', data, callback, errorCallback),
    }

    chat = {
        getUserChats: (callback) => this.get('/chat/getUserChats?user_id=' + getUserId(), callback),
        createChat: (data, callback) => this.post('/chat/createChat', data, callback),
    }

    message = {
        editMessage: (data, callback) => this.post('/message/editMessage', data, callback),
    }

    content = {
        getAllPosts: (callback) => this.get('/post/getAllPosts', callback),
        getPostsByAuthor: (callback) => this.get('/post/getPostsByAuthor?author_id=' + getUserId(), callback),
        createPost: (data, callback) => this.post('/post/createPost', data, callback),
    }
}


const apiClient = new ApiClient();
export default apiClient;
