// Stars made from the game's own tile textures: a light tile = given star, a dark tile = empty star.
// (No star artwork exists in the project assets.)
export function Stars({value=0,label}){
 const shown=Math.round(value*2)/2;
 return <span className="stars" role="img" aria-label={label||`${value} of 5 stars`}>
  {[1,2,3,4,5].map(i=><span key={i} className={'star '+(shown>=i?'star-full':shown>=i-.5?'star-half':'star-empty')}/>)}
 </span>;
}
export function StarPicker({value,onChange}){
 return <span className="stars star-picker" role="radiogroup" aria-label="Your rating">
  {[1,2,3,4,5].map(i=><button key={i} type="button" role="radio" aria-checked={value===i} aria-label={i+(i===1?' star':' stars')}
   className={'star '+(value>=i?'star-full':'star-empty')} onClick={()=>onChange(value===i?0:i)}/>)}
 </span>;
}
