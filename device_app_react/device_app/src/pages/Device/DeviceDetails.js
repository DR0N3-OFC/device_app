import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { API } from '../../Const';
import ActuatorList from '../../components/Actuator/ActuatorList';
import SensorList from '../../components/Sensor/SensorList';
import ActuatorForm from '../../components/Actuator/ActuatorForm';
import SensorForm from '../../components/Sensor/SensorForm';

const DeviceDetails = () => {
  const nav = useNavigate();
  const { id } = useParams();
  const [device, setDevice] = useState([]);

  useEffect(() => {
    // Faça uma solicitação para obter a lista de dispositivos do servidor
    axios.get(`${API}/dispositivo/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setDevice(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter o dispositivo:', error);
      });
  }, [id]);

  const handleActuatorSubmit = formData => {
    // Enviar dados do formulário para o servidor para cadastrar ou editar um gateway
    axios.post(`${API}/atuador`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Atuador cadastrado/editado com sucesso:', response.data);
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar atuador:', error);
      })
      .finally(
        window.location.reload()
      );
  };

  const handleSensorSubmit = formData => {
    // Enviar dados do formulário para o servidor para cadastrar ou editar um gateway
    axios.post(`${API}/sensor`, formData, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        console.log('Sensor cadastrado/editado com sucesso:', response.data);
        // Atualizar a lista de gateways
        // Você pode redirecionar para a lista de gateways ou fazer outra coisa após o sucesso
      })
      .catch(error => {
        console.error('Erro ao cadastrar/editar sensor:', error);
      })
      .finally(
        window.location.reload()
      );
  };

  const handleDelete = () => {
    // Faça uma solicitação DELETE para excluir o gateway
    axios.delete(`${API}/dispositivo/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(() => {
        nav('/devices');
      })
      .catch(error => {
        console.error('Erro ao excluir o dispositivo:', error);
      });
  };

  return (
    <>
      <div className="container mt-4">
        <div className="card">
          <div className="card-header">
            <h1>Detalhes do Dispositivo</h1>
          </div>
          <div className="card-body">
            <p><strong>ID:</strong> {id}</p>
            <p><strong>Nome:</strong> {device.nome}</p>
            <p><strong>Descrição:</strong> {device.descricao}</p>
            <p><strong>Localização:</strong> {device.localizacao}</p>
            <p><strong>Endereço:</strong> {device.endereco}</p>
          <button className='btn btn-secondary' onClick={() => nav(`/device/${id}/edit`)}>Editar</button>
          <button className='btn btn-danger' onClick={handleDelete} >Excluir</button>
          </div>
        </div>
      </div>
      <div>
        <ActuatorList id={id}/>
        <ActuatorForm device={device} onSubmit={handleActuatorSubmit} />
      </div>
      <div>
        <SensorList id={id} />
        <SensorForm device={device} onSubmit={handleSensorSubmit} />
      </div>
    </>
  );
};

export default DeviceDetails;
