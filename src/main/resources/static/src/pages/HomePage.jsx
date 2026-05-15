// src/pages/HomePage.jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { products, categories } from '../services/api';
import ProductCard from '../components/ProductCard';
import { useAuth } from '../context/AuthContext';
import { useChat } from '../context/ChatContext';

export default function HomePage() {
    const navigate = useNavigate();
    const { user } = useAuth();
    const { createSession, sendMessage, setIsOpen } = useChat();

    const [newProducts, setNewProducts] = useState([]);
    const [bestsellers, setBestsellers] = useState([]);
    const [categoryTree, setCategoryTree] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            products.getNew(8),
            products.getBestsellers(8),
            categories.getTree()
        ]).then(([newRes, bestRes, catRes]) => {
            setNewProducts(newRes.data || []);
            setBestsellers(bestRes.data || []);
            setCategoryTree(catRes.data || []);
            setLoading(false);
        }).catch(err => {
            console.error('Ошибка загрузки главной:', err);
            setLoading(false);
        });
    }, []);

    const handleAskExpert = async () => {
        if (!user) {
            navigate('/login');
            return;
        }
        if (user.role === 'ADMIN' || user.role === 'MODERATOR') {
            navigate('/consultant');
            return;
        }

        try {
            await createSession('HomePage', null, window.location.href);
            await sendMessage('Здравствуйте! Нужна помощь с выбором комплектующих/ПК 🙂');
            setIsOpen(true);
        } catch (e) {
            console.error(e);
            alert('Не удалось открыть чат');
        }
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '50px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    return (
        <>
            {/* Hero секция */}
            <section style={{
                background: 'linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%)',
                padding: '60px 0',
                textAlign: 'center',
                borderBottom: '1px solid var(--border)'
            }}>
                <div className="container">
                    <h1 style={{ fontSize: '48px', marginBottom: '16px' }}>
                        Тыжпрограммист
                    </h1>
                    <p style={{ fontSize: '20px', color: 'var(--text-secondary)', marginBottom: '32px' }}>
                        Компьютерная техника, комплектующие и уникальные сервисы
                    </p>
                    <div style={{ display: 'flex', gap: '16px', justifyContent: 'center' }}>
                        <Link to="/catalog" className="btn btn-primary btn-lg">В каталог</Link>
                        <Link to="/configurator" className="btn btn-outline btn-lg">Собрать ПК</Link>
                    </div>
                </div>
            </section>

            <div className="container">
                {/* Категории */}
                <h2 className="section-title">Категории</h2>
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
                    gap: '16px',
                    marginBottom: '40px'
                }}>
                    {categoryTree.slice(0, 6).map(cat => (
                        <Link
                            key={cat.id}
                            to={`/catalog?category=${cat.id}`}
                            style={{
                                background: 'var(--bg-tertiary)',
                                padding: '20px',
                                borderRadius: '16px',
                                textAlign: 'center',
                                textDecoration: 'none',
                                color: 'var(--text-primary)',
                                border: '1px solid var(--border)',
                                transition: 'all 0.2s'
                            }}
                            onMouseEnter={(e) => e.currentTarget.style.borderColor = 'var(--accent)'}
                            onMouseLeave={(e) => e.currentTarget.style.borderColor = 'var(--border)'}
                        >
                            <div style={{ fontSize: '32px', marginBottom: '8px' }}>📁</div>
                            <div style={{ fontWeight: 500 }}>{cat.name}</div>
                        </Link>
                    ))}
                </div>

                {/* Новинки */}
                <h2 className="section-title">🔥 Новинки</h2>
                <div className="product-grid">
                    {newProducts.map(product => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </div>

                {/* Хиты продаж */}
                <h2 className="section-title">⭐ Хиты продаж</h2>
                <div className="product-grid">
                    {bestsellers.map(product => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </div>

                {/* Баннер сервисов */}
                <div style={{
                    background: 'linear-gradient(135deg, var(--accent-bg), var(--bg-tertiary))',
                    borderRadius: '24px',
                    padding: '40px',
                    margin: '40px 0',
                    textAlign: 'center',
                    border: '1px solid var(--border)'
                }}>
                    <h2 style={{ marginBottom: '16px' }}>Нужна помощь с выбором?</h2>
                    <p style={{ marginBottom: '24px', color: 'var(--accent)' }}>
                        Наши консультанты помогут подобрать идеальный ПК под ваш бюджет
                    </p>
                    <button className="btn btn-primary btn-lg" id="openConsultantChat" onClick={handleAskExpert}>
                        💬 Спросить эксперта
                    </button>
                </div>
            </div>
        </>
    );
}