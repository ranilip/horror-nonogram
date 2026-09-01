import {useEffect,useState} from 'react';
import {getLeaderboard} from '../services/gameApi';
import './RecordsPages.css';

// Registered players from the database, each with their best Score Mode result.
export default function LeaderboardPage(){
 const [rows,setRows]=useState(null),[message,setMessage]=useState('');
 useEffect(()=>{let live=true;getLeaderboard().then(r=>{if(live){setRows(r);setMessage(r.length?'':'No scores yet. Register and play Score Mode.');}}).catch(e=>live&&setMessage(e.message));return()=>{live=false;};},[]);
 return <div className="score-page records-page">
  <h1 className="score-title leader-title">Leaderboard</h1>
  <div className="leader-table" role="table" aria-label="Best scores of registered players">
   <div className="leader-row leader-head" role="row"><span role="columnheader">#</span><span role="columnheader">Player</span><span role="columnheader">Best score</span><span role="columnheader">Games</span><span role="columnheader">Last played</span></div>
   {rows?.map(r=><div className={'leader-row'+(r.rank<=3?' leader-top':'')} role="row" key={r.player}>
    <span role="cell" className="leader-rank">{r.rank}</span>
    <span role="cell" className="leader-player">{r.player}</span>
    <strong role="cell">{r.bestScore}</strong>
    <span role="cell">{r.games}</span>
    <span role="cell">{r.lastPlayed?new Date(r.lastPlayed).toLocaleDateString():''}</span>
   </div>)}
   {(message||!rows)&&<p className="records-message" role="status">{message||'Loading…'}</p>}
  </div>
 </div>;
}
