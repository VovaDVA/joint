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

        if (data['id']) {
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
    console.log(data);
    localStorage.setItem('user', JSON.stringify({firstName: data['firstName'], lastName: data['lastName']}));
}

export function getUser() {
    return localStorage.getItem('user');
}

export function deleteSession() {
    const user = localStorage.getItem('user');
    if (user) {
        localStorage.removeItem('user');
        localStorage.removeItem('jwtToken');
        checkToken();
    }
}

export async function getUserById(userId) {
    try {
        const response = await fetch('/auth/get?id=' + userId, {
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
