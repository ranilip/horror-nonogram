import React, { useState } from 'react';
import { registerUser } from '../services/userApi';
import './SignUpPage.css';
import './LoginPage.css';

function SignUpPage() {
    const [player, setPlayer] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            setMessage("Invalid email format");
            return;
        }

        if (password.length < 8) {
            setMessage("Password must be at least 8 characters");
            return;
        }

        if (password !== confirmPassword) {
            setMessage("Passwords do not match");
            return;
        }

        const result = await registerUser({ player, password, email });
        setMessage(result);
    };


    return (
        <div className="signUpPage">
            <p className="loginMessege">{message}</p>
            <form onSubmit={handleSubmit} className="sign">
                <h1>Sign Up</h1>
                <input
                    className="input"
                    value={player}
                    onChange={(e) => setPlayer(e.target.value)}
                    placeholder="Login"
                    required
                />
                <input
                    className="input"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    required
                />
                <input
                    className="input"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />
                <input
                    className="input"
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Confirm Password"
                    required
                />
                <button type="submit" className="iconMod"
                        style={{
                            position: 'absolute',
                            top: '373px',
                            left: '93px'
                        }}>Sign Up</button>
            </form>
        </div>
    );
}

export default SignUpPage;
