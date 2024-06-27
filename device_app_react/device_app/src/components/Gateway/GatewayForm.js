import React, { useEffect, useState } from 'react';

const GatewayForm = ({ gateway = {}, onSubmit }) => {
  const [formData, setFormData] = useState({});

  useEffect(() => {
    if(gateway.gateway_id){
      setFormData(gateway);
    }
  }, [gateway]);

  const handleChange = event => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
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
      <button type="submit" className="btn btn-primary">Salvar</button>
    </form>
  );
};

export default GatewayForm;
