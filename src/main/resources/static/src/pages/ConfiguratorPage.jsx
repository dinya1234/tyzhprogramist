// src/pages/ConfiguratorPage.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { pcBuilds, componentTypes, products, categories, comparisons, relations } from '../services/api';
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
    const [buildCreating, setBuildCreating] = useState(false);
    const [saving, setSaving] = useState(false);
    const [categoryTree, setCategoryTree] = useState([]);

    // Проверка совместимости
    const [compatibilityWarnings, setCompatibilityWarnings] = useState([]);
    const [powerSupplyCalc, setPowerSupplyCalc] = useState({
        estimated: 0,
        recommended: 0,
        warning: null
    });

    const normalize = (value) =>
        String(value || '')
            .toLowerCase()
            .replace(/ё/g, 'е')
            .replace(/[^a-zа-я0-9]+/g, ' ')
            .trim();

    const resolveTypeName = (label) => {
        if (!label) return label;
        const labelNorm = normalize(label);
        const exact = componentTypesList.find((t) => normalize(t.name) === labelNorm);
        if (exact) return exact.name;

        const aliases = {
            'процессор': ['процессор', 'процессоры', 'cpu'],
            'материнская плата': ['материнская плата', 'материнские платы', 'motherboard'],
            'оперативная память': ['оперативная память', 'озу', 'ram'],
            'видеокарта': ['видеокарта', 'видеокарты', 'gpu'],
            'накопитель': ['накопитель', 'накопители', 'ssd', 'hdd', 'storage'],
            'блок питания': ['блок питания', 'блоки питания', 'psu', 'power supply'],
            'корпус': ['корпус', 'корпуса', 'case'],
            'охлаждение': ['охлаждение', 'кулер', 'cooler']
        };

        const matchedEntry = Object.entries(aliases).find(([, values]) =>
            values.some((v) => labelNorm.includes(normalize(v)) || normalize(v).includes(labelNorm))
        );
        if (!matchedEntry) return label;

        const [canonical] = matchedEntry;
        const matchedType = componentTypesList.find((t) => normalize(t.name) === normalize(canonical));
        return matchedType?.name || label;
    };

    const extractWattage = (...texts) => {
        const source = texts
            .filter(Boolean)
            .map((t) => String(t))
            .join(' ')
            .replace(/&nbsp;/gi, ' ');
        if (!source) return null;

        // 650W / 650 W / 650Вт / 650 ватт
        const wattMatch = source.match(/(\d{2,4})(?:[.,]\d+)?\s*(?:w|вт|ватт)\b/i);
        if (wattMatch) {
            return Math.round(Number(wattMatch[1]));
        }

        // 0.75kW / 0,75 кВт / 1 kW
        const kwMatch = source.match(/(\d+(?:[.,]\d+)?)\s*(?:kw|квт)\b/i);
        if (kwMatch) {
            const kw = Number(String(kwMatch[1]).replace(',', '.'));
            if (!Number.isNaN(kw)) {
                return Math.round(kw * 1000);
            }
        }

        return null;
    };

    const estimateComponentPower = (component) => {
        const nameNorm = normalize(component?.name);
        const typeNorm = normalize(component?.typeName || component?.categoryName);
        const combined = `${typeNorm} ${nameNorm}`;

        if (combined.includes('видеокарта') || combined.includes('gpu') || combined.includes('rtx') || combined.includes('rx ')) {
            if (combined.includes('4090')) return 450;
            if (combined.includes('4080') || combined.includes('7900')) return 320;
            if (combined.includes('4070') || combined.includes('7800')) return 250;
            if (combined.includes('4060') || combined.includes('7700')) return 170;
            return 220;
        }

        if (combined.includes('процессор') || combined.includes('cpu') || combined.includes('ryzen') || combined.includes('core i')) {
            if (combined.includes('i9') || combined.includes('ryzen 9')) return 170;
            if (combined.includes('i7') || combined.includes('ryzen 7')) return 125;
            if (combined.includes('i5') || combined.includes('ryzen 5')) return 95;
            return 105;
        }

        if (combined.includes('материнская')) return 55;
        if (combined.includes('оператив') || combined.includes('озу') || combined.includes('ram')) return 12;
        if (combined.includes('накопитель') || combined.includes('ssd')) return 8;
        if (combined.includes('hdd')) return 12;
        if (combined.includes('охлаждение') || combined.includes('кулер')) return 8;
        if (combined.includes('корпус')) return 6;
        return 20;
    };

    // Загрузка типов компонентов
    useEffect(() => {
        loadComponentTypes();
    }, []);

    // Загрузка дерева категорий (нужно, чтобы корректно фильтровать товары по categoryId)
    useEffect(() => {
        categories.getTree()
            .then(res => setCategoryTree(res.data || []))
            .catch(err => {
                console.error('Ошибка загрузки категорий:', err);
                setCategoryTree([]);
            });
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

    useEffect(() => {
        if (!componentTypesList.length || !Object.keys(selectedComponents).length) return;

        const remapped = {};
        Object.values(selectedComponents).forEach((component) => {
            const resolvedTypeName = resolveTypeName(component.typeName || component.categoryName);
            remapped[resolvedTypeName] = { ...component, typeName: resolvedTypeName };
        });
        setSelectedComponents(remapped);
    }, [componentTypesList]);

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
            const typeName = type?.name?.trim();

            const flattenCategories = (nodes) => {
                const out = [];
                const stack = Array.isArray(nodes) ? [...nodes] : [];
                while (stack.length) {
                    const n = stack.shift();
                    if (!n) continue;
                    out.push(n);
                    if (Array.isArray(n.children) && n.children.length) {
                        stack.unshift(...n.children);
                    }
                }
                return out;
            };

            const typeNorm = normalize(typeName);
            const allCats = flattenCategories(categoryTree);

            const explicitMap = {
                'процессор': ['процессор', 'cpu'],
                'материнская плата': ['материнская плата', 'материнские платы', 'motherboard'],
                'оперативная память': ['оперативная память', 'озу', 'ram'],
                'видеокарта': ['видеокарта', 'gpu', 'видеокарты'],
                'накопитель': ['накопитель', 'ssd', 'hdd', 'storage', 'диск'],
                'блок питания': ['блок питания', 'psu'],
                'охлаждение': ['охлаждение', 'кулер', 'cooler'],
                'корпус': ['корпус', 'case']
            };

            const findCategoryIdForType = () => {
                if (!typeNorm) return null;

                // 1) Пробуем прямое совпадение по подстроке в названии категории
                const direct = allCats.find(c => normalize(c.name).includes(typeNorm) || typeNorm.includes(normalize(c.name)));
                if (direct?.id) return direct.id;

                // 2) Пробуем по словарю синонимов
                const entry = Object.entries(explicitMap).find(([k]) => normalize(k) === typeNorm)
                    || Object.entries(explicitMap).find(([, variants]) => variants.some(v => typeNorm.includes(normalize(v)) || normalize(v).includes(typeNorm)));
                if (entry) {
                    const [, variants] = entry;
                    const found = allCats.find(c => variants.some(v => normalize(c.name).includes(normalize(v))));
                    if (found?.id) return found.id;
                }

                return null;
            };

            const categoryId = findCategoryIdForType();

            let response;
            if (categoryId) {
                response = await products.getByCategory(categoryId, { size: 50 });
            } else {
                // Фоллбек: если категорию не удалось сопоставить, хотя бы покажем релевантное поиском
                response = await products.search(typeName || '', { size: 50 });
            }

            setAvailableProducts(response.data.content || response.data || []);
        } catch (error) {
            console.error('Ошибка загрузки товаров:', error);
            setAvailableProducts([]);
        } finally {
            setLoading(false);
        }
    };

    const createNewBuild = async () => {
        if (buildCreating) return null;
        setBuildCreating(true);
        try {
            const baseName = (buildName || 'Моя сборка').trim();
            for (let attempt = 1; attempt <= 12; attempt += 1) {
                let candidateName = attempt === 1 ? baseName : `${baseName} (${attempt})`;
                if (attempt > 10) {
                    candidateName = `${baseName} ${Date.now().toString().slice(-5)}`;
                }

                try {
                    const response = await pcBuilds.create({
                        name: candidateName,
                        isPublic: false
                    });
                    const newId = response.data.id;
                    setCurrentBuildId(newId);
                    if (candidateName !== buildName) {
                        setBuildName(candidateName);
                    }
                    return newId;
                } catch (error) {
                    const message = String(error?.response?.data?.message || '').toLowerCase();
                    const isDuplicateName = error?.response?.status === 400 && message.includes('сборка с таким именем');
                    if (!isDuplicateName || attempt === 12) {
                        console.error('Ошибка создания сборки:', error);
                        return null;
                    }
                }
            }
            return null;
        } finally {
            setBuildCreating(false);
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
                const typeName = resolveTypeName(comp.componentType);
                components[typeName] = {
                    id: comp.productId,
                    name: comp.productName,
                    price: comp.price,
                    quantity: comp.quantity,
                    typeName,
                    categoryName: comp.componentType
                };
            });

            const withDetailsEntries = await Promise.all(
                Object.entries(components).map(async ([typeName, component]) => {
                    try {
                        const detailsRes = await products.getById(component.id);
                        return [typeName, {
                            ...component,
                            shortDescription: detailsRes.data?.shortDescription,
                            fullDescription: detailsRes.data?.fullDescription
                        }];
                    } catch (e) {
                        return [typeName, component];
                    }
                })
            );
            setSelectedComponents(Object.fromEntries(withDetailsEntries));
        } catch (error) {
            console.error('Ошибка загрузки сборки:', error);
        }
    };

    const selectComponent = async (product) => {
        let buildId = currentBuildId;
        if (!buildId) {
            buildId = await createNewBuild();
        }
        if (!buildId) {
            alert('Не удалось создать сборку. Проверьте авторизацию и попробуйте снова.');
            return;
        }

        setLoading(true);
        try {
            await pcBuilds.addComponent(buildId, product.id, 1);
            let details = null;
            try {
                const detailsRes = await products.getById(product.id);
                details = detailsRes.data || null;
            } catch (e) {
                details = null;
            }

            setSelectedComponents(prev => ({
                ...prev,
                [currentType.name]: {
                    id: product.id,
                    name: product.name,
                    price: product.price,
                    quantity: 1,
                    typeName: currentType.name,
                    categoryName: product.categoryName,
                    shortDescription: details?.shortDescription,
                    fullDescription: details?.fullDescription
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

    const checkCompatibility = async () => {
        const components = Object.values(selectedComponents);
        if (components.length < 2) {
            setCompatibilityWarnings([]);
            return;
        }

        const warnings = [];
        for (let i = 0; i < components.length; i += 1) {
            for (let j = i + 1; j < components.length; j += 1) {
                const first = components[i];
                const second = components[j];
                if (!first?.id || !second?.id) continue;
                try {
                    const res = await relations.areProductsCompatible(first.id, second.id);
                    if (res.data === false) {
                        warnings.push(`⚠️ ${first.name} и ${second.name} несовместимы`);
                    }
                } catch (error) {
                    console.error('Ошибка проверки совместимости:', error);
                }
            }
        }

        setCompatibilityWarnings(warnings);
    };

    const calculatePowerSupply = () => {
        let estimated = 70; // база: мать + вентиляторы + периферия
        Object.values(selectedComponents).forEach((comp) => {
            estimated += estimateComponentPower(comp) * (comp.quantity || 1);
        });

        const recommended = Math.ceil(estimated * 1.3);
        let warning = null;

        const psu = Object.values(selectedComponents).find((c) =>
            normalize(c.typeName || c.categoryName).includes('блок питания')
        );

        if (psu) {
            const psuPower = extractWattage(psu.name, psu.shortDescription, psu.fullDescription);
            if (psuPower && psuPower < recommended) {
                warning = `⚠️ Блок питания (${psuPower}W) может быть недостаточным. Рекомендуется ${recommended}W`;
            }
            if (!psuPower) {
                warning = `⚠️ Не удалось определить мощность выбранного БП по названию. Требуется не менее ${recommended}W`;
            }
        } else if (Object.keys(selectedComponents).length > 0) {
            warning = `⚠️ Блок питания не выбран. Для этой сборки нужен БП от ${recommended}W`;
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

    const showCompatibilityReport = () => {
        if (compatibilityWarnings.length === 0) {
            alert('✅ Явных проблем совместимости не найдено (проверка по правилам совместимости товаров).');
            return;
        }
        alert(compatibilityWarnings.join('\n'));
    };

    const showPowerReport = () => {
        const lines = [
            `Расчётное потребление: ~${powerSupplyCalc.estimated}W`,
            `Рекомендуемый БП: ${powerSupplyCalc.recommended}W`
        ];
        if (powerSupplyCalc.warning) lines.push(powerSupplyCalc.warning);
        alert(lines.join('\n'));
    };

    const compareThisBuild = async () => {
        if (!currentBuildId) return;
        try {
            const comparisonName = 'Мои сборки';
            await comparisons.createComparison(comparisonName).catch(() => {});
            await comparisons.addToComparison(comparisonName, 'PcBuild', currentBuildId);
            navigate('/comparison');
        } catch (e) {
            alert(e.response?.data?.message || 'Не удалось добавить сборку в сравнение');
        }
    };

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
                        {buildCreating && (
                            <div style={{ marginBottom: 12, fontSize: 12, color: '#9ca3af' }}>
                                Создаём сборку...
                            </div>
                        )}

                        <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '14px' }}>
                            <button className="btn-outline btn-sm" onClick={showCompatibilityReport}>
                                ✅ Проверить совместимость
                            </button>
                            <button className="btn-outline btn-sm" onClick={showPowerReport}>
                                ⚡ Проверить мощность
                            </button>
                            <button className="btn-outline btn-sm" onClick={compareThisBuild}>
                                📊 Сравнить сборки
                            </button>
                        </div>

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