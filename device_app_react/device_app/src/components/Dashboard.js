import React from 'react';
import Map from './Map'; // Substitua pelo caminho correto do seu componente
import Chart from './Chart'; // Substitua pelo caminho correto do seu componente

function App() {
  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-6">
          <Map />
        </div>
        <div className="col-6">
          <Chart />
        </div>
      </div>
    </div>
  );
}

export default App;
