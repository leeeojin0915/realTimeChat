import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { MessageCircle } from 'lucide-react';
import Login from './pages/Login';
import MainLayout from './pages/MainLayout';
import ChatRoom from './pages/ChatRoom';

function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
    setLoading(false);
  }, []);

  const handleLogin = (userData) => {
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };

  const handleUpdateUser = (userData) => {
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  };

  if (loading) return null;

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route 
          path="/*" 
          element={user ? <MainLayout user={user} onLogout={handleLogout} onUpdateUser={handleUpdateUser} /> : <Navigate to="/login" replace />}
        >
          <Route index element={
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', backgroundColor: '#fff', color: 'var(--text-muted)' }}>
              <div style={{ width: 80, height: 80, borderRadius: '50%', backgroundColor: '#F3F4F6', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.5rem' }}>
                <MessageCircle size={40} />
              </div>
              <h3 style={{ margin: '0 0 0.5rem', color: 'var(--text-main)' }}>Messenger</h3>
              <p>대화방을 선택하거나 새로운 대화를 시작해보세요.</p>
            </div>
          } />
          <Route path="room/:roomId" element={<ChatRoom user={user} />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
