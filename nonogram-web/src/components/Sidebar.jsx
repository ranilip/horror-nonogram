import { NavLink } from 'react-router-dom';
import './Sidebar.css';

function Sidebar({ isLoggedIn }) {
    return (
        <nav className="menuSidebar">
            <NavLink to="/" end className="nav-icon home">
                <img className="iconSidebar" src="/images/icon/home-def.png" alt="Home" />
            </NavLink>
            <NavLink to="/leader" className="nav-icon leader">
                <img className="iconSidebar" src="/images/icon/leader-def.png" alt="Leader" />
            </NavLink>
            <NavLink to="/rule" className="nav-icon rule">
                <img className="iconSidebar" src="/images/icon/rule-def.png" alt="Rules" />
            </NavLink>
            <NavLink to="/comments" className="nav-icon comment">
                <img className="iconSidebar" src="/images/icon/comment-def.png" alt="Comments" />
            </NavLink>
            <NavLink to="/login" className="nav-icon log">
                <img
                    className="iconSidebar"
                    src={isLoggedIn ? "/images/icon/log-log.png" : "/images/icon/log-def.png"}
                    alt="Login"
                />
            </NavLink>
        </nav>
    );
}

export default Sidebar;
