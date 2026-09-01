// Procedural sound: no audio files or remote requests. The context starts after the first interaction.
// Ambience is declarative: ambience(kind) remembers what should play, so a scene requested before the
// browser allows audio starts as soon as the player first clicks, taps or presses a key.
const MUTE_KEY='nonogram-sound-muted';
const readMuted=()=>{try{return localStorage.getItem(MUTE_KEY)==='1';}catch{return false;}};
const saveMuted=v=>{try{localStorage.setItem(MUTE_KEY,v?'1':'0');}catch{}};

let context,master;
let muted=readMuted();
let desired=null;                                   // {kind, level}
let current={kind:null,nodes:[],gains:[],timer:null};
let oneShots=[];
const VOLUME=.3;

export function unlockAudio(){
 if(!context){
  const Ctx=window.AudioContext||window.webkitAudioContext;if(!Ctx)return;
  context=new Ctx();master=context.createGain();master.gain.value=muted?0:VOLUME;master.connect(context.destination);
 }
 context.resume().catch(()=>{});
 if(desired&&current.kind!==desired.kind)startAmbience();
}
const now=()=>context.currentTime;
function noise(seconds=4){const buffer=context.createBuffer(1,Math.floor(context.sampleRate*seconds),context.sampleRate),data=buffer.getChannelData(0);for(let i=0;i<data.length;i++)data[i]=Math.random()*2-1;const source=context.createBufferSource();source.buffer=buffer;return source;}
function envelope(peak,attack,release,at=now()){const g=context.createGain();g.gain.setValueAtTime(.0001,at);g.gain.exponentialRampToValueAtTime(peak,at+attack);g.gain.exponentialRampToValueAtTime(.0001,at+attack+release);return g;}
function track(node,stopAt){oneShots.push(node);node.onended=()=>{oneShots=oneShots.filter(n=>n!==node);};node.start();node.stop(stopAt);}

// ---- ambience ------------------------------------------------------------------------------
function stopAmbience(fade=.35){
 clearTimeout(current.timer);
 if(context)for(const g of current.gains)try{g.gain.cancelScheduledValues(now());g.gain.setTargetAtTime(0,now(),fade/3);}catch{}
 const nodes=current.nodes;setTimeout(()=>{for(const n of nodes)try{n.stop();}catch{}},fade*1000+60);
 current={kind:null,nodes:[],gains:[],timer:null};
}
function bed(cutoff,volume){
 const s=noise(6),filter=context.createBiquadFilter(),gain=context.createGain();
 s.loop=true;filter.type='lowpass';filter.frequency.value=cutoff;
 gain.gain.setValueAtTime(0,now());gain.gain.linearRampToValueAtTime(volume,now()+1.2);
 s.connect(filter).connect(gain).connect(master);s.start();
 current.nodes.push(s);current.gains.push(gain);return gain;
}
function thud(){if(!context||muted)return;const t=now(),o=context.createOscillator(),g=envelope(.16,.005,.19,t);o.frequency.setValueAtTime(85,t);o.frequency.exponentialRampToValueAtTime(35,t+.15);o.connect(g).connect(master);track(o,t+.21);}
// One footstep: a heel thump plus a short creak of old boards. Nearer steps are louder and brighter.
function footstep(level){
 if(!context||muted)return;
 const t=now(),pan=context.createStereoPanner?context.createStereoPanner():null;
 if(pan){pan.pan.value=(Math.random()-.5)*.5*(1-level);pan.connect(master);}
 const out=pan||master;
 const o=context.createOscillator(),g=envelope(.07+.25*level,.004,.16,t);
 o.frequency.setValueAtTime(75,t);o.frequency.exponentialRampToValueAtTime(38,t+.12);o.connect(g).connect(out);track(o,t+.2);
 const n=noise(.2),f=context.createBiquadFilter(),ng=envelope(.02+.09*level,.003,.08,t);
 f.type='bandpass';f.frequency.value=500+1100*level;f.Q.value=1.4;n.connect(f).connect(ng).connect(out);track(n,t+.15);
}
function startAmbience(){
 if(!context||!desired)return;
 stopAmbience();current.kind=desired.kind;
 const kind=desired.kind;
 if(kind==='sea'){
  const g=bed(480,.23*(desired.level??1));
  const wave=context.createOscillator(),depth=context.createGain();wave.frequency.value=.12;depth.gain.value=.12*(desired.level??1);wave.connect(depth).connect(g.gain);wave.start();current.nodes.push(wave);
 }else if(kind==='room'){
  bed(1800,.018);const loop=()=>{thud();current.timer=setTimeout(loop,1700);};current.timer=setTimeout(loop,900);
 }else if(kind==='steps'){
  bed(1500,.016);
  // Interval and loudness follow the current level, so the steps close in line by line.
  const loop=()=>{const level=desired?.level??0;footstep(level);current.timer=setTimeout(loop,1350-820*level+Math.random()*90);};
  current.timer=setTimeout(loop,500);
 }else if(kind==='hush'){
  bed(900,.012);
 }
}
export function ambience(kind,{level=1}={}){
 if(desired?.kind===kind){desired.level=level;if(context&&current.kind!==kind)startAmbience();return;}
 desired={kind,level};
 if(context)startAmbience();
}
export function stopAudio(){
 desired=null;stopAmbience(.25);
 for(const n of oneShots)try{n.stop();}catch{}
 oneShots=[];
}

