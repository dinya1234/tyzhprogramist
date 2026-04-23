// src/pages/admin/ConsultantPanel.jsx
import React, { useState, useEffect, useRef } from 'react';
import api from '../../services/api';
import './ConsultantPanel.css';

export default function ConsultantPanel() {
    const [pendingSessions, setPendingSessions] = useState([]);
    const [activeSessions, setActiveSessions] = useState([]);
    const [selectedSession, setSelectedSession] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');
    const [sending, setSending] = useState(false);
    const [loading, setLoading] = useState(true);
    const messagesEndRef = useRef(null);
    const pollingInterval = useRef(null);
    const sessionsInterval = useRef(null);

    // Загрузка ожидающих сессий
    const loadPendingSessions = async () => {
        try {
            const response = await api.get('/chat/consultant/pending');
            console.log('Loaded pending sessions:', response.data);
            setPendingSessions(response.data || []);
        } catch (error) {
            console.error('Failed to load pending sessions:', error);
        }
    };

    // Загрузка активных сессий консультанта
    const loadActiveSessions = async () => {
        try {
            const response = await api.get('/chat/consultant/active');
            console.log('Loaded active sessions:', response.data);
            setActiveSessions(response.data || []);
        } catch (error) {
            console.error('Failed to load active sessions:', error);
        }
    };

    // Загрузка сообщений для выбранной сессии
    const loadMessages = async (sessionId) => {
        if (!sessionId) return;
        try {
            const response = await api.get(`/chat/session/${sessionId}/messages`);
            console.log(`Messages for session ${sessionId}:`, response.data);
            setMessages(response.data || []);
            setTimeout(() => {
                messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
            }, 100);
        } catch (error) {
            console.error('Failed to load messages:', error);
        }
    };

    // Взятие сессии
    const handleTakeSession = async (session) => {
        try {
            console.log('Taking session:', session.id);
            const response = await api.post(`/chat/consultant/session/${session.id}/take`);
            console.log('Take response:', response.data);

            // Обновляем списки
            await loadPendingSessions();
            await loadActiveSessions();

            // Выбираем взятую сессию
            setSelectedSession(response.data);
            await loadMessages(response.data.id);
        } catch (error) {
            console.error('Failed to take session:', error);
            alert('Ошибка при взятии сессии: ' + (error.response?.data?.message || error.message));
        }
    };

    // Выбор сессии
    const handleSelectSession = async (session) => {
        console.log('Selecting session:', session.id);
        setSelectedSession(session);
        await loadMessages(session.id);
    };

    // Отправка сообщения
    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!inputMessage.trim() || sending || !selectedSession) return;

        setSending(true);
        try {
            console.log('Sending message to session:', selectedSession.id, inputMessage);
            await api.post(`/chat/consultant/session/${selectedSession.id}/message`, null, {
                params: { message: inputMessage }
            });
            setInputMessage('');
            // Ждем и обновляем сообщения
            setTimeout(async () => {
                await loadMessages(selectedSession.id);
            }, 500);
        } catch (error) {
            console.error('Failed to send message:', error);
            alert('Ошибка отправки сообщения');
        } finally {
            setSending(false);
        }
    };

    // Закрытие сессии
    const handleCloseSession = async () => {
        if (!selectedSession) return;
        if (!window.confirm('Закрыть эту сессию?')) return;

        try {
            await api.post(`/chat/consultant/session/${selectedSession.id}/close`);
            setSelectedSession(null);
            setMessages([]);
            await loadPendingSessions();
            await loadActiveSessions();
        } catch (error) {
            console.error('Failed to close session:', error);
        }
    };

    // Загрузка всех данных
    const loadAllData = async () => {
        setLoading(true);
        await Promise.all([
            loadPendingSessions(),
            loadActiveSessions()
        ]);
        setLoading(false);
    };

    // Инициализация
    useEffect(() => {
        loadAllData();

        // Обновляем списки каждые 5 секунд
        sessionsInterval.current = setInterval(() => {
            loadPendingSessions();
            loadActiveSessions();
        }, 5000);

        return () => {
            if (sessionsInterval.current) clearInterval(sessionsInterval.current);
            if (pollingInterval.current) clearInterval(pollingInterval.current);
        };
    }, []);

    // Polling для сообщений выбранной сессии
    useEffect(() => {
        if (pollingInterval.current) {
            clearInterval(pollingInterval.current);
        }

        if (selectedSession?.id) {
            pollingInterval.current = setInterval(async () => {
                await loadMessages(selectedSession.id);
            }, 3000);
        }

        return () => {
            if (pollingInterval.current) clearInterval(pollingInterval.current);
        };
    }, [selectedSession?.id]);

    if (loading) {
        return (
            <div style={{ padding: '40px', textAlign: 'center' }}>
                <h3>Загрузка чатов...</h3>
            </div>
        );
    }

    return (
        <div className="consultant-panel">
            <div className="consultant-sidebar">
                <div className="sidebar-section">
                    <h3>🕐 Ожидают ответа ({pendingSessions.length})</h3>
                    <div className="sessions-list">
                        {pendingSessions.length === 0 ? (
                            <div className="empty-state">Нет ожидающих чатов</div>
                        ) : (
                            pendingSessions.map(session => (
                                <div key={session.id} className="session-item pending">
                                    <div className="session-info">
                                        <span className="session-user">
                                            {session.userName || session.userEmail || 'Гость'}
                                        </span>
                                        <span className="session-time">
                                            {new Date(session.startedAt).toLocaleTimeString()}
                                        </span>
                                    </div>
                                    <button
                                        className="take-btn"
                                        onClick={() => handleTakeSession(session)}
                                    >
                                        Взять чат
                                    </button>
                                </div>
                            ))
                        )}
                    </div>
                </div>

                <div className="sidebar-section">
                    <h3>💬 Активные чаты ({activeSessions.length})</h3>
                    <div className="sessions-list">
                        {activeSessions.length === 0 ? (
                            <div className="empty-state">Нет активных чатов</div>
                        ) : (
                            activeSessions.map(session => (
                                <div
                                    key={session.id}
                                    className={`session-item ${selectedSession?.id === session.id ? 'selected' : ''}`}
                                    onClick={() => handleSelectSession(session)}
                                >
                                    <div className="session-info">
                                        <span className="session-user">
                                            {session.userName || session.userEmail || 'Гость'}
                                        </span>
                                        <span className="session-time">
                                            {new Date(session.startedAt).toLocaleTimeString()}
                                        </span>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>

            <div className="consultant-chat">
                {selectedSession ? (
                    <>
                        <div className="chat-header">
                            <div className="user-info">
                                <strong>Чат с: {selectedSession.userName || selectedSession.userEmail || 'Гостем'}</strong>
                                <div className="session-status">
                                    Статус: {selectedSession.status === 'ACTIVE' ? '🟢 Активен' : '🟡 Ожидает'}
                                </div>
                            </div>
                            <button onClick={handleCloseSession} className="close-btn">
                                ✕ Закрыть чат
                            </button>
                        </div>

                        <div className="chat-messages">
                            {messages.length === 0 ? (
                                <div className="empty-messages">Нет сообщений</div>
                            ) : (
                                messages.map((msg, idx) => (
                                    <div
                                        key={msg.id || idx}
                                        className={`message ${msg.senderType?.toLowerCase() || 'system'}`}
                                    >
                                        <div className="message-sender">
                                            {msg.senderType === 'USER' ? '👤 Клиент' :
                                             msg.senderType === 'CONSULTANT' ? '🎧 Вы' : '🤖 Система'}
                                        </div>
                                        <div className="message-text">{msg.message}</div>
                                        <div className="message-time">
                                            {new Date(msg.timestamp).toLocaleTimeString()}
                                        </div>
                                    </div>
                                ))
                            )}
                            <div ref={messagesEndRef} />
                        </div>

                        <form onSubmit={handleSendMessage} className="chat-input">
                            <input
                                type="text"
                                value={inputMessage}
                                onChange={(e) => setInputMessage(e.target.value)}
                                placeholder="Введите сообщение..."
                                disabled={sending}
                                autoFocus
                            />
                            <button type="submit" disabled={!inputMessage.trim() || sending}>
                                {sending ? '⏳' : '📤 Отправить'}
                            </button>
                        </form>
                    </>
                ) : (
                    <div className="no-session">
                        <div className="no-session-icon">💬</div>
                        <h3>Выберите чат из списка слева</h3>
                        <p>Нажмите "Взять чат", чтобы начать общение с пользователем</p>
                    </div>
                )}
            </div>
        </div>
    );
}