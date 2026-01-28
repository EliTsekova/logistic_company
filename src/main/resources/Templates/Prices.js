<script>
    // Тема toggle
    const themeToggle = document.getElementById('themeToggle');
    const htmlElement = document.documentElement;

    // Проверка за запазена тема
    const savedTheme = localStorage.getItem('theme') || 'light';
    if (savedTheme === 'dark') {
        htmlElement.classList.add('dark');
        themeToggle.checked = true;
    } else {
        htmlElement.classList.add('light');
        themeToggle.checked = false;
    }

    // Тема toggle event
    themeToggle.addEventListener('change', () => {
        if (themeToggle.checked) {
            htmlElement.classList.remove('light');
            htmlElement.classList.add('dark');
            localStorage.setItem('theme', 'dark');
            showNotification('Тъмна тема е активирана', 'success');
        } else {
            htmlElement.classList.remove('dark');
            htmlElement.classList.add('light');
            localStorage.setItem('theme', 'light');
            showNotification('Светла тема е активирана', 'success');
        }
    });

    // Mobile menu toggle
    const mobileMenuButton = document.getElementById('mobileMenuButton');
    const mobileMenu = document.getElementById('mobileMenu');

    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
            // Промяна на иконата
            const icon = mobileMenuButton.querySelector('.material-symbols-outlined');
            if (mobileMenu.classList.contains('hidden')) {
                icon.textContent = 'menu';
            } else {
                icon.textContent = 'close';
            }
        });

        // Затваряне на мобилното меню при клик извън него
        document.addEventListener('click', (event) => {
            if (!mobileMenuButton.contains(event.target) && !mobileMenu.contains(event.target)) {
                mobileMenu.classList.add('hidden');
                mobileMenuButton.querySelector('.material-symbols-outlined').textContent = 'menu';
            }
        });
    }

    // Анимация при скрол
    window.addEventListener('scroll', () => {
        const header = document.querySelector('header');
        if (window.scrollY > 50) {
            header.classList.add('shadow-lg');
            header.classList.add('bg-white/95', 'dark:bg-background-dark/95');
        } else {
            header.classList.remove('shadow-lg');
            header.classList.remove('bg-white/95', 'dark:bg-background-dark/95');
        }
    });

    // Брояч за цените
    function animateCounters() {
        const counters = document.querySelectorAll('.counter');
        const speed = 200;

        counters.forEach(counter => {
            const updateCount = () => {
                const target = +counter.getAttribute('data-target');
                const count = +counter.innerText;
                const increment = target / speed;

                if (count < target) {
                    counter.innerText = Math.ceil(count + increment);
                    setTimeout(updateCount, 1);
                } else {
                    counter.innerText = target;
                }
            };

            updateCount();
        });
    }

    // Функция за показване на известия
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
        notification.className = `px-4 py-3 rounded-lg shadow-lg flex items-center gap-3 animate-fadeInUp ${
            type === 'success' ? 'bg-success text-white' :
            type === 'error' ? 'bg-danger text-white' :
            type === 'warning' ? 'bg-warning text-white' :
            'bg-primary text-white'
        }`;

        notification.innerHTML = `
            <span class="material-symbols-outlined">
                ${type === 'success' ? 'check_circle' :
                  type === 'error' ? 'error' :
                  type === 'warning' ? 'warning' : 'info'}
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

    // Бутони за избор на план
    document.querySelectorAll('button:contains("Избери")').forEach(button => {
        button.addEventListener('click', function() {
            const planTitle = this.closest('.price-card').querySelector('h3').textContent;
            showNotification(`План "${planTitle}" е избран`, 'success');

            // Анимация на бутона
            this.style.transform = 'scale(0.95)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 200);
        });
    });

    // Калкулатор за цени - демо функционалност
    document.querySelectorAll('.btn:contains("Калкулатор")').forEach(button => {
        button.addEventListener('click', function(e) {
            if (this.getAttribute('href') === '#') {
                e.preventDefault();
                showNotification('Калкулаторът за цени ще бъде достъпен скоро!', 'info');
            }
        });
    });

    // Активиране на анимации при зареждане
    document.addEventListener('DOMContentLoaded', () => {
        // Добавяне на класове за анимация
        const animatedElements = document.querySelectorAll('.animate-fadeInUp');
        animatedElements.forEach((el, index) => {
            el.style.animationDelay = `${index * 0.1}s`;
        });

        // Инициализиране на броячите, ако съществуват
        if (document.querySelectorAll('.counter').length > 0) {
            animateCounters();
        }

        // Проверка за промяна на системната тема
        const prefersDarkScheme = window.matchMedia('(prefers-color-scheme: dark)');

        const updateThemeBasedOnSystem = (e) => {
            if (!localStorage.getItem('theme')) {
                if (e.matches) {
                    htmlElement.classList.remove('light');
                    htmlElement.classList.add('dark');
                    themeToggle.checked = true;
                } else {
                    htmlElement.classList.remove('dark');
                    htmlElement.classList.add('light');
                    themeToggle.checked = false;
                }
            }
        };

        prefersDarkScheme.addListener(updateThemeBasedOnSystem);

        // Проверка за реферър
        if (document.referrer) {
            const backButton = document.createElement('button');
            backButton.className = 'fixed bottom-4 left-4 z-40 px-4 py-2 bg-primary text-white rounded-lg shadow-lg hover:bg-primary-dark transition-colors hidden md:flex items-center gap-2';
            backButton.innerHTML = `
                <span class="material-symbols-outlined">arrow_back</span>
                Назад
            `;
            backButton.addEventListener('click', () => {
                window.history.back();
            });
            document.body.appendChild(backButton);
        }

        // Имитация на зареждане
        setTimeout(() => {
            showNotification('Добре дошли в LogisticsPro!', 'info');
        }, 1000);
    });

    // Плаващ бутон за връщане нагоре
    window.addEventListener('scroll', () => {
        const scrollButton = document.getElementById('scrollToTop');
        if (!scrollButton) {
            const scrollButton = document.createElement('button');
            scrollButton.id = 'scrollToTop';
            scrollButton.className = 'fixed bottom-4 right-4 z-40 w-12 h-12 bg-primary text-white rounded-full shadow-lg hover:bg-primary-dark transition-all duration-200 flex items-center justify-center opacity-0 pointer-events-none';
            scrollButton.innerHTML = '<span class="material-symbols-outlined">arrow_upward</span>';
            scrollButton.setAttribute('aria-label', 'Към началото');
            document.body.appendChild(scrollButton);

            scrollButton.addEventListener('click', () => {
                window.scrollTo({
                    top: 0,
                    behavior: 'smooth'
                });
            });
        }

        const currentScrollButton = document.getElementById('scrollToTop');
        if (window.scrollY > 500) {
            currentScrollButton.classList.remove('opacity-0', 'pointer-events-none');
            currentScrollButton.classList.add('opacity-100', 'pointer-events-auto');
        } else {
            currentScrollButton.classList.add('opacity-0', 'pointer-events-none');
            currentScrollButton.classList.remove('opacity-100', 'pointer-events-auto');
        }
    });

    // Обработка на форми (демо)
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            showNotification('Формата е изпратена успешно!', 'success');
        });
    });

    // Копиране на имейл адрес
    document.querySelectorAll('a[href^="mailto:"]').forEach(emailLink => {
        emailLink.addEventListener('click', function(e) {
            const email = this.href.replace('mailto:', '');
            navigator.clipboard.writeText(email).then(() => {
                showNotification(`Имейл адресът ${email} е копиран!`, 'success');
            });
        });
    });

    // Ховер ефекти за карти с цени
    document.querySelectorAll('.price-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.querySelector('button')?.classList.add('scale-105');
        });

        card.addEventListener('mouseleave', function() {
            this.querySelector('button')?.classList.remove('scale-105');
        });
    });

    // Добавяне на активен клас при скрол за навигация
    function updateActiveNavLink() {
        const sections = document.querySelectorAll('section[id]');
        const navLinks = document.querySelectorAll('nav a');

        let currentSection = '';
        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;

            if (window.scrollY >= sectionTop - 100) {
                currentSection = section.getAttribute('id');
            }
        });

        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${currentSection}`) {
                link.classList.add('active');
                link.classList.add('text-primary');
            }
        });
    }

    window.addEventListener('scroll', updateActiveNavLink);

    // Интерактивни таблици
    document.querySelectorAll('.pricing-table tr').forEach(row => {
        row.addEventListener('click', function() {
            if (this.parentNode.nodeName === 'TBODY') {
                const weight = this.cells[0].textContent;
                const price = this.cells[1].textContent;
                showNotification(`Избрано: ${weight.trim()} - ${price.trim()}`, 'info');
            }
        });
    });

    // Инициализация на tooltips
    function initTooltips() {
        document.querySelectorAll('[title]').forEach(element => {
            element.addEventListener('mouseenter', function(e) {
                const tooltip = document.createElement('div');
                tooltip.className = 'absolute bg-slate-900 text-white px-3 py-2 rounded-lg text-sm z-50 whitespace-nowrap';
                tooltip.textContent = this.title;
                tooltip.style.top = (e.clientY - 40) + 'px';
                tooltip.style.left = (e.clientX + 10) + 'px';
                tooltip.id = 'tooltip';
                document.body.appendChild(tooltip);
            });

            element.addEventListener('mouseleave', function() {
                const tooltip = document.getElementById('tooltip');
                if (tooltip) tooltip.remove();
            });
        });
    }

    // Зареждане на tooltips
    setTimeout(initTooltips, 500);
</script>