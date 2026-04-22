// src/pages/ConfiguratorPage.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { pcBuilds, componentTypes, products, cart } from '../services/api';
import { useChat } from '../context/ChatContext';
import { useCart } from '../context/CartContext';

export default function ConfiguratorPage() {
    const navigate = useNavigate();
    const { createSession, sendMessage, setIsOpen } = useChat();
    const { addToCart } = useCart();

    // Состояния конфигуратора
    const [step, setStep] = useState(1);
    const [totalSteps, setTotalSteps] = useState(0);
    const [componentTypesList, setComponentTypesList] = useState([]);
    const [currentType, setCurrentType] = useState(null);
    const [availableProducts, setAvailableProducts] = useState([]);
    const [selectedComponents, setSelectedComponents] = useState({});
    const [currentBuildId, setCurrentBuildId] = useState(null);
    const [buildName, setBuildName] = useState('Моя сборка');
    const [isPublic, setIsPublic] = useState(false);
    const [totalPrice, setTotalPrice] = useState(0);
    const [loading, setLoading] = useState(false);
    const [saving, setSaving] = useState(false);

    // Проверка совместимости
    const [compatibilityWarnings, setCompatibilityWarnings] = useState([]);
    const [powerSupplyCalc, setPowerSupplyCalc] = useState({
        estimated: 0,
        recommended: 0,
        warning: null
    });

    // Загрузка типов компонентов
    useEffect(() => {
        loadComponentTypes();
    }, []);

    // Загрузка существующей сборки (если редактируем)
    useEffect(() => {
        const buildIdFromUrl = new URLSearchParams(window.location.search).get('buildId');
        if (buildIdFromUrl) {
            loadBuild(buildIdFromUrl);
        } else {
            createNewBuild();
        }
    }, []);

    // Загрузка товаров для текущего шага
    useEffect(() => {
        if (currentType) {
            loadProductsForType(currentType);
        }
    }, [currentType]);

    // Пересчёт итоговой цены
    useEffect(() => {
        calculateTotalPrice();
        checkCompatibility();
        calculatePowerSupply();
    }, [selectedComponents]);

    const loadComponentTypes = async () => {
        try {
            const response = await componentTypes.getOrdered();
            setComponentTypesList(response.data);
            setTotalSteps(response.data.length);
            if (response.data.length > 0) {
                setCurrentType(response.data[0]);
                setStep(response.data[0].orderStep);
            }

            const stepsResponse = await componentTypes.getTotalSteps();
            setTotalSteps(stepsResponse.data);
        } catch (error) {
            console.error('Ошибка загрузки типов компонентов:', error);
        }
    };

    const loadProductsForType = async (type) => {
        setLoading(true);
        try {
            // Получаем товары по категории (используем имя типа для поиска)
            const response = await products.getAll({
                categoryName: type.name,
                size: 50
            });
            setAvailableProducts(response.data.content || []);
        } catch (error) {
            console.error('Ошибка загрузки товаров:', error);
            setAvailableProducts([]);
        } finally {
            setLoading(false);
        }
    };

    const createNewBuild = async () => {
        try {
            const response = await pcBuilds.create({
                name: buildName,
                isPublic: false
            });
            setCurrentBuildId(response.data.id);
        } catch (error) {
            console.error('Ошибка создания сборки:', error);
        }
    };

    const loadBuild = async (buildId) => {
        try {
            const response = await pcBuilds.getMyBuild(buildId);
            const build = response.data;
            setBuildName(build.name);
            setIsPublic(build.isPublic);
            setCurrentBuildId(build.id);

            // Загружаем компоненты
            const componentsRes = await pcBuilds.getComponents(buildId);
            const components = {};
            componentsRes.data.forEach(comp => {
                components[comp.componentType] = {
                    id: comp.productId,
                    name: comp.productName,
                    price: comp.price,
                    quantity: comp.quantity
                };
            });
            setSelectedComponents(components);
        } catch (error) {
            console.error('Ошибка загрузки сборки:', error);
        }
    };

    const selectComponent = async (product) => {
        if (!currentBuildId) return;

        setLoading(true);
        try {
            await pcBuilds.addComponent(currentBuildId, product.id, 1);

            setSelectedComponents(prev => ({
                ...prev,
                [currentType.name]: {
                    id: product.id,
                    name: product.name,
                    price: product.price,
                    quantity: 1
                }
            }));
        } catch (error) {
            alert(error.response?.data?.message || 'Ошибка добавления компонента');
        } finally {
            setLoading(false);
        }
    };

    const removeComponent = async (typeName) => {
        const component = selectedComponents[typeName];
        if (!component || !currentBuildId) return;

        try {
            await pcBuilds.removeComponent(currentBuildId, component.id);

            const newComponents = { ...selectedComponents };
            delete newComponents[typeName];
            setSelectedComponents(newComponents);
        } catch (error) {
            console.error('Ошибка удаления:', error);
        }
    };

    const calculateTotalPrice = async () => {
        if (!currentBuildId) return;

        try {
            const response = await pcBuilds.getTotalPrice(currentBuildId);
            setTotalPrice(response.data);
        } catch (error) {
            console.error('Ошибка расчёта цены:', error);
        }
    };

    const checkCompatibility = () => {
        const warnings = [];
        const components = Object.values(selectedComponents);

        // Проверка процессора и материнской платы
        const cpu = components.find(c => c.name?.toLowerCase().includes('процессор'));
        const motherboard = components.find(c => c.name?.toLowerCase().includes('материнская'));

        if (cpu && motherboard) {
            // Простая проверка сокета (можно расширить)
            if (cpu.name.includes('Intel') && motherboard.name.includes('AMD')) {
                warnings.push('⚠️ Процессор Intel и материнская плата AMD могут быть несовместимы!');
            }
            if (cpu.name.includes('AMD') && motherboard.name.includes('Intel')) {
                warnings.push('⚠️ Процессор AMD и материнская плата Intel могут быть несовместимы!');
            }
        }

        setCompatibilityWarnings(warnings);
    };

    const calculatePowerSupply = () => {
        let estimated = 150; // Базовая мощность

        Object.values(selectedComponents).forEach(comp => {
            // Примерное энергопотребление по типу компонента
            if (comp.name?.toLowerCase().includes('видеокарта')) estimated += 200;
            else if (comp.name?.toLowerCase().includes('процессор')) estimated += 125;
            else if (comp.name?.toLowerCase().includes('материнская')) estimated += 50;
            else if (comp.name?.toLowerCase().includes('озу')) estimated += 15;
            else if (comp.name?.toLowerCase().includes('ssd')) estimated += 10;
            else if (comp.name?.toLowerCase().includes('hdd')) estimated += 15;
        });

        const recommended = Math.ceil(estimated * 1.3);
        let warning = null;

        const psu = Object.values(selectedComponents).find(c =>
            c.name?.toLowerCase().includes('блок питания')
        );

        if (psu) {
            const psuPower = parseInt(psu.name?.match(/\d+/)?.[0] || 0);
            if (psuPower && psuPower < recommended) {
                warning = `⚠️ Блок питания (${psuPower}W) может быть недостаточным. Рекомендуется ${recommended}W`;
            }
        }

        setPowerSupplyCalc({ estimated, recommended, warning });
    };

    const nextStep = () => {
        const currentIndex = componentTypesList.findIndex(t => t.id === currentType?.id);
        if (currentIndex < componentTypesList.length - 1) {
            setCurrentType(componentTypesList[currentIndex + 1]);
            setStep(componentTypesList[currentIndex + 1].orderStep);
            window.scrollTo(0, 0);
        }
    };

    const prevStep = () => {
        const currentIndex = componentTypesList.findIndex(t => t.id === currentType?.id);
        if (currentIndex > 0) {
            setCurrentType(componentTypesList[currentIndex - 1]);
            setStep(componentTypesList[currentIndex - 1].orderStep);
            window.scrollTo(0, 0);
        }
    };

    const saveBuild = async () => {
        if (!currentBuildId) return;

        setSaving(true);
        try {
            await pcBuilds.update(currentBuildId, buildName, isPublic);
            alert('Сборка сохранена!');
            navigate('/profile');
        } catch (error) {
            alert('Ошибка сохранения сборки');
        } finally {
            setSaving(false);
        }
    };

    const addAllToCart = async () => {
        for (const component of Object.values(selectedComponents)) {
            await addToCart(component.id, component.quantity);
        }
        alert('Все компоненты добавлены в корзину!');
        navigate('/cart');
    };

    const getConsultantHelp = async () => {
        const componentsList = Object.values(selectedComponents)
            .map(c => `- ${c.name}`)
            .join('\n');

        await createSession('Configurator', null, window.location.href);
        await sendMessage(`Нужна помощь со сборкой ПК!\n\nБюджет: ${totalPrice} ₽\n\nВыбранные компоненты:\n${componentsList || 'Пока не выбраны'}`);
        setIsOpen(true);
    };

    const progressPercent = (step / totalSteps) * 100;

    return (
        <div className="container" style={{ marginTop: '30px', marginBottom: '60px' }}>
            <h1 style={{ marginBottom: '24px' }}>🖥️ Конфигуратор ПК</h1>

            {/* Прогресс-бар */}
            <div style={{ marginBottom: '32px' }}>
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    marginBottom: '8px',
                    fontSize: '14px',
                    color: '#9ca3af'
                }}>
                    <span>Шаг {step} из {totalSteps}</span>
                    <span>{Math.round(progressPercent)}%</span>
                </div>
                <div style={{
                    height: '8px',
                    background: '#2a2d36',
                    borderRadius: '4px',
                    overflow: 'hidden'
                }}>
                    <div style={{
                        width: `${progressPercent}%`,
                        height: '100%',
                        background: 'linear-gradient(135deg, #c084fc, #60a5fa)',
                        transition: 'width 0.3s'
                    }} />
                </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: '32px' }}>
                {/* Основная область */}
                <div>
                    {/* Текущий шаг */}
                    <div className="config-section">
                        <h2 style={{ marginBottom: '16px' }}>
                            {currentType?.name || 'Загрузка...'}
                        </h2>

                        {/* Уже выбранный компонент */}
                        {selectedComponents[currentType?.name] && (
                            <div style={{
                                background: '#1e2129',
                                padding: '16px',
                                borderRadius: '12px',
                                marginBottom: '20px'
                            }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <div>
                                        <span style={{ color: '#22c55e' }}>✓ Выбрано:</span>
                                        <span style={{ fontWeight: 'bold', marginLeft: '8px' }}>
                                            {selectedComponents[currentType?.name].name}
                                        </span>
                                        <span style={{ color: '#c084fc', marginLeft: '12px' }}>
                                            {selectedComponents[currentType?.name].price?.toLocaleString()} ₽
                                        </span>
                                    </div>
                                    <button
                                        onClick={() => removeComponent(currentType?.name)}
                                        className="btn-danger btn-sm"
                                    >
                                        Заменить
                                    </button>
                                </div>
                            </div>
                        )}

                        {/* Список доступных товаров */}
                        <h3 style={{ marginBottom: '12px', fontSize: '16px' }}>Доступные компоненты:</h3>

                        {loading ? (
                            <div style={{ textAlign: 'center', padding: '40px' }}>Загрузка...</div>
                        ) : (
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                                {availableProducts.map(product => (
                                    <div
                                        key={product.id}
                                        style={{
                                            display: 'flex',
                                            justifyContent: 'space-between',
                                            alignItems: 'center',
                                            padding: '16px',
                                            background: '#15181f',
                                            borderRadius: '12px',
                                            border: selectedComponents[currentType?.name]?.id === product.id
                                                ? '2px solid #c084fc'
                                                : '1px solid #2a2d36'
                                        }}
                                    >
                                        <div>
                                            <div style={{ fontWeight: 'bold' }}>{product.name}</div>
                                            <div style={{ fontSize: '12px', color: '#9ca3af' }}>
                                                {product.categoryName}
                                            </div>
                                        </div>
                                        <div style={{ textAlign: 'right' }}>
                                            <div style={{ color: '#c084fc', fontWeight: 'bold' }}>
                                                {product.price?.toLocaleString()} ₽
                                            </div>
                                            <button
                                                onClick={() => selectComponent(product)}
                                                disabled={selectedComponents[currentType?.name]?.id === product.id}
                                                className="btn-primary btn-sm"
                                                style={{ marginTop: '8px' }}
                                            >
                                                {selectedComponents[currentType?.name]?.id === product.id
                                                    ? 'Выбрано'
                                                    : 'Выбрать'}
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}

                        {/* Навигация по шагам */}
                        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '32px' }}>
                            <button
                                onClick={prevStep}
                                disabled={step === 1}
                                className="btn-outline"
                            >
                                ← Назад
                            </button>
                            <button
                                onClick={nextStep}
                                disabled={step === totalSteps}
                                className="btn-primary"
                            >
                                Далее →
                            </button>
                        </div>
                    </div>
                </div>

                {/* Боковая панель */}
                <div>
                    {/* Выбранные компоненты */}
                    <div style={{
                        background: '#15181f',
                        borderRadius: '16px',
                        padding: '20px',
                        position: 'sticky',
                        top: '100px'
                    }}>
                        <h3 style={{ marginBottom: '16px' }}>📋 Ваша сборка</h3>

                        <div style={{ marginBottom: '16px' }}>
                            <input
                                type="text"
                                value={buildName}
                                onChange={(e) => setBuildName(e.target.value)}
                                placeholder="Название сборки"
                                style={{
                                    width: '100%',
                                    padding: '8px 12px',
                                    background: '#0a0c10',
                                    border: '1px solid #3f434e',
                                    borderRadius: '8px',
                                    color: 'white'
                                }}
                            />
                        </div>

                        <div style={{ marginBottom: '16px' }}>
                            <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                                <input
                                    type="checkbox"
                                    checked={isPublic}
                                    onChange={(e) => setIsPublic(e.target.checked)}
                                />
                                <span>Опубликовать сборку в каталоге</span>
                            </label>
                        </div>

                        <div style={{ borderTop: '1px solid #2a2d36', margin: '16px 0' }} />

                        {Object.entries(selectedComponents).map(([type, component]) => (
                            <div key={type} style={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                marginBottom: '12px',
                                fontSize: '14px'
                            }}>
                                <span style={{ color: '#9ca3af' }}>{type}:</span>
                                <span style={{ textAlign: 'right' }}>
                                    <div>{component.name}</div>
                                    <div style={{ color: '#c084fc' }}>
                                        {component.price?.toLocaleString()} ₽
                                    </div>
                                </span>
                            </div>
                        ))}

                        <div style={{ borderTop: '1px solid #2a2d36', margin: '16px 0', paddingTop: '16px' }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                                <span>Итого:</span>
                                <span style={{ fontSize: '24px', fontWeight: 'bold', color: '#c084fc' }}>
                                    {totalPrice.toLocaleString()} ₽
                                </span>
                            </div>
                        </div>

                        {/* Предупреждения о совместимости */}
                        {compatibilityWarnings.length > 0 && (
                            <div style={{
                                background: '#7c2d12',
                                padding: '12px',
                                borderRadius: '8px',
                                marginBottom: '16px',
                                fontSize: '12px'
                            }}>
                                {compatibilityWarnings.map((w, i) => (
                                    <div key={i}>{w}</div>
                                ))}
                            </div>
                        )}

                        {/* Калькулятор БП */}
                        <div style={{
                            background: '#1e2129',
                            padding: '12px',
                            borderRadius: '8px',
                            marginBottom: '16px',
                            fontSize: '12px'
                        }}>
                            <div style={{ fontWeight: 'bold', marginBottom: '8px' }}>⚡ Энергопотребление</div>
                            <div>Расчётное: ~{powerSupplyCalc.estimated}W</div>
                            <div>Рекомендуемый БП: {powerSupplyCalc.recommended}W</div>
                            {powerSupplyCalc.warning && (
                                <div style={{ color: '#f59e0b', marginTop: '8px' }}>
                                    {powerSupplyCalc.warning}
                                </div>
                            )}
                        </div>

                        {/* Кнопки действий */}
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            <button onClick={saveBuild} disabled={saving} className="btn-primary">
                                {saving ? 'Сохранение...' : '💾 Сохранить сборку'}
                            </button>
                            <button
                                onClick={addAllToCart}
                                disabled={Object.keys(selectedComponents).length === 0}
                                className="btn-outline"
                            >
                                🛒 Добавить все в корзину
                            </button>
                            <button onClick={getConsultantHelp} className="btn-outline">
                                💬 Помощь со сборкой
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}