// src/pages/CheckoutPage.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { orders, settings } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function CheckoutPage() {
    const navigate = useNavigate();
    const { cart, clearCart, refreshCart, loading: cartLoading } = useCart();
    const { user } = useAuth();

    // Состояния формы
    const [step, setStep] = useState(1); // 1: данные, 2: подтверждение
    const [submitting, setSubmitting] = useState(false);

    // Данные заказа
    const [orderData, setOrderData] = useState({
        deliveryMethod: 'courier',
        deliveryAddress: '',
        deliveryAddressComment: '',
        paymentMethod: 'card_online',
        comment: '',
        usePersonalData: true,
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        phone: user?.phone || '',
        email: user?.email || ''
    });

    // Доступные методы доставки
    const deliveryMethods = [
        { id: 'courier', name: '🚚 Курьерская доставка', price: 500, description: 'Доставка по городу' },
        { id: 'pickup', name: '🏪 Самовывоз', price: 0, description: 'Самовывоз из магазина' },
        { id: 'postamat', name: '📦 Постамат', price: 350, description: 'Доставка до постамата' }
    ];

    // Доступные методы оплаты
    const paymentMethods = [
        { id: 'card_online', name: '💳 Банковская карта онлайн', description: 'Оплата через платёжную систему' },
        { id: 'card_cod', name: '💳 Картой при получении', description: 'Оплата картой курьеру' },
        { id: 'cash_cod', name: '💰 Наличными при получении', description: 'Оплата наличными курьеру' },
        { id: 'credit', name: '🏦 Кредит / Рассрочка', description: 'Оплата частями' }
    ];

    // Информация о доставке с бэка
    const [deliveryInfo, setDeliveryInfo] = useState({
        deliveryCost: 500,
        freeDeliveryThreshold: 5000,
        isFree: false
    });

    // Ошибки валидации
    const [errors, setErrors] = useState({});

    // Расчёты
    const subtotal = cart.totalPrice || 0;
    const deliveryCost = orderData.deliveryMethod === 'pickup' ? 0 :
        (deliveryInfo.isFree ? 0 : (deliveryMethods.find(d => d.id === orderData.deliveryMethod)?.price || 500));
    const total = subtotal + deliveryCost;

    // Загрузка информации о доставке
    useEffect(() => {
        loadDeliveryInfo();
    }, [subtotal]);

    // Проверка авторизации
    useEffect(() => {
        if (!user) {
            navigate('/login');
        }
    }, [user, navigate]);

    // Проверка корзины
    useEffect(() => {
        if (!cartLoading && (!cart.items || cart.items.length === 0)) {
            navigate('/cart');
        }
    }, [cart, cartLoading, navigate]);

    const loadDeliveryInfo = async () => {
        try {
            const response = await settings.getDeliveryInfo(subtotal);
            setDeliveryInfo(response.data);
        } catch (error) {
            console.error('Ошибка загрузки информации о доставке:', error);
        }
    };

    // Обновление полей формы
    const updateField = (field, value) => {
        setOrderData(prev => ({ ...prev, [field]: value }));
        if (errors[field]) {
            setErrors(prev => ({ ...prev, [field]: '' }));
        }
    };

    // Валидация формы
    const validateStep1 = () => {
        const newErrors = {};

        if (orderData.deliveryMethod !== 'pickup') {
            if (!orderData.deliveryAddress.trim()) {
                newErrors.deliveryAddress = 'Укажите адрес доставки';
            }
        }

        if (!orderData.phone.trim()) {
            newErrors.phone = 'Укажите номер телефона';
        } else if (!/^(\+7|8)?[\s\-]?\(?[0-9]{3}\)?[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}[\s\-]?[0-9]{2}$/.test(orderData.phone.replace(/\s/g, ''))) {
            newErrors.phone = 'Введите корректный номер телефона';
        }

        if (!orderData.email.trim()) {
            newErrors.email = 'Укажите email';
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(orderData.email)) {
            newErrors.email = 'Введите корректный email';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    // Создание заказа
    const createOrder = async () => {
        if (!validateStep1()) {
            setStep(1);
            return;
        }

        setSubmitting(true);

        try {
            const orderRequest = {
                deliveryMethod: orderData.deliveryMethod,
                deliveryAddress: orderData.deliveryMethod === 'pickup'
                    ? 'Самовывоз'
                    : `${orderData.deliveryAddress}${orderData.deliveryAddressComment ? ` (${orderData.deliveryAddressComment})` : ''}`,
                paymentMethod: orderData.paymentMethod,
                comment: orderData.comment
            };

            const response = await orders.create(orderRequest);
            const newOrder = response.data;

            // Очищаем корзину
            await clearCart();

            // Переход на страницу успеха
            navigate(`/order-success?orderId=${newOrder.id}`);

        } catch (error) {
            console.error('Ошибка создания заказа:', error);
            alert(error.response?.data?.message || 'Ошибка при оформлении заказа');
        } finally {
            setSubmitting(false);
        }
    };

    // Форматирование цены
    const formatPrice = (price) => {
        return price?.toLocaleString() + ' ₽' || '0 ₽';
    };

    if (cartLoading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '30px' }}>
                <Link to="/cart" style={{ color: '#9ca3af', textDecoration: 'none' }}>← Назад</Link>
                <h1 style={{ margin: 0 }}>Оформление заказа</h1>
            </div>

            {/* Шаги */}
            <div style={{ display: 'flex', marginBottom: '32px', borderBottom: '1px solid #2a2d36' }}>
                <div style={{
                    flex: 1,
                    textAlign: 'center',
                    padding: '12px',
                    borderBottom: step === 1 ? '2px solid #c084fc' : 'none',
                    color: step === 1 ? '#c084fc' : '#9ca3af'
                }}>
                    <span style={{ fontWeight: 'bold' }}>1. Данные для доставки</span>
                </div>
                <div style={{
                    flex: 1,
                    textAlign: 'center',
                    padding: '12px',
                    borderBottom: step === 2 ? '2px solid #c084fc' : 'none',
                    color: step === 2 ? '#c084fc' : '#9ca3af'
                }}>
                    <span style={{ fontWeight: 'bold' }}>2. Подтверждение заказа</span>
                </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 380px', gap: '32px' }}>
                {/* Основная форма */}
                <div>
                    {step === 1 ? (
                        <>
                            {/* Контактные данные */}
                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Контактные данные</h2>

                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
                                    <div>
                                        <label className="checkout-label">Имя</label>
                                        <input
                                            type="text"
                                            value={orderData.firstName}
                                            onChange={(e) => updateField('firstName', e.target.value)}
                                            className="checkout-input"
                                            placeholder="Иван"
                                        />
                                    </div>
                                    <div>
                                        <label className="checkout-label">Фамилия</label>
                                        <input
                                            type="text"
                                            value={orderData.lastName}
                                            onChange={(e) => updateField('lastName', e.target.value)}
                                            className="checkout-input"
                                            placeholder="Иванов"
                                        />
                                    </div>
                                </div>

                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                                    <div>
                                        <label className="checkout-label">Телефон *</label>
                                        <input
                                            type="tel"
                                            value={orderData.phone}
                                            onChange={(e) => updateField('phone', e.target.value)}
                                            className={`checkout-input ${errors.phone ? 'error' : ''}`}
                                            placeholder="+7 (999) 123-45-67"
                                        />
                                        {errors.phone && <div className="checkout-error">{errors.phone}</div>}
                                    </div>
                                    <div>
                                        <label className="checkout-label">Email *</label>
                                        <input
                                            type="email"
                                            value={orderData.email}
                                            onChange={(e) => updateField('email', e.target.value)}
                                            className={`checkout-input ${errors.email ? 'error' : ''}`}
                                            placeholder="ivan@example.com"
                                        />
                                        {errors.email && <div className="checkout-error">{errors.email}</div>}
                                    </div>
                                </div>
                            </div>

                            {/* Способ доставки */}
                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Способ доставки</h2>

                                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                                    {deliveryMethods.map(method => (
                                        <label key={method.id} className="checkout-radio">
                                            <input
                                                type="radio"
                                                name="deliveryMethod"
                                                value={method.id}
                                                checked={orderData.deliveryMethod === method.id}
                                                onChange={(e) => updateField('deliveryMethod', e.target.value)}
                                            />
                                            <div>
                                                <div style={{ fontWeight: 'bold' }}>{method.name}</div>
                                                <div style={{ fontSize: '12px', color: '#9ca3af' }}>{method.description}</div>
                                            </div>
                                            <div style={{ marginLeft: 'auto', color: '#c084fc' }}>
                                                {method.price === 0 ? 'Бесплатно' : `${method.price} ₽`}
                                            </div>
                                        </label>
                                    ))}
                                </div>

                                {orderData.deliveryMethod !== 'pickup' && (
                                    <div style={{ marginTop: '20px' }}>
                                        <label className="checkout-label">Адрес доставки *</label>
                                        <input
                                            type="text"
                                            value={orderData.deliveryAddress}
                                            onChange={(e) => updateField('deliveryAddress', e.target.value)}
                                            className={`checkout-input ${errors.deliveryAddress ? 'error' : ''}`}
                                            placeholder="Улица, дом, квартира"
                                        />
                                        {errors.deliveryAddress && <div className="checkout-error">{errors.deliveryAddress}</div>}

                                        <label className="checkout-label" style={{ marginTop: '12px' }}>Комментарий к адресу</label>
                                        <input
                                            type="text"
                                            value={orderData.deliveryAddressComment}
                                            onChange={(e) => updateField('deliveryAddressComment', e.target.value)}
                                            className="checkout-input"
                                            placeholder="Подъезд, этаж, домофон"
                                        />
                                    </div>
                                )}

                                {orderData.deliveryMethod === 'pickup' && (
                                    <div style={{
                                        marginTop: '16px',
                                        padding: '16px',
                                        background: '#1e2129',
                                        borderRadius: '12px'
                                    }}>
                                        <div style={{ fontWeight: 'bold', marginBottom: '8px' }}>🏪 Адрес самовывоза:</div>
                                        <div>г. Ростов-на-Дону, ул. Программистов, д. 1</div>
                                        <div style={{ fontSize: '12px', color: '#9ca3af', marginTop: '8px' }}>
                                            Часы работы: Пн-Пт 10:00-20:00, Сб-Вс 11:00-18:00
                                        </div>
                                    </div>
                                )}
                            </div>

                            {/* Способ оплаты */}
                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Способ оплаты</h2>

                                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                                    {paymentMethods.map(method => (
                                        <label key={method.id} className="checkout-radio">
                                            <input
                                                type="radio"
                                                name="paymentMethod"
                                                value={method.id}
                                                checked={orderData.paymentMethod === method.id}
                                                onChange={(e) => updateField('paymentMethod', e.target.value)}
                                            />
                                            <div>
                                                <div style={{ fontWeight: 'bold' }}>{method.name}</div>
                                                <div style={{ fontSize: '12px', color: '#9ca3af' }}>{method.description}</div>
                                            </div>
                                        </label>
                                    ))}
                                </div>
                            </div>

                            {/* Комментарий к заказу */}
                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Комментарий к заказу</h2>
                                <textarea
                                    value={orderData.comment}
                                    onChange={(e) => updateField('comment', e.target.value)}
                                    className="checkout-textarea"
                                    placeholder="Дополнительная информация к заказу..."
                                    rows={3}
                                />
                            </div>

                            <button onClick={() => setStep(2)} className="btn btn-primary btn-lg" style={{ width: '100%' }}>
                                Продолжить →
                            </button>
                        </>
                    ) : (
                        <>
                            {/* Шаг 2: Подтверждение */}
                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Ваши данные</h2>
                                <div className="checkout-confirm-row">
                                    <span>Получатель:</span>
                                    <span>{orderData.firstName} {orderData.lastName}</span>
                                </div>
                                <div className="checkout-confirm-row">
                                    <span>Телефон:</span>
                                    <span>{orderData.phone}</span>
                                </div>
                                <div className="checkout-confirm-row">
                                    <span>Email:</span>
                                    <span>{orderData.email}</span>
                                </div>
                            </div>

                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Доставка</h2>
                                <div className="checkout-confirm-row">
                                    <span>Способ:</span>
                                    <span>{deliveryMethods.find(m => m.id === orderData.deliveryMethod)?.name}</span>
                                </div>
                                {orderData.deliveryMethod !== 'pickup' && (
                                    <div className="checkout-confirm-row">
                                        <span>Адрес:</span>
                                        <span>{orderData.deliveryAddress} {orderData.deliveryAddressComment}</span>
                                    </div>
                                )}
                            </div>

                            <div className="checkout-section">
                                <h2 className="checkout-section-title">Оплата</h2>
                                <div className="checkout-confirm-row">
                                    <span>Способ:</span>
                                    <span>{paymentMethods.find(m => m.id === orderData.paymentMethod)?.name}</span>
                                </div>
                            </div>

                            {orderData.comment && (
                                <div className="checkout-section">
                                    <h2 className="checkout-section-title">Комментарий</h2>
                                    <p style={{ color: '#9ca3af' }}>{orderData.comment}</p>
                                </div>
                            )}

                            <div style={{ display: 'flex', gap: '16px', marginTop: '24px' }}>
                                <button onClick={() => setStep(1)} className="btn-outline btn-lg" style={{ flex: 1 }}>
                                    ← Назад
                                </button>
                                <button
                                    onClick={createOrder}
                                    disabled={submitting}
                                    className="btn btn-primary btn-lg"
                                    style={{ flex: 2 }}
                                >
                                    {submitting ? 'Оформление...' : '✅ Подтвердить заказ'}
                                </button>
                            </div>
                        </>
                    )}
                </div>

                {/* Боковая панель с заказом */}
                <div>
                    <div className="checkout-summary">
                        <h3 style={{ marginBottom: '20px' }}>Ваш заказ</h3>

                        {/* Товары */}
                        <div style={{ maxHeight: '300px', overflowY: 'auto', marginBottom: '16px' }}>
                            {cart.items?.map((item) => (
                                <div key={item.productId} className="checkout-item">
                                    <div className="checkout-item-info">
                                        <div className="checkout-item-name">{item.productName}</div>
                                        <div className="checkout-item-quantity">x{item.quantity}</div>
                                    </div>
                                    <div className="checkout-item-price">
                                        {(item.price * item.quantity).toLocaleString()} ₽
                                    </div>
                                </div>
                            ))}
                        </div>

                        <div style={{ borderTop: '1px solid #2a2d36', margin: '16px 0' }} />

                        {/* Итоги */}
                        <div className="checkout-summary-row">
                            <span>Товары ({cart.totalItems} шт.)</span>
                            <span>{formatPrice(subtotal)}</span>
                        </div>

                        <div className="checkout-summary-row">
                            <span>Доставка</span>
                            <span>{deliveryCost === 0 ? 'Бесплатно' : formatPrice(deliveryCost)}</span>
                        </div>

                        <div className="checkout-summary-total">
                            <span>Итого к оплате</span>
                            <span>{formatPrice(total)}</span>
                        </div>

                        {deliveryInfo.isFree && (
                            <div className="checkout-free-badge">
                                🎉 Бесплатная доставка
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}