import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { API } from '../../Const';
import DeviceList from '../../components/Device/DeviceList';

const GatewayDetails = () => {
  const nav = useNavigate();
  const { id } = useParams();
  const [gateway, setGateway] = useState({});

  useEffect(() => {
    // Faça uma solicitação para obter a lista de gateways do servidor
    axios.get(`${API}/gateway/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setGateway(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter o gateway:', error);
      });
  }, [id]);

  const handleDelete = () => {
    // Faça uma solicitação DELETE para excluir o gateway
    axios.delete(`${API}/gateway/${id}`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(() => {
        nav('/gateways');
      })
      .catch(error => {
        console.error('Erro ao excluir o gateway:', error);
      });
  };

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h1>Detalhes do Gateway</h1>
        </div>
        <div className="card-body">
          <p><strong>ID:</strong> {id}</p>
          <p><strong>Nome:</strong> {gateway.nome}</p>
          <p><strong>Descrição:</strong> {gateway.descricao}</p>
          <p><strong>Endereço:</strong> {gateway.endereco}</p>
          <button className='btn btn-secondary' onClick={() => nav(`/gateway/${id}/edit`)}>Editar</button>
          <button className='btn btn-danger' onClick={handleDelete} >Excluir</button>
        </div>
      </div>
      <div className="mt-4">
        <DeviceList id={id}/>
      </div>
    </div>
  );
};

export default GatewayDetails;
