export function saveToken(token) {
    localStorage.setItem('jwtToken', token);
}

export function getToken() {
    return localStorage.getItem('jwtToken');
}

export function checkToken() {
    const jwtToken = localStorage.getItem('jwtToken');
    const currentPath = window.location.pathname;

    if (jwtToken && isTokenValid(jwtToken)) {
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
        } else {
            // alert(data['message']);
        }

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

export function getUserId(id) {
    const user = getUser();
    if (!user) return;
    return id != getUser().userId;
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
