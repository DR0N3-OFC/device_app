import axios from 'axios';
import React, { useReducer, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { API } from '../Const';
import { useAuth } from '../AuthContext';

const RegisterPage = () => {
  const nav = useNavigate();
  const { login } = useAuth();

  const [submitting, setSubmitting] = useState(false);

  const formReducer = (state, event) => {
    if (event.reset) {
      return {
        nome: '',
        email: '',
        senha: ''
      }
    }

    return {
      ...state,
      [event.name]: event.value
    }
  }

  const [formData, setFormData] = useReducer(formReducer, {
    nome: '',
    email: '',
    senha: ''
  });

  const handleChange = event => {
    setFormData({
      name: event.target.name,
      value: event.target.value,
    });
  }

  const handleSave = event => {
    event.preventDefault();
    setSubmitting(true);

    axios.post(`${API}/pessoa`, formData)
      .then((response) => {
        setFormData({ reset: true });

        login(response.data.token);
        console.log(response.data.token)
        nav('/login');
      })
      .catch(err => {
        console.log(err);
      })
      .finally(() => {
        setSubmitting(false);
      });
  };

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Registro</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSave}>
            <fieldset className="form-group mb-3" disabled={submitting}>
              <label htmlFor="nome" className="form-label">Nome:</label>
              <input
                type="text"
                id="nome"
                name="nome"
                className="form-control"
                placeholder="Fulano"
                onChange={handleChange}
                value={formData.nome || ''}
                required
              />
            </fieldset>

            <fieldset className="form-group mb-3" disabled={submitting}>
              <label htmlFor="email" className="form-label">E-mail:</label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-control"
                placeholder="email@example.com"
                onChange={handleChange}
                value={formData.email || ''}
                required
              />
            </fieldset>

            <fieldset className="form-group mb-3" disabled={submitting}>
              <label htmlFor="senha" className="form-label">Senha:</label>
              <input
                type="password"
                id="senha"
                name="senha"
                className="form-control"
                placeholder="********"
                onChange={handleChange}
                value={formData.senha || ''}
                required
              />
            </fieldset>

            <div className="mt-3">
              <button type="submit" className="btn btn-success me-2" style={{ minWidth: '100px' }} disabled={submitting}>
                <i className="fas fa-check-circle"></i> Salvar
              </button>

              <button className="btn btn-light" style={{ minWidth: '100px' }} onClick={() => nav('/user')} disabled={submitting}>
                <i className="fas fa-times-circle"></i> Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
