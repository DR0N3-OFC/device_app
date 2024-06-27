import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API } from '../../Const';
import { Link, useNavigate } from 'react-router-dom';

const GatewayList = () => {
  const [gateways, setGateways] = useState([]);
  const nav = useNavigate();
  
  useEffect(() => {
    const fetchGateways = async () => {
      try {
        const response = await axios.get(`${API}/gateway`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} });
        setGateways(response.data);
      } catch (error) {
        console.error('Erro ao obter a lista de gateways:', error);
        nav('/login');
      }
    };

    fetchGateways();
  }, [nav]);

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Lista de Gateways</h2>
        </div>
        <div className="card-body">
          <ul className="list-group">
            {gateways.length > 0 ? (
              gateways.map(gateway => (
                <li key={gateway.gateway_id} className="list-group-item">
                  <Link to={`/gateway/${gateway.gateway_id}`} className="text-decoration-none">
                    <h3>{gateway.nome}</h3>
                  </Link>
                </li>
              ))
            ) : (
              <p>Ainda não existem gateways cadastrados.</p>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default GatewayList;
