import {useState} from 'react';
import {toggleSound,soundEnabled} from './sound';
// One shared, remembered sound setting for the whole game.
export default function SoundToggle({className=''}){
 const [enabled,setEnabled]=useState(soundEnabled);
 return <button type="button" className={'sound-toggle '+className} onClick={()=>setEnabled(toggleSound())} aria-pressed={enabled} aria-label={enabled?'Mute sound':'Enable sound'}>{enabled?'Sound on':'Sound off'}</button>;
}
