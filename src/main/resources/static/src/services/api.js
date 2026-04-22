// src/services/api.js
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: { 'Content-Type': 'application/json' }
});

// Перехватчик для добавления JWT токена
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    // Для гостевой корзины добавляем X-Session-Key
    const sessionKey = localStorage.getItem('sessionKey');
    if (sessionKey && !config.headers['X-Session-Key']) {
        config.headers['X-Session-Key'] = sessionKey;
    }

    return config;
});

// Перехватчик для refresh токена
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const refreshToken = localStorage.getItem('refreshToken');
            if (refreshToken) {
                try {
                    const response = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
                    const { accessToken, refreshToken: newRefreshToken } = response.data;

                    localStorage.setItem('accessToken', accessToken);
                    localStorage.setItem('refreshToken', newRefreshToken);

                    originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                    return api(originalRequest);
                } catch (refreshError) {
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('refreshToken');
                    localStorage.removeItem('user');
                    window.location.href = '/login';
                }
            }
        }

        return Promise.reject(error);
    }
);

// Генерация guest session key
const getOrCreateSessionKey = () => {
    let sessionKey = localStorage.getItem('sessionKey');
    if (!sessionKey) {
        sessionKey = 'SESSION_' + crypto.randomUUID();
        localStorage.setItem('sessionKey', sessionKey);
    }
    return sessionKey;
};

// ========== АВТОРИЗАЦИЯ ==========
export const auth = {
    register: (data) => api.post('/register', data),
    login: (data) => api.post('/login', data),
    refresh: (refreshToken) => api.post('/auth/refresh', { refreshToken }),
    logout: () => api.post('/auth/logout'),
    changePassword: (oldPassword, newPassword) =>
        api.put('/profile/me/password', null, { params: { oldPassword, newPassword } })
};

// ========== ТОВАРЫ ==========
export const products = {
    getAll: (params) => api.get('/products', { params }),
    getById: (id) => api.get(`/products/${id}`),
    getByCategory: (categoryId, params) => api.get(`/products/category/${categoryId}`, { params }),
    search: (q, params) => api.get('/products/search', { params: { q, ...params } }),
    getNew: (limit = 12) => api.get('/products/new', { params: { limit } }),
    getBestsellers: (limit = 12) => api.get('/products/bestsellers', { params: { limit } }),
    delete: (id) => api.delete(`/products/${id}`)
};

// ========== КАТЕГОРИИ ==========
export const categories = {
    getAll: () => api.get('/categories/roots'),
    getTree: () => api.get('/categories/tree'),
    getBySlug: (slug) => api.get(`/categories/${slug}`)
};

// ========== КОРЗИНА ==========
export const cart = {
    get: () => api.get('/cart', {
        headers: { 'X-Session-Key': getOrCreateSessionKey() }
    }),
    add: (productId, quantity = 1) => api.post('/cart/add', { productId, quantity }, {
        headers: { 'X-Session-Key': getOrCreateSessionKey() }
    }),
    update: (productId, quantity) => api.put(`/cart/update?productId=${productId}&quantity=${quantity}`, null, {
        headers: { 'X-Session-Key': getOrCreateSessionKey() }
    }),
    remove: (productId) => api.delete(`/cart/remove/${productId}`, {
        headers: { 'X-Session-Key': getOrCreateSessionKey() }
    }),
    clear: () => api.delete('/cart/clear', {
        headers: { 'X-Session-Key': getOrCreateSessionKey() }
    })
};

// ========== ЗАКАЗЫ ==========
export const orders = {
    create: (data) => api.post('/orders', data),
    getMyOrders: (params) => api.get('/orders/me', { params }),
    getById: (id) => api.get(`/orders/me/${id}`),
    cancel: (id) => api.post(`/orders/me/${id}/cancel`),
    getAll: (params) => api.get('/orders', { params }),
    updateStatus: (id, status) => api.put(`/orders/${id}/status?status=${status}`)
};

// ========== ПОЛЬЗОВАТЕЛИ (для админа) ==========
export const users = {
    getAll: (params) => api.get('/users', { params }),
    getById: (id) => api.get(`/users/${id}`),
    updateRole: (id, role) => api.put(`/users/${id}/role?role=${role}`),
    updateActive: (id, active) => api.put(`/users/${id}/active?active=${active}`),
    delete: (id) => api.delete(`/users/${id}`)
};

