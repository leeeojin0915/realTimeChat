import React, { useState, useEffect, useRef } from 'react';
import { Outlet, useNavigate, Link, useLocation } from 'react-router-dom';
import axios from 'axios';
import { API_BASE } from '../utils/common';
import { User, MessageCircle, Settings, LogOut, Search, Plus, X, Users, Camera } from 'lucide-react';

export default function MainLayout({ user, onLogout, onUpdateUser }) {
    const navigate = useNavigate();
    const location = useLocation();
    const [friends, setFriends] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [friendUsername, setFriendUsername] = useState('');
    const [selectedFriend, setSelectedFriend] = useState(null);
    const [isNewChatModalOpen, setIsNewChatModalOpen] = useState(false);
    const [isMyProfileModalOpen, setIsMyProfileModalOpen] = useState(false);
    const [selectedFriendIds, setSelectedFriendIds] = useState([]);
    const profileImgInputRef = useRef(null);

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }
        fetchFriends();
        fetchRooms();

        const timer = setInterval(() => {
            fetchFriends();
            fetchRooms();
        }, 5000);

        return () => clearInterval(timer);
    }, [user, navigate]);

    const fetchFriends = async () => {
        try {
            const res = await axios.get(`${API_BASE}/api/messenger/friends/${user.id}`);
            setFriends(res.data);
        } catch (e) { console.error(e); }
    };

    const fetchRooms = async () => {
        try {
            const res = await axios.get(`${API_BASE}/api/messenger/rooms/${user.id}`);
            setRooms(res.data);
        } catch (e) { console.error(e); }
    };

    const addFriend = async () => {
        if (!friendUsername.trim()) return;
        try {
            await axios.post(`${API_BASE}/api/messenger/friends/${user.id}`, { friendUsername });
            setFriendUsername('');
            fetchFriends();
        } catch (e) { 
            alert(e.response?.data || 'Error adding friend'); 
        }
    };

    const startChat = async (friendIds) => {
        try {
            const res = await axios.post(`${API_BASE}/api/messenger/rooms`, { 
                memberId: user.id, 
                friendIds: friendIds 
            });
            setSelectedFriend(null);
            setIsNewChatModalOpen(false);
            setSelectedFriendIds([]);
            navigate(`/room/${res.data.id}`);
            fetchRooms();
        } catch (e) { console.error(e); }
    };

    const toggleFriendSelection = (id) => {
        setSelectedFriendIds(prev => 
            prev.includes(id) ? prev.filter(fId => fId !== id) : [...prev, id]
        );
    };

    const handleProfileClick = () => {
        profileImgInputRef.current?.click();
    };

    const handleProfileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            // 1. Upload file
            const uploadRes = await axios.post(`${API_BASE}/api/files/upload`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            const newFileUrl = uploadRes.data.fileUrl;

            // 2. Update profile
            const updateRes = await axios.post(`${API_BASE}/api/messenger/members/${user.id}/profile`, {
                nickname: user.nickname,
                profileImageUrl: newFileUrl
            });
            
            // 3. Update local state
            onUpdateUser(updateRes.data);
            setIsMyProfileModalOpen(false);
            alert('프로필 사진이 변경되었습니다.');
        } catch (e) {
            console.error(e);
            alert('프로필 변경에 실패했습니다.');
        }
    };

    return (
        <div style={{ display: 'flex', height: '100vh', backgroundColor: '#F3F4F6' }}>
            {/* Sidebar */}
            <div style={{ width: 320, backgroundColor: '#fff', borderRight: '1px solid var(--border-color)', display: 'flex', flexDirection: 'column' }}>
                {/* Me Section */}
                <div style={{ padding: '1.5rem', borderBottom: '1px solid var(--border-color)', backgroundColor: '#F9FAFB' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                        <div 
                            onClick={() => setIsMyProfileModalOpen(true)}
                            style={{ 
                                width: 50, height: 50, borderRadius: '1rem', backgroundColor: 'var(--primary)', 
                                display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', 
                                overflow: 'hidden', cursor: 'pointer' 
                            }}
                            title="My Profile"
                        >
                            {user?.profileImageUrl ? <img src={`${API_BASE}${user.profileImageUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} /> : <User size={24} />}
                        </div>
                        <input type="file" ref={profileImgInputRef} style={{ display: 'none' }} onChange={handleProfileChange} accept="image/*" />
                        <div style={{ flex: 1 }}>
                            <div style={{ fontWeight: '600', fontSize: '1.1rem' }}>{user?.nickname}</div>
                            <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>@{user?.username}</div>
                        </div>
                        <button onClick={onLogout} style={{ padding: '0.5rem', background: 'transparent', color: '#EF4444' }} title="Logout">
                            <LogOut size={18} />
                        </button>
                    </div>
                </div>

                {/* Add Friend Input */}
                <div style={{ padding: '1rem', borderBottom: '1px solid var(--border-color)' }}>
                    <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <input 
                            placeholder="Add friend by username..." 
                            value={friendUsername}
                            onChange={e => setFriendUsername(e.target.value)}
                            onKeyDown={e => e.key === 'Enter' && addFriend()}
                            style={{ flex: 1, padding: '0.5rem 0.75rem', borderRadius: '0.5rem', border: '1px solid var(--border-color)', fontSize: '0.9rem' }}
                        />
                        <button onClick={addFriend} style={{ padding: '0.5rem', borderRadius: '0.5rem' }}><Plus size={18} /></button>
                    </div>
                </div>

                {/* List Container */}
                <div style={{ flex: 1, overflowY: 'auto' }}>
                    {/* Friends Section */}
                    <div style={{ padding: '1rem' }}>
                        <div style={{ fontSize: '0.75rem', fontWeight: '600', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.5rem' }}>Friends ({friends.length})</div>
                        {friends.map(f => (
                            <div 
                                key={f.id} 
                                onClick={() => setSelectedFriend(f.friend)}
                                style={{ 
                                    display: 'flex', alignItems: 'center', gap: '0.75rem', padding: '0.75rem', borderRadius: '0.75rem', cursor: 'pointer',
                                    transition: 'background 0.2s'
                                }}
                                onMouseEnter={e => e.currentTarget.style.backgroundColor = '#F9FAFB'}
                                onMouseLeave={e => e.currentTarget.style.backgroundColor = 'transparent'}
                            >
                                <div style={{ width: 40, height: 40, borderRadius: '0.75rem', backgroundColor: '#E5E7EB', overflow: 'hidden' }}>
                                    {f.friend.profileImageUrl && <img src={`${API_BASE}${f.friend.profileImageUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} />}
                                </div>
                                <div>
                                    <div style={{ fontWeight: '500', fontSize: '0.95rem' }}>{f.friend.nickname}</div>
                                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Online</div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Rooms Section */}
                    <div style={{ padding: '1rem' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                            <div style={{ fontSize: '0.75rem', fontWeight: '600', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Chat Rooms</div>
                            <button onClick={() => setIsNewChatModalOpen(true)} style={{ padding: '0.25rem', background: 'transparent', color: 'var(--primary)' }} title="New Chat">
                                <Plus size={16} />
                            </button>
                        </div>
                        {rooms.map(r => (
                            <Link 
                                to={`/room/${r.id}`} 
                                key={r.id} 
                                style={{ 
                                    display: 'flex', alignItems: 'center', gap: '0.75rem', padding: '0.75rem', borderRadius: '0.75rem', textDecoration: 'none', color: 'inherit',
                                    backgroundColor: location.pathname === `/room/${r.id}` ? '#EEF2FF' : 'transparent',
                                    marginBottom: '0.25rem',
                                    transition: 'background 0.2s'
                                }}
                                onMouseEnter={e => { if (location.pathname !== `/room/${r.id}`) e.currentTarget.style.backgroundColor = '#F9FAFB'; }}
                                onMouseLeave={e => { if (location.pathname !== `/room/${r.id}`) e.currentTarget.style.backgroundColor = 'transparent'; }}
                            >
                                <div style={{ width: 40, height: 40, borderRadius: '0.75rem', backgroundColor: 'var(--primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}>
                                    <MessageCircle size={20} />
                                </div>
                                <div style={{ flex: 1, minWidth: 0 }}>
                                    <div style={{ fontWeight: '500', fontSize: '0.95rem', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{r.name}</div>
                                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{r.lastMessage || 'No messages yet'}</div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>
            </div>

            {/* Main Content */}
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                <Outlet />
            </div>

            {/* Friend Info Modal */}
            {selectedFriend && (
                <div 
                    onClick={() => setSelectedFriend(null)}
                    style={{ position: 'fixed', inset: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}
                >
                    <div 
                        onClick={e => e.stopPropagation()}
                        style={{ backgroundColor: '#fff', borderRadius: '1.5rem', width: 320, padding: '2rem', textAlign: 'center', position: 'relative', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.1)' }}
                    >
                        <button onClick={() => setSelectedFriend(null)} style={{ position: 'absolute', top: '1.25rem', right: '1.25rem', background: 'transparent', color: 'var(--text-muted)' }}><X size={20} /></button>
                        <div style={{ width: 80, height: 80, borderRadius: '2rem', backgroundColor: '#E5E7EB', margin: '0 auto 1.5rem', overflow: 'hidden' }}>
                            {selectedFriend.profileImageUrl ? <img src={`${API_BASE}${selectedFriend.profileImageUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} /> : <User size={40} style={{margin:'20px', color:'#9CA3AF'}} />}
                        </div>
                        <h2 style={{ margin: '0 0 0.5rem', fontSize: '1.5rem' }}>{selectedFriend.nickname}</h2>
                        <p style={{ color: 'var(--text-muted)', margin: '0 0 2rem' }}>@{selectedFriend.username}</p>
                        <button 
                            onClick={() => startChat([selectedFriend.id])}
                            style={{ width: '100%', padding: '1rem', borderRadius: '1rem', fontWeight: '600', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}
                        >
                            <MessageCircle size={20} /> Start Chat
                        </button>
                    </div>
                </div>
            )}

            {/* My Profile Modal */}
            {isMyProfileModalOpen && (
                <div 
                    onClick={() => setIsMyProfileModalOpen(false)}
                    style={{ position: 'fixed', inset: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}
                >
                    <div 
                        onClick={e => e.stopPropagation()}
                        style={{ backgroundColor: '#fff', borderRadius: '1.5rem', width: 320, padding: '2rem', textAlign: 'center', position: 'relative', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.1)' }}
                    >
                        <button onClick={() => setIsMyProfileModalOpen(false)} style={{ position: 'absolute', top: '1.25rem', right: '1.25rem', background: 'transparent', color: 'var(--text-muted)' }}><X size={20} /></button>
                        <div style={{ position: 'relative', width: 80, height: 80, margin: '0 auto 1.5rem' }}>
                            <div style={{ width: '100%', height: '100%', borderRadius: '2rem', backgroundColor: 'var(--primary)', overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}>
                                {user?.profileImageUrl ? <img src={`${API_BASE}${user.profileImageUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} /> : <User size={40} />}
                            </div>
                            <button 
                                onClick={handleProfileClick}
                                style={{ position: 'absolute', bottom: -5, right: -5, width: 32, height: 32, borderRadius: '50%', backgroundColor: '#fff', border: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 0, color: 'var(--text-main)', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}
                                title="Change Photo"
                            >
                                <Camera size={16} />
                            </button>
                        </div>
                        <h2 style={{ margin: '0 0 0.5rem', fontSize: '1.5rem' }}>{user?.nickname}</h2>
                        <p style={{ color: 'var(--text-muted)', margin: '0 0 2rem' }}>@{user?.username}</p>
                        
                        <div style={{ borderTop: '1px solid var(--border-color)', paddingTop: '1.5rem', display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                            <button 
                                onClick={onLogout}
                                style={{ width: '100%', padding: '0.75rem', borderRadius: '0.75rem', fontWeight: '600', backgroundColor: '#FEF2F2', color: '#EF4444', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}
                            >
                                <LogOut size={18} /> Logout
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* New Chat Modal */}
            {isNewChatModalOpen && (
                <div 
                    onClick={() => { setIsNewChatModalOpen(false); setSelectedFriendIds([]); }}
                    style={{ position: 'fixed', inset: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}
                >
                    <div 
                        onClick={e => e.stopPropagation()}
                        style={{ backgroundColor: '#fff', borderRadius: '1.5rem', width: 400, maxHeight: '80vh', display: 'flex', flexDirection: 'column', position: 'relative', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.1)' }}
                    >
                        <div style={{ padding: '1.5rem', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <h3 style={{ margin: 0 }}>Create New Chat</h3>
                            <button onClick={() => { setIsNewChatModalOpen(false); setSelectedFriendIds([]); }} style={{ background: 'transparent', color: 'var(--text-muted)' }}><X size={20} /></button>
                        </div>
                        <div style={{ flex: 1, overflowY: 'auto', padding: '1rem' }}>
                            {friends.map(f => (
                                <div 
                                    key={f.id} 
                                    onClick={() => toggleFriendSelection(f.friend.id)}
                                    style={{ 
                                        display: 'flex', alignItems: 'center', gap: '0.75rem', padding: '0.75rem', borderRadius: '0.75rem', cursor: 'pointer',
                                        backgroundColor: selectedFriendIds.includes(f.friend.id) ? '#EEF2FF' : 'transparent'
                                    }}
                                >
                                    <div style={{ width: 40, height: 40, borderRadius: '0.75rem', backgroundColor: '#E5E7EB', overflow: 'hidden' }}>
                                        {f.friend.profileImageUrl && <img src={`${API_BASE}${f.friend.profileImageUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} />}
                                    </div>
                                    <div style={{ flex: 1 }}>{f.friend.nickname}</div>
                                    <div style={{ width: 24, height: 24, borderRadius: '50%', border: '2px solid var(--primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                        {selectedFriendIds.includes(f.friend.id) && <div style={{ width: 12, height: 12, borderRadius: '50%', backgroundColor: 'var(--primary)' }} />}
                                    </div>
                                </div>
                            ))}
                        </div>
                        <div style={{ padding: '1.5rem', borderTop: '1px solid var(--border-color)' }}>
                            <button 
                                disabled={selectedFriendIds.length === 0}
                                onClick={() => startChat(selectedFriendIds)}
                                style={{ width: '100%', padding: '1rem', borderRadius: '1rem', fontWeight: '600', opacity: selectedFriendIds.length === 0 ? 0.5 : 1 }}
                            >
                                Create Chat ({selectedFriendIds.length})
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
