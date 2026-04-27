// src/pages/ComparisonPage.jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { comparisons, products, pcBuilds } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function ComparisonPage() {
    const { user } = useAuth();
    const { addToCart } = useCart();

    // Состояния
    const [comparisonNames, setComparisonNames] = useState([]);
    const [selectedComparison, setSelectedComparison] = useState(null);
    const [comparisonItems, setComparisonItems] = useState([]);
    const [loading, setLoading] = useState(true);

    // Для добавления новых элементов
    const [showAddModal, setShowAddModal] = useState(false);
    const [searchType, setSearchType] = useState('product'); // product, pcBuild
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [newComparisonName, setNewComparisonName] = useState('');
    const [creatingNew, setCreatingNew] = useState(false);

    // Загрузка списка сравнений пользователя
    useEffect(() => {
        if (user) {
            loadComparisons();
        } else {
            setLoading(false);
        }
    }, [user]);

    const loadComparisons = async () => {
        try {
            const response = await comparisons.getMyNames();
            setComparisonNames(response.data || []);

            if (response.data && response.data.length > 0) {
                setSelectedComparison(response.data[0]);
                loadComparisonItems(response.data[0]);
            }
        } catch (error) {
            console.error('Ошибка загрузки сравнений:', error);
        } finally {
            setLoading(false);
        }
    };

    const loadComparisonItems = async (comparisonName) => {
        try {
            const response = await comparisons.getComparison(comparisonName);
            const items = response.data || [];
            setComparisonItems(items);

            // Загружаем детали для каждого элемента
            const itemsWithDetails = await Promise.all(items.map(async (item) => {
                if (item.fromContentType === 'Product') {
                    const productRes = await products.getById(item.fromObjectId);
                    return {
                        ...item,
                        details: productRes.data,
                        type: 'product'
                    };
                } else if (item.fromContentType === 'PcBuild') {
                    let buildRes;
                    try {
                        buildRes = await pcBuilds.getMyBuild(item.fromObjectId);
                    } catch (_) {
                        buildRes = await pcBuilds.getPublicBuild(item.fromObjectId);
                    }
                    return {
                        ...item,
                        details: buildRes.data,
                        type: 'pcBuild'
                    };
                }
                return item;
            }));

            setComparisonItems(itemsWithDetails);
        } catch (error) {
            console.error('Ошибка загрузки элементов сравнения:', error);
        }
    };

    const handleComparisonChange = (name) => {
        setSelectedComparison(name);
        setCreatingNew(false);
        loadComparisonItems(name);
    };

    const handleSearch = async () => {
        if (!searchTerm.trim()) return;

        try {
            if (searchType === 'product') {
                const response = await products.search(searchTerm, { page: 0, size: 10 });
                setSearchResults(response.data.content || []);
            } else {
                const response = await pcBuilds.getMyBuilds({ page: 0, size: 100 });
                const allBuilds = response.data.content || [];
                const normalizedTerm = searchTerm.trim().toLowerCase();
                const filtered = allBuilds.filter((build) =>
                    String(build.name || '').toLowerCase().includes(normalizedTerm)
                );
                setSearchResults(filtered.slice(0, 10));
            }
        } catch (error) {
            console.error('Ошибка поиска:', error);
        }
    };

    const handleAddToComparison = async (itemId, itemName) => {
        try {
            if (creatingNew) {
                const normalizedNewName = String(newComparisonName || '').trim();
                if (!normalizedNewName) {
                    alert('Введите название сравнения');
                    return;
                }
                // Сравнение создается на сервере автоматически при добавлении первого элемента
                await comparisons.addToComparison(normalizedNewName, searchType === 'product' ? 'Product' : 'PcBuild', itemId);
                await loadComparisons();
                setSelectedComparison(normalizedNewName);
                setCreatingNew(false);
                setNewComparisonName('');
            } else if (selectedComparison) {
                await comparisons.addToComparison(selectedComparison, searchType === 'product' ? 'Product' : 'PcBuild', itemId);
                await loadComparisonItems(selectedComparison);
            }
            setShowAddModal(false);
            setSearchTerm('');
            setSearchResults([]);
        } catch (error) {
            alert(error.response?.data?.message || 'Ошибка добавления');
        }
    };

    const handleRemoveFromComparison = async (itemId, contentType) => {
        if (!selectedComparison) return;

        if (window.confirm('Удалить из сравнения?')) {
            try {
                if (contentType === 'Product') {
                    await comparisons.removeProduct(selectedComparison, itemId);
                } else {
                    await comparisons.removePcBuild(selectedComparison, itemId);
                }
                await loadComparisonItems(selectedComparison);
            } catch (error) {
                alert('Ошибка удаления');
            }
        }
    };

    const handleDeleteComparison = async () => {
        if (!selectedComparison) return;

        if (window.confirm(`Удалить сравнение "${selectedComparison}"?`)) {
            try {
                await comparisons.deleteComparison(selectedComparison);
                await loadComparisons();
                setSelectedComparison(null);
                setComparisonItems([]);
            } catch (error) {
                alert('Ошибка удаления');
            }
        }
    };

    const formatPrice = (price) => {
        return price?.toLocaleString() + ' ₽' || '0 ₽';
    };

    // Получение характеристик для отображения в таблице
    const getProductSpecs = (product) => {
        return {
            'Бренд': product.brand || '—',
            'Модель': product.model || product.name,
            'Категория': product.categoryName || '—',
            'Цена': formatPrice(product.price),
            'Наличие': product.quantity > 0 ? 'В наличии' : 'Нет в наличии',
            'Рейтинг': product.rating ? `${product.rating} ⭐` : '—',
            'Гарантия': `${product.warrantyMonths || 12} мес.`,
            'Вес': product.weight ? `${product.weight} кг` : '—'
        };
    };

    const getBuildSpecs = (build) => {
        const totalPower = build.components?.reduce((sum, comp) => {
            if (comp.componentType?.toLowerCase().includes('блок питания')) return sum;
            if (comp.componentType?.toLowerCase().includes('видеокарта')) return sum + 200;
            if (comp.componentType?.toLowerCase().includes('процессор')) return sum + 125;
            return sum + 50;
        }, 0) || 0;

        return {
            'Название': build.name,
            'Автор': build.userName || '—',
            'Цена': formatPrice(build.totalPrice),
            'Кол-во компонентов': build.components?.length || 0,
            'Просмотров': build.viewsCount || 0,
            'Дата создания': new Date(build.createdAt).toLocaleDateString('ru-RU'),
            'Рек. мощность БП': totalPower > 0 ? `${Math.ceil(totalPower * 1.3)}W` : '—'
        };
    };

    if (!user) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>📊 Сравнение товаров</h2>
                <p style={{ color: '#9ca3af', marginBottom: '30px' }}>
                    Для использования сравнения необходимо войти в аккаунт
                </p>
                <Link to="/login" className="btn-primary btn-lg">Войти</Link>
            </div>
        );
    }

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            <div className="comparison-header">
                <h1>📊 Сравнение товаров и сборок</h1>
                <button className="btn-primary" onClick={() => {
                    setCreatingNew(true);
                    setShowAddModal(true);
                }}>
                    ➕ Новое сравнение
                </button>
            </div>

            {/* Список сравнений */}
            {comparisonNames.length > 0 ? (
                <div className="comparison-tabs">
                    {comparisonNames.map(name => (
                        <button
                            key={name}
                            className={`comparison-tab ${selectedComparison === name ? 'active' : ''}`}
                            onClick={() => handleComparisonChange(name)}
                        >
                            {name}
                        </button>
                    ))}
                    <button className="comparison-tab add" onClick={() => {
                        // Добавить элемент в уже выбранное сравнение
                        setCreatingNew(false);
                        setShowAddModal(true);
                    }}>
                        + Добавить
                    </button>
                    {selectedComparison && (
                        <button className="comparison-delete" onClick={handleDeleteComparison}>
                            🗑️ Удалить
                        </button>
                    )}
                </div>
            ) : (
                <div className="comparison-empty">
                    <span>📊</span>
                    <h3>У вас нет сравнений</h3>
                    <p>Создайте новое сравнение, чтобы сравнивать товары и сборки ПК</p>
                    <button className="btn-primary" onClick={() => {
                        setCreatingNew(true);
                        setShowAddModal(true);
                    }}>
                        Создать сравнение
                    </button>
                </div>
            )}

            {/* Таблица сравнения */}
            {selectedComparison && comparisonItems.length > 0 && (
                <div className="comparison-table-wrapper">
                    <table className="comparison-table">
                        <thead>
                        <tr>
                            <th className="comparison-label">Характеристика</th>
                            {comparisonItems.map((item, idx) => (
                                <th key={idx} className="comparison-item-header">
                                    <div className="comparison-item-type">
                                        {item.type === 'product' ? '📦 Товар' : '🖥️ Сборка ПК'}
                                    </div>
                                    <div className="comparison-item-name">
                                        {item.type === 'product' ? item.details?.name : item.details?.name}
                                    </div>
                                    <button
                                        className="comparison-remove"
                                        onClick={() => handleRemoveFromComparison(item.fromObjectId, item.fromContentType)}
                                    >
                                        ✕
                                    </button>
                                </th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {/* Изображение */}
                        <tr>
                            <td className="comparison-label">Изображение</td>
                            {comparisonItems.map((item, idx) => (
                                <td key={idx} className="comparison-image-cell">
                                    {item.type === 'product' ? (
                                        item.details?.mainImage ? (
                                            <img src={item.details.mainImage} alt={item.details.name} />
                                        ) : (
                                            <div className="comparison-image-placeholder">📦</div>
                                        )
                                    ) : (
                                        <div className="comparison-image-placeholder">🖥️</div>
                                    )}
                                </td>
                            ))}
                        </tr>

                        {/* Характеристики */}
                        {comparisonItems[0]?.type === 'product' ? (
                            // Характеристики товара
                            Object.keys(getProductSpecs(comparisonItems[0]?.details || {})).map(specKey => (
                                <tr key={specKey}>
                                    <td className="comparison-label">{specKey}</td>
                                    {comparisonItems.map((item, idx) => {
                                        const specs = getProductSpecs(item.details || {});
                                        return (
                                            <td key={idx} className="comparison-value">
                                                {specs[specKey]}
                                            </td>
                                        );
                                    })}
                                </tr>
                            ))
                        ) : (
                            // Характеристики сборки
                            Object.keys(getBuildSpecs(comparisonItems[0]?.details || {})).map(specKey => (
                                <tr key={specKey}>
                                    <td className="comparison-label">{specKey}</td>
                                    {comparisonItems.map((item, idx) => {
                                        const specs = getBuildSpecs(item.details || {});
                                        return (
                                            <td key={idx} className="comparison-value">
                                                {specs[specKey]}
                                            </td>
                                        );
                                    })}
                                </tr>
                            ))
                        )}

                        {/* Кнопка добавления в корзину (только для товаров) */}
                        {comparisonItems[0]?.type === 'product' && (
                            <tr>
                                <td className="comparison-label">Действие</td>
                                {comparisonItems.map((item, idx) => (
                                    <td key={idx} className="comparison-action">
                                        <button
                                            className="btn-primary btn-sm"
                                            onClick={() => addToCart(item.fromObjectId, 1)}
                                            disabled={item.details?.quantity === 0}
                                        >
                                            🛒 В корзину
                                        </button>
                                        <Link to={`/product/${item.fromObjectId}`} className="btn-outline btn-sm">
                                            Подробнее
                                        </Link>
                                    </td>
                                ))}
                            </tr>
                        )}

                        {/* Кнопка просмотра сборки */}
                        {comparisonItems[0]?.type === 'pcBuild' && (
                            <tr>
                                <td className="comparison-label">Действие</td>
                                {comparisonItems.map((item, idx) => (
                                    <td key={idx} className="comparison-action">
                                        <Link to={`/configurator?buildId=${item.fromObjectId}`} className="btn-primary btn-sm">
                                            Редактировать
                                        </Link>
                                        <Link to={`/profile?tab=builds`} className="btn-outline btn-sm">
                                            Сохранить
                                        </Link>
                                    </td>
                                ))}
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            )}

            {selectedComparison && comparisonItems.length === 0 && (
                <div className="comparison-empty-items">
                    <p>В этом сравнении пока нет элементов</p>
                    <button className="btn-primary" onClick={() => {
                        setCreatingNew(false);
                        setShowAddModal(true);
                    }}>
                        Добавить товар или сборку
                    </button>
                </div>
            )}

            {/* Модальное окно добавления */}
            {showAddModal && (
                <div className="comparison-modal-overlay" onClick={() => {
                    setShowAddModal(false);
                    setCreatingNew(false);
                }}>
                    <div className="comparison-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="comparison-modal-header">
                            <h3>{creatingNew ? 'Новое сравнение' : 'Добавить в сравнение'}</h3>
                            <button className="comparison-modal-close" onClick={() => {
                                setShowAddModal(false);
                                setCreatingNew(false);
                            }}>✕</button>
                        </div>

                        {creatingNew && (
                            <div className="comparison-modal-field">
                                <label>Название сравнения</label>
                                <input
                                    type="text"
                                    value={newComparisonName}
                                    onChange={(e) => setNewComparisonName(e.target.value)}
                                    placeholder="Например: Игровые ПК 2024"
                                    className="comparison-modal-input"
                                />
                            </div>
                        )}

                        <div className="comparison-modal-field">
                            <label>Тип</label>
                            <div className="comparison-type-switch">
                                <button
                                    className={`comparison-type-btn ${searchType === 'product' ? 'active' : ''}`}
                                    onClick={() => setSearchType('product')}
                                >
                                    📦 Товар
                                </button>
                                <button
                                    className={`comparison-type-btn ${searchType === 'pcBuild' ? 'active' : ''}`}
                                    onClick={() => setSearchType('pcBuild')}
                                >
                                    🖥️ Сборка ПК
                                </button>
                            </div>
                        </div>

                        <div className="comparison-modal-field">
                            <label>Поиск</label>
                            <div className="comparison-search">
                                <input
                                    type="text"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    placeholder={searchType === 'product' ? 'Название товара...' : 'Название сборки...'}
                                    className="comparison-modal-input"
                                    onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                                />
                                <button onClick={handleSearch} className="btn-primary">Найти</button>
                            </div>
                        </div>

                        {searchResults.length > 0 && (
                            <div className="comparison-results">
                                {searchResults.map(result => (
                                    <div key={result.id} className="comparison-result-item" onClick={() => handleAddToComparison(result.id, result.name)}>
                                        <div className="comparison-result-info">
                                            <div className="comparison-result-name">{result.name}</div>
                                            <div className="comparison-result-price">
                                                {searchType === 'product' ? formatPrice(result.price) : `${result.components?.length || 0} компонентов`}
                                            </div>
                                        </div>
                                        <button className="comparison-result-add">+ Добавить</button>
                                    </div>
                                ))}
                            </div>
                        )}

                        {searchTerm && searchResults.length === 0 && (
                            <div className="comparison-no-results">
                                Ничего не найдено
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}