import {BrowserRouter as Router, Routes, Route, NavLink} from 'react-router-dom';
import './App.css';
import Sidebar from './components/Sidebar.jsx';
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RulePage from "./pages/RulePage";
import SignUpPage from "./pages/SignUpPage";
import {useState} from "react";

function App() {
    const [loggedUser, setLoggedUser] = useState(null);

    return (
        <Router>
            <div className="appLayout">
                <Sidebar isLoggedIn={!!loggedUser} />

                <div className="PageContent">
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/login" element={<LoginPage loggedUser={loggedUser} setLoggedUser={setLoggedUser} />} />
                        <Route path="/rule" element={<RulePage />} />
                        <Route path="/signup" element={<SignUpPage />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
