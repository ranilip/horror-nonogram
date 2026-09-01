import {useState,useEffect,useRef,useCallback} from 'react';
import {Link} from 'react-router-dom';
import PageSurface from '../PageSurface';
import {readLayout,placement,ButtonArt} from '../layout';
import {STEPS,ART,stepById,newPlayback,restorePlayback} from '../story/trailer';
import {unlockAudio,ambience,scareSound,stopAudio,sfx} from '../sound';
import SoundToggle from '../SoundToggle';
import ScaledSite,{pageArt} from '../ScaledSite';
import './StoryModePage.css';

const KEY='nonogram-story-v1';
const readSaved=()=>{try{return JSON.parse(localStorage.getItem(KEY));}catch{return null;}};
const reducedScare=()=>typeof window!=='undefined'&&window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;

function Frame({art=[],children}){return <ScaledSite page="story" art={art}>{children}</ScaledSite>;}

// Story Mode teaser. All timing and audio are driven from the step table in story/trailer.js.
export default function StoryModePage(){
 const [story,setStory]=useState(()=>restorePlayback(readSaved()));
 useEffect(()=>{try{localStorage.setItem(KEY,JSON.stringify(story));}catch{}},[story]);
 const step=stepById(story.step);
 const fired=useRef(new Set());
 const go=useCallback(id=>setStory(s=>s.play===story.play&&s.step===step.id?{...s,step:id}:s),[story.play,step.id]);
 const replay=()=>{unlockAudio();setStory(newPlayback());};
 // Audio per step; one-time events fire once per playback.
 useEffect(()=>{
  const key=story.play+':'+step.id;
  if(step.kind==='scare'){if(!fired.current.has(key)){fired.current.add(key);scareSound({soft:reducedScare()});}}
  else if(step.kind==='puzzle')stopAudio();
  else if(step.audio)ambience(step.audio,{level:step.level});
 },[story.play,step]);
 useEffect(()=>stopAudio,[]);   // leaving Story Mode silences everything
 // Preload every teaser image so the screamer shows on its first frame.
 // The puzzle step's background, sidebar icons and fonts are warmed up too, so nothing pops in late.
 useEffect(()=>{
  const urls=[...new Set(STEPS.map(s=>ART[s.art]).filter(Boolean)),'/images/background/back.webp','/images/icon/mode.png','/images/icon/mode-hav.png','/images/tiles/tile-dark.png','/images/tiles/tile-light.png','/images/tiles/tile-grey.png',...readLayout('story').filter(i=>i.image).map(i=>'/images/icon/'+i.image)];
  for(const url of urls){const img=new Image();img.src=url;img.decode?.().catch(()=>{});}
  for(const font of ['45px "BILLH___.TTF"','20px Mom','20px Love'])document.fonts?.load(font).catch(()=>{});
 },[]);
 // Timed steps (the screamer): a single timer, cleared if the step changes first.
 useEffect(()=>{if(!step.duration)return;const t=setTimeout(()=>go(step.next),step.duration);return()=>clearTimeout(t);},[step,go]);
 const next=useCallback(()=>{if(step.kind!=='text')return;unlockAudio();sfx('advance');go(step.next);},[step,go]);
 useEffect(()=>{
  if(step.kind!=='text')return;
  const key=e=>{const t=document.activeElement;if(e.repeat||e.altKey||e.ctrlKey||e.metaKey)return;if(['Space','Enter','NumpadEnter','ArrowRight'].includes(e.code)&&!['BUTTON','A','INPUT','TEXTAREA','SELECT'].includes(t?.tagName)){e.preventDefault();next();}};
  window.addEventListener('keydown',key);return()=>window.removeEventListener('keydown',key);
 },[step,next]);
 const onPuzzle=useCallback(won=>go(won?'e-0':'lost'),[go]);

 if(step.kind==='scare')return <Frame><img className={'story-art story-scare'+(reducedScare()?' story-scare-soft':'')} src={ART[step.art]} alt="A face appears at the window"/></Frame>;

 if(step.kind==='puzzle'){
  const items=readLayout('story');
  return <><Frame art={pageArt('story','back.webp',items)}>
   <PageSurface page="story" items={items} onStoryComplete={onPuzzle}/>
   {items.filter(i=>i.kind!=='content'&&!i.text).map(item=><Link className={'placed-button button-'+item.id} key={item.id} to={item.to} aria-label={item.label} style={placement(item)}><ButtonArt item={item}/></Link>)}
  </Frame><SoundToggle className="sound-toggle-fixed"/></>;
 }

 if(step.kind==='ending')return <Frame art={[ART[step.art]]}>
  <img className={'story-art story-ending-art '+(step.won?'won':'lost')} src={ART[step.art]} alt=""/><div className="story-ending-shade"/>
  <div className="story-top"><Link className="story-home" to="/">← Home</Link><span/><SoundToggle/></div>
  <section className="story-ending">
   <p className="story-eyebrow">{step.won?'The first night · teaser complete':'The light has gone out'}</p>
   <h1>{step.won?'“She saw you.”':'Lost to the dark.'}</h1>
   <p className="story-ending-text">{step.won?'He is standing in the doorway. Still smiling. In his hand, the little mermaid.':'The pattern slips away. Outside, the sea falls silent. Something is breathing on the other side of the glass.'}</p>
   <h2>Full story mode<br/>coming soon.</h2>
   <div className="story-actions">
    {!step.won&&<button className="story-button" onClick={()=>go('puzzle')}>Try again</button>}
    <button className="story-button" onClick={replay}>Replay Story</button>
    <Link className="story-button" to="/score">Score Mode</Link>
   </div>
  </section>
 </Frame>;

 return <Frame art={[ART[step.art]]}>
  <img key={step.art} className="story-art" src={ART[step.art]} alt={step.place}/><div className="story-shade"/>
  <div className="story-top"><Link className="story-home" to="/">← Home</Link><span className="story-chapter">{step.chapter} · {step.place}</span><SoundToggle/></div>
  <div className="story-narrative" role="button" tabIndex={0} onClick={next} aria-label="Continue story" aria-keyshortcuts="Enter ArrowRight Space">
   <span className="story-speaker">{step.speaker}</span>
   <p className="story-line" key={step.id} aria-live="polite">{step.text}</p>
   <span className="story-continue" aria-hidden="true">{step.id==='s0-0'?'Enter ↵':'↵'}</span>
  </div>
 </Frame>;
}
