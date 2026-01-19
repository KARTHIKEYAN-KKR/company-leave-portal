document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const errorMsg = document.getElementById('error-msg');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            errorMsg.textContent = 'Logging in...';

            try {
                const response = await fetch('http://localhost:8080/employee-leave-tracker/api/employees/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                });

                if (response.ok) {
                    const user = await response.json();
                    localStorage.setItem('user', JSON.stringify(user));

                    if (user.role === 'ADMIN') {
                        window.location.href = 'admin_dashboard.html';
                    } else {
                        window.location.href = 'dashboard.html';
                    }
                } else {
                    const error = await response.json();
                    errorMsg.textContent = error.error || 'Login failed';
                }
            } catch (err) {
                errorMsg.textContent = 'Network error. Is the server running?';
                console.error(err);
            }
        });
    }
});
