// src/context/ChatContext.jsx
import React, { createContext, useState, useContext, useEffect, useCallback, useRef } from 'react';
import api from '../services/api';

const ChatContext = createContext();

export const useChat = () => useContext(ChatContext);

export const ChatProvider = ({ children, isModerator = false }) => {
    const [activeSession, setActiveSession] = useState(null);
    const [messages, setMessages] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [pendingSessions, setPendingSessions] = useState([]);
    const [activeSessions, setActiveSessions] = useState([]);
    const [userRole, setUserRole] = useState(null);

    const pollingInterval = useRef(null);
    const sessionsPollingInterval = useRef(null);

    // Получаем роль пользователя
    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        setUserRole(user.role);
    }, []);

    // Функция для загрузки сообщений сессии
    const loadSessionMessages = useCallback(async (sessionId) => {
        if (!sessionId) return;
        try {
            const response = await api.get(`/chat/session/${sessionId}/messages`);
            const newMessages = response.data || [];
            setMessages(prev => {
                // Сравниваем по длине или по последнему сообщению
                if (prev.length !== newMessages.length) {
                    console.log('Messages updated:', newMessages.length);
                    return newMessages;
                }
                return prev;
            });
            return newMessages;
        } catch (error) {
            console.error('Failed to load messages:', error);
            return [];
        }
    }, []);

    // Polling для получения новых сообщений в текущей сессии
    const startPolling = useCallback((sessionId) => {
        if (pollingInterval.current) {
            clearInterval(pollingInterval.current);
        }

        console.log('Starting polling for session:', sessionId);

        pollingInterval.current = setInterval(async () => {
            if (sessionId) {
                await loadSessionMessages(sessionId);
            }
        }, 2000); // Каждые 2 секунды
    }, [loadSessionMessages]);

    const stopPolling = useCallback(() => {
        if (pollingInterval.current) {
            clearInterval(pollingInterval.current);
            pollingInterval.current = null;
        }
    }, []);

    // Для модератора - загрузка ожидающих сессий
    const loadPendingSessions = useCallback(async () => {
        if (!isModerator) return;
        try {
            const response = await api.get('/chat/consultant/pending');
            const sessions = response.data || [];
            setPendingSessions(sessions);
            console.log('Pending sessions loaded:', sessions.length);
            return sessions;
        } catch (error) {
            console.error('Failed to load pending sessions:', error);
            return [];
        }
    }, [isModerator]);

    // Для модератора - загрузка активных сессий
    const loadActiveConsultantSessions = useCallback(async () => {
        if (!isModerator) return;
        try {
            const response = await api.get('/chat/consultant/active');
            const sessions = response.data || [];
            setActiveSessions(sessions);
            console.log('Active sessions loaded:', sessions.length);
            return sessions;
        } catch (error) {
            console.error('Failed to load active sessions:', error);
            return [];
        }
    }, [isModerator]);

    // Создание новой сессии (только для пользователей)
    const createSession = useCallback(async (contextType, contextId, sourceUrl) => {
        setIsLoading(true);
        try {
            const response = await api.post('/chat/session', null, {
                params: { sourceUrl, contextType, contextId }
            });
            const session = response.data;
            setActiveSession(session);

            await loadSessionMessages(session.id);
            startPolling(session.id);
            setIsOpen(true);

            console.log('Session created:', session);
            return session;
        } catch (error) {
            console.error('Failed to create chat session:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    }, [startPolling, loadSessionMessages]);

    const createNewSession = useCallback(async (sourceUrl) => {
        setActiveSession(null);
        setMessages([]);
        stopPolling();
        return createSession(null, null, sourceUrl || window.location.href);
    }, [createSession, stopPolling]);

    // Загрузка существующей сессии
    const loadSession = useCallback(async (sessionId) => {
        setIsLoading(true);
        try {
            const response = await api.get(`/chat/session/${sessionId}`);
            const session = response.data;
            setActiveSession(session);

            await loadSessionMessages(sessionId);
            startPolling(sessionId);
            setIsOpen(true);

            console.log('Session loaded:', session);
            return session;
        } catch (error) {
            console.error('Failed to load session:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    }, [startPolling, loadSessionMessages]);

    // Отправка сообщения (для пользователя)
    const sendMessage = useCallback(async (message) => {
        if (!activeSession || !message.trim()) return;

        console.log('Sending user message:', { sessionId: activeSession.id, message });

        try {
            await api.post('/chat/message', {
                sessionId: activeSession.id,
                message: message
            });

            // Сразу обновляем сообщения
            setTimeout(async () => {
                await loadSessionMessages(activeSession.id);
            }, 300);
        } catch (error) {
            console.error('Failed to send message:', error);
            if (error?.response?.status === 400) {
                try {
                    const refreshedSession = await api.get(`/chat/session/${activeSession.id}`);
                    const session = refreshedSession.data;
                    setActiveSession(session);
                    if (session?.status === 'CLOSED') {
                        localStorage.removeItem('chatSessionId');
                    }
                } catch (refreshError) {
                    console.error('Failed to refresh session state:', refreshError);
                }
            }
            throw error;
        }
    }, [activeSession, loadSessionMessages]);

    // Отправка сообщения как консультант (для модератора)
    const sendConsultantMessage = useCallback(async (sessionId, message) => {
        if (!message.trim()) return;

        console.log('Sending consultant message:', { sessionId, message });

        try {
            await api.post(`/chat/consultant/session/${sessionId}/message`, null, {
                params: { message }
            });

            // Если это активная сессия, обновляем сообщения
            if (activeSession?.id === sessionId) {
                setTimeout(async () => {
                    await loadSessionMessages(sessionId);
                }, 300);
            }
        } catch (error) {
            console.error('Failed to send consultant message:', error);
            alert('Не удалось отправить сообщение');
            throw error;
        }
    }, [activeSession?.id, loadSessionMessages]);

    // Принятие сессии консультантом
    const takeSession = useCallback(async (sessionId) => {
        if (!isModerator) return;

        console.log('Taking session:', sessionId);

        try {
            const response = await api.post(`/chat/consultant/session/${sessionId}/take`);
            const session = response.data;
            setActiveSession(session);

            // Удаляем из ожидающих
            setPendingSessions(prev => prev.filter(s => s.id !== sessionId));

            // Загружаем сообщения
            await loadSessionMessages(sessionId);
            startPolling(sessionId);
            setIsOpen(true);

            // Обновляем список активных сессий
            await loadActiveConsultantSessions();

            console.log('Session taken:', session);
            return session;
        } catch (error) {
            console.error('Failed to take session:', error);
            alert('Не удалось взять сессию');
            throw error;
        }
    }, [isModerator, startPolling, loadSessionMessages, loadActiveConsultantSessions]);

    // Закрытие сессии
    const closeSession = useCallback(async (sessionId) => {
        try {
            if (isModerator) {
                await api.post(`/chat/consultant/session/${sessionId}/close`);
                await loadActiveConsultantSessions();
            } else {
                await api.post(`/chat/me/session/${sessionId}/close`);
            }

            setActiveSession(null);
            setMessages([]);
            setIsOpen(false);
            stopPolling();

            console.log('Session closed:', sessionId);
        } catch (error) {
            console.error('Failed to close session:', error);
        }
    }, [isModerator, stopPolling, loadActiveConsultantSessions]);

    // Загрузка истории чатов пользователя
    const loadMySessions = useCallback(async () => {
        try {
            const response = await api.get('/chat/me/sessions');
            return response.data.content;
        } catch (error) {
            console.error('Failed to load my sessions:', error);
            return [];
        }
    }, []);

    // Для модератора - периодическая загрузка сессий (каждые 3 секунды)
    useEffect(() => {
        if (!isModerator) return;

        const loadAllSessions = async () => {
            await Promise.all([
                loadPendingSessions(),
                loadActiveConsultantSessions()
            ]);
        };

        // Загружаем сразу
        loadAllSessions();

        // И устанавливаем интервал
        sessionsPollingInterval.current = setInterval(loadAllSessions, 3000);

        return () => {
            if (sessionsPollingInterval.current) {
                clearInterval(sessionsPollingInterval.current);
            }
        };
    }, [isModerator, loadPendingSessions, loadActiveConsultantSessions]);

    // Для модератора - если есть активная сессия, обновляем её сообщения
    useEffect(() => {
        if (isModerator && activeSession?.id) {
            // Обновляем сообщения активной сессии каждые 2 секунды
            const interval = setInterval(async () => {
                await loadSessionMessages(activeSession.id);
            }, 2000);

            return () => clearInterval(interval);
        }
    }, [isModerator, activeSession?.id, loadSessionMessages]);

    // Очистка polling при размонтировании
    useEffect(() => {
        return () => {
            stopPolling();
        };
    }, [stopPolling]);

    const value = {
        activeSession,
        messages,
        isOpen,
        isLoading,
        pendingSessions,
        activeSessions,
        userRole,
        isConnected: false,
        typingUsers: {},
        isModerator,
        createSession,
        createNewSession,
        loadSession,
        sendMessage,
        sendConsultantMessage,
        closeSession,
        loadPendingSessions,
        takeSession,
        loadActiveConsultantSessions,
        loadMySessions,
        setIsOpen,
        loadSessionMessages,
        handleTyping: () => {}
    };

    return (
        <ChatContext.Provider value={value}>
            {children}
        </ChatContext.Provider>
    );
};