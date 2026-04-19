import React, { useState } from 'react';
import { cart } from '../services/api';

export default function ProductCard({ product }) {
    const [adding, setAdding] = useState(false);

    const addToCart = async () => {
        setAdding(true);
        try {
            await cart.add(product.id, 1);
            alert(`${product.name} добавлен в корзину`);
        } catch (error) {
            console.error('Ошибка добавления в корзину:', error);
            alert('Ошибка при добавлении в корзину');
        } finally {
            setAdding(false);
        }
    };

    return (
        <div className="product-card">
            {product.isNew && <div className="badge">🔥 Новинка</div>}
            {product.isBestseller && <div className="badge" style={{ background: '#f59e0b' }}>⭐ Хит</div>}
            <div className="product-img" style={{ fontSize: '48px' }}>
                📦
            </div>
            <div className="product-info">
                <div className="product-title">{product.name}</div>
                <div className="product-price">{product.price?.toLocaleString()} ₽</div>
                {product.categoryName && (
                    <div style={{ fontSize: '12px', color: '#9ca3af', marginBottom: '12px' }}>
                        {product.categoryName}
                    </div>
                )}
                <button
                    onClick={addToCart}
                    disabled={adding}
                    className="btn btn-primary"
                    style={{ width: '100%' }}
                >
                    {adding ? 'Добавление...' : '🛒 В корзину'}
                </button>
            </div>
        </div>
    );
}