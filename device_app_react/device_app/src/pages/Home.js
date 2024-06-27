import React, { useEffect } from 'react';
import Dashboard from '../components/Dashboard';
import { useAuth } from '../AuthContext';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const { loggedIn } = useAuth();
  const nav = useNavigate();

  useEffect(() => {
    if (!loggedIn) {
      nav('/login');
    }
  }, [loggedIn, nav]);

  return (
    <div>
      <h1>Home</h1>
      <Dashboard />
    </div>
  );
};

export default Home;
