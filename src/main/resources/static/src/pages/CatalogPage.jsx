
import React, { useState, useEffect } from 'react';
import { products, categories } from '../services/api';
import ProductCard from '../components/ProductCard';

export default function CatalogPage() {
    const [allProducts, setAllProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [categoryList, setCategoryList] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);

    // Фильтры
    const [priceMin, setPriceMin] = useState('');
    const [priceMax, setPriceMax] = useState('');
    const [sortBy, setSortBy] = useState('createdAt,desc');

    // Пагинация
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    // Загрузка категорий
    useEffect(() => {
        categories.getAll()
            .then(res => {
                setCategoryList(res.data);
            })
            .catch(err => console.error('Ошибка загрузки категорий:', err));
    }, []);

    // Загрузка товаров
    useEffect(() => {
        setLoading(true);

        const params = {
            page: page,
            size: 12,
            sort: sortBy
        };

        if (selectedCategory) {
            params.categoryId = selectedCategory;
        }

        if (priceMin) params.minPrice = priceMin;
        if (priceMax) params.maxPrice = priceMax;

        products.getAll(params)
            .then(res => {
                setAllProducts(res.data.content || []);
                setFilteredProducts(res.data.content || []);
                setTotalPages(res.data.totalPages || 0);
                setTotalElements(res.data.totalElements || 0);
                setLoading(false);
            })
            .catch(err => {
                console.error('Ошибка загрузки товаров:', err);
                setLoading(false);
            });
    }, [page, selectedCategory, sortBy, priceMin, priceMax]);

    // Применение фильтров на клиенте (для цены)
    useEffect(() => {
        let filtered = [...allProducts];

        if (priceMin) {
            filtered = filtered.filter(p => p.price >= Number(priceMin));
        }
        if (priceMax) {
            filtered = filtered.filter(p => p.price <= Number(priceMax));
        }

        setFilteredProducts(filtered);
    }, [priceMin, priceMax, allProducts]);

    const handleCategoryClick = (categoryId) => {
        setSelectedCategory(selectedCategory === categoryId ? null : categoryId);
        setPage(0);
    };

    const handleSortChange = (e) => {
        setSortBy(e.target.value);
        setPage(0);
    };

    const resetFilters = () => {
        setSelectedCategory(null);
        setPriceMin('');
        setPriceMax('');
        setSortBy('createdAt,desc');
        setPage(0);
    };

    if (loading && page === 0) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '50px' }}>
                <h2>Загрузка товаров...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px' }}>
            <div style={{ display: 'flex', gap: '30px' }}>
                {/* Боковая панель фильтров */}
                <aside style={{ width: '280px', flexShrink: 0 }}>
                    <div style={{ background: '#15181f', padding: '20px', borderRadius: '16px', position: 'sticky', top: '100px' }}>
                        <h3 style={{ marginBottom: '16px' }}>Фильтры</h3>

                        {/* Категории */}
                        <div style={{ marginBottom: '20px' }}>
                            <label style={{ display: 'block', marginBottom: '8px' }}>Категории</label>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                                {categoryList.map(cat => (
                                    <label key={cat.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                                        <input
                                            type="checkbox"
                                            checked={selectedCategory === cat.id}
                                            onChange={() => handleCategoryClick(cat.id)}
                                        />
                                        <span>{cat.name}</span>
                                    </label>
                                ))}
                            </div>
                        </div>

                        {/* Цена */}
                        <div style={{ marginBottom: '20px' }}>
                            <label style={{ display: 'block', marginBottom: '8px' }}>Цена</label>
                            <div style={{ display: 'flex', gap: '8px' }}>
                                <input
                                    type="number"
                                    placeholder="От"
                                    value={priceMin}
                                    onChange={(e) => setPriceMin(e.target.value)}
                                    style={{ width: '50%', padding: '8px', background: '#0a0c10', border: '1px solid #3f434e', borderRadius: '8px', color: 'white' }}
                                />
                                <input
                                    type="number"
                                    placeholder="До"
                                    value={priceMax}
                                    onChange={(e) => setPriceMax(e.target.value)}
                                    style={{ width: '50%', padding: '8px', background: '#0a0c10', border: '1px solid #3f434e', borderRadius: '8px', color: 'white' }}
                                />
                            </div>
                        </div>

                        {/* Сортировка */}
                        <div style={{ marginBottom: '20px' }}>
                            <label style={{ display: 'block', marginBottom: '8px' }}>Сортировка</label>
                            <select
                                value={sortBy}
                                onChange={handleSortChange}
                                style={{ width: '100%', padding: '8px', background: '#0a0c10', border: '1px solid #3f434e', borderRadius: '8px', color: 'white' }}
                            >
                                <option value="createdAt,desc">Новинки</option>
                                <option value="price,asc">Цена (по возрастанию)</option>
                                <option value="price,desc">Цена (по убыванию)</option>
                                <option value="name,asc">Название (А-Я)</option>
                            </select>
                        </div>

                        <button
                            onClick={resetFilters}
                            className="btn-outline"
                            style={{ width: '100%', marginTop: '20px' }}
                        >
                            Сбросить фильтры
                        </button>
                    </div>
                </aside>

                {/* Основной контент */}
                <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                        <h2>Товары</h2>
                        <span style={{ color: '#9ca3af' }}>Найдено: {totalElements}</span>
                    </div>

                    {filteredProducts.length === 0 ? (
                        <div style={{ textAlign: 'center', padding: '60px', background: '#15181f', borderRadius: '16px' }}>
                            🔍 Товары не найдены. Попробуйте изменить фильтры.
                        </div>
                    ) : (
                        <>
                            <div className="product-grid">
                                {filteredProducts.map(product => (
                                    <ProductCard key={product.id} product={product} />
                                ))}
                            </div>

                            {/* Пагинация */}
                            {totalPages > 1 && (
                                <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '40px' }}>
                                    <button
                                        onClick={() => setPage(p => Math.max(0, p - 1))}
                                        disabled={page === 0}
                                        className="btn-outline"
                                    >
                                        ← Назад
                                    </button>
                                    <span style={{ padding: '8px 16px', background: '#15181f', borderRadius: '8px' }}>
                    Страница {page + 1} из {totalPages}
                  </span>
                                    <button
                                        onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                                        disabled={page >= totalPages - 1}
                                        className="btn-outline"
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