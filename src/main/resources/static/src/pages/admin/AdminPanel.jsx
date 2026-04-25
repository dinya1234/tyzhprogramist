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
        activeChats: 0,
        activeToday: 0,
        unverified: 0,
        inactive: 0,
        byRole: []
    });

    const [recentOrders, setRecentOrders] = useState([]);
    const [topProducts, setTopProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    // Состояния для разных вкладок
    const [productsList, setProductsList] = useState([]);
    const [categoriesList, setCategoriesList] = useState([]);
    const [flatCategories, setFlatCategories] = useState([]);
    const [ordersList, setOrdersList] = useState([]);
    const [usersList, setUsersList] = useState([]);
    const [feedbacksList, setFeedbacksList] = useState([]);
    const [repairsList, setRepairsList] = useState([]);

    // Формы
    const [editingProduct, setEditingProduct] = useState(null);
    const [editingCategory, setEditingCategory] = useState(null);
    const [productModalOpen, setProductModalOpen] = useState(false);
    const [productForm, setProductForm] = useState({
        id: null,
        name: '',
        slug: '',
        price: '',
        oldPrice: '',
        quantity: 0,
        sku: '',
        shortDescription: '',
        fullDescription: '',
        warrantyMonths: 12,
        isActive: true,
        isNew: false,
        isBestseller: false,
        categoryId: ''
    });

    const [categoryModalOpen, setCategoryModalOpen] = useState(false);
    const [categoryForm, setCategoryForm] = useState({
        id: null,
        name: '',
        slug: '',
        parentId: '',
        order: ''
    });

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
            const usersStatsRes = await users.getStatistics().catch(() => ({ data: null }));
            // Статистика заказов
            const ordersRes = await orders.getAll({ page: 0, size: 5 });
            setRecentOrders(ordersRes.data.content || []);

            // Статистика товаров
            const productsRes = await products.getAll({ page: 0, size: 1 });

            // Отзывы на модерации
            const feedbacksRes = await feedbacks.getPendingModeration();

            // Заявки на ремонт
            const repairsRes = await repairRequests.getAll({ page: 0, size: 1 });

            const userStats = usersStatsRes?.data || {};

            setStats({
                totalUsers: usersRes.data.totalElements || 0,
                totalOrders: ordersRes.data.totalElements || 0,
                totalProducts: productsRes.data.totalElements || 0,
                totalRevenue: 1250000,
                pendingFeedbacks: feedbacksRes.data.totalElements || 0,
                pendingRepairs: repairsRes.data.totalElements || 0,
                activeChats: 3,
                activeToday: userStats.activeToday || 0,
                unverified: userStats.unverified || 0,
                inactive: userStats.inactive || 0,
                byRole: userStats.byRole || []
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

            const flatten = (nodes) => {
                const out = [];
                const stack = Array.isArray(nodes) ? [...nodes] : [];
                while (stack.length) {
                    const n = stack.shift();
                    if (!n) continue;
                    out.push({ id: n.id, name: n.name, slug: n.slug, parentId: n.parentId, order: n.order });
                    if (Array.isArray(n.children) && n.children.length) stack.unshift(...n.children);
                }
                return out;
            };
            setFlatCategories(flatten(res.data || []));
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
            const pendingRes = await feedbacks.getPendingModeration({ page: 0, size: 50 });
            setFeedbacksList(pendingRes.data.content || []);
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

    const openCreateProduct = () => {
        setProductForm({
            id: null,
            name: '',
            slug: '',
            price: '',
            oldPrice: '',
            quantity: 0,
            sku: '',
            shortDescription: '',
            fullDescription: '',
            warrantyMonths: 12,
            isActive: true,
            isNew: false,
            isBestseller: false,
            categoryId: ''
        });
        setProductModalOpen(true);
    };

    const openEditProduct = (p) => {
        setProductForm({
            id: p.id,
            name: p.name || '',
            slug: p.slug || '',
            price: p.price ?? '',
            oldPrice: p.oldPrice ?? '',
            quantity: p.quantity ?? 0,
            sku: p.sku || '',
            shortDescription: p.shortDescription || '',
            fullDescription: p.fullDescription || '',
            warrantyMonths: p.warrantyMonths ?? 12,
            isActive: p.isActive !== false,
            isNew: !!p.isNew,
            isBestseller: !!p.isBestseller,
            categoryId: p.categoryId ? String(p.categoryId) : ''
        });
        setProductModalOpen(true);
    };

    const slugify = (input) => {
        const s = String(input || '').trim().toLowerCase();
        const map = {
            'а':'a','б':'b','в':'v','г':'g','д':'d','е':'e','ё':'e','ж':'zh','з':'z','и':'i','й':'y',
            'к':'k','л':'l','м':'m','н':'n','о':'o','п':'p','р':'r','с':'s','т':'t','у':'u','ф':'f',
            'х':'h','ц':'c','ч':'ch','ш':'sh','щ':'sch','ъ':'','ы':'y','ь':'','э':'e','ю':'yu','я':'ya'
        };
        const translit = s.replace(/[а-яё]/g, (ch) => map[ch] ?? ch);
        return translit
            .replace(/[^a-z0-9]+/g, '-')
            .replace(/-+/g, '-')
            .replace(/^-|-$/g, '');
    };

    const handleSaveProduct = async () => {
        const categoryId = productForm.categoryId ? parseInt(productForm.categoryId, 10) : null;
        const name = productForm.name?.trim();
        const sku = productForm.sku?.trim();
        const slug = (productForm.slug?.trim() || slugify(name));

        if (!name || !categoryId) {
            alert('Укажите название и категорию');
            return;
        }
        if (!sku) {
            alert('Укажите SKU (артикул)');
            return;
        }
        if (!slug) {
            alert('Укажите slug (или заполните название, чтобы он сгенерировался)');
            return;
        }

        const payload = {
            name,
            sku,
            slug,
            price: Number(productForm.price) || 0,
            oldPrice: productForm.oldPrice === '' ? null : (Number(productForm.oldPrice) || null),
            quantity: Number(productForm.quantity) || 0,
            shortDescription: productForm.shortDescription,
            fullDescription: productForm.fullDescription,
            warrantyMonths: Number(productForm.warrantyMonths) || 12,
            isActive: !!productForm.isActive,
            isNew: !!productForm.isNew,
            isBestseller: !!productForm.isBestseller
        };

        try {
            if (productForm.id) {
                await products.update(productForm.id, payload, categoryId);
            } else {
                await products.create(payload, categoryId);
            }
            setProductModalOpen(false);
            await loadProducts();
            await loadDashboardData();
        } catch (e) {
            alert(e.response?.data?.message || 'Ошибка сохранения товара');
        }
    };

    const openCreateCategory = () => {
        setCategoryForm({ id: null, name: '', slug: '', parentId: '', order: '' });
        setCategoryModalOpen(true);
    };

    const openEditCategory = (cat) => {
        setCategoryForm({
            id: cat.id,
            name: cat.name || '',
            slug: cat.slug || '',
            parentId: cat.parentId ? String(cat.parentId) : '',
            order: cat.order != null ? String(cat.order) : ''
        });
        setCategoryModalOpen(true);
    };

    const handleSaveCategory = async () => {
        if (!categoryForm.name.trim() || !categoryForm.slug.trim()) {
            alert('Укажите name и slug');
            return;
        }
        const params = {
            name: categoryForm.name.trim(),
            slug: categoryForm.slug.trim(),
            parentId: categoryForm.parentId ? parseInt(categoryForm.parentId, 10) : undefined,
            order: categoryForm.order === '' ? undefined : parseInt(categoryForm.order, 10)
        };

        try {
            if (categoryForm.id) {
                await categories.update(categoryForm.id, params);
            } else {
                await categories.create(params);
            }
            setCategoryModalOpen(false);
            await loadCategories();
        } catch (e) {
            alert(e.response?.data?.message || 'Ошибка сохранения категории');
        }
    };

    const handleDeleteCategory = async (id) => {
        if (!window.confirm('Удалить категорию?')) return;
        try {
            await categories.delete(id);
            await loadCategories();
        } catch (e) {
            alert(e.response?.data?.message || 'Ошибка удаления категории');
        }
    };

    const handleToggleUserActive = async (userId, nextActive) => {
        try {
            await users.updateActive(userId, nextActive);
            await loadUsers();
            await loadDashboardData();
        } catch (e) {
            alert(e.response?.data?.message || 'Ошибка обновления статуса');
        }
    };

    const handleDeleteUser = async (userId) => {
        if (!window.confirm('Удалить пользователя? Это действие необратимо.')) return;
        try {
            await users.delete(userId);
            await loadUsers();
            await loadDashboardData();
        } catch (e) {
            alert(e.response?.data?.message || 'Ошибка удаления пользователя');
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

    const handleFeedbackReject = async (id) => {
        if (!window.confirm('Отклонить отзыв/вопрос?')) return;
        try {
            await feedbacks.delete(id);
            loadFeedbacks();
        } catch (error) {
            alert('Ошибка отклонения');
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
                                <div className="admin-stat-icon">⚡</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.activeToday}</h3>
                                    <p>Активны сегодня</p>
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
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">📧</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.unverified}</h3>
                                    <p>Без подтверждения email</p>
                                </div>
                            </div>
                            <div className="admin-stat-card">
                                <div className="admin-stat-icon">🕒</div>
                                <div className="admin-stat-info">
                                    <h3>{stats.inactive}</h3>
                                    <p>Неактивны (3+ мес.)</p>
                                </div>
                            </div>
                        </div>

                        {/* Роли */}
                        {Array.isArray(stats.byRole) && stats.byRole.length > 0 && (
                            <div className="admin-section">
                                <h2>👥 Пользователи по ролям</h2>
                                <table className="admin-table">
                                    <thead>
                                    <tr>
                                        <th>Роль</th>
                                        <th>Кол-во</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {stats.byRole.map((row, idx) => (
                                        <tr key={idx}>
                                            <td>{String(row[0])}</td>
                                            <td>{Number(row[1] ?? 0)}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}

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
                            <button className="btn-primary" onClick={openCreateProduct}>➕ Добавить товар</button>
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
                                        <button className="admin-btn-edit" onClick={() => openEditProduct(product)}>✏️</button>
                                        <button className="admin-btn-delete" onClick={() => handleProductDelete(product.id)}>🗑️</button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>

                        {/* Модалка товара */}
                        {productModalOpen && (
                            <div style={{
                                position: 'fixed',
                                inset: 0,
                                background: 'rgba(0,0,0,0.55)',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                zIndex: 2000,
                                padding: 16
                            }}>
                                <div style={{
                                    width: 'min(900px, 100%)',
                                    maxHeight: '90vh',
                                    overflowY: 'auto',
                                    background: '#0f1218',
                                    border: '1px solid #2a2d36',
                                    borderRadius: 16,
                                    padding: 18
                                }}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 12, marginBottom: 12 }}>
                                        <h2 style={{ margin: 0 }}>{productForm.id ? `✏️ Редактирование товара #${productForm.id}` : '➕ Новый товар'}</h2>
                                        <button className="btn-outline btn-sm" onClick={() => setProductModalOpen(false)}>Закрыть</button>
                                    </div>

                                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Название</div>
                                            <input
                                                value={productForm.name}
                                                onChange={(e) => setProductForm(p => ({ ...p, name: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Slug</div>
                                            <input
                                                value={productForm.slug}
                                                onChange={(e) => setProductForm(p => ({ ...p, slug: e.target.value }))}
                                                placeholder="Авто из названия, если пусто"
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Категория</div>
                                            <select
                                                value={productForm.categoryId}
                                                onChange={(e) => setProductForm(p => ({ ...p, categoryId: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            >
                                                <option value="">Выберите категорию</option>
                                                {flatCategories.map(c => (
                                                    <option key={c.id} value={c.id}>{c.name}</option>
                                                ))}
                                            </select>
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Цена</div>
                                            <input
                                                type="number"
                                                value={productForm.price}
                                                onChange={(e) => setProductForm(p => ({ ...p, price: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Старая цена (опц.)</div>
                                            <input
                                                type="number"
                                                value={productForm.oldPrice}
                                                onChange={(e) => setProductForm(p => ({ ...p, oldPrice: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Кол-во</div>
                                            <input
                                                type="number"
                                                value={productForm.quantity}
                                                onChange={(e) => setProductForm(p => ({ ...p, quantity: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>SKU</div>
                                            <input
                                                value={productForm.sku}
                                                onChange={(e) => setProductForm(p => ({ ...p, sku: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                    </div>

                                    <div style={{ marginTop: 12 }}>
                                        <div style={{ marginBottom: 6, color: '#9ca3af' }}>Короткое описание</div>
                                        <textarea
                                            rows={3}
                                            value={productForm.shortDescription}
                                            onChange={(e) => setProductForm(p => ({ ...p, shortDescription: e.target.value }))}
                                            style={{ width: '100%', padding: 10, borderRadius: 12, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                        />
                                    </div>

                                    <div style={{ marginTop: 12 }}>
                                        <div style={{ marginBottom: 6, color: '#9ca3af' }}>Полное описание (HTML можно)</div>
                                        <textarea
                                            rows={6}
                                            value={productForm.fullDescription}
                                            onChange={(e) => setProductForm(p => ({ ...p, fullDescription: e.target.value }))}
                                            style={{ width: '100%', padding: 10, borderRadius: 12, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                        />
                                    </div>

                                    <div style={{ marginTop: 12, display: 'flex', gap: 12, alignItems: 'center', flexWrap: 'wrap' }}>
                                        <label style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                                            <input
                                                type="checkbox"
                                                checked={!!productForm.isActive}
                                                onChange={(e) => setProductForm(p => ({ ...p, isActive: e.target.checked }))}
                                            />
                                            Активен
                                        </label>
                                        <label style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                                            <input
                                                type="checkbox"
                                                checked={!!productForm.isNew}
                                                onChange={(e) => setProductForm(p => ({ ...p, isNew: e.target.checked }))}
                                            />
                                            Новинка
                                        </label>
                                        <label style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                                            <input
                                                type="checkbox"
                                                checked={!!productForm.isBestseller}
                                                onChange={(e) => setProductForm(p => ({ ...p, isBestseller: e.target.checked }))}
                                            />
                                            Хит продаж
                                        </label>

                                        <div style={{ marginLeft: 'auto', display: 'flex', gap: 10 }}>
                                            <button className="btn-outline" onClick={() => setProductModalOpen(false)}>Отмена</button>
                                            <button className="btn-primary" onClick={handleSaveProduct}>Сохранить</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                )}

                {/* КАТЕГОРИИ */}
                {activeTab === 'categories' && (
                    <div>
                        <div className="admin-header">
                            <h1>📁 Управление категориями</h1>
                            <button className="btn-primary" onClick={openCreateCategory}>➕ Добавить категорию</button>
                        </div>

                        <div className="admin-categories-tree">
                            {categoriesList.map(cat => (
                                <div key={cat.id} className="admin-category-item">
                                    <div className="admin-category-header">
                                        <span className="admin-category-name">📁 {cat.name}</span>
                                        <div className="admin-actions">
                                            <button className="admin-btn-edit" onClick={() => openEditCategory(cat)}>✏️</button>
                                            <button className="admin-btn-delete" onClick={() => handleDeleteCategory(cat.id)}>🗑️</button>
                                        </div>
                                    </div>
                                    {cat.children && cat.children.length > 0 && (
                                        <div className="admin-category-children">
                                            {cat.children.map(child => (
                                                <div key={child.id} className="admin-category-child">
                                                    <span>📄 {child.name}</span>
                                                    <div className="admin-actions">
                                                        <button className="admin-btn-edit" onClick={() => openEditCategory(child)}>✏️</button>
                                                        <button className="admin-btn-delete" onClick={() => handleDeleteCategory(child.id)}>🗑️</button>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>

                        {categoryModalOpen && (
                            <div style={{
                                position: 'fixed',
                                inset: 0,
                                background: 'rgba(0,0,0,0.55)',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                zIndex: 2000,
                                padding: 16
                            }}>
                                <div style={{
                                    width: 'min(720px, 100%)',
                                    background: '#0f1218',
                                    border: '1px solid #2a2d36',
                                    borderRadius: 16,
                                    padding: 18
                                }}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 12, marginBottom: 12 }}>
                                        <h2 style={{ margin: 0 }}>{categoryForm.id ? `✏️ Категория #${categoryForm.id}` : '➕ Новая категория'}</h2>
                                        <button className="btn-outline btn-sm" onClick={() => setCategoryModalOpen(false)}>Закрыть</button>
                                    </div>

                                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Name</div>
                                            <input
                                                value={categoryForm.name}
                                                onChange={(e) => setCategoryForm(p => ({ ...p, name: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Slug</div>
                                            <input
                                                value={categoryForm.slug}
                                                onChange={(e) => setCategoryForm(p => ({ ...p, slug: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Parent (опц.)</div>
                                            <select
                                                value={categoryForm.parentId}
                                                onChange={(e) => setCategoryForm(p => ({ ...p, parentId: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            >
                                                <option value="">(root)</option>
                                                {flatCategories.map(c => (
                                                    <option key={c.id} value={c.id}>{c.name}</option>
                                                ))}
                                            </select>
                                        </div>
                                        <div>
                                            <div style={{ marginBottom: 6, color: '#9ca3af' }}>Order (опц.)</div>
                                            <input
                                                type="number"
                                                value={categoryForm.order}
                                                onChange={(e) => setCategoryForm(p => ({ ...p, order: e.target.value }))}
                                                style={{ width: '100%', padding: 10, borderRadius: 10, border: '1px solid #2a2d36', background: '#0a0c10', color: 'white' }}
                                            />
                                        </div>
                                    </div>

                                    <div style={{ marginTop: 14, display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                                        <button className="btn-outline" onClick={() => setCategoryModalOpen(false)}>Отмена</button>
                                        <button className="btn-primary" onClick={handleSaveCategory}>Сохранить</button>
                                    </div>
                                </div>
                            </div>
                        )}
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
                                        <button
                                            className="admin-btn-delete"
                                            onClick={() => handleToggleUserActive(userItem.id, !userItem.isActive)}
                                            title={userItem.isActive ? 'Заблокировать' : 'Разблокировать'}
                                        >
                                            {userItem.isActive ? '🔒' : '🔓'}
                                        </button>
                                        <button
                                            className="admin-btn-delete"
                                            onClick={() => handleDeleteUser(userItem.id)}
                                            title="Удалить"
                                        >
                                            🗑️
                                        </button>
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
                            {feedbacksList.map(feedback => (
                                <div key={feedback.id} className="admin-feedback-card">
                                    <div className="admin-feedback-header">
                                        <span className="admin-feedback-user">{feedback.userName}</span>
                                        <span className="admin-feedback-product">{feedback.productName}</span>
                                        <span className="admin-feedback-date">{formatDate(feedback.createdAt)}</span>
                                    </div>
                                    <div className="admin-feedback-text">{feedback.text}</div>
                                    <div className="admin-feedback-actions">
                                        <button className="btn-primary btn-sm" onClick={() => handleFeedbackPublish(feedback.id)}>✅ Опубликовать</button>
                                        <button className="btn-danger btn-sm" onClick={() => handleFeedbackReject(feedback.id)}>❌ Отклонить</button>
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