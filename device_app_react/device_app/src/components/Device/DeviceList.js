import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API } from '../../Const';
import { Link, useNavigate } from 'react-router-dom';

const GatewayList = ({ id }) => {
  const [devices, setDevices] = useState([]);
  const nav = useNavigate();

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const response = id
          ? await axios.get(`${API}/gateway/${id}/dispositivos`, { headers: { "Authorization": `Bearer ${sessionStorage.getItem('jwt')}` } })
          : await axios.get(`${API}/dispositivo`, { headers: { "Authorization": `Bearer ${sessionStorage.getItem('jwt')}` } });

        setDevices(response.data);
      } catch (error) {
        console.error('Erro ao obter a lista de dispositivos:', error);
        nav('/login');
      }
    };

    fetchDevices();
  }, [id, nav]);

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Lista de Dispositivos</h2>
        </div>
        <div className="card-body">
          <ul className="list-group">
            {devices.length > 0 ? (
              devices.map(device => (
                <li key={device.dispositivo_id} className="list-group-item">
                  <Link to={`/device/${device.dispositivo_id}`} className="text-decoration-none">
                    <h3>{device.nome}</h3>
                  </Link>
                </li>
              ))
            ) : (
              <p>Ainda não existem dispositivos cadastrados.</p>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default GatewayList;
