// Story Mode teaser: one ordered step table. Text comes from the original story.txt
// (Nonogram/src/main/resources), art from the original 1920×1080 PNGs in the same folder.
//   text   → ends on Enter / Space / Right Arrow / tap
//   scare  → ends after `duration` ms, fires once per playback
//   puzzle → solved → next, time out → fail
//   ending → replay / exit
// audio: 'sea' | 'room' | 'steps' (level 0…1 = how close the footsteps are) | 'hush'

export const ART = {
  arrival: '/images/story/opening.webp',
  room: '/images/story/room.webp',
  keeper: '/images/story/scream-man.webp',
};

// Original window face: story.txt "~(live: null time: 60) 7 0011100…" (width 7, unlimited lives, 60 s).
export const FACE = {
  rows: ['0011100', '0111110', '1101011', '1101011', '1111111', '0100010', '0100010', '0111110'],
  lives: null,
  time: 60,
};
export const faceGrid = () => FACE.rows.map(row => [...row].map(Number));

export const SCARE_MS = 1100;
export const FIRST_STEP = 's0-0';

const SCENES = [
  {art: 'arrival', chapter: 'I', place: 'The crossing', audio: 'sea', lines: [
    'The cold wind slaps my face. Rain taps the wooden boat. Ahead, a rocky island wrapped in fog.',
    'From time to time, I catch a glimpse of a blinking light. The lighthouse.',
    'My companion sits next to me. Silent. His eyes are clouded, like dried salt. We have never met before, but we will be stuck here together for weeks.',
    'An old man stands on the shore. He says nothing. Just nods and points toward a wooden house. Then disappears into the mist.',
  ]},
  {art: 'room', chapter: 'II', place: 'The keepers’ room', audio: 'room', lines: [
    'Inside the house, everything stinks of mold and dampness. Two beds. A rusty stove. A table.',
    'I throw my things onto one bed and sit down. Something hard presses against me from beneath the mattress.',
    'I reach inside, tear the lining, and pull out a carved figurine. A mermaid. Made of yellowed bone, worn smooth.',
    'Her eyes are hollow, her mouth slightly open. I swear she is staring at me.',
  ]},
  {art: 'room', chapter: 'III', place: 'The first night', audio: 'steps', approach: [.25, .6, 1], lines: [
    'Night falls. My first shift. I descend to the steam signal. Hot air sears my face. The iron breathes.',
    'I shovel coal into the furnace. It roars. Suddenly, a sound. Metal scraping.',
    'I turn. In the window…',
  ]},
];
// The lines that follow the window puzzle in story.txt.
const EPILOGUE = [
  {speaker: 'Narrator', text: 'Face. Pale. Hollow.'},
  {speaker: 'You', text: '«Who’s there?..»'},
  {speaker: 'Narrator', text: 'Silence.'},
];

function build() {
  const steps = [];
  SCENES.forEach((scene, si) => scene.lines.forEach((text, li) => steps.push({
    id: `s${si}-${li}`, kind: 'text', art: scene.art, chapter: scene.chapter, place: scene.place,
    speaker: 'Narrator', text, audio: scene.audio, level: scene.approach?.[li] ?? 1,
  })));
  steps.push({id: 'scare', kind: 'scare', art: 'keeper', duration: SCARE_MS});
  steps.push({id: 'puzzle', kind: 'puzzle', fail: 'lost'});
  EPILOGUE.forEach((line, li) => steps.push({
    id: `e-${li}`, kind: 'text', art: 'room', chapter: 'III', place: 'The first night',
    speaker: line.speaker, text: line.text, audio: 'hush', level: 1,
  }));
  steps.push({id: 'ending', kind: 'ending', won: true, art: 'keeper', audio: 'sea', level: .45});
  steps.push({id: 'lost', kind: 'ending', won: false, art: 'arrival', audio: 'sea', level: .45});
  const linear = steps.filter(s => s.id !== 'lost');
  linear.forEach((s, i) => { if (i < linear.length - 1) s.next = linear[i + 1].id; });
  return steps;
}
export const STEPS = build();
const byId = new Map(STEPS.map(s => [s.id, s]));
export const stepById = id => byId.get(id) ?? byId.get(FIRST_STEP);
export const newPlayback = () => ({step: FIRST_STEP, play: Math.random().toString(36).slice(2)});
// A reload never replays the screamer: it resumes at the puzzle.
export function restorePlayback(saved) {
  if (!saved || typeof saved.step !== 'string' || !byId.has(saved.step) || typeof saved.play !== 'string') return newPlayback();
  return saved.step === 'scare' ? {...saved, step: 'puzzle'} : saved;
}
