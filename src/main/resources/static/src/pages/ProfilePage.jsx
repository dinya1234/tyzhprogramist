// src/pages/ProfilePage.jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { orders, pcBuilds, feedbacks, repairRequests, auth } from '../services/api';

export default function ProfilePage() {
    const { user, logout } = useAuth();
    const [activeTab, setActiveTab] = useState('orders'); // orders, builds, feedbacks, repairs, settings

    // Данные
    const [ordersList, setOrdersList] = useState([]);
    const [buildsList, setBuildsList] = useState([]);
    const [feedbacksList, setFeedbacksList] = useState([]);
    const [repairsList, setRepairsList] = useState([]);

    // Настройки профиля
    const [settings, setSettings] = useState({
        notifications: true,
        chatConsent: false
    });
    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
    });

    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState(false);
    const [buildActionId, setBuildActionId] = useState(null);
    const [message, setMessage] = useState({ text: '', type: '' });

    // Загрузка данных
    useEffect(() => {
        loadAllData();
        loadUserSettings();
    }, []);

    const loadAllData = async () => {
        setLoading(true);
        try {
            const [ordersRes, buildsRes, feedbacksRes, repairsRes] = await Promise.all([
                orders.getMyOrders({ page: 0, size: 10 }),
                pcBuilds.getMyBuilds({ page: 0, size: 10 }),
                feedbacks.getMy({ page: 0, size: 10 }),
                repairRequests.getMyRequests({ page: 0, size: 10 })
            ]);

            setOrdersList(ordersRes.data.content || []);
            setBuildsList(buildsRes.data.content || []);
            setFeedbacksList(feedbacksRes.data.content || []);
            setRepairsList(repairsRes.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки данных профиля:', error);
        } finally {
            setLoading(false);
        }
    };

    const loadUserSettings = () => {
        // Загрузка настроек из localStorage или API
        const notifications = localStorage.getItem('notifications') === 'true';
        const chatConsent = localStorage.getItem('chatConsent') === 'true';
        setSettings({ notifications, chatConsent });
    };

    const updateSettings = async (key, value) => {
        setUpdating(true);
        try {

            localStorage.setItem(key, value);
            setSettings(prev => ({ ...prev, [key]: value }));
            showMessage('Настройки сохранены', 'success');
        } catch (error) {
            showMessage('Ошибка сохранения', 'error');
        } finally {
            setUpdating(false);
        }
    };

    const changePassword = async (e) => {
        e.preventDefault();

        if (passwordData.newPassword !== passwordData.confirmPassword) {
            showMessage('Пароли не совпадают', 'error');
            return;
        }

        if (passwordData.newPassword.length < 6) {
            showMessage('Пароль должен быть не менее 6 символов', 'error');
            return;
        }

        setUpdating(true);
        try {
            await auth.changePassword(passwordData.oldPassword, passwordData.newPassword);
            showMessage('Пароль успешно изменён', 'success');
            setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
        } catch (error) {
            showMessage(error.response?.data?.message || 'Ошибка смены пароля', 'error');
        } finally {
            setUpdating(false);
        }
    };

    const showMessage = (text, type) => {
        setMessage({ text, type });
        setTimeout(() => setMessage({ text: '', type: '' }), 3000);
    };

    const handleToggleBuildVisibility = async (build) => {
        if (!build?.id) return;
        setBuildActionId(build.id);
        try {
            const nextPublic = !build.isPublic;
            const response = await pcBuilds.update(build.id, build.name, nextPublic);
            const updatedBuild = response.data;
            setBuildsList((prev) =>
                prev.map((b) => (b.id === build.id ? { ...b, ...updatedBuild, isPublic: updatedBuild?.isPublic ?? nextPublic } : b))
            );
            showMessage(nextPublic ? 'Сборка опубликована' : 'Сборка скрыта из каталога', 'success');
        } catch (error) {
            showMessage(error.response?.data?.message || 'Ошибка обновления доступа', 'error');
        } finally {
            setBuildActionId(null);
        }
    };

    const handleDeleteBuild = async (build) => {
        if (!build?.id) return;
        if (!window.confirm(`Удалить сборку "${build.name}"?`)) return;

        setBuildActionId(build.id);
        try {
            await pcBuilds.delete(build.id);
            setBuildsList((prev) => prev.filter((b) => b.id !== build.id));
            showMessage('Сборка удалена', 'success');
        } catch (error) {
            showMessage(error.response?.data?.message || 'Ошибка удаления сборки', 'error');
        } finally {
            setBuildActionId(null);
        }
    };

    const formatDate = (dateStr) => {
        return new Date(dateStr).toLocaleDateString('ru-RU');
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

    const getRepairStatusBadge = (status) => {
        const statusMap = {
            'Принята': { text: '📋 Принята', color: '#f59e0b' },
            'Диагностика': { text: '🔧 Диагностика', color: '#3b82f6' },
            'Ремонт': { text: '🛠️ Ремонт', color: '#8b5cf6' },
            'Готов к выдаче': { text: '✅ Готов', color: '#22c55e' },
            'Выдан': { text: '📦 Выдан', color: '#9ca3af' },
            'Отменена': { text: '❌ Отменена', color: '#ef4444' }
        };
        const info = statusMap[status] || { text: status, color: '#9ca3af' };
        return <span style={{ background: info.color, padding: '4px 8px', borderRadius: '20px', fontSize: '11px', color: 'white' }}>{info.text}</span>;
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            {/* Сообщение */}
            {message.text && (
                <div className={`profile-message ${message.type}`}>
                    {message.text}
                </div>
            )}

            <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: '32px' }}>
                {/* Боковая панель */}
                <div className="profile-sidebar">
                    <div className="profile-avatar">
                        <div className="profile-avatar-circle">
                            {user?.username?.[0]?.toUpperCase() || '👤'}
                        </div>
                        <h3>{user?.username}</h3>
                        <p style={{ color: '#9ca3af', fontSize: '14px' }}>{user?.email}</p>
                        <p style={{ color: '#c084fc', fontSize: '12px' }}>
                            {user?.role === 'ADMIN' ? '👑 Администратор' : user?.role === 'MODERATOR' ? '🛡️ Модератор' : '👤 Клиент'}
                        </p>
                    </div>

                    <nav className="profile-nav">
                        <button
                            className={`profile-nav-item ${activeTab === 'orders' ? 'active' : ''}`}
                            onClick={() => setActiveTab('orders')}
                        >
                            📦 Заказы ({ordersList.length})
                        </button>
                        <button
                            className={`profile-nav-item ${activeTab === 'builds' ? 'active' : ''}`}
                            onClick={() => setActiveTab('builds')}
                        >
                            🖥️ Сборки ПК ({buildsList.length})
                        </button>
                        <button
                            className={`profile-nav-item ${activeTab === 'feedbacks' ? 'active' : ''}`}
                            onClick={() => setActiveTab('feedbacks')}
                        >
                            💬 Отзывы ({feedbacksList.length})
                        </button>
                        <button
                            className={`profile-nav-item ${activeTab === 'repairs' ? 'active' : ''}`}
                            onClick={() => setActiveTab('repairs')}
                        >
                            🔧 Заявки на ремонт ({repairsList.length})
                        </button>
                        <button
                            className={`profile-nav-item ${activeTab === 'settings' ? 'active' : ''}`}
                            onClick={() => setActiveTab('settings')}
                        >
                            ⚙️ Настройки
                        </button>
                    </nav>

                    <button onClick={logout} className="btn-danger" style={{ width: '100%', marginTop: '20px' }}>
                        Выйти
                    </button>
                </div>

                {/* Основной контент */}
                <div className="profile-content">
                    {/* Заказы */}
                    {activeTab === 'orders' && (
                        <div>
                            <h2 style={{ marginBottom: '20px' }}>📦 Мои заказы</h2>

                            {ordersList.length === 0 ? (
                                <div className="profile-empty">
                                    <span>🛒</span>
                                    <p>У вас пока нет заказов</p>
                                    <Link to="/catalog" className="btn-primary">Перейти в каталог</Link>
                                </div>
                            ) : (
                                <div className="profile-orders">
                                    {ordersList.map(order => (
                                        <div key={order.id} className="profile-order-card">
                                            <div className="profile-order-header">
                                                <div>
                                                    <span className="profile-order-number">Заказ #{order.orderNumber || order.id}</span>
                                                    <span className="profile-order-date">{formatDate(order.createdAt)}</span>
                                                </div>
                                                {getOrderStatusBadge(order.status)}
                                            </div>

                                            <div className="profile-order-items">
                                                {order.items?.slice(0, 3).map((item, idx) => (
                                                    <div key={idx} className="profile-order-item">
                                                        <span>{item.productName}</span>
                                                        <span>{item.quantity} шт. × {item.price?.toLocaleString()} ₽</span>
                                                    </div>
                                                ))}
                                                {order.items?.length > 3 && (
                                                    <div className="profile-order-more">+ ещё {order.items.length - 3} товаров</div>
                                                )}
                                            </div>

                                            <div className="profile-order-footer">
                                                <span>Сумма: <strong>{order.totalPrice?.toLocaleString()} ₽</strong></span>
                                                <Link to={`/order-success?orderId=${order.id}`} className="profile-order-link">Подробнее →</Link>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}

                    {/* Сборки ПК */}
                    {activeTab === 'builds' && (
                        <div>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                                <h2>🖥️ Мои сборки ПК</h2>
                                <Link to="/configurator" className="btn-primary">➕ Новая сборка</Link>
                            </div>

                            {buildsList.length === 0 ? (
                                <div className="profile-empty">
                                    <span>🖥️</span>
                                    <p>У вас пока нет сборок ПК</p>
                                    <Link to="/configurator" className="btn-primary">Собрать ПК</Link>
                                </div>
                            ) : (
                                <div className="profile-builds">
                                    {buildsList.map(build => (
                                        <div key={build.id} className="profile-build-card">
                                            <div className="profile-build-header">
                                                <div>
                                                    <h3>{build.name}</h3>
                                                    <span className="profile-build-date">{formatDate(build.createdAt)}</span>
                                                </div>
                                                <div>
                                                    {build.isPublic ? (
                                                        <span style={{ color: '#22c55e', fontSize: '12px' }}>🌍 Опубликована</span>
                                                    ) : (
                                                        <span style={{ color: '#9ca3af', fontSize: '12px' }}>🔒 Приватная</span>
                                                    )}
                                                </div>
                                            </div>

                                            <div className="profile-build-components">
                                                {build.components?.slice(0, 5).map((comp, idx) => (
                                                    <span key={idx} className="profile-build-component">
                                                        {comp.componentType}
                                                    </span>
                                                ))}
                                                {build.components?.length > 5 && (
                                                    <span>+{build.components.length - 5}</span>
                                                )}
                                            </div>

                                            <div className="profile-build-footer">
                                                <span className="profile-build-price">{build.totalPrice?.toLocaleString()} ₽</span>
                                                <div className="profile-build-actions">
                                                    <Link to={`/configurator?buildId=${build.id}`} className="profile-build-link">Редактировать</Link>
                                                    <button
                                                        className="profile-build-link"
                                                        onClick={() => handleToggleBuildVisibility(build)}
                                                        disabled={buildActionId === build.id}
                                                    >
                                                        {buildActionId === build.id
                                                            ? 'Сохранение...'
                                                            : (build.isPublic ? 'Сделать приватной' : 'Опубликовать')}
                                                    </button>
                                                    <button
                                                        className="profile-build-link danger"
                                                        onClick={() => handleDeleteBuild(build)}
                                                        disabled={buildActionId === build.id}
                                                    >
                                                        {buildActionId === build.id ? 'Удаление...' : 'Удалить'}
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}

                    {/* Отзывы */}
                    {activeTab === 'feedbacks' && (
                        <div>
                            <h2 style={{ marginBottom: '20px' }}>💬 Мои отзывы и вопросы</h2>

                            {feedbacksList.length === 0 ? (
                                <div className="profile-empty">
                                    <span>💬</span>
                                    <p>У вас пока нет отзывов</p>
                                    <Link to="/catalog" className="btn-primary">Оставить отзыв</Link>
                                </div>
                            ) : (
                                <div className="profile-feedbacks">
                                    {feedbacksList.map(feedback => (
                                        <div key={feedback.id} className="profile-feedback-card">
                                            <div className="profile-feedback-header">
                                                <div>
                                                    <Link to={`/product/${feedback.productId}`} className="profile-feedback-product">
                                                        {feedback.productName}
                                                    </Link>
                                                    <span className="profile-feedback-date">{formatDate(feedback.createdAt)}</span>
                                                </div>
                                                {feedback.feedbackType === 'REVIEW' && (
                                                    <div className="profile-feedback-rating">
                                                        {'★'.repeat(feedback.rating)}{'☆'.repeat(5 - feedback.rating)}
                                                    </div>
                                                )}
                                            </div>

                                            <div className="profile-feedback-text">{feedback.text}</div>

                                            {feedback.answer && (
                                                <div className="profile-feedback-answer">
                                                    <span>📝 Ответ магазина:</span>
                                                    <p>{feedback.answer}</p>
                                                </div>
                                            )}

                                            <div className="profile-feedback-status">
                                                {feedback.isPublished ? (
                                                    <span style={{ color: '#22c55e' }}>✓ Опубликовано</span>
                                                ) : (
                                                    <span style={{ color: '#f59e0b' }}>⏳ На модерации</span>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}

                    {/* Заявки на ремонт */}
                    {activeTab === 'repairs' && (
                        <div>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                                <h2>🔧 Заявки на ремонт</h2>
                                <Link to="/service" className="btn-primary">➕ Новая заявка</Link>
                            </div>

                            {repairsList.length === 0 ? (
                                <div className="profile-empty">
                                    <span>🔧</span>
                                    <p>У вас пока нет заявок на ремонт</p>
                                    <Link to="/service" className="btn-primary">Оставить заявку</Link>
                                </div>
                            ) : (
                                <div className="profile-repairs">
                                    {repairsList.map(repair => (
                                        <div key={repair.id} className="profile-repair-card">
                                            <div className="profile-repair-header">
                                                <div>
                                                    <span className="profile-repair-id">Заявка #{repair.id}</span>
                                                    <span className="profile-repair-date">{formatDate(repair.createdAt)}</span>
                                                </div>
                                                {getRepairStatusBadge(repair.status)}
                                            </div>

                                            <div className="profile-repair-device">
                                                <strong>Устройство:</strong> {repair.deviceType}
                                            </div>

                                            <div className="profile-repair-problem">
                                                <strong>Проблема:</strong> {repair.problemDescription}
                                            </div>

                                            {repair.masterComment && (
                                                <div className="profile-repair-comment">
                                                    <strong>Комментарий мастера:</strong> {repair.masterComment}
                                                </div>
                                            )}

                                            <div className="profile-repair-footer">
                                                {repair.finalPrice ? (
                                                    <span className="profile-repair-price">💰 {repair.finalPrice.toLocaleString()} ₽</span>
                                                ) : repair.estimatedPrice ? (
                                                    <span className="profile-repair-price">💰 Ориентировочно: {repair.estimatedPrice.toLocaleString()} ₽</span>
                                                ) : (
                                                    <span className="profile-repair-price">💰 Стоимость не определена</span>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}

                    {/* Настройки */}
                    {activeTab === 'settings' && (
                        <div>
                            <h2 style={{ marginBottom: '20px' }}>⚙️ Настройки профиля</h2>

                            {/* Уведомления */}
                            <div className="profile-settings-section">
                                <h3>Уведомления</h3>
                                <label className="profile-settings-switch">
                                    <input
                                        type="checkbox"
                                        checked={settings.notifications}
                                        onChange={(e) => updateSettings('notifications', e.target.checked)}
                                        disabled={updating}
                                    />
                                    <span>Получать уведомления о статусе заказов</span>
                                </label>
                                <label className="profile-settings-switch">
                                    <input
                                        type="checkbox"
                                        checked={settings.chatConsent}
                                        onChange={(e) => updateSettings('chatConsent', e.target.checked)}
                                        disabled={updating}
                                    />
                                    <span>Разрешить обработку данных чата для улучшения сервиса</span>
                                </label>
                            </div>

                            {/* Смена пароля */}
                            <div className="profile-settings-section">
                                <h3>Смена пароля</h3>
                                <form onSubmit={changePassword} className="profile-password-form">
                                    <input
                                        type="password"
                                        placeholder="Текущий пароль"
                                        value={passwordData.oldPassword}
                                        onChange={(e) => setPasswordData(prev => ({ ...prev, oldPassword: e.target.value }))}
                                        required
                                        className="checkout-input"
                                    />
                                    <input
                                        type="password"
                                        placeholder="Новый пароль"
                                        value={passwordData.newPassword}
                                        onChange={(e) => setPasswordData(prev => ({ ...prev, newPassword: e.target.value }))}
                                        required
                                        className="checkout-input"
                                    />
                                    <input
                                        type="password"
                                        placeholder="Подтвердите новый пароль"
                                        value={passwordData.confirmPassword}
                                        onChange={(e) => setPasswordData(prev => ({ ...prev, confirmPassword: e.target.value }))}
                                        required
                                        className="checkout-input"
                                    />
                                    <button type="submit" disabled={updating} className="btn-primary">
                                        {updating ? 'Сохранение...' : 'Изменить пароль'}
                                    </button>
                                </form>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}