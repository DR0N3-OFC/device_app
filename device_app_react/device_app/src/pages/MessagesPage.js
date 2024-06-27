import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../AuthContext';
import { useNavigate } from 'react-router-dom';

const Messages = () => {
    const { loggedIn } = useAuth();
    const [messages, setMessages] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
       if (!loggedIn) {
           nav('/login');
       }
    }, [loggedIn, nav]);

    useEffect(() => {
        async function fetchMessages() {
            try {
                const response = await axios.get(`http://localhost:${process.env.REACT_APP_RABBITMQ_PORT}/messages`);
                setMessages(response.data);
            } catch (error) {
                console.error('Erro ao buscar mensagens:', error);
            }
        }

        fetchMessages();
    }, []);

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Mensagens do RabbitMQ</h1>
            <table className="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th scope="col">Num.</th>
                    <th scope="col">Mensagem</th>
                </tr>
                </thead>
                <tbody>
                {messages.length > 0 ? messages.map((message, index) => (
                    <tr key={index}>
                    <th scope="row">{index + 1}</th>
                    <td>{message.content}</td>
                    </tr>
                )) : (
                    <tr><td colSpan="2" className="text-center">Ainda não existem mensagens cadastradas.</td></tr>
                )}
                </tbody>
            </table>
            <div className="d-flex justify-content-center">
                <button
                    className="btn btn-secondary"
                    onClick={() => navigate('/')}
                >
                    Cancelar
                </button>
            </div>
        </div>
    );
}

export default Messages;
