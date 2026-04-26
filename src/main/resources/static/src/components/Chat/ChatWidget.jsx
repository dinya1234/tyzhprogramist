// src/components/Chat/ChatWidget.jsx
import React, { useState, useEffect } from 'react';
import { useChat } from '../../context/ChatContext';
import { useAuth } from '../../context/AuthContext';
import ChatWindow from './ChatWindow';
import './Chat.css';

export default function ChatWidget() {
    const {
        isOpen,
        setIsOpen,
        createNewSession,
        isLoading,
        activeSession,
        messages,
        loadSession
    } = useChat();

    const { isAuthenticated } = useAuth();
    const [isMinimized, setIsMinimized] = useState(false);

    // Чат поддержки не должен сам создаваться до регистрации/логина.
    // Создаём/подгружаем сессию только когда пользователь ОТКРЫВАЕТ чат.
    useEffect(() => {
        const ensureSession = async () => {
            if (!isAuthenticated) return;
            if (!isOpen) return;
            if (activeSession || isLoading) return;

            const savedSessionId = localStorage.getItem('chatSessionId');
            if (savedSessionId) {
                try {
                    const existingSession = await loadSession(parseInt(savedSessionId, 10));
                    if (existingSession?.status === 'CLOSED') {
                        localStorage.removeItem('chatSessionId');
                    }
                    return;
                } catch (e) {
                    localStorage.removeItem('chatSessionId');
                }
            }

            const session = await createNewSession(window.location.href);
            if (session?.id) {
                localStorage.setItem('chatSessionId', session.id);
            }
        };

        ensureSession();
    }, [isAuthenticated, isOpen, activeSession, isLoading, loadSession, createNewSession]);

    const handleOpen = () => {
        if (!isAuthenticated) {
            // если не авторизован — не открываем поддержку
            window.location.href = '/login';
            return;
        }
        setIsOpen(true);
        setIsMinimized(false);
    };

    const handleMinimize = () => {
        setIsMinimized(!isMinimized);
        if (isMinimized) {
            setIsOpen(true);
        } else {
            setIsOpen(false);
        }
    };

    // Считаем непрочитанные
    const unreadCount = messages.filter(m => m.senderType !== 'USER' && !m.isRead).length;

    // Показываем загрузку
    if (isLoading && !activeSession) {
        return (
            <div className="chat-widget-button loading" style={{
                position: 'fixed', bottom: '20px', right: '20px',
                width: '60px', height: '60px', borderRadius: '50%',
                background: '#ccc', display: 'flex', alignItems: 'center',
                justifyContent: 'center', fontSize: '24px'
            }}>
                ⏳
            </div>
        );
    }

    return (
        <div className="chat-widget">
            {isOpen && !isMinimized && (
                <ChatWindow onMinimize={handleMinimize} />
            )}

            {(!isOpen || isMinimized) && (
                <button
                    className="chat-widget-button"
                    onClick={handleOpen}
                    style={{
                        position: 'fixed',
                        bottom: '20px',
                        right: '20px',
                        width: '60px',
                        height: '60px',
                        borderRadius: '50%',
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        border: 'none',
                        cursor: 'pointer',
                        fontSize: '24px',
                        color: 'white',
                        boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                        zIndex: 1000
                    }}
                >
                    {isLoading ? '⏳' : '💬'}
                    {unreadCount > 0 && (
                        <span style={{
                            position: 'absolute',
                            top: '-5px',
                            right: '-5px',
                            background: 'red',
                            color: 'white',
                            borderRadius: '50%',
                            width: '22px',
                            height: '22px',
                            fontSize: '12px',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center'
                        }}>
                            {unreadCount}
                        </span>
                    )}
                </button>
            )}
        </div>
    );
}