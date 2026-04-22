// src/components/Header.jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function Header() {
    const { user, logout } = useAuth();
    const { cart } = useCart();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
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
                    </ul>
                    <div className="header-actions">
                        <button className="icon-btn" id="consultantTrigger">💬</button>
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