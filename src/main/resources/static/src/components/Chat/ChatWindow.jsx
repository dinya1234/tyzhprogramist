// src/components/Chat/ChatWindow.jsx
import React, { useState, useRef, useEffect } from 'react';
import { useChat } from '../../context/ChatContext';
import ChatMessage from './ChatMessage';

export default function ChatWindow({ onMinimize }) {
    const { messages, sendMessage, closeSession, activeSession, isLoading } = useChat();
    const [inputMessage, setInputMessage] = useState('');
    const [sending, setSending] = useState(false);
    const messagesEndRef = useRef(null);
    const inputRef = useRef(null);

    // Автоскролл к последнему сообщению
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    // Фокус на поле ввода при открытии
    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    const handleSend = async (e) => {
        e.preventDefault();
        if (!inputMessage.trim() || sending) return;

        setSending(true);
        try {
            await sendMessage(inputMessage.trim());
            setInputMessage('');
        } catch (error) {
            console.error('Failed to send:', error);
        } finally {
            setSending(false);
            inputRef.current?.focus();
        }
    };

    const handleClose = async () => {
        if (activeSession && confirm('Закрыть чат?')) {
            await closeSession(activeSession.id);
        }
        onMinimize();
    };

    // Системное сообщение о начале чата
    const welcomeMessage = {
        id: 'welcome',
        senderType: 'SYSTEM',
        message: 'Здравствуйте! Соединяем вас со свободным консультантом. Пожалуйста, задайте ваш вопрос. Среднее время ответа — 2 минуты.',
        timestamp: new Date().toISOString()
    };

    const allMessages = [welcomeMessage, ...messages];

    return (
        <div className="chat-window">
            {/* Заголовок */}
            <div className="chat-header">
                <div className="chat-header-info">
                    <span className="chat-icon">💬</span>
                    <span className="chat-title">Консультант</span>
                    {activeSession?.status === 'ACTIVE' && (
                        <span className="chat-status online">Онлайн</span>
                    )}
                    {activeSession?.status === 'PENDING' && (
                        <span className="chat-status waiting">Ожидание</span>
                    )}
                </div>
                <div className="chat-header-actions">
                    <button onClick={onMinimize} className="chat-minimize">−</button>
                    <button onClick={handleClose} className="chat-close">✕</button>
                </div>
            </div>

            {/* Сообщения */}
            <div className="chat-messages">
                {allMessages.map((msg, idx) => (
                    <ChatMessage key={msg.id || idx} message={msg} />
                ))}
                {isLoading && (
                    <div className="chat-typing">
                        <span>.</span><span>.</span><span>.</span>
                    </div>
                )}
                <div ref={messagesEndRef} />
            </div>

            {/* Форма ввода */}
            <form onSubmit={handleSend} className="chat-input-area">
                <input
                    ref={inputRef}
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="Введите сообщение..."
                    disabled={sending || activeSession?.status === 'CLOSED'}
                />
                <button
                    type="submit"
                    disabled={!inputMessage.trim() || sending}
                    className="btn-primary"
                >
                    {sending ? '⏳' : '📤'}
                </button>
            </form>
        </div>
    );
}