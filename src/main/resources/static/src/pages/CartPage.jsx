// src/pages/CartPage.jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { settings } from '../services/api';

export default function CartPage() {
    const navigate = useNavigate();
    const { cart, updateQuantity, removeFromCart, clearCart, refreshCart, loading } = useCart();

    const [deliveryInfo, setDeliveryInfo] = useState({
        deliveryCost: 500,
        freeDeliveryThreshold: 5000,
        isFree: false,
        remainingForFree: 0
    });
    const [isUpdating, setIsUpdating] = useState(false);
    const [promoCode, setPromoCode] = useState('');
    const [discount, setDiscount] = useState(0);
    const [promoError, setPromoError] = useState('');

    // Загрузка информации о доставке
    useEffect(() => {
        loadDeliveryInfo();
    }, [cart.totalPrice]);

    const loadDeliveryInfo = async () => {
        try {
            const response = await settings.getDeliveryInfo(cart.totalPrice || 0);
            setDeliveryInfo(response.data);
        } catch (error) {
            console.error('Ошибка загрузки информации о доставке:', error);
        }
    };

    // Обработка изменения количества
    const handleQuantityChange = async (productId, newQuantity) => {
        if (newQuantity < 1) return;
        if (newQuantity > 99) return;

        setIsUpdating(true);
        await updateQuantity(productId, newQuantity);
        setIsUpdating(false);
    };

    // Обработка удаления товара
    const handleRemoveItem = async (productId, productName) => {
        if (window.confirm(`Удалить "${productName}" из корзины?`)) {
            await removeFromCart(productId);
        }
    };

    // Очистка корзины
    const handleClearCart = async () => {
        if (window.confirm('Очистить всю корзину?')) {
            await clearCart();
        }
    };

    // Применение промокода
    const applyPromoCode = async () => {
        if (!promoCode.trim()) {
            setPromoError('Введите промокод');
            return;
        }

        setPromoError('');
        if (promoCode.toUpperCase() === 'WELCOME10') {
            setDiscount(10);
            alert('Промокод применён! Скидка 10%');
        } else if (promoCode.toUpperCase() === 'FREESHIP') {
            setDeliveryInfo(prev => ({ ...prev, deliveryCost: 0, isFree: true }));
            alert('Бесплатная доставка активирована!');
        } else {
            setPromoError('Неверный промокод');
        }
    };

    // Расчёт итоговой суммы
    const subtotal = cart.totalPrice || 0;
    const deliveryCost = deliveryInfo.isFree ? 0 : (deliveryInfo.deliveryCost || 500);
    const discountAmount = (subtotal * discount) / 100;
    const total = subtotal + deliveryCost - discountAmount;

    // Переход к оформлению
    const handleCheckout = () => {
        if (cart.items?.length === 0) {
            alert('Корзина пуста');
            return;
        }
        navigate('/checkout');
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка корзины...</h2>
            </div>
        );
    }

    if (!cart.items || cart.items.length === 0) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <div style={{ fontSize: '64px', marginBottom: '20px' }}>🛒</div>
                <h2>Корзина пуста</h2>
                <p style={{ color: '#9ca3af', marginBottom: '30px' }}>
                    Добавьте товары в корзину, чтобы оформить заказ
                </p>
                <Link to="/catalog" className="btn btn-primary btn-lg">
                    Перейти в каталог
                </Link>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            <h1 style={{ marginBottom: '30px' }}>🛒 Корзина</h1>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 380px', gap: '32px' }}>
                {/* Список товаров */}
                <div>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        marginBottom: '20px',
                        paddingBottom: '12px',
                        borderBottom: '1px solid #2a2d36'
                    }}>
                        <span>Товары ({cart.totalItems} шт.)</span>
                        <button onClick={handleClearCart} className="btn-danger btn-sm">
                            Очистить корзину
                        </button>
                    </div>

                    {cart.items.map((item) => (
                        <div key={item.productId} className="cart-item">
                            <div className="cart-item-image">
                                {item.productImage ? (
                                    <img src={item.productImage} alt={item.productName} />
                                ) : (
                                    <span>📦</span>
                                )}
                            </div>

                            <div className="cart-item-info">
                                <Link to={`/product/${item.productId}`} className="cart-item-title">
                                    {item.productName}
                                </Link>
                                <div className="cart-item-price">
                                    {item.price.toLocaleString()} ₽
                                </div>
                            </div>

                            <div className="cart-item-quantity">
                                <button
                                    onClick={() => handleQuantityChange(item.productId, item.quantity - 1)}
                                    disabled={isUpdating || item.quantity <= 1}
                                    className="btn-outline btn-sm"
                                >
                                    −
                                </button>
                                <span className="cart-item-quantity-value">{item.quantity}</span>
                                <button
                                    onClick={() => handleQuantityChange(item.productId, item.quantity + 1)}
                                    disabled={isUpdating}
                                    className="btn-outline btn-sm"
                                >
                                    +
                                </button>
                            </div>

                            <div className="cart-item-total">
                                {(item.price * item.quantity).toLocaleString()} ₽
                            </div>

                            <button
                                onClick={() => handleRemoveItem(item.productId, item.productName)}
                                className="cart-item-remove"
                                title="Удалить"
                            >
                                ✕
                            </button>
                        </div>
                    ))}
                </div>

                {/* Боковая панель с итогом */}
                <div>
                    <div className="cart-summary">
                        <h3 style={{ marginBottom: '20px' }}>Итого</h3>

                        <div className="cart-summary-row">
                            <span>Товары ({cart.totalItems} шт.)</span>
                            <span>{subtotal.toLocaleString()} ₽</span>
                        </div>

                        <div className="cart-summary-row">
                            <span>Доставка</span>
                            <span>
                                {deliveryCost === 0 ? (
                                    <span style={{ color: '#22c55e' }}>Бесплатно</span>
                                ) : (
                                    `${deliveryCost.toLocaleString()} ₽`
                                )}
                            </span>
                        </div>

                        {discount > 0 && (
                            <div className="cart-summary-row" style={{ color: '#22c55e' }}>
                                <span>Скидка ({discount}%)</span>
                                <span>-{discountAmount.toLocaleString()} ₽</span>
                            </div>
                        )}

                        {deliveryInfo.remainingForFree > 0 && !deliveryInfo.isFree && (
                            <div className="cart-summary-free">
                                Добавьте товаров на {deliveryInfo.remainingForFree.toLocaleString()} ₽
                                для бесплатной доставки
                            </div>
                        )}

                        <div className="cart-summary-total">
                            <span>К оплате</span>
                            <span>{total.toLocaleString()} ₽</span>
                        </div>

                        {/* Промокод */}
                        <div className="cart-promo">
                            <input
                                type="text"
                                value={promoCode}
                                onChange={(e) => setPromoCode(e.target.value)}
                                placeholder="Промокод"
                                className="cart-promo-input"
                            />
                            <button onClick={applyPromoCode} className="btn-outline btn-sm">
                                Применить
                            </button>
                        </div>
                        {promoError && (
                            <div style={{ color: '#ef4444', fontSize: '12px', marginTop: '8px' }}>
                                {promoError}
                            </div>
                        )}

                        <button onClick={handleCheckout} className="btn btn-primary btn-lg" style={{ width: '100%' }}>
                            Оформить заказ →
                        </button>

                        <Link to="/catalog" className="cart-continue">
                            ← Продолжить покупки
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}