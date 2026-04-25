// src/components/Header.jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { useChat } from '../context/ChatContext';

export default function Header() {
    const { user, logout } = useAuth();
    const { cart } = useCart();
    const { setIsOpen } = useChat();
    const navigate = useNavigate();
    const isStaff = user?.role === 'ADMIN' || user?.role === 'MODERATOR';

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleOpenSupportChat = () => {
        if (!user) {
            navigate('/login');
            return;
        }
        setIsOpen(true);
    };

    return (
        <header className="header">
            <div className="container">
                <div className="navbar">
                    <Link to="/" className="logo">Тыжпрограммист</Link>
                    <ul className="nav-links">
                        <li><Link to="/catalog">Каталог</Link></li>
                        <li><Link to="/configurator">Собрать ПК</Link></li>
                        <li><Link to="/service">Сервис</Link></li>
                        {user && <li><Link to="/profile">Профиль</Link></li>}
                        {(user?.role === 'ADMIN' || user?.role === 'MODERATOR') && (
                            <li><Link to="/consultant">Чат (консультант)</Link></li>
                        )}
                        {user?.role === 'ADMIN' && (
                            <li><Link to="/admin">Админ-панель</Link></li>
                        )}
                    </ul>
                    <div className="header-actions">
                        {!isStaff && (
                            <button
                                className="icon-btn"
                                id="consultantTrigger"
                                onClick={handleOpenSupportChat}
                                title="Чат поддержки"
                            >
                                💬
                            </button>
                        )}
                        <Link to="/cart" className="icon-btn" style={{ position: 'relative' }}>
                            🛒
                            {cart.totalItems > 0 && (
                                <span className="cart-count">{cart.totalItems}</span>
                            )}
                        </Link>
                        {user ? (
                            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                <span style={{ color: '#c084fc' }}>{user.username}</span>
                                <button onClick={handleLogout} className="btn-outline btn-sm">Выйти</button>
                            </div>
                        ) : (
                            <Link to="/login" className="btn-outline btn-sm">Войти</Link>
                        )}
                    </div>
                </div>
            </div>
        </header>
    );
}