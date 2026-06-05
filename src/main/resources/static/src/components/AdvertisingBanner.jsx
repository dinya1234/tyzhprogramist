// src/components/AdvertisingBanner.jsx (без кнопки удаления - только показ)
import React, { useState } from 'react';
import { Link } from 'react-router-dom';

export default function AdvertisingBanner({ banner }) {
    const [imageError, setImageError] = useState(false);

    return (
        <div className="advertising-banner" style={{
            flex: '0 0 auto',
            width: '300px',
            marginRight: '16px',
            borderRadius: '12px',
            overflow: 'hidden',
            background: 'var(--bg-tertiary)',
            border: '1px solid var(--border)',
            transition: 'transform 0.2s, box-shadow 0.2s',
            position: 'relative'
        }}
        onMouseEnter={(e) => {
            e.currentTarget.style.transform = 'translateY(-4px)';
            e.currentTarget.style.boxShadow = 'var(--shadow)';
        }}
        onMouseLeave={(e) => {
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow = 'none';
        }}
        >
            <Link to={banner.link} target={banner.targetBlank ? '_blank' : '_self'} style={{ textDecoration: 'none' }}>
                {banner.imageUrl && !imageError ? (
                    <img
                        src={banner.imageUrl}
                        alt={banner.title}
                        style={{
                            width: '100%',
                            height: '150px',
                            objectFit: 'cover'
                        }}
                        onError={() => setImageError(true)}
                    />
                ) : (
                    <div style={{
                        width: '100%',
                        height: '150px',
                        background: 'var(--accent-gradient)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '48px'
                    }}>
                        📢
                    </div>
                )}
                <div style={{ padding: '12px' }}>
                    <h4 style={{
                        margin: '0 0 8px 0',
                        fontSize: '14px',
                        color: 'var(--text-primary)',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        whiteSpace: 'nowrap'
                    }}>
                        {banner.title}
                    </h4>
                    {banner.description && (
                        <p style={{
                            margin: 0,
                            fontSize: '12px',
                            color: 'var(--text-secondary)',
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            display: '-webkit-box',
                            WebkitLineClamp: 2,
                            WebkitBoxOrient: 'vertical'
                        }}>
                            {banner.description}
                        </p>
                    )}
                </div>
            </Link>
        </div>
    );
}