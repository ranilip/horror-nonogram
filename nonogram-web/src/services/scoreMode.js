// Browser port of the original Java ScoreMode, Game, Field and Generate classes.
// Original scoring; random dimensions 5–8 (at most 8 × 8, requested 2026-11-20). No Java server call.
export function generatePuzzle(min = 5, max = 8, random = Math.random) {
  const integer = () => min + Math.floor(random() * (max - min + 1));
  const rows = integer(), cols = integer();
  const grid = Array.from({length: rows}, () => Array.from({length: cols}, () => random() < 0.5 ? 0 : 1));
  grid[Math.floor(random() * rows)][Math.floor(random() * cols)] = 1;
  return grid;
}
export function newGame(solution = generatePuzzle()) {
  return {solution, cells: solution.map(row => row.map(() => 'closed')), lives: 3, currentScore: 0, totalScore: 0, round: 1, status: 'playing'};
}
export function move(game, row, col) {
  if (game.status !== 'playing' || game.cells[row]?.[col] !== 'closed') return game;
  const correct = game.solution[row][col] === 1;
  const cells = game.cells.map(line => [...line]);
  cells[row][col] = correct ? 'correct' : 'empty';
  const lives = game.lives - (correct ? 0 : 1);
  const currentScore = game.currentScore - (correct ? 0 : 50);
  const solved = game.solution.every((line, r) => line.every((cell, c) => !cell || cells[r][c] === 'correct'));
  const reward = solved ? game.solution.flat().filter(Boolean).length * 250 : 0;
  return {...game, cells, lives, currentScore: solved ? 0 : currentScore,
    totalScore: game.totalScore + (solved ? currentScore + reward : 0),
    status: solved ? 'solved' : lives <= 0 ? 'failed' : 'playing'};
}
export function nextPuzzle(game, solution = generatePuzzle()) {
  if (game.status !== 'solved') return game;
  return {...game, solution, cells: solution.map(row => row.map(() => 'closed')), round: game.round + 1, status: 'playing'};
}
export function hints(solution, cells) {
  const result = [];
  let length = 0, complete = true;
  for (let i = 0; i <= solution.length; i++) {
    if (solution[i] === 1) {length++; complete = complete && cells[i] === 'correct';}
    else if (length) {result.push(complete ? 'x' : length); length = 0; complete = true;}
  }
  return result.length ? result : [0];
}

// GameLoopUI.handleSolved rewards the completed puzzle and immediately resets it.
export function playTurn(game, row, col) {
  const played = move(game, row, col);
  return played.status === 'solved' ? nextPuzzle(played) : played;
}
