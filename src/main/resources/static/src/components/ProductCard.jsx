// src/components/ProductCard.jsx
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useChat } from '../context/ChatContext';

export default function ProductCard({ product }) {
    const [adding, setAdding] = useState(false);
    const { addToCart } = useCart();
    const { createSession, sendMessage, setIsOpen } = useChat();

    // Добавление в корзину
    const handleAddToCart = async (e) => {
        e.preventDefault();
        e.stopPropagation();

        setAdding(true);
        const result = await addToCart(product.id, 1);
        if (result.success) {
            // Показываем временное уведомление
            showNotification(`${product.name} добавлен в корзину`, 'success');
        } else {
            showNotification(result.error || 'Ошибка при добавлении в корзину', 'error');
        }
        setAdding(false);
    };

    // Открыть чат с вопросом о товаре
    const handleQuickQuestion = async (e) => {
        e.preventDefault();
        e.stopPropagation();

        await createSession('Product', product.id, window.location.href);
        await sendMessage(`Вопрос по товару: ${product.name}\nСсылка: /product/${product.id}`);
        setIsOpen(true);
    };

    // Уведомление
    const showNotification = (message, type = 'success') => {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        notification.style.cssText = `
            position: fixed;
            bottom: 20px;
            left: 20px;
            padding: 12px 20px;
            background: ${type === 'success' ? '#22c55e' : '#ef4444'};
            color: white;
            border-radius: 8px;
            z-index: 9999;
            animation: slideIn 0.3s ease;
        `;
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    };

    // Форматирование цены
    const formatPrice = (price) => {
        return price?.toLocaleString() + ' ₽' || '0 ₽';
    };

    // Рендер звёзд рейтинга
    const renderStars = (rating) => {
        if (!rating || rating === 0) return '☆☆☆☆☆';
        const fullStars = Math.floor(rating);
        const hasHalf = rating % 1 >= 0.5;
        return (
            <>
                {'★'.repeat(fullStars)}
                {hasHalf && '½'}
                {'☆'.repeat(5 - fullStars - (hasHalf ? 1 : 0))}
            </>
        );
    };

    return (
        <div className="product-card">
            {/* Бейджи новинка/хит */}
            <div style={{ position: 'absolute', display: 'flex', gap: '8px', padding: '12px', zIndex: 1 }}>
                {product.isNew && (
                    <span className="badge" style={{ background: '#c084fc' }}>
                        🔥 Новинка
                    </span>
                )}
                {product.isBestseller && (
                    <span className="badge" style={{ background: '#f59e0b' }}>
                        ⭐ Хит
                    </span>
                )}
            </div>

            {/* Ссылка на страницу товара */}
            <Link to={`/product/${product.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                {/* Изображение товара */}
                <div className="product-img">
                    {product.mainImage ? (
                        <img
                            src={product.mainImage}
                            alt={product.name}
                            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        />
                    ) : (
                        <span style={{ fontSize: '48px' }}>📦</span>
                    )}
                </div>

                {/* Информация о товаре */}
                <div className="product-info">
                    <div className="product-title">{product.name}</div>

                    {/* Рейтинг */}
                    {product.rating > 0 && (
                        <div className="rating" style={{ fontSize: '12px', color: '#fbbf24' }}>
                            {renderStars(product.rating)}
                            <span style={{ color: '#9ca3af', marginLeft: '4px' }}>
                                ({product.rating?.toFixed(1)})
                            </span>
                        </div>
                    )}

                    {/* Категория */}
                    {product.categoryName && (
                        <div style={{ fontSize: '12px', color: '#9ca3af', marginBottom: '8px' }}>
                            {product.categoryName}
                        </div>
                    )}

                    {/* Цена */}
                    <div className="product-price">
                        {product.oldPrice && product.oldPrice > product.price && (
                            <span style={{
                                fontSize: '14px',
                                color: '#9ca3af',
                                textDecoration: 'line-through',
                                marginRight: '8px'
                            }}>
                                {formatPrice(product.oldPrice)}
                            </span>
                        )}
                        <span style={{ fontSize: '20px', fontWeight: 'bold' }}>
                            {formatPrice(product.price)}
                        </span>
                    </div>

                    {/* Наличие */}
                    <div style={{
                        fontSize: '12px',
                        marginBottom: '12px',
                        color: product.quantity > 0 ? '#22c55e' : '#ef4444'
                    }}>
                        {product.quantity > 0 ? '✅ В наличии' : '❌ Нет в наличии'}
                    </div>
                </div>
            </Link>

            {/* Кнопки действий */}
            <div className="product-info" style={{ paddingTop: 0, display: 'flex', gap: '8px' }}>
                <button
                    onClick={handleAddToCart}
                    disabled={adding || product.quantity === 0}
                    className="btn btn-primary"
                    style={{ flex: 2 }}
                >
                    {adding ? '⏳' : '🛒 В корзину'}
                </button>
                <button
                    onClick={handleQuickQuestion}
                    className="btn-outline"
                    style={{ flex: 1, padding: '10px 8px' }}
                    title="Быстрый вопрос о товаре"
                >
                    ❓
                </button>
            </div>
        </div>
    );
}