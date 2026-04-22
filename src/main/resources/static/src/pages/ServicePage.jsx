// src/pages/ServicePage.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { repairRequests, fileUpload } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function ServicePage() {
    const navigate = useNavigate();
    const { user } = useAuth();

    // Состояние формы
    const [formData, setFormData] = useState({
        deviceType: '',
        problemDescription: '',
        estimatedPrice: '',
        contactPhone: user?.phone || '',
        contactEmail: user?.email || ''
    });

    // Файлы
    const [files, setFiles] = useState([]);
    const [uploading, setUploading] = useState(false);

    // Статус отправки
    const [submitting, setSubmitting] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState('');

    // Список последних заявок пользователя
    const [recentRequests, setRecentRequests] = useState([]);
    const [loading, setLoading] = useState(true);

    // Типы устройств
    const deviceTypes = [
        { id: 'notebook', name: '💻 Ноутбук', price: 'от 1500 ₽' },
        { id: 'pc', name: '🖥️ Компьютер/Системный блок', price: 'от 1000 ₽' },
        { id: 'monitor', name: '🖥️ Монитор', price: 'от 1200 ₽' },
        { id: 'laptop', name: '📱 Планшет', price: 'от 1500 ₽' },
        { id: 'phone', name: '📱 Смартфон', price: 'от 1000 ₽' },
        { id: 'printer', name: '🖨️ Принтер/МФУ', price: 'от 1000 ₽' },
        { id: 'console', name: '🎮 Игровая консоль', price: 'от 1500 ₽' },
        { id: 'other', name: '🔧 Другое', price: 'договорная' }
    ];

    // Загрузка последних заявок
    useEffect(() => {
        if (user) {
            loadRecentRequests();
        } else {
            setLoading(false);
        }
    }, [user]);

    const loadRecentRequests = async () => {
        try {
            const response = await repairRequests.getMyRequests({ page: 0, size: 5 });
            setRecentRequests(response.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки заявок:', error);
        } finally {
            setLoading(false);
        }
    };

    // Обновление полей формы
    const updateField = (field, value) => {
        setFormData(prev => ({ ...prev, [field]: value }));
        setError('');
    };

    // Обработка файлов
    const handleFileChange = (e) => {
        const selectedFiles = Array.from(e.target.files);
        setFiles(prev => [...prev, ...selectedFiles]);
    };

    const removeFile = (index) => {
        setFiles(prev => prev.filter((_, i) => i !== index));
    };

    // Отправка заявки
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Валидация
        if (!formData.deviceType) {
            setError('Выберите тип устройства');
            return;
        }
        if (!formData.problemDescription.trim()) {
            setError('Опишите проблему');
            return;
        }
        if (!formData.contactPhone.trim()) {
            setError('Укажите контактный телефон');
            return;
        }

        setSubmitting(true);
        setError('');

        try {
            // Создание заявки
            const requestData = {
                deviceType: deviceTypes.find(d => d.id === formData.deviceType)?.name || formData.deviceType,
                problemDescription: formData.problemDescription,
                estimatedPrice: formData.estimatedPrice ? parseFloat(formData.estimatedPrice) : null
            };

            const response = await repairRequests.create(requestData);
            const newRequest = response.data;

            // Загрузка файлов
            if (files.length > 0 && newRequest.id) {
                setUploading(true);
                const formData = new FormData();
                files.forEach(file => {
                    formData.append('files', file);
                });
                await fileUpload.uploadForRepair(newRequest.id, formData);
                setUploading(false);
            }

            setSuccess(true);

            // Обновляем список заявок
            await loadRecentRequests();

            // Сброс формы
            setFormData({
                deviceType: '',
                problemDescription: '',
                estimatedPrice: '',
                contactPhone: user?.phone || '',
                contactEmail: user?.email || ''
            });
            setFiles([]);

            // Автоматически скрываем сообщение об успехе через 5 секунд
            setTimeout(() => setSuccess(false), 5000);

        } catch (error) {
            console.error('Ошибка отправки заявки:', error);
            setError(error.response?.data?.message || 'Ошибка при отправке заявки');
        } finally {
            setSubmitting(false);
            setUploading(false);
        }
    };

    // Получение статуса заявки
    const getStatusInfo = (status) => {
        const statusMap = {
            'Принята': { text: '📋 Принята', color: '#f59e0b', description: 'Заявка принята, ожидает обработки' },
            'Диагностика': { text: '🔧 Диагностика', color: '#3b82f6', description: 'Проводится диагностика устройства' },
            'Ремонт': { text: '🛠️ Ремонт', color: '#8b5cf6', description: 'Устройство в ремонте' },
            'Готов к выдаче': { text: '✅ Готов к выдаче', color: '#22c55e', description: 'Ремонт завершён, можно забрать' },
            'Выдан': { text: '📦 Выдан', color: '#9ca3af', description: 'Устройство выдано клиенту' },
            'Отменена': { text: '❌ Отменена', color: '#ef4444', description: 'Заявка отменена' }
        };
        return statusMap[status] || { text: status, color: '#9ca3af', description: '' };
    };

    const formatDate = (dateStr) => {
        return new Date(dateStr).toLocaleDateString('ru-RU', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (!user) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>🔧 Сервисный центр</h2>
                <p style={{ color: '#9ca3af', marginBottom: '30px' }}>
                    Для оформления заявки на ремонт необходимо войти в аккаунт
                </p>
                <button onClick={() => navigate('/login')} className="btn-primary btn-lg">
                    Войти
                </button>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            {/* Заголовок */}
            <div style={{ textAlign: 'center', marginBottom: '40px' }}>
                <h1 style={{ marginBottom: '16px' }}>🔧 Сервисный центр</h1>
                <p style={{ color: '#9ca3af', maxWidth: '600px', margin: '0 auto' }}>
                    Профессиональный ремонт компьютерной техники и электроники.
                    Гарантия на все виды работ до 12 месяцев.
                </p>
            </div>

            {/* Уведомления */}
            {success && (
                <div className="service-success">
                    <span>✅</span>
                    <div>
                        <strong>Заявка успешно отправлена!</strong>
                        <p>Наш специалист свяжется с вами в ближайшее время.</p>
                    </div>
                </div>
            )}

            {error && (
                <div className="service-error">
                    <span>❌</span>
                    <div>
                        <strong>Ошибка</strong>
                        <p>{error}</p>
                    </div>
                </div>
            )}

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 400px', gap: '32px' }}>
                {/* Форма заявки */}
                <div className="service-form-container">
                    <h2 style={{ marginBottom: '24px' }}>📝 Оставить заявку на ремонт</h2>

                    <form onSubmit={handleSubmit} className="service-form">
                        {/* Тип устройства */}
                        <div className="service-form-group">
                            <label className="service-label">Тип устройства *</label>
                            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '12px' }}>
                                {deviceTypes.map(device => (
                                    <label key={device.id} className="service-device-option">
                                        <input
                                            type="radio"
                                            name="deviceType"
                                            value={device.id}
                                            checked={formData.deviceType === device.id}
                                            onChange={(e) => updateField('deviceType', e.target.value)}
                                        />
                                        <div>
                                            <div style={{ fontWeight: 'bold' }}>{device.name}</div>
                                            <div style={{ fontSize: '11px', color: '#9ca3af' }}>{device.price}</div>
                                        </div>
                                    </label>
                                ))}
                            </div>
                        </div>

                        {/* Описание проблемы */}
                        <div className="service-form-group">
                            <label className="service-label">Описание проблемы *</label>
                            <textarea
                                value={formData.problemDescription}
                                onChange={(e) => updateField('problemDescription', e.target.value)}
                                placeholder="Опишите подробно, что случилось с устройством..."
                                rows={5}
                                className="service-textarea"
                                required
                            />
                        </div>

                        {/* Ориентировочная стоимость */}
                        <div className="service-form-group">
                            <label className="service-label">Ориентировочная стоимость (₽)</label>
                            <input
                                type="number"
                                value={formData.estimatedPrice}
                                onChange={(e) => updateField('estimatedPrice', e.target.value)}
                                placeholder="Например, 3000"
                                className="service-input"
                            />
                            <div style={{ fontSize: '12px', color: '#9ca3af', marginTop: '4px' }}>
                                Необязательное поле. Точную стоимость назовёт мастер после диагностики.
                            </div>
                        </div>

                        {/* Файлы */}
                        <div className="service-form-group">
                            <label className="service-label">Фото/видео проблемы</label>
                            <div className="service-file-zone">
                                <input
                                    type="file"
                                    id="fileUpload"
                                    multiple
                                    accept="image/*,video/*"
                                    onChange={handleFileChange}
                                    style={{ display: 'none' }}
                                />
                                <label htmlFor="fileUpload" className="service-file-label">
                                    📎 Выберите файлы
                                </label>
                                <div style={{ fontSize: '12px', color: '#9ca3af', marginTop: '8px' }}>
                                    Можно загрузить фото или видео (макс. 10 МБ)
                                </div>
                            </div>

                            {files.length > 0 && (
                                <div className="service-file-list">
                                    {files.map((file, index) => (
                                        <div key={index} className="service-file-item">
                                            <span>{file.name} ({(file.size / 1024).toFixed(1)} KB)</span>
                                            <button type="button" onClick={() => removeFile(index)}>✕</button>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Контактные данные */}
                        <div className="service-form-group">
                            <label className="service-label">Контактный телефон *</label>
                            <input
                                type="tel"
                                value={formData.contactPhone}
                                onChange={(e) => updateField('contactPhone', e.target.value)}
                                placeholder="+7 (999) 123-45-67"
                                className="service-input"
                                required
                            />
                        </div>

                        <div className="service-form-group">
                            <label className="service-label">Email для уведомлений</label>
                            <input
                                type="email"
                                value={formData.contactEmail}
                                onChange={(e) => updateField('contactEmail', e.target.value)}
                                placeholder="your@email.com"
                                className="service-input"
                            />
                        </div>

                        {/* Кнопка отправки */}
                        <button
                            type="submit"
                            disabled={submitting || uploading}
                            className="btn btn-primary btn-lg"
                            style={{ width: '100%' }}
                        >
                            {submitting ? 'Отправка...' : uploading ? 'Загрузка файлов...' : '📤 Отправить заявку'}
                        </button>
                    </form>
                </div>

                {/* Боковая панель */}
                <div>
                    {/* Информация о сервисе */}
                    <div className="service-info">
                        <h3 style={{ marginBottom: '16px' }}>ℹ️ Информация</h3>

                        <div className="service-info-item">
                            <span>🕐</span>
                            <div>
                                <strong>Время работы</strong>
                                <p>Пн-Пт: 10:00 - 20:00</p>
                                <p>Сб-Вс: 11:00 - 18:00</p>
                            </div>
                        </div>

                        <div className="service-info-item">
                            <span>📍</span>
                            <div>
                                <strong>Адрес</strong>
                                <p>г. Ростов-на-Дону</p>
                                <p>ул. Программистов, д. 1</p>
                            </div>
                        </div>

                        <div className="service-info-item">
                            <span>📞</span>
                            <div>
                                <strong>Телефон</strong>
                                <p>+7 (863) 123-45-67</p>
                            </div>
                        </div>

                        <div className="service-info-item">
                            <span>🔧</span>
                            <div>
                                <strong>Гарантия</strong>
                                <p>До 12 месяцев на работы</p>
                                <p>До 30 дней на запчасти</p>
                            </div>
                        </div>
                    </div>

                    {/* Последние заявки */}
                    {recentRequests.length > 0 && (
                        <div className="service-recent">
                            <h3 style={{ marginBottom: '16px' }}>📋 Ваши последние заявки</h3>

                            {recentRequests.map(request => {
                                const statusInfo = getStatusInfo(request.status);
                                return (
                                    <div key={request.id} className="service-recent-item">
                                        <div className="service-recent-header">
                                            <span className="service-recent-id">Заявка #{request.id}</span>
                                            <span className="service-recent-status" style={{ background: statusInfo.color }}>
                                                {statusInfo.text}
                                            </span>
                                        </div>
                                        <div className="service-recent-device">{request.deviceType}</div>
                                        <div className="service-recent-date">{formatDate(request.createdAt)}</div>
                                        <button
                                            onClick={() => navigate('/profile', { state: { tab: 'repairs' } })}
                                            className="service-recent-link"
                                        >
                                            Подробнее →
                                        </button>
                                    </div>
                                );
                            })}

                            <button
                                onClick={() => navigate('/profile', { state: { tab: 'repairs' } })}
                                className="btn-outline btn-sm"
                                style={{ width: '100%', marginTop: '12px' }}
                            >
                                Все заявки
                            </button>
                        </div>
                    )}
                </div>
            </div>

            {/* Этапы работы */}
            <div style={{ marginTop: '60px' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '32px' }}>Как мы работаем</h2>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '24px' }}>
                    {[
                        { step: 1, icon: '📝', title: 'Оставьте заявку', desc: 'Опишите проблему и приложите фото' },
                        { step: 2, icon: '🔧', title: 'Диагностика', desc: 'Мастер проведёт диагностику' },
                        { step: 3, icon: '🛠️', title: 'Ремонт', desc: 'Согласуем стоимость и отремонтируем' },
                        { step: 4, icon: '✅', title: 'Выдача', desc: 'Заберите готовое устройство' }
                    ].map(step => (
                        <div key={step.step} className="service-step">
                            <div className="service-step-icon">{step.icon}</div>
                            <div className="service-step-number">Шаг {step.step}</div>
                            <h4>{step.title}</h4>
                            <p>{step.desc}</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}