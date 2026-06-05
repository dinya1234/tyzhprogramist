// src/components/AddBannerForm.jsx
import React, { useState, useEffect } from 'react';

export default function AddBannerForm({ banner, onSave, onClose }) {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        imageUrl: '',
        link: '/catalog',
        targetBlank: false,
        order: 0
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (banner) {
            console.log('Загрузка баннера для редактирования:', banner);
            setFormData({
                title: banner.title || '',
                description: banner.description || '',
                imageUrl: banner.imageUrl || '',
                link: banner.link || '/catalog',
                targetBlank: banner.targetBlank || false,
                order: banner.displayOrder || banner.order || 0
            });
        }
    }, [banner]);

    const validate = () => {
        const newErrors = {};
        if (!formData.title.trim()) newErrors.title = 'Введите заголовок';
        if (!formData.link.trim()) newErrors.link = 'Введите ссылку';
        return newErrors;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const newErrors = validate();
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        const bannerToSave = {
            title: formData.title,
            description: formData.description,
            imageUrl: formData.imageUrl,
            link: formData.link,
            targetBlank: formData.targetBlank,
            order: parseInt(formData.order) || 0
        };


        if (banner) {
            bannerToSave.id = banner.id;
            bannerToSave.displayOrder = bannerToSave.order;
        }

        console.log('Отправка баннера:', bannerToSave);
        onSave(bannerToSave);
    };

    return (
        <div style={{
            position: 'fixed',
            inset: 0,
            background: 'rgba(0,0,0,0.75)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1200,
            padding: '20px'
        }} onClick={onClose}>
            <div style={{
                background: 'var(--bg-tertiary)',
                borderRadius: '20px',
                width: '100%',
                maxWidth: '500px',
                maxHeight: '90vh',
                overflowY: 'auto',
                border: '1px solid var(--border)'
            }} onClick={(e) => e.stopPropagation()}>
                <div style={{
                    padding: '20px 24px',
                    borderBottom: '1px solid var(--border)',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }}>
                    <h2 style={{ margin: 0 }}>{banner ? '✏️ Редактировать баннер' : '➕ Добавить рекламный баннер'}</h2>
                    <button onClick={onClose} style={{
                        background: 'none',
                        border: 'none',
                        color: 'var(--text-secondary)',
                        fontSize: '24px',
                        cursor: 'pointer'
                    }}>✕</button>
                </div>

                <form onSubmit={handleSubmit} style={{ padding: '24px' }}>
                    <div style={{ marginBottom: '16px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)' }}>
                            Заголовок *
                        </label>
                        <input
                            type="text"
                            value={formData.title}
                            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                            style={{
                                width: '100%',
                                padding: '10px 12px',
                                background: 'var(--bg-input)',
                                border: `1px solid ${errors.title ? 'var(--danger)' : 'var(--border-light)'}`,
                                borderRadius: '8px',
                                color: 'var(--text-primary)'
                            }}
                        />
                        {errors.title && <div style={{ color: 'var(--danger)', fontSize: '12px', marginTop: '4px' }}>{errors.title}</div>}
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)' }}>
                            Описание
                        </label>
                        <textarea
                            value={formData.description}
                            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                            rows={2}
                            style={{
                                width: '100%',
                                padding: '10px 12px',
                                background: 'var(--bg-input)',
                                border: '1px solid var(--border-light)',
                                borderRadius: '8px',
                                color: 'var(--text-primary)',
                                resize: 'vertical'
                            }}
                            placeholder="Краткое описание акции"
                        />
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)' }}>
                            URL изображения
                        </label>
                        <input
                            type="text"
                            value={formData.imageUrl}
                            onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
                            placeholder="https://example.com/image.jpg"
                            style={{
                                width: '100%',
                                padding: '10px 12px',
                                background: 'var(--bg-input)',
                                border: '1px solid var(--border-light)',
                                borderRadius: '8px',
                                color: 'var(--text-primary)'
                            }}
                        />
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)' }}>
                            Ссылка *
                        </label>
                        <input
                            type="text"
                            value={formData.link}
                            onChange={(e) => setFormData({ ...formData, link: e.target.value })}
                            placeholder="/catalog или https://example.com"
                            style={{
                                width: '100%',
                                padding: '10px 12px',
                                background: 'var(--bg-input)',
                                border: `1px solid ${errors.link ? 'var(--danger)' : 'var(--border-light)'}`,
                                borderRadius: '8px',
                                color: 'var(--text-primary)'
                            }}
                        />
                        {errors.link && <div style={{ color: 'var(--danger)', fontSize: '12px', marginTop: '4px' }}>{errors.link}</div>}
                    </div>

                    <div style={{ marginBottom: '16px' }}>
                        <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                            <input
                                type="checkbox"
                                checked={formData.targetBlank}
                                onChange={(e) => setFormData({ ...formData, targetBlank: e.target.checked })}
                            />
                            <span style={{ color: 'var(--text-primary)' }}>Открывать в новой вкладке</span>
                        </label>
                    </div>

                    <div style={{ marginBottom: '24px' }}>
                        <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)' }}>
                            Порядок сортировки (меньше = выше)
                        </label>
                        <input
                            type="number"
                            value={formData.order}
                            onChange={(e) => setFormData({ ...formData, order: parseInt(e.target.value) || 0 })}
                            style={{
                                width: '100%',
                                padding: '10px 12px',
                                background: 'var(--bg-input)',
                                border: '1px solid var(--border-light)',
                                borderRadius: '8px',
                                color: 'var(--text-primary)'
                            }}
                        />
                    </div>

                    <div style={{ display: 'flex', gap: '12px' }}>
                        <button type="button" onClick={onClose} className="btn-outline" style={{ flex: 1 }}>
                            Отмена
                        </button>
                        <button type="submit" className="btn-primary" style={{ flex: 1 }}>
                            {banner ? 'Сохранить' : 'Добавить'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}