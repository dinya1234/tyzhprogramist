import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
    const navigate = useNavigate();
    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login, register } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        let result;
        if (isLogin) {
            // Вход: используем username (логин) и пароль
            result = await login(username, password);
        } else {
            // Регистрация: email, password, username
            result = await register({
                email: email,
                password: password,
                username: username
            });
        }

        setLoading(false);

        if (result.success) {
            console.log('Успех, редирект на главную');
            navigate('/');
        } else {
            setError(result.error);
        }
    };

    return (
        <div style={{
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            background: '#0a0c10'
        }}>
            <div style={{
                background: '#15181f',
                padding: '40px',
                borderRadius: '24px',
                width: '100%',
                maxWidth: '450px',
                border: '1px solid #2a2d36'
            }}>
                <h1 style={{ color: '#c084fc', marginBottom: '30px', textAlign: 'center' }}>
                    {isLogin ? 'Вход' : 'Регистрация'}
                </h1>

                {error && (
                    <div style={{
                        background: '#7f1a1a',
                        color: '#fecaca',
                        padding: '12px',
                        borderRadius: '8px',
                        marginBottom: '20px',
                        textAlign: 'center'
                    }}>
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    {/* Поле Логин — для входа И для регистрации */}
                    <div style={{ marginBottom: '20px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: '#9ca3af' }}>
                            {isLogin ? 'Логин *' : 'Имя пользователя (логин) *'}
                        </label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            placeholder="Введите логин"
                            style={{
                                width: '100%',
                                padding: '12px',
                                background: '#0a0c10',
                                border: '1px solid #3f434e',
                                borderRadius: '8px',
                                color: 'white'
                            }}
                        />
                    </div>

                    {/* Поле Email — ТОЛЬКО при регистрации */}
                    {!isLogin && (
                        <div style={{ marginBottom: '20px' }}>
                            <label style={{ display: 'block', marginBottom: '8px', color: '#9ca3af' }}>
                                Email *
                            </label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                placeholder="your@email.com"
                                style={{
                                    width: '100%',
                                    padding: '12px',
                                    background: '#0a0c10',
                                    border: '1px solid #3f434e',
                                    borderRadius: '8px',
                                    color: 'white'
                                }}
                            />
                        </div>
                    )}

                    {/* Поле Пароль — для входа и регистрации */}
                    <div style={{ marginBottom: '25px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: '#9ca3af' }}>
                            Пароль *
                        </label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            placeholder="••••••••"
                            style={{
                                width: '100%',
                                padding: '12px',
                                background: '#0a0c10',
                                border: '1px solid #3f434e',
                                borderRadius: '8px',
                                color: 'white'
                            }}
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            width: '100%',
                            padding: '14px',
                            background: 'linear-gradient(135deg, #c084fc, #60a5fa)',
                            border: 'none',
                            borderRadius: '12px',
                            color: '#111317',
                            fontWeight: 'bold',
                            fontSize: '16px',
                            cursor: loading ? 'not-allowed' : 'pointer',
                            opacity: loading ? 0.7 : 1
                        }}
                    >
                        {loading ? 'Загрузка...' : (isLogin ? 'Войти' : 'Зарегистрироваться')}
                    </button>
                </form>

                <button
                    onClick={() => {
                        setIsLogin(!isLogin);
                        setError('');
                        setEmail(''); // Очищаем email при переключении
                    }}
                    style={{
                        width: '100%',
                        marginTop: '20px',
                        background: 'transparent',
                        border: 'none',
                        color: '#c084fc',
                        cursor: 'pointer',
                        fontSize: '14px'
                    }}
                >
                    {isLogin ? 'Нет аккаунта? Зарегистрироваться' : 'Уже есть аккаунт? Войти'}
                </button>
            </div>
        </div>
    );
}