import {HashRouter as Router,Link,useLocation} from 'react-router-dom';
import {useState,useEffect} from 'react';
import {currentUser} from './services/gameApi';
import './App.css';
// Shared stage styles (.site-viewport, .scaled-site, .placed-button) live in LayoutEditor.css.
import './LayoutEditor.css';
import PageSurface from './PageSurface';
import {readLayout,placement,ButtonArt,pages} from './layout';
import StoryModePage from './pages/StoryModePage';
import SoundToggle from './SoundToggle';
import ScaledSite,{pageArt} from './ScaledSite';
import {setupUiSounds} from './sound';
setupUiSounds();
function Site(){
 const location=useLocation();
 const [loggedUser,setLoggedUser]=useState(null);
 // The server keeps the login in a session, so it survives a page reload.
 useEffect(()=>{currentUser().then(me=>{if(me?.loggedIn)setLoggedUser(me.login);}).catch(()=>{});},[]);
 if(location.pathname==='/story')return <StoryModePage/>;
 const page=Object.keys(pages).find(p=>pages[p].route===location.pathname);
 const items=readLayout(page||'home');
 return <><ScaledSite page={page||'home'} art={pageArt(page||'home',pages[page||'home'].background,items)}>
 {page?<PageSurface page={page} items={items} loggedUser={loggedUser} setLoggedUser={setLoggedUser}/>:<div className="unfinished-page">Ця сторінка ще не реалізована у репозиторії.<br/><Link to="/">На головну</Link></div>}
 {items.filter(i=>i.kind!=='content'&&(page==='home'||!i.text)).map(item=><Link className={'placed-button button-'+item.id} key={item.id} to={item.to} aria-label={item.label} style={placement(item)}><ButtonArt item={item}/></Link>)}
 </ScaledSite><SoundToggle className="sound-toggle-fixed"/></>;
}
export default function App(){return <Router><Site/></Router>;}
