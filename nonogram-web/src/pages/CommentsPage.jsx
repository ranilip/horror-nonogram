import {useCallback,useEffect,useState} from 'react';
import {getComments,getAverageRating,postComment} from '../services/gameApi';
import {Stars,StarPicker} from './Stars';
import './RecordsPages.css';

// Comments with stars. Logged in → posted under the player's login; otherwise anonymously.
export default function CommentsPage({loggedUser=null,editorPreview=false}){
 const [items,setItems]=useState(null),[average,setAverage]=useState(0),[message,setMessage]=useState('');
 const [text,setText]=useState(''),[stars,setStars]=useState(0),[status,setStatus]=useState(''),[sending,setSending]=useState(false);
 const reload=useCallback(()=>Promise.all([getComments(),getAverageRating()]).then(([list,avg])=>{setItems(list);setAverage(Number(avg)||0);setMessage(list.length?'':'No comments yet. Be the first.');}).catch(e=>setMessage(e.message)),[]);
 useEffect(()=>{reload();},[reload]);
 const rated=items?.filter(c=>c.rating).length??0;
 async function submit(e){
  e.preventDefault();if(editorPreview||sending)return;
  if(!text.trim()){setStatus('Write a comment first.');return;}
  setSending(true);
  try{setStatus(await postComment(text.trim(),stars||null));setText('');setStars(0);await reload();}
  catch(err){setStatus(err.message);}
  setSending(false);
 }
 return <div className="score-page records-page">
  <h1 className="score-title comments-title">Comments</h1>
  <div className="comments-summary"><Stars value={average} label={`Average rating ${average.toFixed(1)} of 5`}/><span>{average?average.toFixed(1):'–'} / 5 · {rated} {rated===1?'rating':'ratings'}</span></div>
  <div className="comments-list" tabIndex={0} aria-label="Comments">
   {items?.map(c=>{const name=c.player&&c.player!=='null'?c.player:null;return <article className="comment-item" key={c.id}>
    <header><b className={name?'':'comment-anonymous'}>{name||'Anonymous'}</b>{c.rating?<Stars value={c.rating}/>:null}<time>{c.commentedOn?new Date(c.commentedOn).toLocaleDateString():''}</time></header>
    <p>{c.comment}</p>
   </article>;})}
   {(message||!items)&&<p className="records-message" role="status">{message||'Loading…'}</p>}
  </div>
  <form className="comment-form" onSubmit={submit}>
   <div className="comment-form-top"><StarPicker value={stars} onChange={setStars}/><span className="comment-as">{loggedUser?'Posting as '+loggedUser:'Posting anonymously · log in to use your nickname'}</span></div>
   <div className="comment-form-row"><input className="input comment-input" value={text} onChange={e=>setText(e.target.value)} maxLength={255} placeholder="Leave a comment" aria-label="Comment"/><button type="submit" className="score-new-game comment-send" disabled={sending}>Post</button></div>
   {status&&<p className="comment-status" role="status">{status}</p>}
  </form>
 </div>;
}
