// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth, AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import { ChatProvider } from './context/ChatContext';
import LoginPage from './pages/LoginPage';
import Header from './components/Header';
import Footer from './components/Footer';
import ChatWidget from './components/Chat/ChatWidget';
import './index.css';

// Страницы
import HomePage from './pages/HomePage';
import CatalogPage from './pages/CatalogPage';
import ProductPage from './pages/ProductPage';
import ConfiguratorPage from './pages/ConfiguratorPage';
import ServicePage from './pages/ServicePage';
import ProfilePage from './pages/ProfilePage';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import ConsultantPanel from './pages/admin/ConsultantPanel';
import ChatHistory from './pages/ChatHistory';
import OrderSuccessPage from './pages/OrderSuccessPage';
import AdminPanel from './pages/admin/AdminPanel';
import ComparisonPage from './pages/ComparisonPage';

// Компонент для защищённых маршрутов с проверкой ролей
function PrivateRoute({ children, requiredRoles = [] }) {
    const { isAuthenticated, loading, user } = useAuth();

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '50px' }}>
                <h2>Загрузка...</h2>
            </div>
        );
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    // Проверка ролей для админ-панели
    if (requiredRoles.length > 0 && user) {
        const userRole = user.role;
        if (!requiredRoles.includes(userRole)) {
            return <Navigate to="/" replace />;
        }
    }

    return children;
}

function AppContent() {
    const { user } = useAuth();
    const isModerator = user?.role === 'MODERATOR' || user?.role === 'ADMIN';

    return (
        <BrowserRouter>
            <Header />
            <main style={{ minHeight: 'calc(100vh - 140px)' }}>
                <Routes>
                    {/* Публичные маршруты (доступны всем) */}
                    <Route path="/" element={<HomePage />} />
                    <Route path="/catalog" element={<CatalogPage />} />
                    <Route path="/product/:id" element={<ProductPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/cart" element={<CartPage />} />

                    {/* Приватные маршруты (только для авторизованных) */}
                    <Route path="/configurator" element={
                        <PrivateRoute>
                            <ConfiguratorPage />
                        </PrivateRoute>
                    } />
                    <Route path="/service" element={
                        <PrivateRoute>
                            <ServicePage />
                        </PrivateRoute>
                    } />
                    <Route path="/profile" element={
                        <PrivateRoute>
                            <ProfilePage />
                        </PrivateRoute>
                    } />
                    <Route path="/checkout" element={
                        <PrivateRoute>
                            <CheckoutPage />
                        </PrivateRoute>
                    } />
                    <Route path="/order-success" element={
                        <PrivateRoute>
                            <OrderSuccessPage />
                        </PrivateRoute>
                    } />
                    <Route path="/chat-history" element={
                        <PrivateRoute>
                            <ChatHistory />
                        </PrivateRoute>
                    } />
                    <Route path="/comparison" element={
                        <PrivateRoute>
                            <ComparisonPage />
                        </PrivateRoute>
                    } />

                    {/* Админ/Модератор маршруты */}
                    <Route path="/consultant" element={
                        <PrivateRoute requiredRoles={['ADMIN', 'MODERATOR']}>
                            <ConsultantPanel />
                        </PrivateRoute>
                    } />

                    <Route path="/admin" element={
                        <PrivateRoute requiredRoles={['ADMIN']}>
                            <AdminPanel />
                        </PrivateRoute>
                    } />
                </Routes>
            </main>
            <Footer />
            <ChatWidget />
        </BrowserRouter>
    );
}

function App() {
    return (
        <AuthProvider>
            <CartProvider>
                <ChatProvider>
                    <AppContent />
                </ChatProvider>
            </CartProvider>
        </AuthProvider>
    );
}

export default App;