// ---- screamer ------------------------------------------------------------------------------
// A short, sharp hit: noise burst, a rising screech and a sub thump. soft = reduced scare.
export function scareSound({soft=false}={}){
 if(!context)return;
 desired=null;stopAmbience(.02);
 const t=now();
 const n=noise(1.2),f=context.createBiquadFilter(),g=envelope(soft?.16:.55,.01,.9,t);
 f.type='bandpass';f.frequency.value=950;f.Q.value=.7;n.connect(f).connect(g).connect(master);track(n,t+1.1);
 const sub=context.createOscillator(),sg=envelope(soft?.15:.45,.005,.5,t);sub.frequency.setValueAtTime(70,t);sub.frequency.exponentialRampToValueAtTime(28,t+.45);sub.connect(sg).connect(master);track(sub,t+.6);
 if(!soft)for(const [from,to] of [[420,1250],[447,1320]]){
  const o=context.createOscillator(),lp=context.createBiquadFilter(),og=envelope(.11,.015,.75,t);
  o.type='sawtooth';o.frequency.setValueAtTime(from,t);o.frequency.exponentialRampToValueAtTime(to,t+.5);
  lp.type='lowpass';lp.frequency.value=3200;o.connect(lp).connect(og).connect(master);track(o,t+.85);
 }
}

// ---- interface and gameplay effects --------------------------------------------------------
function tone(type,from,to,peak,length,at=now()){const o=context.createOscillator(),g=envelope(peak,.004,length,at);o.type=type;o.frequency.setValueAtTime(from,at);if(to!==from)o.frequency.exponentialRampToValueAtTime(to,at+length);o.connect(g).connect(master);track(o,at+length+.05);}
function burst(type,freq,peak,length,at=now(),q=1){const n=noise(length+.05),f=context.createBiquadFilter(),g=envelope(peak,.002,length,at);f.type=type;f.frequency.value=freq;f.Q.value=q;n.connect(f).connect(g).connect(master);track(n,at+length+.05);}
export function sfx(kind){
 if(!context||muted)return;
 const t=now();
 switch(kind){
  case 'click':burst('highpass',1400,.09,.035,t);tone('sine',170,120,.12,.06,t);break;      // stone button
  case 'hover':tone('sine',820,620,.025,.04,t);break;
  case 'advance':burst('bandpass',1900,.03,.07,t,.8);break;                               // turning the passage
  case 'fill':tone('triangle',330,230,.1,.07,t);burst('lowpass',900,.03,.03,t);break;    // tile pressed in
  case 'cross':burst('bandpass',2600,.06,.05,t,2);break;                                  // scratched mark
  case 'mistake':tone('sine',110,104,.16,.38,t);tone('sine',117,110,.12,.38,t);break;
  case 'solved':[392,466,587].forEach((f,i)=>tone('sine',f,f,.07,.9,t+i*.09));break;
  default:{if(kind!=='fail')break;const o=context.createOscillator(),lp=context.createBiquadFilter(),g=envelope(.13,.02,1.2,t);o.type='sawtooth';o.frequency.setValueAtTime(190,t);o.frequency.exponentialRampToValueAtTime(48,t+1.2);lp.type='lowpass';lp.frequency.value=420;o.connect(lp).connect(g).connect(master);track(o,t+1.3);break;}
 }
}
// Delegated sounds for every button/link, so pages need no per-element wiring.
// Tiles (.cell) and the story text box play their own sounds.
let uiReady=false;
const CLICKABLE='button,a[href],[role="button"]';
const HOVERABLE='.placed-button,.nav-icon,.iconMod,.score-new-game,.story-button,.story-home';
const OWN_SOUND='.score-cell,.story-narrative';
export function setupUiSounds(){
 if(uiReady||typeof document==='undefined')return;uiReady=true;
 document.addEventListener('pointerdown',e=>{unlockAudio();const el=e.target.closest?.(CLICKABLE);if(el&&!el.closest(OWN_SOUND)&&!el.disabled)sfx('click');},true);
 document.addEventListener('keydown',e=>{unlockAudio();if(e.repeat||(e.key!=='Enter'&&e.key!==' '))return;const el=document.activeElement;if(el?.matches?.(CLICKABLE)&&!el.closest(OWN_SOUND))sfx('click');},true);
 document.addEventListener('pointerover',e=>{if(e.pointerType!=='mouse')return;const el=e.target.closest?.(HOVERABLE);if(el&&!(e.relatedTarget&&el.contains(e.relatedTarget)))sfx('hover');},true);
}

export function toggleSound(){muted=!muted;saveMuted(muted);unlockAudio();if(master)master.gain.value=muted?0:VOLUME;return !muted;}
export function soundEnabled(){return !muted;}
