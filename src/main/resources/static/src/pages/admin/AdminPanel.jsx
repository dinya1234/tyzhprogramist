// src/pages/admin/AdminPanel.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import {
    products,
    categories,
    orders,
    users,
    feedbacks,
    repairRequests,
    settings,
    componentTypes,
    pcBuilds
} from '../../services/api';

export default function AdminPanel() {
    const { user } = useAuth();
    const [activeTab, setActiveTab] = useState('dashboard'); // dashboard, products, categories, orders, users, feedbacks, repairs, settings

    // Данные для дашборда
    const [stats, setStats] = useState({
        totalUsers: 0,
        totalOrders: 0,
        totalProducts: 0,
        totalRevenue: 0,
        pendingFeedbacks: 0,
        pendingRepairs: 0,
        activeChats: 0
    });

    const [recentOrders, setRecentOrders] = useState([]);
    const [topProducts, setTopProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    // Состояния для разных вкладок
    const [productsList, setProductsList] = useState([]);
    const [categoriesList, setCategoriesList] = useState([]);
    const [ordersList, setOrdersList] = useState([]);
    const [usersList, setUsersList] = useState([]);
    const [feedbacksList, setFeedbacksList] = useState([]);
    const [repairsList, setRepairsList] = useState([]);

    // Формы
    const [editingProduct, setEditingProduct] = useState(null);
    const [editingCategory, setEditingCategory] = useState(null);

    // Фильтры
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

    // Загрузка данных
    useEffect(() => {
        loadDashboardData();
        loadProducts();
        loadCategories();
        loadOrders();
        loadUsers();
        loadFeedbacks();
        loadRepairs();
    }, []);

    const loadDashboardData = async () => {
        try {
            // Статистика пользователей
            const usersRes = await users.getAll({ page: 0, size: 1 });
            // Статистика заказов
            const ordersRes = await orders.getAll({ page: 0, size: 5 });
            setRecentOrders(ordersRes.data.content || []);

            // Статистика товаров
            const productsRes = await products.getAll({ page: 0, size: 1 });

            // Отзывы на модерации
            const feedbacksRes = await feedbacks.getPendingModeration();

            // Заявки на ремонт
            const repairsRes = await repairRequests.getAll({ page: 0, size: 1 });

            setStats({
                totalUsers: usersRes.data.totalElements || 0,
                totalOrders: ordersRes.data.totalElements || 0,
                totalProducts: productsRes.data.totalElements || 0,
                totalRevenue: 1250000,
                pendingFeedbacks: feedbacksRes.data.totalElements || 0,
                pendingRepairs: repairsRes.data.totalElements || 0,
                activeChats: 3
            });

            // Топ товаров
            setTopProducts([
                { id: 1, name: 'Intel Core i9-13900K', sales: 45, revenue: 2249550 },
                { id: 2, name: 'RTX 4080', sales: 32, revenue: 3199680 },
                { id: 3, name: 'Samsung 980 Pro 1TB', sales: 78, revenue: 779220 }
            ]);
        } catch (error) {
            console.error('Ошибка загрузки дашборда:', error);
        } finally {
            setLoading(false);
        }
    };

    const loadProducts = async () => {
        try {
            const res = await products.getAll({ page: 0, size: 50 });
            setProductsList(res.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки товаров:', error);
        }
    };

    const loadCategories = async () => {
        try {
            const res = await categories.getTree();
            setCategoriesList(res.data || []);
        } catch (error) {
            console.error('Ошибка загрузки категорий:', error);
        }
    };

    const loadOrders = async () => {
        try {
            const res = await orders.getAll({ page: 0, size: 50 });
            setOrdersList(res.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки заказов:', error);
        }
    };

    const loadUsers = async () => {
        try {
            const res = await users.getAll({ page: 0, size: 50 });
            setUsersList(res.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки пользователей:', error);
        }
    };

    const loadFeedbacks = async () => {
        try {
            const [pendingRes, allRes] = await Promise.all([
                feedbacks.getPendingModeration({ page: 0, size: 50 }),
                feedbacks.getAll({ page: 0, size: 50 })
            ]);
            setFeedbacksList(allRes.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки отзывов:', error);
        }
    };

    const loadRepairs = async () => {
        try {
            const res = await repairRequests.getAll({ page: 0, size: 50 });
            setRepairsList(res.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки заявок:', error);
        }
    };

    const handleProductDelete = async (id) => {
        if (window.confirm('Удалить товар?')) {
            try {
                await products.delete(id);
                loadProducts();
            } catch (error) {
                alert('Ошибка удаления');
            }
        }
    };

    const handleOrderStatusUpdate = async (id, status) => {
        try {
            await orders.updateStatus(id, status);
            loadOrders();
        } catch (error) {
            alert('Ошибка обновления статуса');
        }
    };

    const handleUserRoleUpdate = async (id, role) => {
        try {
            await users.updateRole(id, role);
            loadUsers();
        } catch (error) {
            alert('Ошибка обновления роли');
        }
    };

    const handleFeedbackPublish = async (id) => {
        try {
            await feedbacks.publish(id);
            loadFeedbacks();
        } catch (error) {
            alert('Ошибка публикации');
        }
    };

    const handleRepairStatusUpdate = async (id, status) => {
        try {
            await repairRequests.updateStatus(id, status);
            loadRepairs();
        } catch (error) {
            alert('Ошибка обновления статуса');
        }
    };

    const formatDate = (dateStr) => {
        return new Date(dateStr).toLocaleDateString('ru-RU');
    };

    const formatPrice = (price) => {
        return price?.toLocaleString() + ' ₽' || '0 ₽';
    };

    const getOrderStatusBadge = (status) => {
        const statusMap = {
            'NEW': { text: '🆕 Новый', color: '#f59e0b' },
            'PAID': { text: '💰 Оплачен', color: '#22c55e' },
            'SHIPPED': { text: '🚚 Отправлен', color: '#3b82f6' },
            'DELIVERED': { text: '✅ Доставлен', color: '#22c55e' },
            'CANCELLED': { text: '❌ Отменён', color: '#ef4444' }
        };
        const info = statusMap[status] || { text: status, color: '#9ca3af' };
        return <span style={{ background: info.color, padding: '4px 8px', borderRadius: '20px', fontSize: '11px', color: 'white' }}>{info.text}</span>;
    };

    const getUserRoleBadge = (role) => {
        const roleMap = {
            'CLIENT': { text: '👤 Клиент', color: '#9ca3af' },
            'MODERATOR': { text: '🛡️ Модератор', color: '#3b82f6' },
            'ADMIN': { text: '👑 Админ', color: '#ef4444' }
        };
        const info = roleMap[role] || { text: role, color: '#9ca3af' };
        return <span style={{ background: info.color, padding: '4px 8px', borderRadius: '20px', fontSize: '11px', color: 'white' }}>{info.text}</span>;
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка панели управления...</h2>
            </div>
        );
    }

    if (user?.role !== 'ADMIN') {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>⛔ Доступ запрещён</h2>
                <p style={{ color: '#9ca3af' }}>У вас нет прав доступа к этой странице</p>
            </div>
        );
    }

    return (
        <div className="admin-panel">
            {/* Боковая панель */}
            <div className="admin-sidebar">
                <div className="admin-logo">
                    <h2>👑 Админ-панель</h2>
                </div>

                <nav className="admin-nav">
                    <button className={`admin-nav-item ${activeTab === 'dashboard' ? 'active' : ''}`} onClick={() => setActiveTab('dashboard')}>
                        📊 Дашборд
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'products' ? 'active' : ''}`} onClick={() => setActiveTab('products')}>
                        📦 Товары
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'categories' ? 'active' : ''}`} onClick={() => setActiveTab('categories')}>
                        📁 Категории
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'orders' ? 'active' : ''}`} onClick={() => setActiveTab('orders')}>
                        📋 Заказы
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'users' ? 'active' : ''}`} onClick={() => setActiveTab('users')}>
                        👥 Пользователи
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'feedbacks' ? 'active' : ''}`} onClick={() => setActiveTab('feedbacks')}>
                        💬 Отзывы
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'repairs' ? 'active' : ''}`} onClick={() => setActiveTab('repairs')}>
                        🔧 Ремонт
                    </button>
                    <button className={`admin-nav-item ${activeTab === 'settings' ? 'active' : ''}`} onClick={() => setActiveTab('settings')}>
                        ⚙️ Настройки
                    </button>
                </nav>
            </div>

            {/* Основной контент */}
            <div className="admin-content">
                {/* ДАШБОРД */}
                {activeTab === 'dashboard' && (
                    <div>
                        <h1>📊 Дашборд</h1>

                        {/* Статистика */}
                        <div className="admin-stats-grid">
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">👥</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.totalUsers}</h3>
                                    <p>Пользователей</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">📦</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.totalOrders}</h3>
                                    <p>Заказов</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">🛒</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.totalProducts}</h3>
                                    <p>Товаров</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">💰</div>
                                <div className="admin-stat-info">
                                    <h3>{formatPrice(stats.totalRevenue)}</h3>
                                    <p>Выручка</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">💬</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.pendingFeedbacks}</h3>
                                    <p>На модерации</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">🔧</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.pendingRepairs}</h3>
                                    <p>Заявок на ремонт</p>
                                </div>
                            </div>
                        </div>

                        {/* Последние заказы */}
                        <div className="admin-section">
                            <h2>🆕 Последние заказы</h2>
                            <table className="admin-table">
                                <thead>
                                <tr>
                                    <th>№</th>
                                    <th>Дата</th>
                                    <th>Покупатель</th>
                                    <th>Сумма</th>
                                    <th>Статус</th>
                                </tr>
                                </thead>
                                <tbody>
                                {recentOrders.map(order => (
                                    <tr key={order.id}>
                                        <td>#{order.orderNumber || order.id}</td>
                                        <td>{formatDate(order.createdAt)}</td>
                                        <td>{order.userName || 'Гость'}</td>
                                        <td>{formatPrice(order.totalPrice)}</td>
                                        <td>{getOrderStatusBadge(order.status)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>

                        {/* Топ товаров */}
                        <div className="admin-section">
                            <h2>🏆 Топ товаров по продажам</h2>
                            <table className="admin-table">
                                <thead>
                                <tr>
                                    <th>Товар</th>
                                    <th>Продано</th>
                                    <th>Выручка</th>
                                </tr>
                                </thead>
                                <tbody>
                                {topProducts.map(product => (
                                    <tr key={product.id}>
                                        <td>{product.name}</td>
                                        <td>{product.sales} шт.</td>
                                        <td>{formatPrice(product.revenue)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {/* ТОВАРЫ */}
                {activeTab === 'products' && (
                    <div>
                        <div className="admin-header">
                            <h1>📦 Управление товарами</h1>
                            <button className="btn-primary">➕ Добавить товар</button>
                        </div>

                        <div className="admin-search">
                            <input type="text" placeholder="Поиск товаров..." className="admin-search-input" />
                        </div>

                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Название</th>
                                <th>Категория</th>
                                <th>Цена</th>
                                <th>Наличие</th>
                                <th>Статус</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {productsList.map(product => (
                                <tr key={product.id}>
                                    <td>{product.id}</td>
                                    <td>{product.name}</td>
                                    <td>{product.categoryName}</td>
                                    <td>{formatPrice(product.price)}</td>
                                    <td>{product.quantity || 0} шт.</td>
                                    <td>{product.isActive ? '✅ Активен' : '❌ Неактивен'}</td>
                                    <td className="admin-actions">
                                        <button className="admin-btn-edit">✏️</button>
                                        <button className="admin-btn-delete" onClick={() => handleProductDelete(product.id)}>🗑️</button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* КАТЕГОРИИ */}
                {activeTab === 'categories' && (
                    <div>
                        <div className="admin-header">
                            <h1>📁 Управление категориями</h1>
                            <button className="btn-primary">➕ Добавить категорию</button>
                        </div>

                        <div className="admin-categories-tree">
                            {categoriesList.map(cat => (
                                <div key={cat.id} className="admin-category-item">
                                    <div className="admin-category-header">
                                        <span className="admin-category-name">📁 {cat.name}</span>
                                        <div className="admin-actions">
                                            <button className="admin-btn-edit">✏️</button>
                                            <button className="admin-btn-delete">🗑️</button>
                                        </div>
                                    </div>
                                    {cat.children && cat.children.length > 0 && (
                                        <div className="admin-category-children">
                                            {cat.children.map(child => (
                                                <div key={child.id} className="admin-category-child">
                                                    <span>📄 {child.name}</span>
                                                    <div className="admin-actions">
                                                        <button className="admin-btn-edit">✏️</button>
                                                        <button className="admin-btn-delete">🗑️</button>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {/* ЗАКАЗЫ */}
                {activeTab === 'orders' && (
                    <div>
                        <div className="admin-header">
                            <h1>📋 Управление заказами</h1>
                            <select className="admin-filter" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
                                <option value="">Все статусы</option>
                                <option value="NEW">Новые</option>
                                <option value="PAID">Оплаченные</option>
                                <option value="SHIPPED">Отправленные</option>
                                <option value="DELIVERED">Доставленные</option>
                                <option value="CANCELLED">Отменённые</option>
                            </select>
                        </div>

                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>№</th>
                                <th>Дата</th>
                                <th>Покупатель</th>
                                <th>Товаров</th>
                                <th>Сумма</th>
                                <th>Статус</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {ordersList.map(order => (
                                <tr key={order.id}>
                                    <td>#{order.orderNumber || order.id}</td>
                                    <td>{formatDate(order.createdAt)}</td>
                                    <td>{order.userName || 'Гость'}</td>
                                    <td>{order.items?.length || 0}</td>
                                    <td>{formatPrice(order.totalPrice)}</td>
                                    <td>{getOrderStatusBadge(order.status)}</td>
                                    <td className="admin-actions">
                                        <select onChange={(e) => handleOrderStatusUpdate(order.id, e.target.value)} value={order.status}>
                                            <option value="NEW">Новый</option>
                                            <option value="PAID">Оплачен</option>
                                            <option value="SHIPPED">Отправлен</option>
                                            <option value="DELIVERED">Доставлен</option>
                                            <option value="CANCELLED">Отменён</option>
                                        </select>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* ПОЛЬЗОВАТЕЛИ */}
                {activeTab === 'users' && (
                    <div>
                        <div className="admin-header">
                            <h1>👥 Управление пользователями</h1>
                            <div className="admin-search">
                                <input type="text" placeholder="Поиск пользователей..." className="admin-search-input" />
                            </div>
                        </div>

                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Имя пользователя</th>
                                <th>Email</th>
                                <th>Телефон</th>
                                <th>Роль</th>
                                <th>Статус</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {usersList.map(userItem => (
                                <tr key={userItem.id}>
                                    <td>{userItem.id}</td>
                                    <td>{userItem.username}</td>
                                    <td>{userItem.email}</td>
                                    <td>{userItem.phone || '—'}</td>
                                    <td>{getUserRoleBadge(userItem.role)}</td>
                                    <td>{userItem.isActive ? '✅ Активен' : '❌ Заблокирован'}</td>
                                    <td className="admin-actions">
                                        <select onChange={(e) => handleUserRoleUpdate(userItem.id, e.target.value)} value={userItem.role}>
                                            <option value="CLIENT">Клиент</option>
                                            <option value="MODERATOR">Модератор</option>
                                            <option value="ADMIN">Админ</option>
                                        </select>
                                        <button className="admin-btn-delete">🔒</button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* ОТЗЫВЫ */}
                {activeTab === 'feedbacks' && (
                    <div>
                        <div className="admin-header">
                            <h1>💬 Модерация отзывов</h1>
                        </div>

                        <div className="admin-feedbacks">
                            {feedbacksList.filter(f => !f.isPublished).map(feedback => (
                                <div key={feedback.id} className="admin-feedback-card">
                                    <div className="admin-feedback-header">
                                        <span className="admin-feedback-user">{feedback.userName}</span>
                                        <span className="admin-feedback-product">{feedback.productName}</span>
                                        <span className="admin-feedback-date">{formatDate(feedback.createdAt)}</span>
                                    </div>
                                    <div className="admin-feedback-text">{feedback.text}</div>
                                    <div className="admin-feedback-actions">
                                        <button className="btn-primary btn-sm" onClick={() => handleFeedbackPublish(feedback.id)}>✅ Опубликовать</button>
                                        <button className="btn-danger btn-sm">❌ Отклонить</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {/* РЕМОНТ */}
                {activeTab === 'repairs' && (
                    <div>
                        <div className="admin-header">
                            <h1>🔧 Заявки на ремонт</h1>
                        </div>

                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>№</th>
                                <th>Дата</th>
                                <th>Клиент</th>
                                <th>Устройство</th>
                                <th>Статус</th>
                                <th>Стоимость</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {repairsList.map(repair => (
                                <tr key={repair.id}>
                                    <td>#{repair.id}</td>
                                    <td>{formatDate(repair.createdAt)}</td>
                                    <td>{repair.userName}</td>
                                    <td>{repair.deviceType}</td>
                                    <td>
                                        <select onChange={(e) => handleRepairStatusUpdate(repair.id, e.target.value)} value={repair.status}>
                                            <option value="Принята">Принята</option>
                                            <option value="Диагностика">Диагностика</option>
                                            <option value="Ремонт">Ремонт</option>
                                            <option value="Готов к выдаче">Готов к выдаче</option>
                                            <option value="Выдан">Выдан</option>
                                            <option value="Отменена">Отменена</option>
                                        </select>
                                    </td>
                                    <td>{repair.finalPrice ? formatPrice(repair.finalPrice) : repair.estimatedPrice ? formatPrice(repair.estimatedPrice) : '—'}</td>
                                    <td className="admin-actions">
                                        <button className="admin-btn-edit">✏️</button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* НАСТРОЙКИ */}
                {activeTab === 'settings' && (
                    <div>
                        <h1>⚙️ Настройки сайта</h1>

                        <div className="admin-settings">
                            <div className="admin-settings-section">
                                <h3>Настройки доставки</h3>
                                <div className="admin-settings-field">
                                    <label>Стоимость доставки (₽)</label>
                                    <input type="number" className="admin-settings-input" defaultValue="500" />
                                </div>
                                <div className="admin-settings-field">
                                    <label>Бесплатная доставка от (₽)</label>
                                    <input type="number" className="admin-settings-input" defaultValue="5000" />
                                </div>
                            </div>

                            <div className="admin-settings-section">
                                <h3>Контактная информация</h3>
                                <div className="admin-settings-field">
                                    <label>Адрес самовывоза</label>
                                    <input type="text" className="admin-settings-input" defaultValue="г. Ростов-на-Дону, ул. Программистов, д. 1" />
                                </div>
                                <div className="admin-settings-field">
                                    <label>Телефон</label>
                                    <input type="text" className="admin-settings-input" defaultValue="+7 (863) 123-45-67" />
                                </div>
                                <div className="admin-settings-field">
                                    <label>Часы работы</label>
                                    <input type="text" className="admin-settings-input" defaultValue="Пн-Пт: 10:00-20:00, Сб-Вс: 11:00-18:00" />
                                </div>
                            </div>

                            <div className="admin-settings-section">
                                <h3>Настройки сайта</h3>
                                <div className="admin-settings-field">
                                    <label>Название сайта</label>
                                    <input type="text" className="admin-settings-input" defaultValue="Тыжпрограммист" />
                                </div>
                            </div>

                            <button className="btn-primary">Сохранить настройки</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}