// ========== СБОРКИ ПК ==========
export const pcBuilds = {
    getPublic: (params) => api.get('/pc-builds/public', { params }),
    getMyBuilds: (params) => api.get('/pc-builds/me', { params }),
    getMyBuild: (id) => api.get(`/pc-builds/me/${id}`),
    create: (data) => api.post('/pc-builds/me', data),
    update: (id, name, isPublic) => api.put(`/pc-builds/me/${id}?name=${name}&isPublic=${isPublic}`),
    delete: (id) => api.delete(`/pc-builds/me/${id}`),
    addComponent: (buildId, productId, quantity = 1) =>
        api.post(`/pc-builds/me/${buildId}/components/${productId}?quantity=${quantity}`),
    removeComponent: (buildId, productId) =>
        api.delete(`/pc-builds/me/${buildId}/components/${productId}`),
    getComponents: (buildId) => api.get(`/pc-builds/me/${buildId}/components`),
    getTotalPrice: (buildId) => api.get(`/pc-builds/me/${buildId}/total`),
    getPublicBuild: (id) => api.get(`/pc-builds/public/${id}`),
    searchPublic: (q, params) => api.get('/pc-builds/public/search', { params: { q, ...params } })
};

// ========== ТИПЫ КОМПОНЕНТОВ (для конфигуратора) ==========
export const componentTypes = {
    getAll: () => api.get('/component-types'),
    getOrdered: () => api.get('/component-types/ordered'),
    getByStep: (step) => api.get(`/component-types/step/${step}`),
    getFirst: () => api.get('/component-types/first'),
    getLast: () => api.get('/component-types/last'),
    getProgress: (currentStep) => api.get(`/component-types/progress/${currentStep}`),
    getTotalSteps: () => api.get('/component-types/total-steps')
};

// ========== ОТЗЫВЫ ==========
export const feedbacks = {
    getProductReviews: (productId, params) => api.get(`/feedbacks/products/${productId}/reviews`, { params }),
    getProductQuestions: (productId, params) => api.get(`/feedbacks/products/${productId}/questions`, { params }),
    getRating: (productId) => api.get(`/feedbacks/products/${productId}/rating`),
    create: (data) => api.post('/feedbacks', data),
    delete: (id) => api.delete(`/feedbacks/me/${id}`),
    getMy: (params) => api.get('/feedbacks/me', { params }),
    getPendingModeration: (params) => api.get('/feedbacks/pending', { params }),
    getAll: (params) => api.get('/feedbacks', { params }),
    publish: (id) => api.post(`/feedbacks/${id}/publish`)
};

// ========== ЗАЯВКИ НА РЕМОНТ ==========
export const repairRequests = {
    create: (data) => api.post('/repair-requests', data),
    getMyRequests: (params) => api.get('/repair-requests/me', { params }),
    getById: (id) => api.get(`/repair-requests/me/${id}`),
    cancel: (id) => api.post(`/repair-requests/me/${id}/cancel`),
    getAll: (params) => api.get('/repair-requests', { params }),
    updateStatus: (id, status) => api.put(`/repair-requests/${id}/status?status=${status}`)
};

// ========== НАСТРОЙКИ САЙТА ==========
export const settings = {
    getPublic: () => api.get('/settings/public'),
    getDeliveryInfo: (orderAmount) => api.get('/settings/delivery-info', { params: { orderAmount } }),
    getContactInfo: () => api.get('/settings/contact-info')
};

// ========== ЧАТ ==========
export const chat = {
    createSession: (sourceUrl, contextType, contextId) =>
        api.post('/chat/session', null, { params: { sourceUrl, contextType, contextId } }),
    sendMessage: (sessionId, message) =>
        api.post('/chat/message', { sessionId, message }),
    getMessages: (sessionId) => api.get(`/chat/session/${sessionId}/messages`),
    getLatestMessages: (sessionId, limit = 20) =>
        api.get(`/chat/session/${sessionId}/messages/latest`, { params: { limit } }),
    getMySessions: (params) => api.get('/chat/me/sessions', { params }),
    closeSession: (sessionId) => api.post(`/chat/me/session/${sessionId}/close`),
    getPendingSessions: () => api.get('/chat/consultant/pending'),
    takeSession: (sessionId) => api.post(`/chat/consultant/session/${sessionId}/take`),
    sendConsultantMessage: (sessionId, message) =>
        api.post(`/chat/consultant/session/${sessionId}/message?message=${encodeURIComponent(message)}`),
    getConsultantSessions: (params) => api.get('/chat/consultant/sessions', { params }),
    getActiveConsultantSessions: () => api.get('/chat/consultant/active'),
    closeConsultantSession: (sessionId) => api.post(`/chat/consultant/session/${sessionId}/close`),
    getAllSessions: (params) => api.get('/chat/sessions', { params }),
    getChatStatistics: () => api.get('/chat/statistics'),
    getCurrentLoad: () => api.get('/chat/load')
};

