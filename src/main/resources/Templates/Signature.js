<script>
// Инициализация
document.addEventListener('DOMContentLoaded', () => {
    // Променливи
    const canvas = document.getElementById('signatureCanvas');
    const ctx = canvas.getContext('2d');
    const signaturePlaceholder = document.getElementById('signaturePlaceholder');
    const signaturePreview = document.getElementById('signaturePreview');
    const clearSignatureBtn = document.getElementById('clearSignature');
    const undoSignatureBtn = document.getElementById('undoSignature');
    const saveSignatureBtn = document.getElementById('saveSignature');
    const recipientType = document.getElementById('recipientType');
    const additionalFields = document.getElementById('additionalFields');
    const deliveryNotes = document.getElementById('deliveryNotes');
    const verifyInfo = document.getElementById('verifyInfo');
    const agreeTerms = document.getElementById('agreeTerms');
    const confirmBtn = document.getElementById('confirmBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const closeModalBtn = document.getElementById('closeModal');
    const successModal = document.getElementById('successModal');
    const closeSuccessModalBtn = document.getElementById('closeSuccessModal');

    let isDrawing = false;
    let lastX = 0;
    let lastY = 0;
    let drawingHistory = [];
    let currentDrawing = [];

    // Инициализация на canvas
    function initCanvas() {
        // Настройки на canvas
        const rect = canvas.getBoundingClientRect();
        canvas.width = rect.width;
        canvas.height = rect.height;

        // Настройки на контекста
        ctx.lineWidth = 2;
        ctx.lineCap = 'round';
        ctx.lineJoin = 'round';
        ctx.strokeStyle = '#13ec25';

        // Изчистване на canvas
        clearCanvas();

        // Събития за рисуване
        canvas.addEventListener('mousedown', startDrawing);
        canvas.addEventListener('mousemove', draw);
        canvas.addEventListener('mouseup', stopDrawing);
        canvas.addEventListener('mouseout', stopDrawing);

        // Touch events за мобилни устройства
        canvas.addEventListener('touchstart', handleTouchStart, { passive: false });
        canvas.addEventListener('touchmove', handleTouchMove, { passive: false });
        canvas.addEventListener('touchend', stopDrawing);

        // Преоразмеряване на canvas при промяна на размера на прозореца
        window.addEventListener('resize', resizeCanvas);
    }

    function resizeCanvas() {
        // Запазване на текущия чертеж
        const currentImage = canvas.toDataURL();
        const img = new Image();

        // Преоразмеряване на canvas
        const rect = canvas.getBoundingClientRect();
        const oldWidth = canvas.width;
        const oldHeight = canvas.height;

        canvas.width = rect.width;
        canvas.height = rect.height;

        // Възстановяване на чертежа с правилни пропорции
        if (drawingHistory.length > 0) {
            img.onload = () => {
                const scaleX = canvas.width / oldWidth;
                const scaleY = canvas.height / oldHeight;

                // Мащабиране на историята
                drawingHistory = drawingHistory.map(path =>
                    path.map(point => ({
                        x: point.x * scaleX,
                        y: point.y * scaleY
                    }))
                );

                // Прерисуване
                redrawCanvas();
            };
            img.src = currentImage;
        } else {
            clearCanvas();
        }
    }

    // Функции за рисуване
    function startDrawing(e) {
        isDrawing = true;
        const coords = getCoordinates(e);
        [lastX, lastY] = [coords.x, coords.y];
        currentDrawing = [{x: lastX, y: lastY}];
        signaturePlaceholder.classList.add('hidden');
        signaturePreview.classList.remove('hidden');

        // Увеличаване дебелината на линията за touch устройства
        if (e.type.includes('touch')) {
            ctx.lineWidth = 3;
        }
    }

    function draw(e) {
        if (!isDrawing) return;

        e.preventDefault();
        const coords = getCoordinates(e);

        // Адаптиране на дебелината според скоростта на рисуване
        const speed = Math.sqrt(
            Math.pow(coords.x - lastX, 2) +
            Math.pow(coords.y - lastY, 2)
        );

        if (e.type.includes('touch')) {
            ctx.lineWidth = Math.max(2, Math.min(5, 6 - speed * 0.1));
        }

        ctx.beginPath();
        ctx.moveTo(lastX, lastY);
        ctx.lineTo(coords.x, coords.y);
        ctx.stroke();

        currentDrawing.push({x: coords.x, y: coords.y});
        [lastX, lastY] = [coords.x, coords.y];
    }

    function stopDrawing() {
        if (!isDrawing) return;
        isDrawing = false;
        if (currentDrawing.length > 1) {
            drawingHistory.push([...currentDrawing]);
            validateForm(); // Валидираме формата след като потребителят е подписал
        }
        // Връщаме стандартна дебелина
        ctx.lineWidth = 2;
    }

    // Touch handlers
    function handleTouchStart(e) {
        e.preventDefault();
        const touch = e.touches[0];
        const mouseEvent = new MouseEvent('mousedown', {
            clientX: touch.clientX,
            clientY: touch.clientY
        });
        canvas.dispatchEvent(mouseEvent);
    }

    function handleTouchMove(e) {
        e.preventDefault();
        const touch = e.touches[0];
        const mouseEvent = new MouseEvent('mousemove', {
            clientX: touch.clientX,
            clientY: touch.clientY
        });
        canvas.dispatchEvent(mouseEvent);
    }

    function getCoordinates(e) {
        const rect = canvas.getBoundingClientRect();
        let clientX, clientY;

        if (e.type.includes('touch')) {
            clientX = e.touches[0].clientX;
            clientY = e.touches[0].clientY;
        } else {
            clientX = e.clientX;
            clientY = e.clientY;
        }

        // Изчисляваме координати спрямо canvas
        const scaleX = canvas.width / rect.width;
        const scaleY = canvas.height / rect.height;

        return {
            x: (clientX - rect.left) * scaleX,
            y: (clientY - rect.top) * scaleY
        };
    }

    // Изчистване на подписа
    function clearCanvas() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = document.documentElement.classList.contains('dark') ? '#0d1a0e' : '#ffffff';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        drawingHistory = [];
        currentDrawing = [];
        signaturePlaceholder.classList.remove('hidden');
        signaturePreview.classList.add('hidden');
        validateForm(); // Валидираме формата след изчистване
    }

    // Отмяна на последно действие
    function undoLast() {
        if (drawingHistory.length === 0) return;

        drawingHistory.pop();
        redrawCanvas();

        if (drawingHistory.length === 0) {
            signaturePlaceholder.classList.remove('hidden');
            signaturePreview.classList.add('hidden');
        }
        validateForm();
    }

    function redrawCanvas() {
        // Изчистване
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = document.documentElement.classList.contains('dark') ? '#0d1a0e' : '#ffffff';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        // Прерисуване на всички пътища
        drawingHistory.forEach(path => {
            if (path.length < 2) return;

            ctx.beginPath();
            ctx.moveTo(path[0].x, path[0].y);

            for (let i = 1; i < path.length; i++) {
                ctx.lineTo(path[i].x, path[i].y);
            }

            ctx.stroke();
        });
    }

    // Запазване на подписа
    function saveSignature() {
        if (drawingHistory.length === 0) {
            showNotification('Моля, създайте подпис преди да го запазите', 'warning');
            return;
        }

        try {
            const signatureData = canvas.toDataURL('image/png');
            localStorage.setItem('lastSignature', signatureData);

            // Създаване на линк за изтегляне
            const link = document.createElement('a');
            link.download = `signature-${Date.now()}.png`;
            link.href = signatureData;

            // Симулиране на клик
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            showNotification('Подписът е запазен успешно!', 'success');
        } catch (error) {
            console.error('Грешка при запазване на подписа:', error);
            showNotification('Възникна грешка при запазване на подписа', 'error');
        }
    }

    // Показване на допълнителни полета за получател
    function toggleAdditionalFields() {
        const value = recipientType.value;
        if (value && value !== 'personal') {
            additionalFields.classList.remove('hidden');
        } else {
            additionalFields.classList.add('hidden');
        }
        validateForm();
    }

    // Валидация на формата
    function validateForm() {
        const isSignatureValid = drawingHistory.length > 0;
        const isRecipientValid = recipientType.value !== '';
        const isVerified = verifyInfo.checked && agreeTerms.checked;

        const isValid = isSignatureValid && isRecipientValid && isVerified;

        // Актуализиране на състоянието на бутона
        confirmBtn.disabled = !isValid;

        // Добавяне/премахване на визуални индикатори
        if (isValid) {
            confirmBtn.classList.remove('opacity-50');
            confirmBtn.classList.add('hover:brightness-110');
        } else {
            confirmBtn.classList.add('opacity-50');
            confirmBtn.classList.remove('hover:brightness-110');
        }

        return isValid;
    }

    // Показване на известия
    function showNotification(message, type = 'info') {
        // Създаване на контейнер за известия, ако не съществува
        let notificationContainer = document.getElementById('notification-container');
        if (!notificationContainer) {
            notificationContainer = document.createElement('div');
            notificationContainer.id = 'notification-container';
            notificationContainer.className = 'fixed top-4 right-4 z-50 space-y-2';
            document.body.appendChild(notificationContainer);
        }

        // Създаване на известието
        const notification = document.createElement('div');
        notification.className = `px-4 py-3 rounded-lg shadow-lg flex items-center gap-3 animate-slideIn ${
            type === 'success' ? 'bg-green-600 text-white' :
            type === 'warning' ? 'bg-yellow-500 text-white' :
            type === 'error' ? 'bg-red-600 text-white' :
            'bg-primary text-black'
        }`;

        notification.innerHTML = `
            <span class="material-symbols-outlined text-lg">
                ${type === 'success' ? 'check_circle' :
                  type === 'warning' ? 'warning' :
                  type === 'error' ? 'error' : 'info'}
            </span>
            <span class="font-bold">${message}</span>
        `;

        notificationContainer.appendChild(notification);

        // Премахване на известието след 4 секунди
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100px)';
            notification.style.transition = 'all 0.3s ease';

            setTimeout(() => {
                notification.remove();
                // Премахване на контейнера, ако няма повече известия
                if (notificationContainer.children.length === 0) {
                    notificationContainer.remove();
                }
            }, 300);
        }, 4000);
    }

    // Потвърждаване на доставката
    function confirmDelivery() {
        if (!validateForm()) {
            showNotification('Моля, попълнете всички задължителни полета', 'warning');
            return;
        }

        // Симулиране на заявка
        const originalText = confirmBtn.innerHTML;
        const originalState = confirmBtn.disabled;

        confirmBtn.innerHTML = `
            <span class="material-symbols-outlined animate-spin">refresh</span>
            <span>Обработване...</span>
        `;
        confirmBtn.disabled = true;

        // Симулиране на API заявка
        setTimeout(() => {
            // Възстановяване на бутона
            confirmBtn.innerHTML = originalText;
            confirmBtn.disabled = originalState;

            // Показване на успешен модал
            successModal.classList.remove('hidden');

            // Запазване на данните в localStorage
            const deliveryData = {
                timestamp: new Date().toISOString(),
                shipmentId: 'LE-88231',
                recipientType: recipientType.value,
                notes: deliveryNotes.value,
                signature: canvas.toDataURL('image/png')
            };

            try {
                const deliveries = JSON.parse(localStorage.getItem('deliveries') || '[]');
                deliveries.push(deliveryData);
                localStorage.setItem('deliveries', JSON.stringify(deliveries));
                localStorage.setItem('lastDelivery', JSON.stringify(deliveryData));
            } catch (error) {
                console.error('Грешка при запазване на данните:', error);
            }

            showNotification('Доставката е успешно потвърдена!', 'success');
        }, 1500);
    }

    // Инициализация на събития
    function initEvents() {
        // Canvas events
        if (clearSignatureBtn) {
            clearSignatureBtn.addEventListener('click', clearCanvas);
        }

        if (undoSignatureBtn) {
            undoSignatureBtn.addEventListener('click', undoLast);
        }

        if (saveSignatureBtn) {
            saveSignatureBtn.addEventListener('click', saveSignature);
        }

        // Form events
        if (recipientType) {
            recipientType.addEventListener('change', toggleAdditionalFields);
        }

        if (verifyInfo) {
            verifyInfo.addEventListener('change', validateForm);
        }

        if (agreeTerms) {
            agreeTerms.addEventListener('change', validateForm);
        }

        // Textarea auto-resize
        if (deliveryNotes) {
            deliveryNotes.addEventListener('input', function() {
                this.style.height = 'auto';
                this.style.height = (this.scrollHeight) + 'px';
            });

            // Инициализиране на textarea височина
            deliveryNotes.style.height = 'auto';
            deliveryNotes.style.height = (deliveryNotes.scrollHeight) + 'px';
        }

        // Button events
        if (confirmBtn) {
            confirmBtn.addEventListener('click', confirmDelivery);
        }

        if (cancelBtn) {
            cancelBtn.addEventListener('click', () => {
                if (drawingHistory.length > 0 || (deliveryNotes && deliveryNotes.value.trim() !== '')) {
                    if (confirm('Имате незапазени промени. Сигурни ли сте, че искате да затворите?')) {
                        window.location.href = '/dashboard';
                    }
                } else {
                    window.location.href = '/dashboard';
                }
            });
        }

        if (closeModalBtn) {
            closeModalBtn.addEventListener('click', () => {
                if (drawingHistory.length > 0 || (deliveryNotes && deliveryNotes.value.trim() !== '')) {
                    if (confirm('Имате незапазени промени. Сигурни ли сте, че искате да затворите?')) {
                        window.history.back();
                    }
                } else {
                    window.history.back();
                }
            });
        }

        if (closeSuccessModalBtn) {
            closeSuccessModalBtn.addEventListener('click', () => {
                successModal.classList.add('hidden');
                window.location.href = '/dashboard';
            });
        }

        // Затваряне с ESC
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                if (successModal && !successModal.classList.contains('hidden')) {
                    successModal.classList.add('hidden');
                    window.location.href = '/dashboard';
                } else if (drawingHistory.length > 0 || (deliveryNotes && deliveryNotes.value.trim() !== '')) {
                    if (confirm('Имате незапазени промени. Сигурни ли сте, че искате да затворите?')) {
                        window.history.back();
                    }
                } else {
                    window.history.back();
                }
            }
        });

        // Валидация при промяна на canvas
        canvas.addEventListener('mousedown', () => {
            setTimeout(validateForm, 100);
        });

        canvas.addEventListener('touchend', () => {
            setTimeout(validateForm, 100);
        });
    }

    // Зареждане на предишен подпис от localStorage
    function loadPreviousSignature() {
        const lastSignature = localStorage.getItem('lastSignature');
        if (lastSignature) {
            const img = new Image();
            img.onload = () => {
                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
                signaturePlaceholder.classList.add('hidden');
                signaturePreview.classList.remove('hidden');

                // Обработка на историята от изображението
                const tempCanvas = document.createElement('canvas');
                const tempCtx = tempCanvas.getContext('2d');
                tempCanvas.width = canvas.width;
                tempCanvas.height = canvas.height;
                tempCtx.drawImage(img, 0, 0);

                // Тук може да се добави логика за възстановяване на drawingHistory
                // от изображението (по-сложна имплементация)
            };
            img.src = lastSignature;
        }
    }

    // Инициализиране на приложението
    function initApp() {
        // Инициализиране на canvas
        if (canvas) {
            initCanvas();
        }

        // Инициализиране на събития
        initEvents();

        // Зареждане на предишен подпис
        setTimeout(loadPreviousSignature, 500);

        // Първоначална валидация
        setTimeout(validateForm, 100);

        // Фокус върху първото поле
        setTimeout(() => {
            if (recipientType) {
                recipientType.focus();
            }
        }, 300);
    }

    // Стартиране на приложението
    initApp();
});
</script>