// src/context/CartContext.jsx
import React, { createContext, useState, useContext, useEffect } from 'react';
import { cart as cartApi } from '../services/api';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }) => {
    const [cart, setCart] = useState({ items: [], totalPrice: 0, totalItems: 0 });
    const [loading, setLoading] = useState(true);

    const loadCart = async () => {
        try {
            const response = await cartApi.get();
            setCart(response.data);
        } catch (error) {
            console.error('Ошибка загрузки корзины:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadCart();
    }, []);

    const addToCart = async (productId, quantity = 1) => {
        try {
            await cartApi.add(productId, quantity);
            await loadCart();
            return { success: true };
        } catch (error) {
            console.error('Ошибка добавления:', error);
            return { success: false, error: error.response?.data?.message || 'Ошибка добавления' };
        }
    };

    const updateQuantity = async (productId, quantity) => {
        try {
            await cartApi.update(productId, quantity);
            await loadCart();
            return { success: true };
        } catch (error) {
            console.error('Ошибка обновления:', error);
            return { success: false };
        }
    };

    const removeFromCart = async (productId) => {
        try {
            await cartApi.remove(productId);
            await loadCart();
            return { success: true };
        } catch (error) {
            console.error('Ошибка удаления:', error);
            return { success: false };
        }
    };

    const clearCart = async () => {
        try {
            await cartApi.clear();
            await loadCart();
            return { success: true };
        } catch (error) {
            console.error('Ошибка очистки:', error);
            return { success: false };
        }
    };

    return (
        <CartContext.Provider value={{
            cart,
            loading,
            addToCart,
            updateQuantity,
            removeFromCart,
            clearCart,
            refreshCart: loadCart
        }}>
            {children}
        </CartContext.Provider>
    );
};