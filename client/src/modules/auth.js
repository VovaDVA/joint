export async function saveToken(token) {
    localStorage.setItem('jwtToken', token);
    await isTokenValid(token);
}

export function getToken() {
    return localStorage.getItem('jwtToken');
}

export async function checkToken() {
    const jwtToken = localStorage.getItem('jwtToken');
    const currentPath = window.location.pathname;

    const tokenValid = await isTokenValid(jwtToken);

    if (jwtToken && tokenValid) {
        if (currentPath === '/login' || currentPath === '/register') {
            window.location.href = '/';
        }
    } else {
        if (currentPath !== '/login' && currentPath !== '/register') {
            window.location.href = '/login';
        }
    }
}

async function isTokenValid(token) {
    try {
        const response = await fetch('/auth/user', {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'  // Пример добавления других заголовков
            }
        });

        const data = await response.json();
        console.log(data);

        if (data['userId']) {
            saveUserData(data);
            return true;
        }
        
        deleteSession();
        return false;

    } catch (error) {
        console.error(error);
    }
}

export function removeToken() {
    localStorage.removeItem('jwtToken');
}

function saveUserData(data) {
    localStorage.setItem('user', JSON.stringify(data));
}

export function getUser() {
    return JSON.parse(localStorage.getItem('user'));
}

export function isUserIdEqual(id) {
    const user = getUser();
    if (!user) return;
    return id != getUser().userId;
}

export function getUserId() {
    const user = getUser();
    if (!user) return null;
    return user.userId;
}

export function getUserName() {
    const user = getUser();
    if (!user) return '-';
    return user.firstName + ' ' + user.lastName;
}

export function getUserDescription() {
    const user = getUser();
    if (!user) return '-';
    return user.description;
}

export function getUserAvatar() {
    const user = getUser();
    if (!user) return null;
    return user.avatar;
}

export function getUserBanner() {
    const user = getUser();
    if (!user) return null;
    return user.banner;
}

export function deleteSession() {
    const user = getUser();
    if (user) {
        localStorage.removeItem('user');
        localStorage.removeItem('jwtToken');
        checkToken();
    }
}

export async function getUserById(userId) {
    try {
        const response = await fetch('/auth/user/get?userId=' + userId, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getToken(),
                'Content-Type': 'application/json'  // Пример добавления других заголовков
            }
        });

        const data = await response.json();
        console.log(data);

        return data;

    } catch (error) {
        console.error(error);
    }
}
