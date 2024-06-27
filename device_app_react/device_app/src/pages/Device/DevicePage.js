import axios from 'axios';
import React from 'react';
import { API } from '../../Const';
import DeviceForm from '../../components/Device/DeviceForm';
import DeviceList from '../../components/Device/DeviceList';

const DevicePage = () => {
  const handleSubmit = formData => {
    axios.post(`${API}/dispositivo`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Dispositivo cadastrado/editado com sucesso:', response.data);
        window.location.reload();
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar gateway:', error);
      });
  };

  return (
    <div>
      <DeviceList />
      <DeviceForm onSubmit={handleSubmit} />
    </div>
  );
};

export default DevicePage;
