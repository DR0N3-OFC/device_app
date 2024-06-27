import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API } from '../../Const';
import { Link } from 'react-router-dom';

const ActuatorList = ({ id }) => {
  const [actuators, setActuators] = useState([]);

  useEffect(() => {
    const fetchActuators = async () => {
      try {
        const response = id
          ? await axios.get(`${API}/dispositivo/${id}/atuadores`, { headers: { "Authorization": `Bearer ${sessionStorage.getItem('jwt')}` } })
          : await axios.get(`${API}/atuador`);

        setActuators(response.data);
      } catch (error) {
        console.error('Erro ao obter a lista de atuadores:', error);
      }
    };

    fetchActuators();
  }, [id]);

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Lista de Atuadores</h2>
        </div>
        <div className="card-body">
          <ul className="list-group">
            {actuators.map(actuator => (
              <li key={actuator.atuador_id} className="list-group-item">
                <Link to={`/actuator/${actuator.atuador_id}`} className="text-decoration-none">
                  <h3>{actuator.nome}</h3>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ActuatorList;