// ========== СРАВНЕНИЕ ==========
export const comparisons = {
    getMyNames: () => api.get('/relations/comparisons'),
    getComparison: (name) => api.get(`/relations/comparisons/${name}`),
    getComparisonGrouped: (name) => api.get(`/relations/comparisons/${name}/grouped`),
    addProduct: (name, productId) => api.post(`/relations/comparisons/${name}/products/${productId}`),
    addPcBuild: (name, pcBuildId) => api.post(`/relations/comparisons/${name}/pc-builds/${pcBuildId}`),
    addToComparison: (name, contentType, objectId) => {
        if (contentType === 'Product') {
            return api.post(`/relations/comparisons/${name}/products/${objectId}`);
        } else {
            return api.post(`/relations/comparisons/${name}/pc-builds/${objectId}`);
        }
    },
    removeProduct: (name, productId) => api.delete(`/relations/comparisons/${name}/products/${productId}`),
    removePcBuild: (name, pcBuildId) => api.delete(`/relations/comparisons/${name}/pc-builds/${pcBuildId}`),
    deleteComparison: (name) => api.delete(`/relations/comparisons/${name}`),
    createComparison: (name) => api.post(`/relations/comparisons/${name}`),
    getCount: (name) => api.get(`/relations/comparisons/${name}/count`),
    existsInComparison: (name, contentType, objectId) =>
        api.get(`/relations/comparisons/exists?comparisonName=${name}&contentType=${contentType}&objectId=${objectId}`)
};

// ========== ФАЙЛЫ ==========
export const fileUpload = {
    uploadForRepair: (repairRequestId, formData) =>
        api.post(`/files/repair-requests/${repairRequestId}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        }),
    uploadForProduct: (productId, formData) =>
        api.post(`/files/products/${productId}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        }),
    uploadForReview: (reviewId, formData) =>
        api.post(`/files/reviews/${reviewId}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        }),
    getFiles: (contentType, objectId) =>
        api.get(`/files/${contentType}/${objectId}`),
    deleteFile: (fileId) =>
        api.delete(`/files/${fileId}`)
};

// ========== WEBSOCKET ДЛЯ ЧАТА ==========
class ChatWebSocket {
    constructor() {
        this.socket = null;
        this.listeners = new Map();
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 3000;
    }

    connect(sessionId, token) {
        const wsUrl = import.meta.env.VITE_WS_URL || 'ws://localhost:8080/ws';
        this.socket = new WebSocket(`${wsUrl}/chat?sessionId=${sessionId}&token=${token}`);

        this.socket.onopen = () => {
            console.log('WebSocket connected');
            this.reconnectAttempts = 0;
            this.emit('connected', { sessionId });
        };

        this.socket.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                this.emit('message', data);
            } catch (e) {
                console.error('Failed to parse message:', e);
            }
        };

        this.socket.onclose = () => {
            console.log('WebSocket disconnected');
            this.emit('disconnected', {});
            this.reconnect();
        };

        this.socket.onerror = (error) => {
            console.error('WebSocket error:', error);
            this.emit('error', error);
        };
    }

    reconnect() {
        if (this.reconnectAttempts >= this.maxReconnectAttempts) return;

        setTimeout(() => {
            this.reconnectAttempts++;
            console.log(`Reconnecting... attempt ${this.reconnectAttempts}`);
        }, this.reconnectDelay);
    }

    send(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(message));
        } else {
            console.warn('WebSocket is not open');
        }
    }

    on(event, callback) {
        if (!this.listeners.has(event)) {
            this.listeners.set(event, []);
        }
        this.listeners.get(event).push(callback);
    }

    off(event, callback) {
        if (!this.listeners.has(event)) return;
        const index = this.listeners.get(event).indexOf(callback);
        if (index !== -1) this.listeners.get(event).splice(index, 1);
    }

    emit(event, data) {
        if (!this.listeners.has(event)) return;
        this.listeners.get(event).forEach(callback => callback(data));
    }

    disconnect() {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
        }
    }
}

export const chatWS = new ChatWebSocket();

export default api;