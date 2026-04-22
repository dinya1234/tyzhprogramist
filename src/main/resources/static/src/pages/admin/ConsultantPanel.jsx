// src/pages/admin/ConsultantPanel.jsx
import React, { useState, useEffect } from 'react';
import { useChat } from '../../context/ChatContext';
import ChatWindow from '../../components/Chat/ChatWindow';

export default function ConsultantPanel() {
    const {
        pendingSessions,
        activeSessions,
        takeSession,
        activeSession,
        loadPendingSessions,
        loadActiveConsultantSessions
    } = useChat();

    const [selectedSession, setSelectedSession] = useState(null);
    const [activeTab, setActiveTab] = useState('pending'); // pending, active

    useEffect(() => {
        loadPendingSessions();
        loadActiveConsultantSessions();
    }, []);

    const handleTakeSession = async (sessionId) => {
        await takeSession(sessionId);
        setSelectedSession(sessionId);
        setActiveTab('active');
    };

    const formatDate = (dateStr) => {
        const date = new Date(dateStr);
        return date.toLocaleString('ru-RU');
    };

    return (
        <div className="consultant-panel">
            <div className="consultant-sidebar">
                <div className="consultant-tabs">
                    <button
                        className={activeTab === 'pending' ? 'active' : ''}
                        onClick={() => setActiveTab('pending')}
                    >
                        Очередь ({pendingSessions.length})
                    </button>
                    <button
                        className={activeTab === 'active' ? 'active' : ''}
                        onClick={() => setActiveTab('active')}
                    >
                        Активные ({activeSessions.length})
                    </button>
                </div>

                <div className="consultant-sessions-list">
                    {activeTab === 'pending' && pendingSessions.map(session => (
                        <div
                            key={session.id}
                            className={`session-item ${selectedSession === session.id ? 'selected' : ''}`}
                            onClick={() => setSelectedSession(session.id)}
                        >
                            <div className="session-info">
                                <span className="session-user">
                                    {session.userName || 'Гость'}
                                </span>
                                <span className="session-time">
                                    {formatDate(session.startedAt)}
                                </span>
                            </div>
                            <div className="session-status pending">Ожидает</div>
                            <button
                                className="btn-primary btn-sm"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleTakeSession(session.id);
                                }}
                            >
                                Принять
                            </button>
                        </div>
                    ))}

                    {activeTab === 'active' && activeSessions.map(session => (
                        <div
                            key={session.id}
                            className={`session-item ${selectedSession === session.id ? 'selected' : ''}`}
                            onClick={() => setSelectedSession(session.id)}
                        >
                            <div className="session-info">
                                <span className="session-user">
                                    {session.userName || 'Гость'}
                                </span>
                                <span className="session-time">
                                    с {formatDate(session.startedAt)}
                                </span>
                            </div>
                            <div className="session-status active">Активен</div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="consultant-chat-area">
                {activeSession && selectedSession === activeSession.id ? (
                    <ChatWindow onMinimize={() => {}} />
                ) : (
                    <div className="no-session-selected">
                        <span>💬</span>
                        <p>Выберите сессию из списка слева</p>
                    </div>
                )}
            </div>
        </div>
    );
}