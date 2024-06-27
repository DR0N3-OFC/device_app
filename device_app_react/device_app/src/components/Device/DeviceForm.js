import React, { useEffect, useState } from 'react';

const DeviceForm = ({ device = {}, onSubmit }) => {
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    localizacao: '',
    endereco: '',
    gateway: {
      gateway_id: ''
    }
  });

  useEffect(() => {
    if (device.dispositivo_id) {
      setFormData({
        ...formData,
        nome: device.nome || '',
        descricao: device.descricao || '',
        localizacao: device.localizacao || '',
        endereco: device.endereco || '',
        gateway: {
          gateway_id: device.gateway ? device.gateway.gateway_id || '' : ''
        }
      });
    }
  }, [device, formData]);

  const handleChange = event => {
    const { name, value } = event.target;
    // Se o campo pertencer ao objeto gateway, atualize o estado de formData aninhando o campo
    if (name.startsWith("gateway.")) {
      setFormData(prevState => ({
        ...prevState,
        gateway: {
          ...prevState.gateway,
          [name.split(".")[1]]: value
        }
      }));
    } else {
      // Caso contrário, atualize o estado de formData normalmente
      setFormData(prevState => ({
        ...prevState,
        [name]: value
      }));
    }
  };

  const handleSubmit = event => {
    event.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="p-4 border rounded">
      <div className="mb-3">
        <label htmlFor="nome" className="form-label">Nome:</label>
        <input 
          type="text" 
          id="nome" 
          name="nome" 
          className="form-control" 
          value={formData.nome || ''} 
          onChange={handleChange} 
        />
      </div>
      <div className="mb-3">
        <label htmlFor="descricao" className="form-label">Descrição:</label>
        <input 
          type="text" 
          id="descricao" 
          name="descricao" 
          className="form-control" 
          value={formData.descricao || ''} 
          onChange={handleChange} 
        />
      </div>
      <div className="mb-3">
        <label htmlFor="localizacao" className="form-label">Localização:</label>
        <input 
          type="text" 
          id="localizacao" 
          name="localizacao" 
          className="form-control" 
          value={formData.localizacao || ''} 
          onChange={handleChange} 
        />
      </div>
      <div className="mb-3">
        <label htmlFor="endereco" className="form-label">Endereço:</label>
        <input 
          type="text" 
          id="endereco" 
          name="endereco" 
          className="form-control" 
          value={formData.endereco || ''} 
          onChange={handleChange} 
        />
      </div>
      <div className="mb-3">
        <label htmlFor="gateway_id" className="form-label">Gateway (ID):</label>
        <input 
          type="number" 
          id="gateway_id" 
          name="gateway.gateway_id" // Defina o nome como "gateway.gateway_id"
          className="form-control" 
          value={formData.gateway.gateway_id} 
          onChange={handleChange} 
        />
      </div>
      <button type="submit" className="btn btn-primary">Salvar</button>
    </form>
  );
};

export default DeviceForm;
