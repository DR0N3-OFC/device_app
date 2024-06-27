import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API } from '../../Const';
import { Link } from 'react-router-dom';

const SensorList = ({ id }) => {
  const [sensors, setSensors] = useState([]);

  useEffect(() => {
    const fetchSensors = async () => {
      try {
        let response;
        if (id) {
          response = await axios.get(`${API}/dispositivo/${id}/sensores`, {
            headers: { "Authorization": `Bearer ${sessionStorage.getItem('jwt')}` }
          });
        } else {
          response = await axios.get(`${API}/sensor`, {
            headers: { "Authorization": `Bearer ${sessionStorage.getItem('jwt')}` }
          });
        }
        setSensors(response.data);
      } catch (error) {
        console.error('Erro ao obter a lista de sensores:', error);
      }
    };

    fetchSensors();
  }, [id]);

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Lista de Sensores</h2>
        </div>
        <div className="card-body">
          <ul className="list-group">
            {sensors.length > 0 ? sensors.map(sensor => (
              <li key={sensor.sensor_id} className="list-group-item">
                <Link to={`/sensor/${sensor.sensor_id}`}>
                  <h3>{sensor.nome}</h3>
                </Link>
              </li>
            )) : (
              <li className="list-group-item">Ainda não existem sensores cadastrados.</li>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default SensorList;
