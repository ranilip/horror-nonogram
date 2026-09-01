import {useState} from 'react';
import confirmedLayouts from './confirmed-layouts.json';
export const W = 1920, H = 1080;
export const KEY = 'nonogram-layout-v2';
export const defaults = [
 {id:'home',label:'Головна',image:'home-def.png',hover:'home-hov.png',to:'/',x:236.16,y:231.984,w:129.984,h:125.604},
 {id:'leader',label:'Рекорди',image:'leader-def.png',hover:'leader-hov.png',to:'/leader',x:236.16,y:359.856,w:129.984,h:138.024},
 {id:'rule',label:'Правила',image:'rule-def.png',hover:'rule-hov.png',to:'/rule',x:234.816,y:500.688,w:129.984,h:138.024},
 {id:'comments',label:'Коментарі',image:'comment-def.png',hover:'comment-hov.png',to:'/comments',x:235.968,y:642.276,w:129.984,h:138.024},
 {id:'login',label:'Вхід',image:'log-def.png',hover:'log-hov.png',to:'/login',x:235.776,y:785.808,w:129.984,h:138.024},
 {id:'story',label:'Story Mode',image:'mode.png',hover:'mode-hav.png',to:'/story',text:'Story Mode',x:618.816,y:694.332,w:431.04,h:114.048},
 {id:'score',label:'Score Mode',image:'mode.png',hover:'mode-hav.png',to:'/score',text:'Score Mode',x:1237.824,y:694.008,w:431.04,h:114.048}
];
export function validate(value,page='home'){
 const base=pageDefaults(page);
 if(!Array.isArray(value)||!(value.length===base.length||(base.some(i=>i.id==='error')&&value.length===base.length-1&&!value.some(i=>i.id==='error'))))throw new Error('Потрібні координати всіх елементів цієї сторінки.');
 return base.map(d=>{const v=value.find(v=>v.id===d.id);if(!v&&d.id==='error')return {...d};if(!v||!['x','y','w','h'].every(k=>Number.isFinite(v[k]))||v.w<0||v.h<0)throw new Error('Некоректні координати: '+d.label);return {...d,x:v.x,y:v.y,w:v.w,h:v.h};});
}
// Positions come only from the code (defaults + confirmed-layouts.json); the in-browser layout editor was removed.
export function readLayout(page='home'){return pageDefaults(page).map(d=>({...d}));}
export function useLayout(page='home'){return useState(()=>readLayout(page));}
export function placement(b){return {left:b.x/W*100+'%',top:b.y/H*100+'%',width:b.w/W*100+'%',height:b.h/H*100+'%'};}
export function ButtonArt({item}){return <><img src={'/images/icon/'+item.image} alt="" draggable="false"/>{item.text&&<span>{item.text}</span>}</>;}

export const pages={home:{label:'Головна',route:'/',background:'home.webp'},login:{label:'Вхід',route:'/login',background:'login.webp'},signup:{label:'Реєстрація',route:'/signup',background:'signup.webp'},rule:{label:'Правила',route:'/rule',background:'rule.webp'},score:{label:'Score Mode · Гра',route:'/score',background:'back.webp'},scoreover:{label:'Score Mode · Game Over',route:'/score/game-over',background:'back.webp'},story:{label:'Story Mode · Нонограма',route:'/story',background:'back.webp'},leader:{label:'Рекорди',route:'/leader',background:'back.webp'},comments:{label:'Коментарі',route:'/comments',background:'back.webp'}};
const content=(id,label,selector,x,y,w,h)=>({id,label,selector,kind:'content',x,y,w,h});
const pageContent={
 story:[content('title','Заголовок','.score-title',610,235,1050,100),content('stats','Час і прогрес','.score-stats',650,400,250,330),content('board','Обличчя з підказками','.score-board-area',930,345,700,540),content('note','Підказка','.score-tools',650,880,960,70)],
 scoreover:[content('title','Заголовок Game Over','.score-over-title',610,390,1050,140),content('saved','Збереження результату','.score-over-note',610,535,1050,60),content('restart','Кнопка New Game','.score-new-game',920,620,430,114)],
 leader:[content('title','Заголовок','.leader-title',610,235,1050,100),content('table','Таблиця рекордів','.leader-table',650,370,960,560)],
 comments:[content('title','Заголовок','.comments-title',610,235,1050,100),content('summary','Середня оцінка','.comments-summary',650,345,960,50),content('list','Список коментарів','.comments-list',650,405,960,345),content('form','Зірки і поле коментаря','.comment-form',650,765,960,175)],
 score:[content('title','Заголовок','.score-title',610,235,1050,100),content('stats','Життя та рахунок','.score-stats',650,400,250,330),content('board','Поле з підказками','.score-board-area',950,365,650,470),content('tools','Джерело малюнка','.score-tools',650,850,960,95)],
 login:[content('error','Повідомлення помилки','.loginMessege',599.21,53.45,831.3,84.4),content('title','Заголовок','.login-form h1',650,220,720,100),content('username','Поле логіна','.login-form input:nth-of-type(1)',650,390,720,90),content('password','Поле пароля','.login-form input:nth-of-type(2)',650,540,720,90),content('register','Посилання на реєстрацію','.signUpText',650,690,720,55),content('submit','Кнопка входу','.login-form .iconMod',780,820,450,115)],
 signup:[content('error','Повідомлення помилки','.loginMessege',599.21,53.45,831.3,84.4),content('title','Заголовок','.sign h1',650,200,720,100),content('username','Поле логіна','.sign input:nth-of-type(1)',650,350,720,85),content('email','Поле email','.sign input:nth-of-type(2)',650,455,720,85),content('password','Поле пароля','.sign input:nth-of-type(3)',650,560,720,85),content('confirm','Підтвердження пароля','.sign input:nth-of-type(4)',650,665,720,85),content('submit','Кнопка реєстрації','.sign .iconMod',780,850,450,115)],
 rule:[content('title','Заголовок','.rules-container h1',690,220,900,90),content('objective','Мета гри','.rules-container section:nth-child(2)',690,390,900,120),content('grid','Ігрове поле','.rules-container section:nth-child(3)',690,540,900,120),content('clues','Підказки','.rules-container section:nth-child(4)',690,690,900,230)]
};
export function pageDefaults(page='home'){
 const base=page==='home'?defaults:defaults.filter(i=>!i.text).concat(pageContent[page]||[]);
 const confirmed=confirmedLayouts[page];
 if(!confirmed)return base;
 return base.map(item=>{
  if(!confirmed[item.id])return item;
  const [left,top,width,height]=confirmed[item.id];
  return {...item,x:left*W/100,y:top*H/100,w:width*W/100,h:height*H/100};
 });
}
export function storageKey(page='home'){return page==='home'?KEY:KEY+'-'+page+(page==='score'?'-pictures-v1':'')+(confirmedLayouts[page]?(['login','signup'].includes(page)?'-confirmed-v2':'-confirmed-v1'):'');}
