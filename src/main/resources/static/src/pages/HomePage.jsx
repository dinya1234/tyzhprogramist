// src/pages/HomePage.jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { products, categories } from '../services/api';
import ProductCard from '../components/ProductCard';

export default function HomePage() {
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
                background: 'linear-gradient(135deg, #0a0c10 0%, #1a1f2e 100%)',
                padding: '60px 0',
                textAlign: 'center',
                borderBottom: '1px solid #2a2d36'
            }}>
                <div className="container">
                    <h1 style={{ fontSize: '48px', marginBottom: '16px' }}>
                        Тыжпрограммист
                    </h1>
                    <p style={{ fontSize: '20px', color: '#9ca3af', marginBottom: '32px' }}>
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
                            to={`/catalog?category=${cat.slug}`}
                            style={{
                                background: '#15181f',
                                padding: '20px',
                                borderRadius: '16px',
                                textAlign: 'center',
                                textDecoration: 'none',
                                color: '#e4e6eb',
                                border: '1px solid #2a2d36',
                                transition: 'all 0.2s'
                            }}
                            onMouseEnter={(e) => e.currentTarget.style.borderColor = '#c084fc'}
                            onMouseLeave={(e) => e.currentTarget.style.borderColor = '#2a2d36'}
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
                    background: 'linear-gradient(135deg, #1e1b4b, #2e1065)',
                    borderRadius: '24px',
                    padding: '40px',
                    margin: '40px 0',
                    textAlign: 'center'
                }}>
                    <h2 style={{ marginBottom: '16px' }}>Нужна помощь с выбором?</h2>
                    <p style={{ marginBottom: '24px', color: '#c4b5fd' }}>
                        Наши консультанты помогут подобрать идеальный ПК под ваш бюджет
                    </p>
                    <button className="btn btn-primary btn-lg" id="openConsultantChat">
                        💬 Спросить эксперта
                    </button>
                </div>
            </div>
        </>
    );
}