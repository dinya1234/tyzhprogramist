// src/components/HorizontalBannerScroller.jsx (исправленная версия - плавный круговой эффект)
import React, { useRef, useState, useEffect, useCallback } from 'react';
import AdvertisingBanner from './AdvertisingBanner';

export default function HorizontalBannerScroller({ banners, isAdmin }) {
    const scrollContainerRef = useRef(null);
    const [isDragging, setIsDragging] = useState(false);
    const [startX, setStartX] = useState(0);
    const [scrollLeft, setScrollLeft] = useState(0);
    const [showLeftArrow, setShowLeftArrow] = useState(false);
    const [showRightArrow, setShowRightArrow] = useState(false);
    const [direction, setDirection] = useState(1); // 1 = вправо, -1 = влево
    const autoScrollInterval = useRef(null);
    const isAutoScrollingRef = useRef(true);
    const scrollTimeoutRef = useRef(null);

    // Сортировка баннеров
    const sortedBanners = [...banners].sort((a, b) => (a.displayOrder || 0) - (b.displayOrder || 0));

    // Проверка необходимости стрелок
    const checkArrows = useCallback(() => {
        if (!scrollContainerRef.current) return;
        const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;
        setShowLeftArrow(scrollLeft > 10);
        setShowRightArrow(scrollLeft + clientWidth < scrollWidth - 10);
    }, []);

    // Проверка, нужно ли автоскролл
    const shouldAutoScroll = useCallback(() => {
        if (!scrollContainerRef.current) return false;
        const { scrollWidth, clientWidth } = scrollContainerRef.current;
        return scrollWidth > clientWidth;
    }, []);

    // Функция для плавного изменения направления (эффект "по кругу")
    const changeDirection = useCallback(() => {
        if (!scrollContainerRef.current) return;

        const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;

        // Если дошли до конца - меняем направление на движение влево
        if (scrollLeft + clientWidth >= scrollWidth - 5) {
            setDirection(-1);
        }
        // Если дошли до начала - меняем направление на движение вправо
        else if (scrollLeft <= 5) {
            setDirection(1);
        }
    }, []);

    // Функция автоскролла с круговым эффектом
    const startAutoScroll = useCallback(() => {
        if (autoScrollInterval.current) {
            clearInterval(autoScrollInterval.current);
            autoScrollInterval.current = null;
        }

        if (!isAutoScrollingRef.current) return;
        if (!scrollContainerRef.current) return;
        if (!shouldAutoScroll()) return;

        autoScrollInterval.current = setInterval(() => {
            if (!scrollContainerRef.current) return;
            if (!isAutoScrollingRef.current) return;
            if (isDragging) return;

            const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;

            // Проверяем нужно ли сменить направление
            if (direction === 1 && scrollLeft + clientWidth >= scrollWidth - 5) {
                setDirection(-1);
            } else if (direction === -1 && scrollLeft <= 5) {
                setDirection(1);
            }

            // Двигаемся в текущем направлении
            const newPosition = scrollContainerRef.current.scrollLeft + (direction * 1.5);
            scrollContainerRef.current.scrollLeft = newPosition;

            checkArrows();
        }, 30);
    }, [direction, shouldAutoScroll, checkArrows, isDragging]);

    // Остановка автоскролла
    const stopAutoScroll = useCallback(() => {
        isAutoScrollingRef.current = false;
        if (autoScrollInterval.current) {
            clearInterval(autoScrollInterval.current);
            autoScrollInterval.current = null;
        }
    }, []);

    // Возобновление автоскролла после задержки
    const resumeAutoScrollAfterDelay = useCallback(() => {
        if (scrollTimeoutRef.current) {
            clearTimeout(scrollTimeoutRef.current);
        }
        scrollTimeoutRef.current = setTimeout(() => {
            isAutoScrollingRef.current = true;
            startAutoScroll();
        }, 5000);
    }, [startAutoScroll]);

    // Запуск/остановка автоскролла
    useEffect(() => {
        if (!sortedBanners.length) return;

        const timer = setTimeout(() => {
            isAutoScrollingRef.current = true;
            startAutoScroll();
        }, 100);

        return () => {
            clearTimeout(timer);
            if (autoScrollInterval.current) {
                clearInterval(autoScrollInterval.current);
            }
            if (scrollTimeoutRef.current) {
                clearTimeout(scrollTimeoutRef.current);
            }
        };
    }, [sortedBanners.length, startAutoScroll]);

    // Обновление стрелок и направления при изменении размера
    useEffect(() => {
        if (!sortedBanners.length) return;

        checkArrows();
        window.addEventListener('resize', checkArrows);

        const observer = new ResizeObserver(() => {
            checkArrows();
        });

        if (scrollContainerRef.current) {
            observer.observe(scrollContainerRef.current);
        }

        return () => {
            window.removeEventListener('resize', checkArrows);
            observer.disconnect();
        };
    }, [checkArrows, sortedBanners.length]);

    // Drag для ручного скролла
    const handleMouseDown = (e) => {
        if (!scrollContainerRef.current) return;
        stopAutoScroll();
        setIsDragging(true);
        setStartX(e.pageX - scrollContainerRef.current.offsetLeft);
        setScrollLeft(scrollContainerRef.current.scrollLeft);
        scrollContainerRef.current.style.cursor = 'grabbing';
        scrollContainerRef.current.style.userSelect = 'none';
    };

    const handleMouseMove = (e) => {
        if (!isDragging || !scrollContainerRef.current) return;
        e.preventDefault();
        const x = e.pageX - scrollContainerRef.current.offsetLeft;
        const walk = (x - startX) * 1.5;
        scrollContainerRef.current.scrollLeft = scrollLeft - walk;
        checkArrows();
        changeDirection();
    };

    const handleMouseUp = () => {
        setIsDragging(false);
        if (scrollContainerRef.current) {
            scrollContainerRef.current.style.cursor = 'grab';
            scrollContainerRef.current.style.userSelect = 'auto';
        }
        resumeAutoScrollAfterDelay();
    };

    const handleMouseLeave = () => {
        if (isDragging) {
            setIsDragging(false);
            if (scrollContainerRef.current) {
                scrollContainerRef.current.style.cursor = 'grab';
                scrollContainerRef.current.style.userSelect = 'auto';
            }
            resumeAutoScrollAfterDelay();
        }
    };

    // Ручной скролл стрелками
    const scroll = (scrollDirection) => {
        if (!scrollContainerRef.current) return;
        stopAutoScroll();
        const scrollAmount = scrollDirection === 'left' ? -400 : 400;
        scrollContainerRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        setTimeout(() => {
            checkArrows();
            changeDirection();
        }, 300);
        resumeAutoScrollAfterDelay();
    };

    // Если нет баннеров - не показываем
    if (!sortedBanners.length) {
        return null;
    }

    const needsScroll = shouldAutoScroll();

    return (
        <div style={{ position: 'relative', margin: '40px 0' }}>
            <h2 style={{ marginBottom: '16px' }}>📢 Реклама и предложения</h2>

            {/* Стрелка влево */}
            {showLeftArrow && (
                <button
                    onClick={() => scroll('left')}
                    style={{
                        position: 'absolute',
                        left: '-20px',
                        top: '50%',
                        transform: 'translateY(-50%)',
                        width: '40px',
                        height: '40px',
                        borderRadius: '50%',
                        background: 'var(--bg-card)',
                        border: '1px solid var(--border)',
                        color: 'var(--text-primary)',
                        cursor: 'pointer',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '20px',
                        zIndex: 10,
                        transition: 'all 0.2s'
                    }}
                    onMouseEnter={(e) => {
                        e.currentTarget.style.background = 'var(--accent)';
                        e.currentTarget.style.color = 'white';
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.background = 'var(--bg-card)';
                        e.currentTarget.style.color = 'var(--text-primary)';
                    }}
                >
                    ←
                </button>
            )}

            {/* Контейнер с баннерами */}
            <div
                ref={scrollContainerRef}
                style={{
                    display: 'flex',
                    overflowX: 'auto',
                    overflowY: 'hidden',
                    scrollBehavior: 'auto',
                    cursor: needsScroll ? 'grab' : 'default',
                    padding: '8px 4px',
                    scrollbarWidth: 'thin'
                }}
                onMouseDown={needsScroll ? handleMouseDown : undefined}
                onMouseMove={needsScroll ? handleMouseMove : undefined}
                onMouseUp={needsScroll ? handleMouseUp : undefined}
                onMouseLeave={needsScroll ? handleMouseLeave : undefined}
            >
                {sortedBanners.map((banner) => (
                    <AdvertisingBanner
                        key={banner.id}
                        banner={banner}
                        isAdmin={isAdmin}
                    />
                ))}
            </div>

            {/* Стрелка вправо */}
            {showRightArrow && (
                <button
                    onClick={() => scroll('right')}
                    style={{
                        position: 'absolute',
                        right: '-20px',
                        top: '50%',
                        transform: 'translateY(-50%)',
                        width: '40px',
                        height: '40px',
                        borderRadius: '50%',
                        background: 'var(--bg-card)',
                        border: '1px solid var(--border)',
                        color: 'var(--text-primary)',
                        cursor: 'pointer',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '20px',
                        zIndex: 10,
                        transition: 'all 0.2s'
                    }}
                    onMouseEnter={(e) => {
                        e.currentTarget.style.background = 'var(--accent)';
                        e.currentTarget.style.color = 'white';
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.background = 'var(--bg-card)';
                        e.currentTarget.style.color = 'var(--text-primary)';
                    }}
                >
                    →
                </button>
            )}

            {/* Подсказка */}
            {needsScroll && (
                <div style={{
                    textAlign: 'center',
                    marginTop: '12px',
                    fontSize: '12px',
                    color: 'var(--text-muted)'
                }}>
                    💡 Перетащите мышью или используйте стрелки для просмотра
                </div>
            )}
        </div>
    );
}