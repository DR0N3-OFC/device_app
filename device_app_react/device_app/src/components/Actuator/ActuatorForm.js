import React, { useEffect, useState } from 'react';

const ActuatorForm = ({ actuator = {}, onSubmit, device = {} }) => {
  const [formData, setFormData] = useState({
    ...actuator,
    nome: actuator.nome || '',
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
          <h2>Formulário do Atuador</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <fieldset className="form-group mb-3">
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
            </fieldset>
            <input
              type="hidden"
              name="dispositivo_id"
              value={formData.dispositivo.dispositivo_id}
              onChange={handleChange}
              readOnly
            />
            {/* Outros campos do atuador podem ser adicionados aqui */}
            <div className="mt-3">
              <button type="submit" className="btn btn-success me-2">
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
