export const API_BASE = "http://localhost:8080";
export const WS_URL = "ws://localhost:9090/ws-chat";

export const formatDate = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

// Returns standard API headers
export const getHeaders = () => {
    return {
        "Content-Type": "application/json"
    };
};
