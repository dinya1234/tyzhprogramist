import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' }
});

// Перехватчик для добавления JWT токена
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// ========== АВТОРИЗАЦИЯ ==========
export const auth = {
    register: (data) => api.post('/register', data),
    login: (data) => api.post('/login', data),
    refresh: (refreshToken) => api.post('/auth/refresh', { refreshToken }),
    logout: () => api.post('/auth/logout')
};

// ========== ТОВАРЫ ==========
export const products = {
    getAll: (params) => api.get('/products', { params }),
    getById: (id) => api.get(`/products/${id}`),
    getByCategory: (categoryId, params) => api.get(`/products/category/${categoryId}`, { params }),
    search: (q, params) => api.get('/products/search', { params: { q, ...params } }),
    getNew: (limit = 12) => api.get('/products/new', { params: { limit } }),
    getBestsellers: (limit = 12) => api.get('/products/bestsellers', { params: { limit } })
};

// ========== КАТЕГОРИИ ==========
export const categories = {
    getAll: () => api.get('/categories/roots'),
    getTree: () => api.get('/categories/tree'),
    getBySlug: (slug) => api.get(`/categories/${slug}`)
};

// ========== КОРЗИНА ==========
export const cart = {
    get: () => api.get('/cart'),
    add: (productId, quantity = 1) => api.post('/cart/add', { productId, quantity }),
    update: (productId, quantity) => api.put(`/cart/update?productId=${productId}&quantity=${quantity}`),
    remove: (productId) => api.delete(`/cart/remove/${productId}`),
    clear: () => api.delete('/cart/clear')
};

// ========== ЗАКАЗЫ ==========
export const orders = {
    create: (data) => api.post('/orders', data),
    getMyOrders: (params) => api.get('/orders/me', { params }),
    getById: (id) => api.get(`/orders/me/${id}`),
    cancel: (id) => api.post(`/orders/me/${id}/cancel`)
};

// ========== СБОРКИ ПК ==========
export const pcBuilds = {
    getPublic: (params) => api.get('/pc-builds/public', { params }),
    getMyBuilds: (params) => api.get('/pc-builds/me', { params }),
    create: (data) => api.post('/pc-builds/me', data),
    update: (id, name, isPublic) => api.put(`/pc-builds/me/${id}?name=${name}&isPublic=${isPublic}`),
    delete: (id) => api.delete(`/pc-builds/me/${id}`),
    addComponent: (buildId, productId, quantity = 1) =>
        api.post(`/pc-builds/me/${buildId}/components/${productId}?quantity=${quantity}`),
    removeComponent: (buildId, productId) =>
        api.delete(`/pc-builds/me/${buildId}/components/${productId}`),
    getComponents: (buildId) => api.get(`/pc-builds/me/${buildId}/components`),
    getTotalPrice: (buildId) => api.get(`/pc-builds/me/${buildId}/total`)
};

// ========== ОТЗЫВЫ ==========
export const feedbacks = {
    getProductReviews: (productId, params) => api.get(`/feedbacks/products/${productId}/reviews`, { params }),
    getProductQuestions: (productId, params) => api.get(`/feedbacks/products/${productId}/questions`, { params }),
    getRating: (productId) => api.get(`/feedbacks/products/${productId}/rating`),
    create: (data) => api.post('/feedbacks', data),
    delete: (id) => api.delete(`/feedbacks/me/${id}`),
    getMy: (params) => api.get('/feedbacks/me', { params })
};

// ========== ЗАЯВКИ НА РЕМОНТ ==========
export const repairRequests = {
    create: (data) => api.post('/repair-requests', data),
    getMyRequests: (params) => api.get('/repair-requests/me', { params }),
    getById: (id) => api.get(`/repair-requests/me/${id}`),
    cancel: (id) => api.post(`/repair-requests/me/${id}/cancel`)
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
    closeSession: (sessionId) => api.post(`/chat/me/session/${sessionId}/close`)
};

export default api;