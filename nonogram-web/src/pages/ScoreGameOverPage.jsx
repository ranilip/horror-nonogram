import {useEffect,useRef,useState} from 'react';
import {useLocation,useNavigate} from 'react-router-dom';
import {saveScore} from '../services/gameApi';
import './ScoreModePage.css';
import './RecordsPages.css';
export default function ScoreGameOverPage(){
 const navigate=useNavigate();
 // The finished run's score is saved once for a logged-in player; guests are told to log in.
 const score=useLocation().state?.score;
 const [note,setNote]=useState('');
 const saved=useRef(false);
 useEffect(()=>{
  if(!Number.isFinite(score)||saved.current)return;
  saved.current=true;
  if(score<=0){setNote('Score 0');return;}
  setNote('Score '+score);
  saveScore(Math.max(0,score)).then(()=>setNote('Score '+score+' saved to the leaderboard')).catch(e=>setNote('Score '+score+' · '+e.message));
 },[score]);
 return <div className="score-page score-game-over">
  <h1 className="score-title score-over-title">Game Over</h1>
  <p className="score-over-note" role="status">{note}</p>
  <button className="score-new-game" onClick={()=>navigate('/score',{replace:true})}>New Game</button>
 </div>;
}
