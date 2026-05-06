import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_BASE, WS_URL, formatDate } from '../utils/common';
import { Send, Image as ImageIcon, Paperclip, Users, X, LogOut } from 'lucide-react';

export default function ChatRoom({ user }) {
    const { roomId } = useParams();
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [roomInfo, setRoomInfo] = useState(null);
    const [input, setInput] = useState('');
    const wsRef = useRef(null);
    const messagesEndRef = useRef(null);
    const fileInputRef = useRef(null);

    useEffect(() => {
        loadHistory();
        connectWs();
        return () => disconnectWs();
    }, [roomId]);

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const loadHistory = async () => {
        try {
            const res = await axios.get(`${API_BASE}/api/messenger/rooms/${roomId}/messages`);
            setMessages(res.data);
            
            // Fetch room info to show name in header
            const roomRes = await axios.get(`${API_BASE}/api/messenger/rooms/${user.id}`);
            const currentRoom = roomRes.data.find(r => r.id === Number(roomId));
            if (currentRoom) setRoomInfo(currentRoom);
        } catch (e) { console.error(e); }
    };

    const connectWs = () => {
        if (wsRef.current && (wsRef.current.readyState === WebSocket.OPEN || wsRef.current.readyState === WebSocket.CONNECTING)) return;
        
        const ws = new WebSocket(WS_URL);
        wsRef.current = ws;

        ws.onopen = () => {
            console.log("WS Connected");
            ws.send(JSON.stringify({ roomId: Number(roomId), type: 'join' }));
        };

        ws.onmessage = (e) => {
            try {
                const data = JSON.parse(e.data);
                if (data.roomId === Number(roomId)) {
                    setMessages(prev => {
                        // Prevent duplicate messages by ID
                        if (data.id && prev.some(m => m.id === data.id)) return prev;
                        return [...prev, data];
                    });
                }
            } catch (err) { console.error(err); }
        };
    };

    const disconnectWs = () => {
        wsRef.current?.close();
        wsRef.current = null;
    };

    const sendMessage = () => {
        if (!input.trim()) return;
        wsRef.current?.send(JSON.stringify({
            roomId: Number(roomId),
            senderId: user.id,
            content: input,
            fileType: 'TEXT'
        }));
        setInput('');
    };

    const handleFileUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await axios.post(`${API_BASE}/api/files/upload`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            
            const fileUrl = res.data.fileUrl;
            let fileType = 'FILE';
            if (file.type.startsWith('image/')) fileType = 'IMAGE';
            else if (file.type.startsWith('video/')) fileType = 'VIDEO';

            wsRef.current?.send(JSON.stringify({
                roomId: Number(roomId),
                senderId: user.id,
                content: file.name,
                fileUrl: fileUrl,
                fileType: fileType
            }));
        } catch (err) {
            alert('File upload failed');
        }
    };

    const leaveRoom = async () => {
        if (!window.confirm('정말 이 대화방을 나가시겠습니까?')) return;
        try {
            await axios.delete(`${API_BASE}/api/messenger/rooms/${roomId}/leave/${user.id}`);
            navigate('/');
        } catch (e) {
            alert('Error leaving room');
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%', backgroundColor: '#fff' }}>
            {/* Header */}
            <div style={{ padding: '1rem', borderBottom: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', backgroundColor: '#fff', zIndex: 10 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                    <div style={{ width: 40, height: 40, borderRadius: '1rem', backgroundColor: 'var(--primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff' }}>
                        <Users size={20} />
                    </div>
                    <div>
                        <h3 style={{ margin: 0, fontSize: '1rem' }}>{roomInfo?.name || `Room #${roomId}`}</h3>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                            {roomInfo?.otherMemberName ? `Chat with ${roomInfo.otherMemberName}` : 'Group Chat'}
                        </div>
                    </div>
                </div>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                    <button onClick={leaveRoom} style={{ background: 'transparent', color: '#EF4444', padding: '0.5rem' }} title="Leave Room">
                        <LogOut size={20} />
                    </button>
                    <button onClick={() => navigate('/')} style={{ background: 'transparent', color: 'var(--text-muted)', padding: '0.5rem' }} title="Close Chat">
                        <X size={20} />
                    </button>
                </div>
            </div>

            {/* Messages */}
            <div style={{ flex: 1, overflowY: 'auto', padding: '1rem', display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {messages.map((m, idx) => {
                    const isMe = m.senderId === user.id;
                    return (
                        <div key={m.id || idx} style={{ display: 'flex', flexDirection: isMe ? 'row-reverse' : 'row', alignItems: 'flex-end', gap: '0.5rem' }}>
                            {!isMe && (
                                <div style={{ width: 32, height: 32, borderRadius: '50%', backgroundColor: '#ddd', overflow: 'hidden', flexShrink: 0 }}>
                                    {m.senderProfileUrl && <img src={`${API_BASE}${m.senderProfileUrl}`} style={{width:'100%', height:'100%', objectFit:'cover'}} />}
                                </div>
                            )}
                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: isMe ? 'flex-end' : 'flex-start', maxWidth: '70%' }}>
                                {!isMe && <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: '0.25rem', marginLeft: '0.25rem' }}>{m.senderNickname}</div>}
                                
                                <div style={{ 
                                    padding: '0.75rem 1rem', 
                                    backgroundColor: isMe ? 'var(--primary)' : '#F3F4F6', 
                                    color: isMe ? '#fff' : 'var(--text-main)',
                                    borderRadius: isMe ? '1rem 1rem 0 1rem' : '1rem 1rem 1rem 0',
                                    boxShadow: '0 1px 2px rgba(0,0,0,0.05)'
                                }}>
                                    {m.messageType === 'IMAGE' && m.fileUrl ? (
                                        <img src={`${API_BASE}${m.fileUrl}`} style={{ maxWidth: '100%', borderRadius: '0.5rem' }} alt="uploaded" />
                                    ) : m.messageType === 'VIDEO' && m.fileUrl ? (
                                        <video src={`${API_BASE}${m.fileUrl}`} controls style={{ maxWidth: '100%', borderRadius: '0.5rem' }} />
                                    ) : m.messageType === 'FILE' && m.fileUrl ? (
                                        <a href={`${API_BASE}${m.fileUrl}`} download target="_blank" rel="noreferrer" style={{ color: isMe ? '#fff' : 'var(--primary)', textDecoration: 'underline' }}>
                                            {m.content}
                                        </a>
                                    ) : (
                                        <div>{m.content}</div>
                                    )}
                                </div>
                                <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '0.25rem' }}>
                                    {formatDate(m.sentAt)}
                                </div>
                            </div>
                        </div>
                    );
                })}
                <div ref={messagesEndRef} />
            </div>

            {/* Input Area */}
            <div style={{ padding: '1rem', borderTop: '1px solid var(--border-color)', display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
                <button 
                    onClick={() => fileInputRef.current?.click()}
                    style={{ background: 'transparent', color: 'var(--text-muted)', padding: '0.5rem' }}
                >
                    <Paperclip size={20} />
                </button>
                <input 
                    type="file" 
                    ref={fileInputRef} 
                    style={{ display: 'none' }} 
                    onChange={handleFileUpload}
                />
                <input 
                    value={input} 
                    onChange={e => setInput(e.target.value)} 
                    onKeyDown={e => {
                        if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
                            sendMessage();
                        }
                    }}
                    placeholder="Type a message..."
                    style={{ flex: 1, padding: '0.75rem 1rem', borderRadius: '1.5rem', border: '1px solid var(--border-color)', backgroundColor: '#F9FAFB' }}
                />
                <button onClick={sendMessage} style={{ borderRadius: '50%', width: 40, height: 40, padding: 0, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <Send size={18} />
                </button>
            </div>
        </div>
    );
}
