import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { API } from '../../Const';
import GatewayForm from '../../components/Gateway/GatewayForm';
import { useNavigate, useParams } from 'react-router-dom';

const GatewayEditPage = () => {
  const { id } = useParams();
  const nav = useNavigate();
  const [gateway, setGateway] = useState({});

  useEffect(() => {
    // Faça uma solicitação para obter os detalhes do gateway do servidor
    axios.get(`${API}/gateway/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setGateway(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter os detalhes do gateway:', error);
      });
  }, [id]);

  const handleSubmit = formData => {
    // Enviar dados do formulário para o servidor para cadastrar ou editar um gateway
    axios.put(`${API}/gateway/${id}`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Gateway cadastrado/editado com sucesso:', response.data);
        nav(`/gateway/${id}`);
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar gateway:', error);
      });
  };

  return (
    <div>
      <GatewayForm gateway={gateway} onSubmit={handleSubmit} />
    </div>
  );
};

export default GatewayEditPage;
