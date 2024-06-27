import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { API } from '../../Const';
import DeviceForm from '../../components/Device/DeviceForm';
import { useNavigate, useParams } from 'react-router-dom';

const DeviceEditPage = () => {
  const { id } = useParams();
  const nav = useNavigate();
  const [device, setDevice] = useState({});

  useEffect(() => {
    // Faça uma solicitação para obter os detalhes do dispositivo do servidor
    axios.get(`${API}/dispositivo/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setDevice(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter os detalhes do dispositivo:', error);
      });
  }, [id]);

  const handleSubmit = formData => {
    // Enviar dados do formulário para o servidor para cadastrar ou editar um gateway
    axios.put(`${API}/dispositivo/${id}`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Dispositivo cadastrado/editado com sucesso:', response.data);
        nav(`/device/${id}`);
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar gateway:', error);
      });
  };

  return (
    <div>
      <DeviceForm device={device} onSubmit={handleSubmit} />
    </div>
  );
};

export default DeviceEditPage;
