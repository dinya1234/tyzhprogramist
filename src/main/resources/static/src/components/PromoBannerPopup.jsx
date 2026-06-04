import React, { useState, useEffect, useCallback } from 'react';
import { Link, useLocation } from 'react-router-dom';

const PROMO_BANNERS = [
    {
        id: 'black-friday',
        image: '/black-friday-banner.jpg',
        alt: 'Black Friday — скидки на технику',
        link: '/catalog',
    },
    {
        id: 'new-year',
        image: '/new-year-sale.jpg',
        alt: 'Новогодняя распродажа',
        link: '/catalog',
    },
    {
        id: 'gaming-pc',
        image: '/gaming-pc.jpg',
        alt: 'Игровые ПК — собери свой',
        link: '/configurator',
    },
];

const HIDDEN_ROUTES = ['/login', '/admin', '/consultant'];

function isHiddenRoute(pathname) {
    return HIDDEN_ROUTES.some((path) => pathname.startsWith(path));
}

export default function PromoBannerPopup() {
    const location = useLocation();
    const [visible, setVisible] = useState(false);
    const [queue, setQueue] = useState([]);
    const [initialTotal, setInitialTotal] = useState(0);

    const banner = queue[0];
    const totalInQueue = queue.length;
    const currentPosition = initialTotal > 0 ? initialTotal - totalInQueue + 1 : 1;

    const dismissCurrent = useCallback(() => {
        if (!banner) return;
        setQueue((prev) => {
            const next = prev.slice(1);
            if (next.length === 0) setVisible(false);
            return next;
        });
    }, [banner]);

    useEffect(() => {
        if (isHiddenRoute(location.pathname)) {
            return;
        }

        setQueue([...PROMO_BANNERS]);
        setInitialTotal(PROMO_BANNERS.length);
        setVisible(false);

        const timer = window.setTimeout(() => setVisible(true), 800);
        return () => window.clearTimeout(timer);
        // Показываем при каждой полной перезагрузке страницы, не при SPA-навигации
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (!visible) return;
        const onKeyDown = (e) => {
            if (e.key === 'Escape') dismissCurrent();
        };
        window.addEventListener('keydown', onKeyDown);
        return () => window.removeEventListener('keydown', onKeyDown);
    }, [visible, dismissCurrent]);

    if (!visible || !banner) return null;

    return (
        <div
            className="promo-popup-overlay"
            onClick={dismissCurrent}
            role="dialog"
            aria-modal="true"
            aria-label="Рекламное предложение"
        >
            <div className="promo-popup" onClick={(e) => e.stopPropagation()}>
                <button
                    type="button"
                    className="promo-popup-close"
                    onClick={dismissCurrent}
                    aria-label="Закрыть"
                >
                    ×
                </button>

                <Link to={banner.link} onClick={dismissCurrent} className="promo-popup-image-link">
                    <img
                        src={banner.image}
                        alt={banner.alt}
                        className="promo-popup-image"
                    />
                </Link>

                {initialTotal > 1 && (
                    <p className="promo-popup-counter">
                        {currentPosition} из {initialTotal}
                    </p>
                )}

                <div className="promo-popup-actions">
                    <Link to={banner.link} className="btn btn-primary btn-sm" onClick={dismissCurrent}>
                        Подробнее
                    </Link>
                    <button type="button" className="btn btn-outline btn-sm" onClick={dismissCurrent}>
                        Закрыть
                    </button>
                </div>
            </div>
        </div>
    );
}
