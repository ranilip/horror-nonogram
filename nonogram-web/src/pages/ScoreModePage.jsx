import {useState,useEffect,useRef,useLayoutEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {newGame, move, nextPuzzle, hints, generatePuzzle} from '../services/scoreMode';
import {nextPicture} from '../services/picturePuzzles';
import {sfx} from '../sound';
import './ScoreModePage.css';

export default function ScoreModePage({editorPreview=false}){
 // No source/size choice: pixel-art pictures at most 8 × 8 (random 5–8 fallback when the API is unavailable).
 const mode='api',size=8;
 const [game,setGame]=useState(()=>editorPreview?newGame(generatePuzzle(8,8)):null);
 const [loading,setLoading]=useState(!editorPreview),[origin,setOrigin]=useState(editorPreview?'random':'api');
 const [cellSize,setCellSize]=useState(24);
 const board=useRef(null),queue=useRef(null),revision=useRef(0),mounted=useRef(false);
 const navigate=useNavigate();
 useEffect(()=>{const generation=revision;mounted.current=true;return()=>{mounted.current=false;generation.current++;};},[]);
 useEffect(()=>{
  if(editorPreview)return;
  const id=++revision.current;
  setLoading(true);queue.current=null;
  nextPicture(mode,size).then(result=>{
   if(!mounted.current||id!==revision.current)return;
   setGame(newGame(result.solution));setOrigin(result.origin);setLoading(false);
   queue.current=nextPicture(mode,size);
  });
 },[mode,size,editorPreview]);
 useEffect(()=>{if(game?.status==='failed')navigate('/score/game-over',{replace:true,state:{score:game.totalScore}});},[game?.status,game?.totalScore,navigate]);
 const solution=game?.solution||[[0]];
 const cells=game?.cells||[['closed']];
 const rowHints=solution.map((line,r)=>hints(line,cells[r]));
 const colHints=solution[0].map((_,c)=>hints(solution.map(line=>line[c]),cells.map(line=>line[c])));
 const left=Math.max(...rowHints.map(line=>line.length)),top=Math.max(...colHints.map(line=>line.length));
 const rows=solution.length,cols=solution[0].length;
 useLayoutEffect(()=>{
  const area=board.current;if(!area)return;
  const resize=()=>setCellSize(Math.max(1,Math.min(32,Math.floor(Math.min(area.clientWidth/(left+cols),area.clientHeight/(top+rows))))));
  const observer=new ResizeObserver(resize);observer.observe(area);resize();return()=>observer.disconnect();
 },[left,top,rows,cols]);
 async function play(r,c){
  if(loading||!game)return;
  const played=move(game,r,c);if(played===game)return;
  sfx(played.status==='solved'?'solved':played.status==='failed'?'fail':played.cells[r][c]==='correct'?'fill':'mistake');
  setGame(played);
  if(played.status!=='solved')return;
  setLoading(true);
  const id=revision.current;
  const result=await(queue.current||nextPicture(mode,size));
  if(!mounted.current||id!==revision.current)return;
  setGame(nextPuzzle(played,result.solution));setOrigin(result.origin);setLoading(false);
  queue.current=nextPicture(mode,size);
 }
 const source=origin==='fallback'?'Random · API unavailable':origin==='api'?'DiceBear · pixel art':'Random pattern';
 if(game?.status==='failed')return null;
 return <div className="score-page">
  <h1 className="score-title">Score Mode</h1>
  <div className="score-stats"><span>Lives<strong>{'♥'.repeat(game?.lives??3)}{'♡'.repeat(3-(game?.lives??3))}</strong></span><span>Score<strong>{game?.totalScore??0}</strong></span><span>Round<strong>{game?.round??1}</strong></span></div>
  <div className="score-board-area" ref={board} aria-busy={loading}>
   {!game?<span className="score-loading">Loading picture…</span>:<div className="score-board" role="group" aria-label={`${rows} by ${cols} nonogram puzzle`} style={{gridTemplateColumns:`repeat(${left+cols}, ${cellSize}px)`,'--cell-size':cellSize+'px','--clue-size':Math.min(19,cellSize*.64)+'px','--cross-size':Math.min(20,cellSize*.75)+'px'}}>
    {Array.from({length:top},(_,r)=>Array.from({length:left+cols},(_,c)=>{
     const line=colHints[c-left],hint=line?.[r-(top-line.length)];
     return c<left?<span className="score-corner" key={'t'+r+'-'+c}/>:<span className="score-tile score-hint" key={'t'+r+'-'+c}>{hint===0?'':hint??''}</span>;
    }))}
    {cells.map((line,r)=><BoardRow key={r} row={r} line={line} hints={rowHints[r]} left={left} disabled={loading||game.status!=='playing'} play={c=>play(r,c)}/>)}
   </div>}
  </div>
  <div className="score-tools">
   <span className="score-source-note">{loading?'Loading next picture…':source+(game?' · '+rows+' × '+cols:'')}</span>
  </div>
 </div>;
}
function BoardRow({row,line,hints:leftHints,left,disabled,play}){
 return <>{Array.from({length:left},(_,i)=><span className="score-tile score-hint" key={'h'+i}>{leftHints[i-(left-leftHints.length)]||''}</span>)}
  {line.map((cell,c)=><button key={c} className={'score-tile score-cell score-cell-'+cell} aria-label={`Row ${row+1}, column ${c+1}: ${cell}`} disabled={disabled||cell!=='closed'} onClick={()=>play(c)}>{cell==='empty'?'x':''}</button>)}
 </>;
}
