// src/pages/ChatHistory.jsx
import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { chat } from '../services/api';
import { useChat } from '../context/ChatContext';

export default function ChatHistory() {
    const { user } = useAuth();
    const { createSession, setIsOpen } = useChat();

    // Состояния
    const [sessions, setSessions] = useState([]);
    const [selectedSession, setSelectedSession] = useState(null);
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loadingMessages, setLoadingMessages] = useState(false);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [filter, setFilter] = useState('all'); // all, active, closed

    const messagesEndRef = useRef(null);

    // Загрузка списка сессий
    useEffect(() => {
        if (user) {
            loadSessions();
        } else {
            setLoading(false);
        }
    }, [user, page, filter]);

    // Автоскролл к последнему сообщению
    useEffect(() => {
        if (messages.length > 0) {
            messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    const loadSessions = async () => {
        setLoading(true);
        try {
            let endpoint;
            if (user?.role === 'ADMIN' || user?.role === 'MODERATOR') {
                // Для консультантов - все сессии или только свои
                endpoint = filter === 'active' ? chat.getActiveConsultantSessions : chat.getConsultantSessions;
            } else {
                // Для обычных пользователей - только свои
                endpoint = chat.getMySessions;
            }

            const response = await endpoint({ page, size: 20 });
            setSessions(response.data.content || []);
            setTotalPages(response.data.totalPages || 0);

            // Автоматически выбираем первую сессию
            if (response.data.content && response.data.content.length > 0 && !selectedSession) {
                setSelectedSession(response.data.content[0]);
                loadMessages(response.data.content[0].id);
            }
        } catch (error) {
            console.error('Ошибка загрузки сессий:', error);
        } finally {
            setLoading(false);
        }
    };

    const loadMessages = async (sessionId) => {
        setLoadingMessages(true);
        try {
            const response = await chat.getMessages(sessionId);
            setMessages(response.data || []);
        } catch (error) {
            console.error('Ошибка загрузки сообщений:', error);
        } finally {
            setLoadingMessages(false);
        }
    };

    const handleSessionSelect = (session) => {
        setSelectedSession(session);
        loadMessages(session.id);
    };

    const handleNewChat = async () => {
        await createSession(null, null, window.location.href);
        setIsOpen(true);
        // Через секунду обновляем список сессий
        setTimeout(() => loadSessions(), 1000);
    };

    const formatDate = (dateStr) => {
        const date = new Date(dateStr);
        const now = new Date();
        const diffDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));

        if (diffDays === 0) {
            return date.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' });
        } else if (diffDays === 1) {
            return 'Вчера';
        } else if (diffDays < 7) {
            return `${diffDays} дня назад`;
        } else {
            return date.toLocaleDateString('ru-RU');
        }
    };

    const formatFullDate = (dateStr) => {
        return new Date(dateStr).toLocaleString('ru-RU', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getSessionStatusBadge = (status) => {
        switch (status) {
            case 'ACTIVE':
                return { text: '🟢 Активен', color: '#22c55e' };
            case 'PENDING':
                return { text: '🟡 Ожидает', color: '#f59e0b' };
            case 'CLOSED':
                return { text: '⚫ Закрыт', color: '#6b7280' };
            default:
                return { text: status, color: '#9ca3af' };
        }
    };

    const getSenderName = (senderType) => {
        switch (senderType) {
            case 'USER': return user?.username || 'Вы';
            case 'CONSULTANT': return 'Консультант';
            case 'SYSTEM': return 'Система';
            default: return '';
        }
    };

    const getSenderAvatar = (senderType) => {
        switch (senderType) {
            case 'USER': return '👤';
            case 'CONSULTANT': return '🎧';
            case 'SYSTEM': return '🤖';
            default: return '💬';
        }
    };

    const exportChat = () => {
        if (!selectedSession || messages.length === 0) return;

        let content = `=== Чат #${selectedSession.id} ===\n`;
        content += `Дата: ${formatFullDate(selectedSession.startedAt)}\n`;
        content += `Статус: ${selectedSession.status}\n`;
        if (selectedSession.userName) content += `Пользователь: ${selectedSession.userName}\n`;
        if (selectedSession.consultantName) content += `Консультант: ${selectedSession.consultantName}\n`;
        content += `\n${'='.repeat(50)}\n\n`;

        messages.forEach(msg => {
            const time = new Date(msg.timestamp).toLocaleTimeString('ru-RU');
            const sender = getSenderName(msg.senderType);
            content += `[${time}] ${sender}: ${msg.message}\n`;
        });

        const blob = new Blob([content], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `chat_${selectedSession.id}.txt`;
        a.click();
        URL.revokeObjectURL(url);
    };

    if (!user) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>💬 История чатов</h2>
                <p style={{ color: '#9ca3af', marginBottom: '30px' }}>
                    Для просмотра истории чатов необходимо войти в аккаунт
                </p>
                <button onClick={() => window.location.href = '/login'} className="btn-primary btn-lg">
                    Войти
                </button>
            </div>
        );
    }

    if (loading) {
        return (
            <div className="container" style={{ textAlign: 'center', padding: '60px' }}>
                <h2>Загрузка истории...</h2>
            </div>
        );
    }

    return (
        <div className="chat-history-container">
            <div className="chat-history-header">
                <h1>💬 История чатов</h1>
                <button className="btn-primary" onClick={handleNewChat}>
                    ➕ Новый чат
                </button>
            </div>

            <div className="chat-history-layout">
                {/* Боковая панель со списком сессий */}
                <div className="chat-history-sidebar">
                    {/* Фильтры */}
                    <div className="chat-history-filters">
                        <button
                            className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
                            onClick={() => setFilter('all')}
                        >
                            Все
                        </button>
                        <button
                            className={`filter-btn ${filter === 'active' ? 'active' : ''}`}
                            onClick={() => setFilter('active')}
                        >
                            Активные
                        </button>
                        <button
                            className={`filter-btn ${filter === 'closed' ? 'active' : ''}`}
                            onClick={() => setFilter('closed')}
                        >
                            Закрытые
                        </button>
                    </div>

                    {/* Список сессий */}
                    <div className="chat-history-sessions">
                        {sessions.length === 0 ? (
                            <div className="chat-history-empty">
                                <span>💬</span>
                                <p>У вас пока нет чатов</p>
                                <button className="btn-outline btn-sm" onClick={handleNewChat}>
                                    Начать чат
                                </button>
                            </div>
                        ) : (
                            sessions.map(session => {
                                const status = getSessionStatusBadge(session.status);
                                const lastMessage = session.lastMessage || '';
                                return (
                                    <div
                                        key={session.id}
                                        className={`chat-history-session ${selectedSession?.id === session.id ? 'active' : ''}`}
                                        onClick={() => handleSessionSelect(session)}
                                    >
                                        <div className="chat-history-session-header">
                                            <div className="chat-history-session-user">
                                                {session.userName || 'Гость'}
                                            </div>
                                            <div className="chat-history-session-status" style={{ background: status.color }}>
                                                {status.text}
                                            </div>
                                        </div>
                                        <div className="chat-history-session-date">
                                            {formatDate(session.startedAt)}
                                        </div>
                                        {session.consultantName && (
                                            <div className="chat-history-session-consultant">
                                                🎧 {session.consultantName}
                                            </div>
                                        )}
                                    </div>
                                );
                            })
                        )}
                    </div>

                    {/* Пагинация */}
                    {totalPages > 1 && (
                        <div className="chat-history-pagination">
                            <button
                                onClick={() => setPage(p => Math.max(0, p - 1))}
                                disabled={page === 0}
                                className="btn-outline btn-sm"
                            >
                                ←
                            </button>
                            <span>{page + 1} / {totalPages}</span>
                            <button
                                onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                                disabled={page >= totalPages - 1}
                                className="btn-outline btn-sm"
                            >
                                →
                            </button>
                        </div>
                    )}
                </div>

                {/* Основная область с сообщениями */}
                <div className="chat-history-messages">
                    {selectedSession ? (
                        <>
                            <div className="chat-history-messages-header">
                                <div>
                                    <h3>Чат #{selectedSession.id}</h3>
                                    <div className="chat-history-messages-meta">
                                        <span>📅 {formatFullDate(selectedSession.startedAt)}</span>
                                        {selectedSession.consultantName && (
                                            <span>🎧 Консультант: {selectedSession.consultantName}</span>
                                        )}
                                    </div>
                                </div>
                                <button className="btn-outline btn-sm" onClick={exportChat}>
                                    📥 Экспорт
                                </button>
                            </div>

                            <div className="chat-history-messages-list">
                                {loadingMessages ? (
                                    <div style={{ textAlign: 'center', padding: '40px' }}>
                                        Загрузка сообщений...
                                    </div>
                                ) : messages.length === 0 ? (
                                    <div className="chat-history-no-messages">
                                        <span>💬</span>
                                        <p>Нет сообщений</p>
                                    </div>
                                ) : (
                                    messages.map((msg, idx) => (
                                        <div
                                            key={idx}
                                            className={`chat-history-message ${msg.senderType === 'USER' ? 'user' : msg.senderType === 'CONSULTANT' ? 'consultant' : 'system'}`}
                                        >
                                            <div className="chat-history-message-avatar">
                                                {getSenderAvatar(msg.senderType)}
                                            </div>
                                            <div className="chat-history-message-bubble">
                                                <div className="chat-history-message-sender">
                                                    {getSenderName(msg.senderType)}
                                                </div>
                                                <div className="chat-history-message-text">
                                                    {msg.message}
                                                </div>
                                                <div className="chat-history-message-time">
                                                    {new Date(msg.timestamp).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })}
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                )}
                                <div ref={messagesEndRef} />
                            </div>
                        </>
                    ) : (
                        <div className="chat-history-no-selected">
                            <span>💬</span>
                            <p>Выберите чат из списка слева</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}