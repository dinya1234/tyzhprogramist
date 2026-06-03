/**
 * Формирует строки характеристик товара из полей API (таблица products).
 * short_description в БД часто содержит технические параметры через запятую.
 */

function inferLabelFromPart(part) {
    const p = part.trim();
    const rules = [
        [/^LGA\s*\d+/i, 'Сокет'],
        [/^AM[45]\d*/i, 'Сокет'],
        [/^sTRX\d/i, 'Сокет'],
        [/^\d+\s*(?:ядер|ядро)/i, 'Количество ядер'],
        [/^\d+\s*поток/i, 'Количество потоков'],
        [/до\s+[\d.,]+\s*ГГц/i, 'Макс. частота'],
        [/[\d.,]+\s*ГГц/i, 'Частота'],
        [/^\d+\s*ГБ/i, 'Объём памяти'],
        [/^\d+\s*ТБ/i, 'Объём'],
        [/DDR[345]/i, 'Тип памяти'],
        [/NVMe|SSD|HDD|M\.2/i, 'Тип накопителя'],
        [/^\d+\s*(?:Вт|W)\b/i, 'Мощность'],
        [/PCIe/i, 'Интерфейс'],
        [/^\d+["″]/, 'Диагональ'],
        [/IPS|OLED|VA|TN|QD-OLED/i, 'Тип матрицы'],
        [/встроенн/i, 'Встроенная графика'],
        [/Wi-?Fi|Bluetooth/i, 'Беспроводная связь'],
        [/ATX|Micro-ATX|Mini-ITX|E-ATX/i, 'Форм-фактор'],
        [/^\d+\s*мм/i, 'Размер'],
        [/RGB|ARGB/i, 'Подсветка'],
        [/H\d{3}|Z\d{3}|B\d{3}|X\d{3}/i, 'Чипсет'],
        [/^\d+\s*x\s*\d+/i, 'Разрешение'],
        [/G-Sync|FreeSync/i, 'Синхронизация'],
        [/Hz|Гц/i, 'Частота обновления'],
    ];

    for (const [regex, label] of rules) {
        if (regex.test(p)) return label;
    }

    if (/^(Процессор|Видеокарта|Материнская плата|Оперативная память|Блок питания|Корпус|Накопитель|Клавиатура|Мышь|Монитор|Ноутбук|Кулер|SSD|HDD)/i.test(p)) {
        return 'Модель';
    }

    return null;
}

function parseShortDescriptionSpecs(shortDescription, productName) {
    if (!shortDescription) return [];

    const parts = shortDescription.split(/,\s*/).map(s => s.trim()).filter(Boolean);
    const usedLabels = new Set();
    const rows = [];

    parts.forEach((part, index) => {
        const normalizedPart = part.toLowerCase();
        const normalizedName = (productName || '').toLowerCase();
        if (normalizedName && (normalizedPart === normalizedName || normalizedPart.includes(normalizedName))) {
            return;
        }

        let label = inferLabelFromPart(part);
        if (!label) {
            label = index === 0 ? 'Описание' : `Параметр ${index}`;
        }

        let finalLabel = label;
        let counter = 2;
        while (usedLabels.has(finalLabel)) {
            finalLabel = `${label} (${counter++})`;
        }
        usedLabels.add(finalLabel);
        rows.push({ label: finalLabel, value: part });
    });

    return rows;
}

function formatPrice(price) {
    if (price == null) return null;
    return Number(price).toLocaleString('ru-RU') + ' ₽';
}

function formatDate(dateStr) {
    if (!dateStr) return null;
    try {
        return new Date(dateStr).toLocaleDateString('ru-RU');
    } catch {
        return null;
    }
}

/**
 * @param {object} product — ProductFullResponse с бэкенда
 * @param {{ includeCommerce?: boolean }} options
 * @returns {{ label: string, value: string }[]}
 */
export function getProductSpecRows(product, options = {}) {
    if (!product) return [];

    const { includeCommerce = false } = options;
    const rows = [];
    const seen = new Set();

    const add = (label, value) => {
        if (value == null || value === '' || seen.has(label)) return;
        seen.add(label);
        rows.push({ label, value: String(value) });
    };

    parseShortDescriptionSpecs(product.shortDescription, product.name).forEach(row => {
        if (!seen.has(row.label)) {
            seen.add(row.label);
            rows.push(row);
        }
    });

    add('Наименование', product.name);
    add('Артикул', product.sku);
    add('Категория', product.categoryName);
    add('Гарантия', `${product.warrantyMonths ?? 12} мес.`);

    if (product.weight != null) {
        add('Вес', `${product.weight} кг`);
    }

    if (product.quantity != null) {
        add('Наличие', product.quantity > 0 ? `В наличии (${product.quantity} шт.)` : 'Нет в наличии');
    }

    if (product.rating != null) {
        add('Рейтинг', `${Number(product.rating).toFixed(1)} / 5`);
    }

    if (includeCommerce) {
        add('Цена', formatPrice(product.price));
        if (product.oldPrice != null && Number(product.oldPrice) > Number(product.price)) {
            add('Старая цена', formatPrice(product.oldPrice));
        }
    }

    const created = formatDate(product.createdAt);
    if (created) add('Дата появления в каталоге', created);

    if (product.viewsCount != null && product.viewsCount > 0) {
        add('Просмотры', product.viewsCount);
    }

    if (product.purchaseCount != null && product.purchaseCount > 0) {
        add('Количество покупок', product.purchaseCount);
    }

    const badges = [];
    if (product.isNew) badges.push('Новинка');
    if (product.isBestseller) badges.push('Хит продаж');
    if (badges.length) add('Метки', badges.join(', '));

    return rows;
}

/** Объект label → value (для страницы сравнения) */
export function getProductSpecsMap(product) {
    const map = {};
    getProductSpecRows(product, { includeCommerce: true }).forEach(({ label, value }) => {
        map[label] = value;
    });
    return map;
}
