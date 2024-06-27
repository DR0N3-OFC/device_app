import React, { useEffect, useState } from 'react';

const ActuatorForm = ({ sensor = {}, onSubmit, device = {} }) => {
  const [formData, setFormData] = useState({
    ...sensor, 
    nome: sensor.nome || '',
    tipo: sensor.tipo || '',
    dispositivo: {
      dispositivo_id: device.dispositivo_id || ''
    }
  });

  useEffect(() => {
    if (device.dispositivo_id) {
      setFormData(formData => ({
        ...formData,
        dispositivo: {
          ...formData.device,
          dispositivo_id: device.dispositivo_id || ''
        }
      }));
    }
  }, [device.dispositivo_id]);

  const handleChange = event => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = event => {
    event.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header">
          <h2>Cadastro de Atuador</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="nome" className="form-label">Nome</label>
              <input
                type="text"
                id="nome"
                name="nome"
                className="form-control"
                value={formData.nome}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="tipo" className="form-label">Tipo</label>
              <input
                type="text"
                id="tipo"
                name="tipo"
                className="form-control"
                value={formData.tipo}
                onChange={handleChange}
                required
              />
            </div>
            <input
              type="number"
              name="dispositivo_id"
              value={formData.dispositivo.dispositivo_id}
              onChange={handleChange}
              readOnly
              hidden
            />
            <div className="mt-3">
              <button type="submit" className="btn btn-success me-1" style={{ minWidth: '100px' }}>
                <i className="fas fa-check-circle"></i> Salvar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ActuatorForm;
