import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [loggedIn, setLoggedIn] = useState(!!sessionStorage.getItem("loggedIn"));

  const login = (token) => {
    sessionStorage.setItem('loggedIn', true);
    sessionStorage.setItem('jwt', `${token}`);
    setLoggedIn(true);
  };

  const logout = () => {
    sessionStorage.removeItem('loggedIn');
    setLoggedIn(false);
  };

  return (
    <AuthContext.Provider value={{ loggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
