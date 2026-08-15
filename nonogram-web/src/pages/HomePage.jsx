import { NavLink } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    return (
        <div className="homePage">
            <nav className="overlay">
                <NavLink to="/story" className="nav-icon">
                    <div className="iconWrapper">
                        <img className="iconMode" src="/images/icon/mode.png" alt="Story" />
                        <span className="iconText">Story Mode</span>
                    </div>
                </NavLink>
                <NavLink to="/score" className="nav-icon">
                    <div className="iconWrapper">
                        <img className="iconMode" src="/images/icon/mode.png" alt="Score" />
                        <span className="iconText">Score Mode</span>
                    </div>
                </NavLink>
            </nav>
        </div>
    );
}

export default HomePage;
