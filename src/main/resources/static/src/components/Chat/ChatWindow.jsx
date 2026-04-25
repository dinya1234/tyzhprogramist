// src/components/Chat/ChatWindow.jsx
import React, { useState, useRef, useEffect } from 'react';
import { useChat } from '../../context/ChatContext';

export default function ChatWindow({ onMinimize }) {
    const {
        messages,
        sendMessage,
        activeSession,
        isLoading,
        setIsOpen
    } = useChat();

    const [inputMessage, setInputMessage] = useState('');
    const [sending, setSending] = useState(false);
    const messagesEndRef = useRef(null);
    const inputRef = useRef(null);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

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
            alert('Не удалось отправить сообщение');
        } finally {
            setSending(false);
            inputRef.current?.focus();
        }
    };

    const handleClose = async () => {
        // "Закрыть" здесь = скрыть окно, но НЕ завершать сессию,
        // чтобы пользователь мог открыть чат снова и продолжить переписку.
        setIsOpen(false);
        onMinimize?.();
    };

    // Статус чата
    const isActive = activeSession?.status === 'ACTIVE';
    const isPending = activeSession?.status === 'PENDING';
    const isClosed = activeSession?.status === 'CLOSED';

    return (
        <div style={{
            position: 'fixed',
            bottom: '90px',
            right: '20px',
            width: '380px',
            height: '550px',
            background: 'white',
            borderRadius: '12px',
            boxShadow: '0 8px 24px rgba(0,0,0,0.15)',
            display: 'flex',
            flexDirection: 'column',
            overflow: 'hidden',
            zIndex: 1000
        }}>
            {/* Заголовок */}
            <div style={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                color: 'white',
                padding: '12px 16px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
            }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span>💬</span>
                    <span style={{ fontWeight: 600 }}>Поддержка</span>
                    {isActive && <span style={{ fontSize: 11, background: '#4caf50', padding: '2px 6px', borderRadius: 10 }}>Онлайн</span>}
                    {isPending && <span style={{ fontSize: 11, background: '#ff9800', padding: '2px 6px', borderRadius: 10 }}>Ожидание ответа</span>}
                </div>
                <div>
                    <button onClick={onMinimize} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer', fontSize: 18, marginRight: 8 }}>−</button>
                    <button onClick={handleClose} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer', fontSize: 18 }}>✕</button>
                </div>
            </div>

            {/* Сообщения */}
            <div style={{
                flex: 1,
                overflowY: 'auto',
                padding: '16px',
                display: 'flex',
                flexDirection: 'column',
                gap: '12px',
                background: '#f5f5f5'
            }}>
                {messages.length === 0 && !isLoading && (
                    <div style={{ textAlign: 'center', color: '#999', padding: 20 }}>
                        Задайте ваш вопрос, и консультант скоро ответит.
                    </div>
                )}
                {messages.map((msg, idx) => (
                    <div
                        key={msg.id || idx}
                        style={{
                            display: 'flex',
                            justifyContent: msg.senderType === 'USER' ? 'flex-end' : 'flex-start'
                        }}
                    >
                        <div style={{
                            maxWidth: '70%',
                            padding: '8px 12px',
                            borderRadius: 12,
                            background: msg.senderType === 'USER'
                                ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                                : msg.senderType === 'SYSTEM'
                                    ? '#e8e8e8'
                                    : 'white',
                            color: msg.senderType === 'USER' ? 'white' : '#333',
                            boxShadow: '0 1px 2px rgba(0,0,0,0.1)'
                        }}>
                            {msg.senderType !== 'USER' && msg.senderType !== 'SYSTEM' && (
                                <div style={{ fontSize: 11, fontWeight: 600, marginBottom: 4, opacity: 0.7 }}>Консультант</div>
                            )}
                            <div style={{ fontSize: 14, wordWrap: 'break-word' }}>{msg.message}</div>
                            <div style={{ fontSize: 10, marginTop: 4, opacity: 0.6, textAlign: 'right' }}>
                                {new Date(msg.timestamp).toLocaleTimeString()}
                            </div>
                        </div>
                    </div>
                ))}
                {isLoading && (
                    <div style={{ display: 'flex', gap: 4, padding: 8, background: 'white', borderRadius: 12, width: 'fit-content' }}>
                        <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#999', animation: 'typing 1.4s infinite' }}></span>
                        <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#999', animation: 'typing 1.4s infinite 0.2s' }}></span>
                        <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#999', animation: 'typing 1.4s infinite 0.4s' }}></span>
                    </div>
                )}
                <div ref={messagesEndRef} />
            </div>

            {/* Ввод */}
            {!isClosed ? (
                <form onSubmit={handleSend} style={{
                    padding: '12px',
                    background: 'white',
                    borderTop: '1px solid #e0e0e0',
                    display: 'flex',
                    gap: '8px'
                }}>
                    <input
                        ref={inputRef}
                        type="text"
                        value={inputMessage}
                        onChange={(e) => setInputMessage(e.target.value)}
                        placeholder="Введите сообщение..."
                        disabled={sending || isLoading}
                        style={{
                            flex: 1,
                            padding: '8px 12px',
                            border: '1px solid #e0e0e0',
                            borderRadius: '20px',
                            outline: 'none',
                            fontSize: '14px'
                        }}
                    />
                    <button
                        type="submit"
                        disabled={!inputMessage.trim() || sending}
                        style={{
                            width: '36px',
                            height: '36px',
                            borderRadius: '50%',
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            border: 'none',
                            cursor: 'pointer',
                            fontSize: '16px',
                            color: 'white'
                        }}
                    >
                        {sending ? '⏳' : '📤'}
                    </button>
                </form>
            ) : (
                <div style={{ padding: 12, textAlign: 'center', color: '#666', background: '#f5f5f5', borderTop: '1px solid #e0e0e0' }}>
                    Чат закрыт. Спасибо за обращение!
                </div>
            )}
        </div>
    );
}