import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import './App.css';
import AppRoutes from './AppRoutes';
import Navbar from './components/Navbar';
import { AuthProvider } from './AuthContext';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <div className='container mt-4'>
            <AppRoutes />
          </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
