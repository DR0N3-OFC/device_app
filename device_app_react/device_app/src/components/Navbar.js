import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../AuthContext';

const Navbar = () => {
  const { loggedIn, logout } = useAuth();

  return (
    <nav className='navbar navbar-expand-lg bg-body-tertiary'>
      <div className="container-fluid">
        <Link className='navbar-brand' to="/">DeviceAPI</Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className='nav-item'>
              <Link className='nav-link active' to="/devices">Dispositivos</Link>
            </li>
            <li className='nav-item'>
              <Link className='nav-link active' to="/gateways">Gateways</Link>
            </li>
            <li className='nav-item'>
              <Link className='nav-link active' to="/messages">Mensagens</Link>
            </li>
            <li className='nav-item'>
              {!loggedIn ? (
                <Link className='nav-link active' to="/login">Login</Link>
              ) : (
                <Link
                  to="/"
                  className='nav-link active'
                  style={{ textDecoration: 'none', color: 'black' }}
                  onClick={logout}
                >
                  Logout
                </Link>
              )}
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
