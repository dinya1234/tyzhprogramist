// src/components/Chat/ChatWidget.jsx
import React, { useState, useEffect } from 'react';
import { useChat } from '../../context/ChatContext';
import ChatWindow from './ChatWindow';
import './Chat.css';

export default function ChatWidget() {
    const {
        isOpen,
        setIsOpen,
        createSession,
        isLoading,
        activeSession,
        messages,
        loadSession
    } = useChat();

    const [isMinimized, setIsMinimized] = useState(false);

    // Автоматически создаем или загружаем существующую сессию
    useEffect(() => {
        const initChat = async () => {
            // Проверяем, есть ли уже сессия в localStorage
            const savedSessionId = localStorage.getItem('chatSessionId');

            if (savedSessionId && !activeSession) {
                // Пробуем загрузить существующую сессию
                try {
                    await loadSession(parseInt(savedSessionId));
                    console.log('Loaded existing session:', savedSessionId);
                } catch (error) {
                    console.log('Session not found, creating new one');
                    localStorage.removeItem('chatSessionId');
                    await createSession(null, null, window.location.href);
                }
            } else if (!activeSession && !isLoading) {
                // Создаем новую сессию
                const session = await createSession(null, null, window.location.href);
                if (session) {
                    localStorage.setItem('chatSessionId', session.id);
                }
            }
        };

        initChat();
    }, []); // Запускаем только один раз

    const handleOpen = () => {
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