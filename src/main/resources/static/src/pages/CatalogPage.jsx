// src/pages/CatalogPage.jsx
import React, { useState, useEffect } from 'react';
import { products, categories } from '../services/api';
import ProductCard from '../components/ProductCard';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function CatalogPage() {
    const [searchParams, setSearchParams] = useSearchParams();
    const { isAuthenticated } = useAuth();
    const [allProducts, setAllProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [categoryList, setCategoryList] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [priceMin, setPriceMin] = useState('');
    const [priceMax, setPriceMax] = useState('');
    const [sortBy, setSortBy] = useState('createdAt,desc');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const itemsPerPage = 12;

    // Загрузка категорий
    useEffect(() => {
        categories.getAll()
            .then(res => {
                setCategoryList(res.data);
            })
            .catch(err => console.error('Ошибка загрузки категорий:', err));
    }, []);

    // Загрузка всех товаров (один раз)
    useEffect(() => {
        setLoading(true);
        // Загружаем все активные товары без пагинации
        products.getAll({ page: 0, size: 1000, sort: 'createdAt,desc' })
            .then(res => {
                console.log('Загружено товаров:', res.data.content?.length || 0);
                setAllProducts(res.data.content || []);
                setLoading(false);
            })
            .catch(err => {
                console.error('Ошибка загрузки товаров:', err);
                setLoading(false);
            });
    }, []);

    // Применение фильтров и сортировки
    useEffect(() => {
        if (allProducts.length === 0) return;

        let filtered = [...allProducts];

        // Фильтр по категории
        if (selectedCategory) {
            filtered = filtered.filter(product => product.categoryId === parseInt(selectedCategory));
            console.log(`Фильтр по категории: ${selectedCategory}, найдено: ${filtered.length}`);
        }

        // Фильтр по цене
        if (priceMin) {
            filtered = filtered.filter(product => product.price >= parseFloat(priceMin));
        }
        if (priceMax) {
            filtered = filtered.filter(product => product.price <= parseFloat(priceMax));
        }

        // Сортировка
        switch(sortBy) {
            case 'price,asc':
                filtered.sort((a, b) => a.price - b.price);
                break;
            case 'price,desc':
                filtered.sort((a, b) => b.price - a.price);
                break;
            case 'name,asc':
                filtered.sort((a, b) => a.name.localeCompare(b.name));
                break;
            case 'createdAt,desc':
            default:
                filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
                break;
        }

        // Пагинация
        const start = page * itemsPerPage;
        const end = start + itemsPerPage;
        const paginated = filtered.slice(start, end);

        setFilteredProducts(paginated);
        setTotalElements(filtered.length);
        setTotalPages(Math.ceil(filtered.length / itemsPerPage));

    }, [allProducts, selectedCategory, priceMin, priceMax, sortBy, page]);

    // Обновление URL при изменении фильтров
    useEffect(() => {
        const params = new URLSearchParams();
        if (selectedCategory) params.set('category', selectedCategory);
        if (priceMin) params.set('priceMin', priceMin);
        if (priceMax) params.set('priceMax', priceMax);
        if (sortBy !== 'createdAt,desc') params.set('sort', sortBy);
        setSearchParams(params);
    }, [selectedCategory, priceMin, priceMax, sortBy, setSearchParams]);

    // Чтение фильтров из URL при загрузке
    useEffect(() => {
        const categoryId = searchParams.get('category');
        const minPrice = searchParams.get('priceMin');
        const maxPrice = searchParams.get('priceMax');
        const sort = searchParams.get('sort');

        if (categoryId) setSelectedCategory(categoryId);
        if (minPrice) setPriceMin(minPrice);
        if (maxPrice) setPriceMax(maxPrice);
        if (sort) setSortBy(sort);
    }, [searchParams]);

    // Если пришёл slug (старые ссылки) — преобразуем в id, когда категории загрузились
    useEffect(() => {
        const categoryParam = searchParams.get('category');
        if (!categoryParam) return;

        const isNumeric = /^\d+$/.test(categoryParam);
        if (isNumeric) return;

        const match = categoryList.find(c => c.slug === categoryParam);
        if (match?.id) {
            setSelectedCategory(String(match.id));
        }
    }, [categoryList, searchParams]);

    const handleCategoryChange = (e) => {
        setSelectedCategory(e.target.value);
        setPage(0);
    };

    const resetFilters = () => {
        setSelectedCategory('');
        setPriceMin('');
        setPriceMax('');
        setSortBy('createdAt,desc');
        setPage(0);
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '50px' }}>
                <div style={{ fontSize: '24px', marginBottom: '16px' }}>🔄</div>
                <h2>Загрузка товаров...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px' }}>
            {!isAuthenticated && (
                <div style={{
                    background: 'linear-gradient(135deg, #1e1b4b, #2e1065)',
                    borderRadius: '16px',
                    padding: '16px 18px',
                    marginBottom: '18px',
                    border: '1px solid rgba(192, 132, 252, 0.35)'
                }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', gap: '16px', flexWrap: 'wrap', alignItems: 'center' }}>
                        <div>
                            <div style={{ fontWeight: 700, marginBottom: 4 }}>Чтобы смотреть каталог полностью — зарегистрируйтесь</div>
                            <div style={{ color: '#ddd6fe', fontSize: 14 }}>
                                Гостям доступна только витрина на главной странице.
                            </div>
                        </div>
                        <div style={{ display: 'flex', gap: 10 }}>
                            <button className="btn-outline btn-sm" onClick={() => window.location.href = '/login'}>Войти</button>
                            <button className="btn-primary btn-sm" onClick={() => window.location.href = '/login'}>Регистрация</button>
                        </div>
                    </div>
                </div>
            )}
            <div style={{ display: 'flex', gap: '30px', flexWrap: 'wrap' }}>
                {/* Боковая панель фильтров */}
                <aside style={{ width: '280px', flexShrink: 0 }}>
                    <div style={{
                        background: '#15181f',
                        padding: '20px',
                        borderRadius: '16px',
                        position: 'sticky',
                        top: '100px'
                    }}>
                        <h3 style={{ marginBottom: '16px', fontSize: '20px' }}>Фильтры</h3>

                        {/* Категории */}
                        <div style={{ marginBottom: '24px' }}>
                            <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                                Категория
                            </label>
                            <select
                                value={selectedCategory}
                                onChange={handleCategoryChange}
                                disabled={!isAuthenticated}
                                style={{
                                    width: '100%',
                                    padding: '10px',
                                    background: '#0a0c10',
                                    border: '1px solid #3f434e',
                                    borderRadius: '8px',
                                    color: 'white',
                                    cursor: 'pointer'
                                }}
                            >
                                <option value="">Все категории</option>
                                {categoryList.map(cat => (
                                    <option key={cat.id} value={cat.id}>
                                        {cat.name}
                                    </option>
                                ))}
                            </select>
                        </div>

                        {/* Цена */}
                        <div style={{ marginBottom: '24px' }}>
                            <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                                Цена (₽)
                            </label>
                            <div style={{ display: 'flex', gap: '8px' }}>
                                <input
                                    type="number"
                                    placeholder="От"
                                    value={priceMin}
                                    onChange={(e) => {
                                        setPriceMin(e.target.value);
                                        setPage(0);
                                    }}
                                disabled={!isAuthenticated}
                                    style={{
                                        width: '50%',
                                        padding: '10px',
                                        background: '#0a0c10',
                                        border: '1px solid #3f434e',
                                        borderRadius: '8px',
                                        color: 'white'
                                    }}
                                />
                                <input
                                    type="number"
                                    placeholder="До"
                                    value={priceMax}
                                    onChange={(e) => {
                                        setPriceMax(e.target.value);
                                        setPage(0);
                                    }}
                                disabled={!isAuthenticated}
                                    style={{
                                        width: '50%',
                                        padding: '10px',
                                        background: '#0a0c10',
                                        border: '1px solid #3f434e',
                                        borderRadius: '8px',
                                        color: 'white'
                                    }}
                                />
                            </div>
                        </div>

                        {/* Сортировка */}
                        <div style={{ marginBottom: '24px' }}>
                            <label style={{ display: 'block', marginBottom: '8px', fontWeight: '500' }}>
                                Сортировка
                            </label>
                            <select
                                value={sortBy}
                                onChange={(e) => {
                                    setSortBy(e.target.value);
                                    setPage(0);
                                }}
                                disabled={!isAuthenticated}
                                style={{
                                    width: '100%',
                                    padding: '10px',
                                    background: '#0a0c10',
                                    border: '1px solid #3f434e',
                                    borderRadius: '8px',
                                    color: 'white',
                                    cursor: 'pointer'
                                }}
                            >
                                <option value="createdAt,desc">📅 Новинки</option>
                                <option value="price,asc">💰 Цена (по возрастанию)</option>
                                <option value="price,desc">💰 Цена (по убыванию)</option>
                                <option value="name,asc">📝 Название (А-Я)</option>
                            </select>
                        </div>

                        <button
                            onClick={resetFilters}
                            disabled={!isAuthenticated}
                            style={{
                                width: '100%',
                                padding: '10px',
                                background: 'transparent',
                                border: '1px solid #3f434e',
                                borderRadius: '8px',
                                color: 'white',
                                cursor: 'pointer',
                                transition: 'all 0.3s'
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.background = '#ff4444';
                                e.target.style.borderColor = '#ff4444';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.background = 'transparent';
                                e.target.style.borderColor = '#3f434e';
                            }}
                        >
                            🗑️ Сбросить фильтры
                        </button>
                    </div>
                </aside>

                {/* Основной контент */}
                <div style={{ flex: 1 }}>
                    {/* Заголовок и счетчик */}
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        marginBottom: '24px',
                        flexWrap: 'wrap',
                        gap: '16px'
                    }}>
                        <h2 style={{ fontSize: '28px', margin: 0 }}>Каталог товаров</h2>
                        <span style={{
                            color: '#9ca3af',
                            background: '#15181f',
                            padding: '8px 16px',
                            borderRadius: '20px',
                            fontSize: '14px'
                        }}>
                            Найдено: {totalElements} товаров
                        </span>
                    </div>

                    {/* Активные фильтры */}
                    {(selectedCategory || priceMin || priceMax || sortBy !== 'createdAt,desc') && (
                        <div style={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: '8px',
                            marginBottom: '24px',
                            padding: '12px',
                            background: '#15181f',
                            borderRadius: '12px'
                        }}>
                            <span style={{ color: '#9ca3af', marginRight: '8px' }}>Активные фильтры:</span>
                            {selectedCategory && (
                                <span style={{
                                    background: '#2a2f3a',
                                    padding: '4px 12px',
                                    borderRadius: '16px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '8px',
                                    fontSize: '14px'
                                }}>
                                    Категория: {categoryList.find(c => c.id === parseInt(selectedCategory))?.name}
                                    <button
                                        onClick={() => {
                                            setSelectedCategory('');
                                            setPage(0);
                                        }}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: '#ff4444',
                                            cursor: 'pointer',
                                            fontSize: '16px'
                                        }}
                                    >×</button>
                                </span>
                            )}
                            {priceMin && (
                                <span style={{
                                    background: '#2a2f3a',
                                    padding: '4px 12px',
                                    borderRadius: '16px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '8px',
                                    fontSize: '14px'
                                }}>
                                    От {priceMin} ₽
                                    <button
                                        onClick={() => {
                                            setPriceMin('');
                                            setPage(0);
                                        }}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: '#ff4444',
                                            cursor: 'pointer',
                                            fontSize: '16px'
                                        }}
                                    >×</button>
                                </span>
                            )}
                            {priceMax && (
                                <span style={{
                                    background: '#2a2f3a',
                                    padding: '4px 12px',
                                    borderRadius: '16px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '8px',
                                    fontSize: '14px'
                                }}>
                                    До {priceMax} ₽
                                    <button
                                        onClick={() => {
                                            setPriceMax('');
                                            setPage(0);
                                        }}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: '#ff4444',
                                            cursor: 'pointer',
                                            fontSize: '16px'
                                        }}
                                    >×</button>
                                </span>
                            )}
                            {sortBy !== 'createdAt,desc' && (
                                <span style={{
                                    background: '#2a2f3a',
                                    padding: '4px 12px',
                                    borderRadius: '16px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '8px',
                                    fontSize: '14px'
                                }}>
                                    Сортировка: {
                                        sortBy === 'price,asc' ? 'По возрастанию цены' :
                                        sortBy === 'price,desc' ? 'По убыванию цены' :
                                        'По названию'
                                    }
                                    <button
                                        onClick={() => {
                                            setSortBy('createdAt,desc');
                                            setPage(0);
                                        }}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: '#ff4444',
                                            cursor: 'pointer',
                                            fontSize: '16px'
                                        }}
                                    >×</button>
                                </span>
                            )}
                        </div>
                    )}

                    {/* Список товаров */}
                    {filteredProducts.length === 0 ? (
                        <div style={{
                            textAlign: 'center',
                            padding: '80px 20px',
                            background: '#15181f',
                            borderRadius: '16px'
                        }}>
                            <div style={{ fontSize: '64px', marginBottom: '16px' }}>🔍</div>
                            <h3 style={{ marginBottom: '8px' }}>Товары не найдены</h3>
                            <p style={{ color: '#9ca3af' }}>Попробуйте изменить параметры фильтрации</p>
                        </div>
                    ) : (
                        <>
                            <div className="product-grid">
                                {(isAuthenticated ? filteredProducts : filteredProducts.slice(0, itemsPerPage)).map(product => (
                                    <ProductCard key={product.id} product={product} />
                                ))}
                            </div>

                            {/* Пагинация */}
                            {totalPages > 1 && isAuthenticated && (
                                <div style={{
                                    display: 'flex',
                                    justifyContent: 'center',
                                    gap: '12px',
                                    marginTop: '48px',
                                    alignItems: 'center'
                                }}>
                                    <button
                                        onClick={() => setPage(p => Math.max(0, p - 1))}
                                        disabled={page === 0}
                                        style={{
                                            padding: '10px 20px',
                                            background: page === 0 ? '#2a2f3a' : '#15181f',
                                            border: '1px solid #3f434e',
                                            borderRadius: '8px',
                                            color: page === 0 ? '#6c757d' : 'white',
                                            cursor: page === 0 ? 'not-allowed' : 'pointer',
                                            transition: 'all 0.3s'
                                        }}
                                    >
                                        ← Назад
                                    </button>
                                    <span style={{
                                        padding: '10px 20px',
                                        background: '#15181f',
                                        borderRadius: '8px',
                                        minWidth: '120px',
                                        textAlign: 'center'
                                    }}>
                                        Страница {page + 1} из {totalPages}
                                    </span>
                                    <button
                                        onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                                        disabled={page >= totalPages - 1}
                                        style={{
                                            padding: '10px 20px',
                                            background: page >= totalPages - 1 ? '#2a2f3a' : '#15181f',
                                            border: '1px solid #3f434e',
                                            borderRadius: '8px',
                                            color: page >= totalPages - 1 ? '#6c757d' : 'white',
                                            cursor: page >= totalPages - 1 ? 'not-allowed' : 'pointer',
                                            transition: 'all 0.3s'
                                        }}
                                    >
                                        Вперед →
                                    </button>
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}