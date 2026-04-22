// src/pages/OrderSuccessPage.jsx
import React, { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { orders } from '../services/api';

export default function OrderSuccessPage() {
    const [searchParams] = useSearchParams();
    const orderId = searchParams.get('orderId');
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (orderId) {
            loadOrder();
        }
    }, [orderId]);

    const loadOrder = async () => {
        try {
            const response = await orders.getById(orderId);
            setOrder(response.data);
        } catch (error) {
            console.error('Ошибка загрузки заказа:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ textAlign: 'center', padding: '60px', maxWidth: '600px' }}>
            <div style={{ fontSize: '64px', marginBottom: '20px' }}>🎉</div>
            <h1 style={{ marginBottom: '16px' }}>Заказ оформлен!</h1>
            <p style={{ color: '#9ca3af', marginBottom: '8px' }}>
                Номер заказа: <strong style={{ color: '#c084fc' }}>#{order?.orderNumber || orderId}</strong>
            </p>
            <p style={{ color: '#9ca3af', marginBottom: '30px' }}>
                Спасибо за покупку! Мы свяжемся с вами в ближайшее время.
            </p>

            <div style={{ display: 'flex', gap: '16px', justifyContent: 'center' }}>
                <Link to="/catalog" className="btn btn-primary">
                    Продолжить покупки
                </Link>
                <Link to="/profile" className="btn-outline">
                    Мои заказы
                </Link>
            </div>
        </div>
    );
}