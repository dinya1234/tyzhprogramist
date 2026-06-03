// src/pages/ProductPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { products, feedbacks, cart } from '../services/api';
import { useCart } from '../context/CartContext';
import ProductCard from '../components/ProductCard';
import { getProductSpecRows } from '../utils/productSpecs';

export default function ProductPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { addToCart } = useCart();

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('description');

    const [reviews, setReviews] = useState([]);
    const [questions, setQuestions] = useState([]);
    const [averageRating, setAverageRating] = useState(0);

    const [reviewText, setReviewText] = useState('');
    const [reviewRating, setReviewRating] = useState(5);
    const [questionText, setQuestionText] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const [relatedProducts, setRelatedProducts] = useState([]);
    const [frequentlyBought, setFrequentlyBought] = useState([]);
    const [quantity, setQuantity] = useState(1);

    const [reviewsPage, setReviewsPage] = useState(0);
    const [reviewsTotalPages, setReviewsTotalPages] = useState(0);
    const [questionsPage, setQuestionsPage] = useState(0);
    const [questionsTotalPages, setQuestionsTotalPages] = useState(0);

    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const isAuthenticated = !!localStorage.getItem('accessToken');

    useEffect(() => {
        setLoading(true);
        products.getById(id)
            .then(res => {
                setProduct(res.data);
                setRelatedProducts(res.data.relatedProducts || []);
                setFrequentlyBought(res.data.frequentlyBought || []);
            })
            .catch(err => {
                console.error('Ошибка загрузки товара:', err);
                if (err.response?.status === 404) {
                    navigate('/catalog');
                }
            })
            .finally(() => setLoading(false));
    }, [id, navigate]);

    useEffect(() => {
        if (!id) return;

        Promise.all([
            feedbacks.getProductReviews(id, { page: reviewsPage, size: 5 }),
            feedbacks.getProductQuestions(id, { page: questionsPage, size: 5 }),
            feedbacks.getRating(id)
        ]).then(([reviewsRes, questionsRes, ratingRes]) => {
            setReviews(reviewsRes.data.content || []);
            setReviewsTotalPages(reviewsRes.data.totalPages || 0);
            setQuestions(questionsRes.data.content || []);
            setQuestionsTotalPages(questionsRes.data.totalPages || 0);
            setAverageRating(ratingRes.data || 0);
        }).catch(err => {
            console.error('Ошибка загрузки отзывов:', err);
        });
    }, [id, reviewsPage, questionsPage]);

    useEffect(() => {
        if (!id) return;
        feedbacks.getRating(id)
            .then(res => setAverageRating(res.data || 0))
            .catch(console.error);
    }, [id]);

    const handleAddToCart = async () => {
        const result = await addToCart(product.id, quantity);
        if (result.success) {
            alert(`Товар добавлен в корзину (${quantity} шт.)`);
        } else {
            alert(result.error || 'Ошибка добавления в корзину');
        }
    };

    const handleSubmitReview = async (e) => {
        e.preventDefault();
        if (!isAuthenticated) {
            alert('Для оставления отзыва необходимо войти');
            navigate('/login');
            return;
        }

        setSubmitting(true);
        try {
            await feedbacks.create({
                productId: parseInt(id),
                text: reviewText,
                rating: reviewRating,
                isQuestion: false
            });
            alert('Спасибо за отзыв! Он будет опубликован после модерации.');
            setReviewText('');
            setReviewRating(5);
            const res = await feedbacks.getProductReviews(id, { page: 0, size: 5 });
            setReviews(res.data.content || []);
            setReviewsTotalPages(res.data.totalPages || 0);
            setReviewsPage(0);
        } catch (err) {
            alert(err.response?.data?.message || 'Ошибка отправки отзыва');
        } finally {
            setSubmitting(false);
        }
    };

    const handleSubmitQuestion = async (e) => {
        e.preventDefault();
        if (!isAuthenticated) {
            alert('Для отправки вопроса необходимо войти');
            navigate('/login');
            return;
        }

        setSubmitting(true);
        try {
            await feedbacks.create({
                productId: parseInt(id),
                text: questionText,
                isQuestion: true
            });
            alert('Вопрос отправлен! Ответ появится после ответа консультанта.');
            setQuestionText('');
            const res = await feedbacks.getProductQuestions(id, { page: 0, size: 5 });
            setQuestions(res.data.content || []);
            setQuestionsTotalPages(res.data.totalPages || 0);
            setQuestionsPage(0);
        } catch (err) {
            alert(err.response?.data?.message || 'Ошибка отправки вопроса');
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка товара...</h2>
            </div>
        );
    }

    if (!product) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Товар не найден</h2>
                <button onClick={() => navigate('/catalog')} className="btn btn-primary">
                    Вернуться в каталог
                </button>
            </div>
        );
    }

    const renderStars = (rating, showNumber = true) => {
        const fullStars = Math.floor(rating);
        const hasHalf = rating % 1 >= 0.5;

        return (
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ color: '#fbbf24' }}>
                    {'★'.repeat(fullStars)}
                    {hasHalf && '½'}
                    {'☆'.repeat(5 - fullStars - (hasHalf ? 1 : 0))}
                </span>
                {showNumber && <span style={{ color: 'var(--text-secondary)' }}>({rating.toFixed(1)})</span>}
            </span>
        );
    };

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            {/* Хлебные крошки */}
            <div style={{ marginBottom: '24px', color: 'var(--text-secondary)', fontSize: '14px' }}>
                <span onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>Главная</span>
                {' / '}
                <span onClick={() => navigate('/catalog')} style={{ cursor: 'pointer' }}>Каталог</span>
                {' / '}
                <span onClick={() => navigate(`/catalog?category=${product.categorySlug}`)} style={{ cursor: 'pointer' }}>
                    {product.categoryName}
                </span>
                {' / '}
                <span style={{ color: 'var(--text-primary)' }}>{product.name}</span>
            </div>

            {/* Основная информация о товаре */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '48px', marginBottom: '48px' }}>
                <div>
                    <div style={{
                        background: 'var(--bg-tertiary)',
                        borderRadius: '24px',
                        padding: '40px',
                        textAlign: 'center',
                        border: '1px solid var(--border)'
                    }}>
                        {product.mainImage ? (
                            <img
                                src={product.mainImage}
                                alt={product.name}
                                style={{ maxWidth: '100%', maxHeight: '400px', objectFit: 'contain' }}
                            />
                        ) : (
                            <div style={{ fontSize: '120px' }}>📦</div>
                        )}
                    </div>

                    {product.images && product.images.length > 1 && (
                        <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
                            {product.images.slice(0, 4).map((img, idx) => (
                                <div key={idx} style={{
                                    width: '80px',
                                    height: '80px',
                                    background: 'var(--bg-tertiary)',
                                    borderRadius: '12px',
                                    border: '1px solid var(--border)',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    cursor: 'pointer'
                                }}>
                                    <span style={{ fontSize: '32px' }}>📷</span>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <div>
                    {product.isNew && <span className="badge" style={{ background: 'var(--accent)', marginRight: '8px' }}>🔥 Новинка</span>}
                    {product.isBestseller && <span className="badge" style={{ background: '#f59e0b' }}>⭐ Хит продаж</span>}

                    <h1 style={{ fontSize: '32px', marginTop: '16px', marginBottom: '16px', color: 'var(--text-primary)' }}>{product.name}</h1>

                    <div style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '16px' }}>
                        {renderStars(averageRating)}
                        <span style={{ color: 'var(--text-secondary)' }}>|</span>
                        <span
                            onClick={() => setActiveTab('reviews')}
                            style={{ cursor: 'pointer', color: 'var(--accent)' }}
                        >
                            {reviews.length} отзывов
                        </span>
                    </div>

                    <div style={{ marginBottom: '16px', color: 'var(--text-secondary)' }}>
                        Артикул: {product.sku}
                    </div>
                    <div style={{ marginBottom: '24px' }}>
                        {product.quantity > 0 ? (
                            <span style={{ color: 'var(--success)' }}>✅ В наличии ({product.quantity} шт.)</span>
                        ) : (
                            <span style={{ color: 'var(--danger)' }}>❌ Нет в наличии</span>
                        )}
                    </div>

                    <p style={{ color: 'var(--text-secondary)', marginBottom: '24px', lineHeight: 1.6 }}>
                        {product.shortDescription}
                    </p>

                    <div style={{ marginBottom: '24px' }}>
                        {product.oldPrice && product.oldPrice > product.price && (
                            <span style={{
                                fontSize: '20px',
                                color: 'var(--text-secondary)',
                                textDecoration: 'line-through',
                                marginRight: '12px'
                            }}>
                                {product.oldPrice.toLocaleString()} ₽
                            </span>
                        )}
                        <span style={{ fontSize: '36px', fontWeight: 'bold', color: 'var(--accent)' }}>
                            {product.price.toLocaleString()} ₽
                        </span>
                    </div>

                    <div style={{ display: 'flex', alignItems: 'center', gap: '16px', marginBottom: '24px' }}>
                        <span style={{ color: 'var(--text-primary)' }}>Количество:</span>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <button
                                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                className="btn-outline btn-sm"
                                style={{ padding: '8px 12px' }}
                            >
                                -
                            </button>
                            <input
                                type="number"
                                value={quantity}
                                onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                                style={{
                                    width: '60px',
                                    textAlign: 'center',
                                    background: 'var(--bg-input)',
                                    border: '1px solid var(--border-light)',
                                    borderRadius: '8px',
                                    color: 'var(--text-primary)',
                                    padding: '8px'
                                }}
                            />
                            <button
                                onClick={() => setQuantity(quantity + 1)}
                                className="btn-outline btn-sm"
                                style={{ padding: '8px 12px' }}
                            >
                                +
                            </button>
                        </div>
                    </div>

                    <div style={{ display: 'flex', gap: '16px' }}>
                        <button
                            onClick={handleAddToCart}
                            disabled={product.quantity === 0}
                            className="btn btn-primary btn-lg"
                            style={{ flex: 1 }}
                        >
                            🛒 Добавить в корзину
                        </button>
                        <button
                            className="btn-outline btn-lg"
                            onClick={() => alert('Функция "Купить в 1 клик" будет доступна позже')}
                        >
                            ⚡ Купить в 1 клик
                        </button>
                    </div>

                    <div style={{ marginTop: '32px', paddingTop: '24px', borderTop: '1px solid var(--border)' }}>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', fontSize: '14px', color: 'var(--text-secondary)' }}>
                            <div>🚚 Доставка: от 500 ₽ (бесплатно от 5000 ₽)</div>
                            <div>🏪 Самовывоз: бесплатно</div>
                            <div>💳 Оплата: картой онлайн, при получении</div>
                            <div>🔧 Гарантия: {product.warrantyMonths || 12} месяцев</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Табы */}
            <div style={{ marginTop: '48px' }}>
                <div style={{ display: 'flex', gap: '8px', borderBottom: '1px solid var(--border)', marginBottom: '24px' }}>
                    {[
                        { key: 'description', label: 'Описание' },
                        { key: 'specs', label: 'Характеристики' },
                        { key: 'reviews', label: `Отзывы (${reviews.length})` },
                        { key: 'qa', label: `Вопросы-ответы (${questions.length})` }
                    ].map(tab => (
                        <button
                            key={tab.key}
                            onClick={() => setActiveTab(tab.key)}
                            style={{
                                padding: '12px 24px',
                                background: 'none',
                                border: 'none',
                                color: activeTab === tab.key ? 'var(--accent)' : 'var(--text-secondary)',
                                borderBottom: activeTab === tab.key ? '2px solid var(--accent)' : 'none',
                                cursor: 'pointer',
                                fontWeight: activeTab === tab.key ? 'bold' : 'normal',
                                transition: 'all 0.2s'
                            }}
                        >
                            {tab.label}
                        </button>
                    ))}
                </div>

                {/* Описание */}
                {activeTab === 'description' && (
                    <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '32px', color: 'var(--text-primary)' }}>
                        <div dangerouslySetInnerHTML={{ __html: product.fullDescription || product.shortDescription }} />
                    </div>
                )}

                {/* Характеристики */}
                {activeTab === 'specs' && (
                    <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '32px', color: 'var(--text-primary)' }}>
                        <h3 style={{ marginBottom: '20px', color: 'var(--text-primary)' }}>Технические характеристики</h3>
                        <table className="specs-table">
                            <tbody>
                            {getProductSpecRows(product).map(({ label, value }, index) => (
                                <tr key={`${label}-${index}`}>
                                    <td style={{ color: 'var(--text-secondary)' }}>{label}</td>
                                    <td style={{ color: 'var(--text-primary)' }}>{value}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Отзывы */}
                {activeTab === 'reviews' && (
                    <div>
                        {isAuthenticated ? (
                            <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '24px', marginBottom: '32px', color: 'var(--text-primary)' }}>
                                <h3 style={{ marginBottom: '16px', color: 'var(--text-primary)' }}>Оставить отзыв</h3>
                                <form onSubmit={handleSubmitReview}>
                                    <div style={{ marginBottom: '16px' }}>
                                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-primary)' }}>Оценка</label>
                                        <div style={{ display: 'flex', gap: '8px' }}>
                                            {[1, 2, 3, 4, 5].map(star => (
                                                <button
                                                    key={star}
                                                    type="button"
                                                    onClick={() => setReviewRating(star)}
                                                    style={{
                                                        fontSize: '24px',
                                                        background: 'none',
                                                        border: 'none',
                                                        cursor: 'pointer',
                                                        color: star <= reviewRating ? '#fbbf24' : 'var(--text-muted)'
                                                    }}
                                                >
                                                    ★
                                                </button>
                                            ))}
                                        </div>
                                    </div>
                                    <div style={{ marginBottom: '16px' }}>
                                        <textarea
                                            value={reviewText}
                                            onChange={(e) => setReviewText(e.target.value)}
                                            placeholder="Поделитесь впечатлениями о товаре..."
                                            required
                                            rows={4}
                                            style={{
                                                width: '100%',
                                                padding: '12px',
                                                background: 'var(--bg-input)',
                                                border: '1px solid var(--border-light)',
                                                borderRadius: '12px',
                                                color: 'var(--text-primary)',
                                                resize: 'vertical'
                                            }}
                                        />
                                    </div>
                                    <button type="submit" disabled={submitting} className="btn btn-primary">
                                        {submitting ? 'Отправка...' : 'Отправить отзыв'}
                                    </button>
                                </form>
                            </div>
                        ) : (
                            <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '24px', marginBottom: '32px', textAlign: 'center', color: 'var(--text-primary)' }}>
                                <p>Чтобы оставить отзыв, пожалуйста, <button onClick={() => navigate('/login')} style={{ color: 'var(--accent)', background: 'none', border: 'none', cursor: 'pointer' }}>войдите</button> в аккаунт</p>
                            </div>
                        )}

                        {reviews.length === 0 ? (
                            <div style={{ textAlign: 'center', padding: '40px', background: 'var(--bg-tertiary)', borderRadius: '16px', color: 'var(--text-primary)' }}>
                                <span style={{ fontSize: '48px' }}>📝</span>
                                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>Пока нет отзывов. Будьте первым!</p>
                            </div>
                        ) : (
                            <>
                                {reviews.map(review => (
                                    <div key={review.id} style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '20px', marginBottom: '16px', color: 'var(--text-primary)' }}>
                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                                            <div>
                                                <span style={{ fontWeight: 'bold', color: 'var(--text-primary)' }}>{review.userName}</span>
                                                <div style={{ marginTop: '4px' }}>{renderStars(review.rating, false)}</div>
                                            </div>
                                            <span style={{ color: 'var(--text-muted)', fontSize: '12px' }}>
                                                {new Date(review.createdAt).toLocaleDateString('ru-RU')}
                                            </span>
                                        </div>
                                        <p style={{ lineHeight: 1.6, color: 'var(--text-primary)' }}>{review.text}</p>
                                        {review.answer && (
                                            <div style={{ marginTop: '16px', padding: '12px', background: 'var(--bg-card)', borderRadius: '12px' }}>
                                                <span style={{ color: 'var(--accent)', fontWeight: 'bold' }}>Ответ магазина:</span>
                                                <p style={{ marginTop: '8px', color: 'var(--text-primary)' }}>{review.answer}</p>
                                            </div>
                                        )}
                                    </div>
                                ))}

                                {reviewsTotalPages > 1 && (
                                    <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '24px' }}>
                                        <button
                                            onClick={() => setReviewsPage(p => Math.max(0, p - 1))}
                                            disabled={reviewsPage === 0}
                                            className="btn-outline btn-sm"
                                        >
                                            ←
                                        </button>
                                        <span style={{ padding: '8px 16px', background: 'var(--bg-tertiary)', borderRadius: '8px', color: 'var(--text-primary)' }}>
                                            {reviewsPage + 1} / {reviewsTotalPages}
                                        </span>
                                        <button
                                            onClick={() => setReviewsPage(p => Math.min(reviewsTotalPages - 1, p + 1))}
                                            disabled={reviewsPage >= reviewsTotalPages - 1}
                                            className="btn-outline btn-sm"
                                        >
                                            →
                                        </button>
                                    </div>
                                )}
                            </>
                        )}
                    </div>
                )}

                {/* Вопросы-ответы */}
                {activeTab === 'qa' && (
                    <div>
                        {isAuthenticated ? (
                            <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '24px', marginBottom: '32px', color: 'var(--text-primary)' }}>
                                <h3 style={{ marginBottom: '16px', color: 'var(--text-primary)' }}>Задать вопрос</h3>
                                <form onSubmit={handleSubmitQuestion}>
                                    <textarea
                                        value={questionText}
                                        onChange={(e) => setQuestionText(e.target.value)}
                                        placeholder="Что вас интересует о этом товаре?"
                                        required
                                        rows={3}
                                        style={{
                                            width: '100%',
                                            padding: '12px',
                                            background: 'var(--bg-input)',
                                            border: '1px solid var(--border-light)',
                                            borderRadius: '12px',
                                            color: 'var(--text-primary)',
                                            resize: 'vertical',
                                            marginBottom: '16px'
                                        }}
                                    />
                                    <button type="submit" disabled={submitting} className="btn btn-primary">
                                        {submitting ? 'Отправка...' : 'Задать вопрос'}
                                    </button>
                                </form>
                            </div>
                        ) : (
                            <div style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '24px', marginBottom: '32px', textAlign: 'center', color: 'var(--text-primary)' }}>
                                <p>Чтобы задать вопрос, пожалуйста, <button onClick={() => navigate('/login')} style={{ color: 'var(--accent)', background: 'none', border: 'none', cursor: 'pointer' }}>войдите</button> в аккаунт</p>
                            </div>
                        )}

                        {questions.length === 0 ? (
                            <div style={{ textAlign: 'center', padding: '40px', background: 'var(--bg-tertiary)', borderRadius: '16px', color: 'var(--text-primary)' }}>
                                <span style={{ fontSize: '48px' }}>❓</span>
                                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>Пока нет вопросов. Задайте первый!</p>
                            </div>
                        ) : (
                            <>
                                {questions.map(question => (
                                    <div key={question.id} style={{ background: 'var(--bg-tertiary)', borderRadius: '16px', padding: '20px', marginBottom: '16px', color: 'var(--text-primary)' }}>
                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                                            <span style={{ fontWeight: 'bold', color: 'var(--text-primary)' }}>{question.userName}</span>
                                            <span style={{ color: 'var(--text-muted)', fontSize: '12px' }}>
                                                {new Date(question.createdAt).toLocaleDateString('ru-RU')}
                                            </span>
                                        </div>
                                        <p style={{ lineHeight: 1.6, color: 'var(--text-primary)' }}>{question.text}</p>
                                        {question.answer ? (
                                            <div style={{ marginTop: '16px', padding: '12px', background: 'var(--bg-card)', borderRadius: '12px' }}>
                                                <span style={{ color: 'var(--accent)', fontWeight: 'bold' }}>Ответ:</span>
                                                <p style={{ marginTop: '8px', color: 'var(--text-primary)' }}>{question.answer}</p>
                                            </div>
                                        ) : (
                                            <div style={{ marginTop: '16px', color: 'var(--warning)', fontSize: '14px' }}>
                                                ⏳ Ожидает ответа консультанта
                                            </div>
                                        )}
                                    </div>
                                ))}

                                {questionsTotalPages > 1 && (
                                    <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '24px' }}>
                                        <button
                                            onClick={() => setQuestionsPage(p => Math.max(0, p - 1))}
                                            disabled={questionsPage === 0}
                                            className="btn-outline btn-sm"
                                        >
                                            ←
                                        </button>
                                        <span style={{ padding: '8px 16px', background: 'var(--bg-tertiary)', borderRadius: '8px', color: 'var(--text-primary)' }}>
                                            {questionsPage + 1} / {questionsTotalPages}
                                        </span>
                                        <button
                                            onClick={() => setQuestionsPage(p => Math.min(questionsTotalPages - 1, p + 1))}
                                            disabled={questionsPage >= questionsTotalPages - 1}
                                            className="btn-outline btn-sm"
                                        >
                                            →
                                        </button>
                                    </div>
                                )}
                            </>
                        )}
                    </div>
                )}
            </div>

            {/* Рекомендации */}
            {relatedProducts.length > 0 && (
                <div style={{ marginTop: '60px' }}>
                    <h2 className="section-title">Похожие товары</h2>
                    <div className="product-grid">
                        {relatedProducts.slice(0, 4).map(product => (
                            <ProductCard key={product.id} product={product} />
                        ))}
                    </div>
                </div>
            )}

            {/* Часто покупают вместе */}
            {frequentlyBought.length > 0 && (
                <div style={{ marginTop: '40px' }}>
                    <h2 className="section-title">Часто покупают вместе</h2>
                    <div className="product-grid">
                        {frequentlyBought.slice(0, 4).map(product => (
                            <ProductCard key={product.id} product={product} />
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}