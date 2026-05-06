import React, { useState } from 'react';
import axios from 'axios';
import { API_BASE } from '../utils/common';
import { useNavigate } from 'react-router-dom';

export default function Login({ onLogin }) {
    const [isLoginMode, setIsLoginMode] = useState(true);
    const [formData, setFormData] = useState({ username: '', password: '', nickname: '' });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const endpoint = isLoginMode ? '/api/auth/login' : '/api/auth/signup';
            const res = await axios.post(`${API_BASE}${endpoint}`, formData);
            
            onLogin(res.data);
            navigate('/');
        } catch (err) {
            setError(err.response?.data || "An error occurred");
        }
    };

    return (
        <div style={{ display: 'flex', height: '100vh', alignItems: 'center', justifyContent: 'center' }}>
            <div className="glass-panel" style={{ width: 400, padding: '2rem', borderRadius: '1rem' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '1.5rem', color: 'var(--primary)' }}>
                    {isLoginMode ? 'Welcome Back' : 'Create Account'}
                </h2>
                
                {error && <div style={{ color: 'red', marginBottom: '1rem', fontSize: '0.875rem', textAlign: 'center' }}>{error}</div>}

                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    <input 
                        type="text" 
                        name="username" 
                        placeholder="Username" 
                        value={formData.username} 
                        onChange={handleChange} 
                        required 
                    />
                    
                    {!isLoginMode && (
                        <input 
                            type="text" 
                            name="nickname" 
                            placeholder="Nickname" 
                            value={formData.nickname} 
                            onChange={handleChange} 
                            required 
                        />
                    )}
                    
                    <input 
                        type="password" 
                        name="password" 
                        placeholder="Password" 
                        value={formData.password} 
                        onChange={handleChange} 
                        required 
                    />
                    
                    <button type="submit" style={{ padding: '0.75rem', marginTop: '0.5rem' }}>
                        {isLoginMode ? 'Login' : 'Sign Up'}
                    </button>
                </form>

                <div style={{ textAlign: 'center', marginTop: '1rem', fontSize: '0.875rem' }}>
                    <span style={{ color: 'var(--text-muted)' }}>
                        {isLoginMode ? "Don't have an account? " : "Already have an account? "}
                    </span>
                    <span 
                        style={{ color: 'var(--primary)', cursor: 'pointer', fontWeight: 500 }}
                        onClick={() => setIsLoginMode(!isLoginMode)}
                    >
                        {isLoginMode ? 'Sign up' : 'Login'}
                    </span>
                </div>
            </div>
        </div>
    );
}
