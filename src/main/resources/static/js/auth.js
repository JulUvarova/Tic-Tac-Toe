// Общие функции для работы с JWT авторизацией

async function refreshTokens() {
    const refreshToken = sessionStorage.getItem('refreshToken');
    if (!refreshToken) {
        window.location.href = '/ui/auth';
        return false;
    }

    try {
        const response = await fetch('/auth/refresh', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({refreshToken: refreshToken})
        });

        if (response.ok) {
            const jwtResponse = await response.json();
            sessionStorage.setItem('accessToken', jwtResponse.accessToken);
            sessionStorage.setItem('refreshToken', jwtResponse.refreshToken);
            sessionStorage.setItem('tokenType', jwtResponse.type);
            return true;
        } else {
            // Если refresh token тоже истек, перенаправляем на авторизацию
            sessionStorage.clear();
            window.location.href = '/ui/auth';
            return false;
        }
    } catch (error) {
        sessionStorage.clear();
        window.location.href = '/ui/auth';
        return false;
    }
}

async function authFetch(url, options = {}) {
    const accessToken = sessionStorage.getItem('accessToken');
    const tokenType = sessionStorage.getItem('tokenType');
    
    if (!accessToken || !tokenType) {
        window.location.href = '/ui/auth';
        return Promise.reject('Нет авторизации');
    }

    options.headers = options.headers || {};
    options.headers['Authorization'] = tokenType + ' ' + accessToken;

    try {
        let response = await fetch(url, options);
        
        // Если получили 401, пробуем обновить токен
        if (response.status === 401) {
            const refreshed = await refreshTokens();
            if (refreshed) {
                const newAccessToken = sessionStorage.getItem('accessToken');
                const newTokenType = sessionStorage.getItem('tokenType');
                options.headers['Authorization'] = newTokenType + ' ' + newAccessToken;
                response = await fetch(url, options);
            }
        }
        
        return response;
    } catch (error) {
        console.error('Network error:', error);
        throw error;
    }
}

function goToMyProfile() {
    const userId = sessionStorage.getItem('userId');
    if (userId) {
        window.location.href = '/ui/user/' + userId;
    } else {
        authFetch('/auth/me')
            .then(res => res.ok ? res.json() : Promise.reject())
            .then(user => {
                sessionStorage.setItem('userId', user.id);
                window.location.href = '/ui/user/' + user.id;
            })
            .catch(() => alert('Ошибка получения профиля пользователя.'));
    }
}

// Проверка авторизации при загрузке страницы
function checkAuth() {
    const accessToken = sessionStorage.getItem('accessToken');
    const tokenType = sessionStorage.getItem('tokenType');
    
    if (!accessToken || !tokenType) {
        window.location.href = '/ui/auth';
        return false;
    }
    return true;
} 