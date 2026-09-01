import React, { useState } from 'react';
import { loginUser } from '../services/userApi';
import { NavLink } from 'react-router-dom';
import './LoginPage.css';

function LoginPage({ loggedUser, setLoggedUser, editorPreview = false }) {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const result = await loginUser({ login, password });
            if (result === 'OK') {
                setMessage('Logged in successfully');
                setLoggedUser(login);
            } else {
                setMessage(result);
            }
        } catch (error) {
            setMessage('Error connecting to server');
        }
    };

    return (
        <div className="loginPage">
            <p className="loginMessege" role="status">{editorPreview ? 'Error connecting to server' : message}</p>
            <form onSubmit={handleSubmit} className="login-form">
                <h1>Login</h1>
                <input
                    className="input"
                    value={login}
                    onChange={(e) => setLogin(e.target.value)}
                    placeholder="Login"
                    required
                />
                <input
                    className={`input ${password.length > 0 ? 'active' : ''}`}
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />
                <NavLink to="/signup" end className="signUpText">
                    Don't have an account? Register here.
                </NavLink>
                <button type="submit" className="iconMod">Login</button>
            </form>
        </div>
    );
}

export default LoginPage;
