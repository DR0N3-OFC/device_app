import axios from 'axios';
import React from 'react';
import { API } from '../../Const';
import GatewayForm from '../../components/Gateway/GatewayForm';
import GatewayList from '../../components/Gateway/GatewayList';

const GatewayPage = () => {
  const handleSubmit = formData => {
    // Enviar dados do formulário para o servidor para cadastrar ou editar um gateway
    axios.post(`${API}/gateway`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Gateway cadastrado/editado com sucesso:', response.data);
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar gateway:', error);
      })
      .finally(
        window.location.reload()
      );
  };

  return (
    <div>
      <GatewayList />
      <GatewayForm onSubmit={handleSubmit} />
    </div>
  );
};

export default GatewayPage;
