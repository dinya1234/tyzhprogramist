import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth, AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import Header from './components/Header';
import Footer from './components/Footer';
import './index.css';

// Временные заглушки для страниц
function HomePage() { return <div className="container"><h1>Главная страница</h1></div>; }
import CatalogPage from './pages/CatalogPage';
function ConfiguratorPage() { return <div className="container"><h1>Конфигуратор ПК</h1></div>; }
function ServicePage() { return <div className="container"><h1>Сервисный центр</h1></div>; }
function ProfilePage() { return <div className="container"><h1>Личный кабинет</h1></div>; }
function CartPage() { return <div className="container"><h1>Корзина</h1></div>; }

function PrivateRoute({ children }) {
    const { isAuthenticated, loading } = useAuth();
    if (loading) return <div style={{ textAlign: 'center', padding: '50px' }}>Загрузка...</div>;
    return isAuthenticated ? children : <Navigate to="/login" replace />;
}

function AppContent() {
    return (
        <BrowserRouter>
            <Header />
            <main>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/" element={<PrivateRoute><HomePage /></PrivateRoute>} />
                    <Route path="/catalog" element={<PrivateRoute><CatalogPage /></PrivateRoute>} />
                    <Route path="/configurator" element={<PrivateRoute><ConfiguratorPage /></PrivateRoute>} />
                    <Route path="/service" element={<PrivateRoute><ServicePage /></PrivateRoute>} />
                    <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
                    <Route path="/cart" element={<PrivateRoute><CartPage /></PrivateRoute>} />
                </Routes>
            </main>
            <Footer />
        </BrowserRouter>
    );
}

function App() {
    return (
        <AuthProvider>
            <AppContent />
        </AuthProvider>
    );
}

export default App;