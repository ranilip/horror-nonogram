import {useEffect} from 'react';
import {useAssetsReady,preloadEverything} from './preload';
import './ScaledSite.css';

// The shared 16:9 stage. It stays hidden until `art` (+ fonts) is decoded, then fades in.
export default function ScaledSite({page,art=[],children}){
 const shown=useAssetsReady(art);
 useEffect(()=>{if(shown)preloadEverything();},[shown]);
 return <div className="site-viewport"><div className={'scaled-site page-'+page+(shown?' is-ready':' is-loading')} aria-busy={!shown}>{children}</div></div>;
}

const EXTRA={home:['mode','mode-hav'],login:['login1','login-write1','er2','mode'],signup:['login1','er2','mode'],scoreover:['mode']};
const TILES=['dark','light','grey'].map(n=>'/images/tiles/tile-'+n+'.png');
// Everything a page paints on first view: background, sidebar icons and its own controls.
export function pageArt(page,background,items=[]){
 return ['/images/background/'+background,...items.filter(i=>i.image&&(page==='home'||!i.text)).map(i=>'/images/icon/'+i.image),
  ...(EXTRA[page]||[]).map(n=>'/images/icon/'+n+'.png'),...(['score','rule','story'].includes(page)?TILES:[])];
}
