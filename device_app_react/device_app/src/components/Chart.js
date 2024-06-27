import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { API } from '../Const';

const Chart = () => {
  const [data, setData] = useState({});
  console.log(sessionStorage.getItem('jwt'));
  useEffect(() => {
    // Faça uma solicitação para obter os detalhes do gateway do servidor
    axios.get(`${API}/medicao`, { headers: {"Authorization" : `Bearer ${sessionStorage.getItem('jwt')}`} })
      .then(response => {
        setData(response.data);
        console.log(response.data)
      })
      .catch(error => {
        console.error('Erro ao obter as medicoes:', error);
      });
  }, []);

  return (
    <ResponsiveContainer width="100%" height={400}>
      <LineChart
        data={data}
        margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="nome" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="valor" stroke="#8884d8" activeDot={{ r: 8 }} />
      </LineChart>
    </ResponsiveContainer>
  );
};

export default Chart;
