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
        const response = await fetch('/auth/getUserByToken', {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'  // Пример добавления других заголовков
            }
        });

        const data = await response.json();

        if (data['status'] == '200') {
            saveUserData(data);
        } else {
            alert(data['message']);
        }

    } catch (error) {
        console.error(error);
    }
}

export function removeToken() {
    localStorage.removeItem('jwtToken');
}

function saveUserData(data) {
    localStorage.setItem('user', JSON.stringify({username: data['username'], role: data['role']}));
}

export function deleteSession() {
    const user = localStorage.getItem('user');
    if (user) {
        localStorage.removeItem('user');
        localStorage.removeItem('jwtToken');
        checkToken();
    }
}
