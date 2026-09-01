import {useState,useEffect,useRef,useLayoutEffect} from 'react';
import {newGame,move,hints} from '../services/scoreMode';
import {FACE,faceGrid} from '../story/trailer';
import {sfx} from '../sound';
import './ScoreModePage.css';

// The window face, drawn with the same tiles and board code as Score Mode.
// Original rules from story.txt: unlimited lives (a wrong tile only shows ×), 60 seconds.
export default function StoryPuzzlePage({editorPreview=false,onComplete=()=>{}}){
 const [game,setGame]=useState(()=>({...newGame(faceGrid()),lives:Infinity}));
 const [deadline]=useState(()=>Date.now()+FACE.time*1000);
 const [seconds,setSeconds]=useState(FACE.time);
 const [cellSize,setCellSize]=useState(24);
 const board=useRef(null),done=useRef(false),finish=useRef(onComplete);
 finish.current=onComplete;
 useEffect(()=>{if(editorPreview)return;const tick=()=>setSeconds(Math.max(0,Math.ceil((deadline-Date.now())/1000)));const t=setInterval(tick,250);return()=>clearInterval(t);},[deadline,editorPreview]);
 // Ends once: solved → short pause to see the face, then the epilogue; time out → lost.
 useEffect(()=>{
  if(editorPreview||done.current)return;
  if(game.status==='solved'){done.current=true;sfx('solved');setTimeout(()=>finish.current(true),900);}
  else if(seconds<=0){done.current=true;sfx('fail');finish.current(false);}
 },[game.status,seconds,editorPreview]);
 const {solution,cells}=game;
 const rowHints=solution.map((line,r)=>hints(line,cells[r]));
 const colHints=solution[0].map((_,c)=>hints(solution.map(line=>line[c]),cells.map(line=>line[c])));
 const left=Math.max(...rowHints.map(line=>line.length)),top=Math.max(...colHints.map(line=>line.length));
 const rows=solution.length,cols=solution[0].length;
 useLayoutEffect(()=>{
  const area=board.current;if(!area)return;
  const resize=()=>setCellSize(Math.max(1,Math.min(40,Math.floor(Math.min(area.clientWidth/(left+cols),area.clientHeight/(top+rows))))));
  const observer=new ResizeObserver(resize);observer.observe(area);resize();return()=>observer.disconnect();
 },[left,top,rows,cols]);
 function play(r,c){
  if(game.status!=='playing'||seconds<=0)return;
  const played=move(game,r,c);if(played===game)return;
  sfx(played.cells[r][c]==='correct'?'fill':'mistake');setGame(played);
 }
 const filled=cells.flat().filter(c=>c==='correct').length,total=solution.flat().filter(Boolean).length;
 const clock=Math.floor(seconds/60)+':'+String(seconds%60).padStart(2,'0');
 return <div className="score-page story-puzzle-page">
  <h1 className="score-title">Who is at the window?</h1>
  <div className="score-stats"><span>Time<strong className={seconds<=15?'story-urgent':''} role="timer">{clock}</strong></span><span>Found<strong>{filled} / {total}</strong></span></div>
  <div className="score-board-area" ref={board}>
   <div className="score-board" role="group" aria-label={`${rows} by ${cols} nonogram puzzle`} style={{gridTemplateColumns:`repeat(${left+cols}, ${cellSize}px)`,'--cell-size':cellSize+'px','--clue-size':Math.min(22,cellSize*.64)+'px','--cross-size':Math.min(24,cellSize*.75)+'px'}}>
    {Array.from({length:top},(_,r)=>Array.from({length:left+cols},(_,c)=>{
     const line=colHints[c-left],hint=line?.[r-(top-line.length)];
     return c<left?<span className="score-corner" key={'t'+r+'-'+c}/>:<span className="score-tile score-hint" key={'t'+r+'-'+c}>{hint===0?'':hint??''}</span>;
    }))}
    {cells.map((line,r)=><BoardRow key={r} row={r} line={line} hints={rowHints[r]} left={left} disabled={game.status!=='playing'} play={c=>play(r,c)}/>)}
   </div>
  </div>
  <div className="score-tools"><span className="story-note">Reveal the face before the time runs out.</span></div>
 </div>;
}
function BoardRow({row,line,hints:leftHints,left,disabled,play}){
 return <>{Array.from({length:left},(_,i)=><span className="score-tile score-hint" key={'h'+i}>{leftHints[i-(left-leftHints.length)]||''}</span>)}
  {line.map((cell,c)=><button key={c} className={'score-tile score-cell score-cell-'+cell} aria-label={`Row ${row+1}, column ${c+1}: ${cell}`} disabled={disabled||cell!=='closed'} onClick={()=>play(c)}>{cell==='empty'?'x':''}</button>)}
 </>;
}
