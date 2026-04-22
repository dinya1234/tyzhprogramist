// src/components/Chat/ChatWidget.jsx
import React, { useState } from 'react';
import { useChat } from '../../context/ChatContext';
import ChatWindow from './ChatWindow';

export default function ChatWidget() {
    const { isOpen, setIsOpen, createSession, isLoading } = useChat();
    const [isMinimized, setIsMinimized] = useState(false);

    const handleOpen = async () => {
        if (!isOpen) {
            await createSession(null, null, window.location.href);
        } else {
            setIsOpen(false);
            setIsMinimized(false);
        }
    };

    const handleMinimize = () => {
        setIsMinimized(!isMinimized);
    };

    return (
        <div className="chat-widget">
            {isOpen && !isMinimized && (
                <ChatWindow onMinimize={handleMinimize} />
            )}

            {(!isOpen || isMinimized) && (
                <button
                    className="chat-widget-button"
                    onClick={handleOpen}
                    disabled={isLoading}
                >
                    {isLoading ? '⏳' : '💬'}
                    {!isOpen && <span className="chat-badge">1</span>}
                </button>
            )}
        </div>
    );
}