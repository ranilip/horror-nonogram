import {useLayoutEffect,useRef} from 'react';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import SignUpPage from './pages/SignUpPage';
import RulePage from './pages/RulePage';
import ScoreModePage from './pages/ScoreModePage';
import ScoreGameOverPage from './pages/ScoreGameOverPage';
import StoryPuzzlePage from './pages/StoryPuzzlePage';
import LeaderboardPage from './pages/LeaderboardPage';
import CommentsPage from './pages/CommentsPage';
import {placement} from './layout';
export default function PageSurface({page,items,loggedUser=null,setLoggedUser=()=>{},editorPreview=false,onStoryComplete}){
 const ref=useRef(null);
 useLayoutEffect(()=>{
  const el=ref.current,container=el.parentElement;
  const resize=()=>{el.style.setProperty('--stage-scale',container.clientWidth/1365);};
  const observer=new ResizeObserver(resize);observer.observe(container);resize();return()=>observer.disconnect();
 },[page]);
 useLayoutEffect(()=>{
  const el=ref.current;
  el.querySelectorAll('.login-form,.sign,.rules-container').forEach(n=>{n.style.display='contents';n.style.position='static';});
  items.filter(i=>i.kind==='content').forEach(i=>{const n=el.querySelector(i.selector);if(!n)return;n.classList.add('button-'+i.id);Object.assign(n.style,placement(i),{position:'absolute',margin:'0',paddingTop:'0',boxSizing:'border-box'});
   if(n.tagName==='H1'){n.style.display='flex';n.style.alignItems='center';n.style.justifyContent='center';}
  });
 },[page,items]);
 return <div ref={ref} className="legacy-stage"><div className="appLayout"><div className="PageContent">
 {page==='home'?<HomePage/>:page==='login'?<LoginPage editorPreview={editorPreview} loggedUser={loggedUser} setLoggedUser={setLoggedUser}/>:page==='signup'?<SignUpPage editorPreview={editorPreview}/>:page==='score'?<ScoreModePage editorPreview={editorPreview}/>:page==='scoreover'?<ScoreGameOverPage/>:page==='story'?<StoryPuzzlePage editorPreview={editorPreview} onComplete={onStoryComplete}/>:page==='leader'?<LeaderboardPage/>:page==='comments'?<CommentsPage loggedUser={loggedUser} editorPreview={editorPreview}/>:<RulePage/>}
 </div></div></div>;
}
