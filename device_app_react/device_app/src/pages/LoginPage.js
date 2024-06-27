import axios from 'axios';
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { API } from '../Const';

const URL = `${API}/auth`;

const LoginPage = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');

    const handleChange = event => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async event => {
        event.preventDefault();
        setSubmitting(true);
        
        try {
            const response = await axios.post(URL, formData);
            
            const token = response.data.token;
            
            if(token !== undefined){
                login(token);
            }

            navigate('/devices');
        } catch (error) {
            console.error('Error during login:', error);
            setError('Erro ao fazer login. Por favor, verifique suas credenciais e tente novamente.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header">
                    <h2>Login</h2>
                </div>
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <fieldset className="form-group mb-3" disabled={submitting}>
                            <label htmlFor="username" className="form-label">Email</label>
                            <input
                                type="email"
                                id="username"
                                name="username"
                                className="form-control"
                                placeholder="email@example.com"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </fieldset>

                        <fieldset className="form-group mb-3" disabled={submitting}>
                            <label htmlFor="password" className="form-label">Senha</label>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                className="form-control"
                                placeholder="********"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </fieldset>

                        {error && <div className="alert alert-danger">{error}</div>}

                        <div className="mt-3">
                            <button type="submit" className="btn btn-success me-2" disabled={submitting}>
                                {submitting ? 'Aguarde...' : 'Login'}
                            </button>

                            <button
                                className="btn btn-light"
                                onClick={() => navigate('/')}
                                disabled={submitting}
                            >
                                Cancelar
                            </button>
                        </div>
                    </form>
                    <p className='card-text mt-3'>Não tem uma conta? <Link to="/register">Crie uma</Link></p>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
