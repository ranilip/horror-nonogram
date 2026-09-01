import {useEffect,useState} from 'react';
// Loading order: a page is shown only when its background, icons and fonts are decoded,
// so nothing appears on a black screen and nothing pops in afterwards.
// Everything else is fetched once in idle time, so later page changes are instant.
const done=new Set(),pending=new Map();
let fontsReady=false,fontsPromise=null;
const FONTS=['45px "BILLH___.TTF"','20px Mom','bold 20px Love'];
const MAX_WAIT=4000;   // never keep a page hidden longer than this on a slow connection

export function loadImage(url){
 if(done.has(url))return Promise.resolve();
 if(!pending.has(url))pending.set(url,new Promise(resolve=>{
  const img=new Image();
  const finish=()=>{done.add(url);pending.delete(url);resolve();};
  img.onload=()=>(img.decode?img.decode().catch(()=>{}):Promise.resolve()).then(finish);
  img.onerror=finish;
  img.src=url;
 }));
 return pending.get(url);
}
function loadFonts(){
 if(fontsReady)return Promise.resolve();
 if(!fontsPromise)fontsPromise=Promise.all(FONTS.map(f=>document.fonts?.load(f).catch(()=>{}))).then(()=>{fontsReady=true;});
 return fontsPromise;
}
const ready=urls=>fontsReady&&urls.every(u=>done.has(u));

export function useAssetsReady(urls){
 const key=urls.join('|');
 const [state,setState]=useState(()=>({key,ok:ready(urls)}));
 useEffect(()=>{
  const list=key?key.split('|'):[];
  if(ready(list)){setState({key,ok:true});return;}
  let live=true;
  const show=()=>{if(live)setState({key,ok:true});};
  const timer=setTimeout(show,MAX_WAIT);
  Promise.all([...list.map(loadImage),loadFonts()]).then(show);
  return()=>{live=false;clearTimeout(timer);};
 },[key]);
 // A new page counts as ready only after its own check, never on the previous page's result.
 return (state.key===key&&state.ok)||ready(urls);
}

const PAGE_ART=['home','back','login','signup','rule'].map(n=>'/images/background/'+n+'.webp')
 .concat(['opening','room','scream-man'].map(n=>'/images/story/'+n+'.webp'))
 .concat(['mode','mode-hav','login1','login-write1','er2','home-def','leader-def','rule-def','comment-def','log-def'].map(n=>'/images/icon/'+n+'.png'))
 .concat(['dark','light','grey'].map(n=>'/images/tiles/tile-'+n+'.png'));
let warmed=false;
export function preloadEverything(){
 if(warmed)return;warmed=true;
 const idle=window.requestIdleCallback||(fn=>setTimeout(fn,300));
 // One at a time, so the current page never competes with the warm-up for bandwidth.
 idle(()=>PAGE_ART.reduce((chain,url)=>chain.then(()=>loadImage(url)),Promise.resolve()));
}
