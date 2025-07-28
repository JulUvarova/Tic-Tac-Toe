// для работы с JWT авторизацией

async function refreshTokens() {
    const refreshToken = sessionStorage.getItem('refreshToken');
    if (!refreshToken) {
        window.location.href = '/ui/auth';
        return false;
    }

    try {
        // Сначала пробуем обновить только access token
        let response = await fetch('/auth/token', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({refreshToken: refreshToken})
        });

        if (response.ok) {
            const jwtResponse = await response.json();
            sessionStorage.setItem('accessToken', jwtResponse.accessToken);
            sessionStorage.setItem('tokenType', jwtResponse.type);
            return true;
        } else if (response.status === 401) {
            // Если access token не удалось обновить, пробуем обновить оба токена
            response = await fetch('/auth/refresh', {
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
                // Если refresh token тоже истек - авторизуем заново
                sessionStorage.clear();
                window.location.href = '/ui/auth';
                return false;
            }
        } else {
            sessionStorage.clear();
            window.location.href = '/ui/auth';
            return false;
        }
    } catch (error) {
        console.error('Ошибка при обновлении токенов:', error);
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

    // Проверяем, не истекает ли токен в ближайшие 5 минут
    if (isTokenExpiringSoon(accessToken)) {
        const refreshed = await refreshTokens();
        if (!refreshed) {
            window.location.href = '/ui/auth';
            return Promise.reject('Не удалось обновить токен');
        }
    }

    options.headers = options.headers || {};
    const currentAccessToken = sessionStorage.getItem('accessToken');
    const currentTokenType = sessionStorage.getItem('tokenType');
    options.headers['Authorization'] = currentTokenType + ' ' + currentAccessToken;

    try {
        let response = await fetch(url, options);
        
        //  пробуем обновить токен
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

// чекаем истекает ли токен в ближайшие 5 минут
function isTokenExpiringSoon(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expirationTime = payload.exp * 1000; //  в милисек
        const currentTime = Date.now();
        const fiveMinutesInMs = 5 * 60 * 1000;
        
        return (expirationTime - currentTime) < fiveMinutesInMs;
    } catch (error) {
        console.error('Ошибка при проверке токена:', error);
        return false;
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

function logout() {
    sessionStorage.clear();
    window.location.href = '/ui/auth';
}

function toggleProfileMenu() {
    const menu = document.getElementById('profile-menu');
    if (menu) {
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    }
}

function closeProfileMenu() {
    const menu = document.getElementById('profile-menu');
    if (menu) {
        menu.style.display = 'none';
    }
}

// Функция для инициализации обработчика закрытия меню профиля
function initProfileMenuHandler() {
    document.addEventListener('click', function(event) {
        const profileContainer = document.querySelector('.profile-container');
        const profileMenu = document.getElementById('profile-menu');
        
        if (profileContainer && !profileContainer.contains(event.target) && profileMenu) {
            profileMenu.style.display = 'none';
        }
    });
}

// Проверка авторизации при загрузке страницы
function checkAuth() {
    const accessToken = sessionStorage.getItem('accessToken');
    const tokenType = sessionStorage.getItem('tokenType');
    
    if (!accessToken || !tokenType) {
        window.location.href = '/ui/auth';
        return false;
    }
    
    startTokenRefreshTimer(); // регулярно проверяем не истекли ли токены
    
    return true;
}

// Функция для запуска таймера обновления токенов
function startTokenRefreshTimer() {
    setInterval(async () => {
        const accessToken = sessionStorage.getItem('accessToken');
        if (accessToken && isTokenExpiringSoon(accessToken)) {
            console.log('Автоматическое обновление токена...');
            await refreshTokens();
        }
    }, 2 * 60 * 1000);
} 