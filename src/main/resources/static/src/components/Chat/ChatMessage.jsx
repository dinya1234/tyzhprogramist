// src/components/Chat/ChatMessage.jsx
import React from 'react';

export default function ChatMessage({ message }) {
    const { senderType, message: text, timestamp, isTemp } = message;

    const formatTime = (dateStr) => {
        if (!dateStr) return '';
        const date = new Date(dateStr);
        return date.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' });
    };

    const getSenderName = () => {
        switch (senderType) {
            case 'USER': return 'Вы';
            case 'CONSULTANT': return 'Консультант';
            case 'SYSTEM': return 'Система';
            default: return '';
        }
    };

    const getAvatar = () => {
        switch (senderType) {
            case 'USER': return '👤';
            case 'CONSULTANT': return '🎧';
            case 'SYSTEM': return '🤖';
            default: return '💬';
        }
    };

    if (senderType === 'SYSTEM') {
        return (
            <div className="chat-message-system">
                <span className="chat-system-icon">{getAvatar()}</span>
                <span className="chat-system-text">{text}</span>
                <span className="chat-time">{formatTime(timestamp)}</span>
            </div>
        );
    }

    const isUser = senderType === 'USER';

    return (
        <div className={`chat-message ${isUser ? 'user' : 'consultant'}`}>
            <div className="chat-message-avatar">{getAvatar()}</div>
            <div className="chat-message-bubble">
                <div className="chat-message-sender">{getSenderName()}</div>
                <div className="chat-message-text">{text}</div>
                <div className="chat-message-time">
                    {formatTime(timestamp)}
                    {isTemp && <span className="chat-pending"> ⏳</span>}
                </div>
            </div>
        </div>
    );
}