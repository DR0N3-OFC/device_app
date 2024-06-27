import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { API } from '../Const';

const Map = () => {
  const [devices, setDevices] = useState([]);
  
  useEffect(() => {
    // Faça uma solicitação para obter os detalhes do gateway do servidor
    axios.get(`${API}/dispositivo`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setDevices(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter os dispositivos:', error);
      });
  }, []);

  const navbarHeight = 50;

  return (
    <MapContainer center={[-25.30094117764429, -54.114950604648946]} zoom={10} style={{ height: `calc(50vh - ${navbarHeight}px)`, width: "40vw" }}>
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
      />
      {Array.isArray(devices) && devices.map(device => {
        const positionArray = device.localizacao.split(',').map(coord => parseFloat(coord.trim()));

        return (
          <Marker key={device.id} position={positionArray} >
            <Popup >{device.nome}</Popup>
          </Marker>
        );
      })}
    </MapContainer>
  );
};

export default Map;
