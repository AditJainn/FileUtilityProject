import React from 'react';
import logo from '../../assets/KiwiCloud2.png';
import './Header.css';

function Header(){
  return (
<div className="header-left">
  <img src={logo} alt="Kiwi Cloud Logo" />

  <div className="title-stack">
    <h1>Kiwi Cloud</h1>
    <p className="subtitle">File Utility Project</p>
  </div>
</div>
  );
}
export default Header;