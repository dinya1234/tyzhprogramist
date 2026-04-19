import React, { createContext, useState, useContext, useEffect } from 'react';
import { auth } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        const savedUser = localStorage.getItem('user');
        if (token && savedUser) {
            setAccessToken(token);
            setUser(JSON.parse(savedUser));
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        try {
            const response = await auth.login({ username, password });
            console.log('Login response:', response.data);

            const { accessToken, refreshToken, user: userData } = response.data;

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('user', JSON.stringify(userData));

            setAccessToken(accessToken);
            setUser(userData);

            return { success: true, data: userData };
        } catch (error) {
            console.error('Login error:', error);
            return {
                success: false,
                error: error.response?.data?.message || 'Ошибка входа. Проверьте логин и пароль.'
            };
        }
    };

    const register = async (userData) => {
        try {
            console.log('Sending registration data:', userData);

            const response = await auth.register(userData);
            console.log('Register response:', response.data);

            const { accessToken, refreshToken, user: registeredUser } = response.data;

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('user', JSON.stringify(registeredUser));

            setAccessToken(accessToken);
            setUser(registeredUser);

            return { success: true, data: registeredUser };
        } catch (error) {
            console.error('Register error:', error);
            let errorMessage = 'Ошибка регистрации';

            if (error.response?.data?.message) {
                errorMessage = error.response.data.message;
            } else if (error.response?.data?.errors) {
                errorMessage = Object.values(error.response.data.errors).join(', ');
            }

            return {
                success: false,
                error: errorMessage
            };
        }
    };

    const logout = async () => {
        try {
            await auth.logout();
        } catch (e) {
            console.error('Logout error', e);
        }
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        setAccessToken(null);
        setUser(null);
    };

    const value = {
        user,
        loading,
        accessToken,
        login,
        register,
        logout,
        isAuthenticated: !!user
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};