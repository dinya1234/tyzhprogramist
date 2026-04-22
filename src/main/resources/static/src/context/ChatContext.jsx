// src/context/ChatContext.jsx
import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import { chat, chatWS } from '../services/api';

const ChatContext = createContext();

export const useChat = () => useContext(ChatContext);

export const ChatProvider = ({ children }) => {
    const [activeSession, setActiveSession] = useState(null);
    const [messages, setMessages] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [pendingSessions, setPendingSessions] = useState([]);
    const [activeSessions, setActiveSessions] = useState([]);
    const [userRole, setUserRole] = useState(null);

    // Получаем роль пользователя
    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        setUserRole(user.role);
    }, []);

    // Создание новой сессии чата
    const createSession = async (contextType, contextId, sourceUrl) => {
        setIsLoading(true);
        try {
            const response = await chat.createSession(sourceUrl, contextType, contextId);
            const session = response.data;
            setActiveSession(session);

            // Подключаем WebSocket
            const token = localStorage.getItem('accessToken');
            if (token) {
                chatWS.connect(session.id, token);
            }

            // Загружаем историю сообщений
            const messagesRes = await chat.getMessages(session.id);
            setMessages(messagesRes.data || []);

            setIsOpen(true);
            return session;
        } catch (error) {
            console.error('Failed to create chat session:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };

    // Отправка сообщения
    const sendMessage = async (message) => {
        if (!activeSession) return;

        const tempMessage = {
            id: Date.now(),
            senderType: 'USER',
            message: message,
            timestamp: new Date().toISOString(),
            isTemp: true
        };

        setMessages(prev => [...prev, tempMessage]);

        try {
            const response = await chat.sendMessage(activeSession.id, message);
            // Заменяем временное сообщение на реальное
            setMessages(prev => prev.map(msg =>
                msg.id === tempMessage.id ? response.data : msg
            ));
        } catch (error) {
            console.error('Failed to send message:', error);
            setMessages(prev => prev.filter(msg => msg.id !== tempMessage.id));
            throw error;
        }
    };

    // Получение сообщений через WebSocket
    useEffect(() => {
        const handleNewMessage = (data) => {
            setMessages(prev => [...prev, data]);
        };

        chatWS.on('message', handleNewMessage);

        return () => {
            chatWS.off('message', handleNewMessage);
        };
    }, []);

    // Закрытие сессии
    const closeSession = async (sessionId) => {
        try {
            await chat.closeSession(sessionId);
            setActiveSession(null);
            setMessages([]);
            setIsOpen(false);
            chatWS.disconnect();
        } catch (error) {
            console.error('Failed to close session:', error);
        }
    };

    // Получение очереди запросов (для консультантов)
    const loadPendingSessions = useCallback(async () => {
        if (userRole !== 'ADMIN' && userRole !== 'MODERATOR') return;

        try {
            const response = await chat.getPendingSessions();
            setPendingSessions(response.data || []);
        } catch (error) {
            console.error('Failed to load pending sessions:', error);
        }
    }, [userRole]);

    // Принятие сессии консультантом
    const takeSession = async (sessionId) => {
        try {
            const response = await chat.takeSession(sessionId);
            setActiveSession(response.data);
            setPendingSessions(prev => prev.filter(s => s.id !== sessionId));

            // Подключаем WebSocket
            const token = localStorage.getItem('accessToken');
            if (token) {
                chatWS.connect(sessionId, token);
            }

            const messagesRes = await chat.getMessages(sessionId);
            setMessages(messagesRes.data || []);

            setIsOpen(true);
            return response.data;
        } catch (error) {
            console.error('Failed to take session:', error);
            throw error;
        }
    };

    // Отправка сообщения как консультант
    const sendConsultantMessage = async (sessionId, message) => {
        try {
            const response = await chat.sendConsultantMessage(sessionId, message);
            return response.data;
        } catch (error) {
            console.error('Failed to send consultant message:', error);
            throw error;
        }
    };

    // Загрузка активных сессий консультанта
    const loadActiveConsultantSessions = useCallback(async () => {
        if (userRole !== 'ADMIN' && userRole !== 'MODERATOR') return;

        try {
            const response = await chat.getActiveConsultantSessions();
            setActiveSessions(response.data || []);
        } catch (error) {
            console.error('Failed to load active sessions:', error);
        }
    }, [userRole]);

    // Периодическая загрузка очереди для консультантов
    useEffect(() => {
        if (userRole !== 'ADMIN' && userRole !== 'MODERATOR') return;

        loadPendingSessions();
        loadActiveConsultantSessions();

        const interval = setInterval(() => {
            loadPendingSessions();
            loadActiveConsultantSessions();
        }, 5000); // Каждые 5 секунд

        return () => clearInterval(interval);
    }, [userRole, loadPendingSessions, loadActiveConsultantSessions]);

    const value = {
        activeSession,
        messages,
        isOpen,
        isLoading,
        pendingSessions,
        activeSessions,
        userRole,
        createSession,
        sendMessage,
        closeSession,
        loadPendingSessions,
        takeSession,
        sendConsultantMessage,
        loadActiveConsultantSessions,
        setIsOpen
    };

    return (
        <ChatContext.Provider value={value}>
            {children}
        </ChatContext.Provider>
    );
};