// REST calls to the Spring server (GameStudioServer). In development CRA proxies /api to :8080 (setupProxy.js).
const GAME = 'nonogram';

async function request(path, body) {
  let response;
  try {
    response = await fetch('/api' + path, body === undefined ? {credentials: 'same-origin'} : {
      method: 'POST', credentials: 'same-origin',
      headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body),
    });
  } catch {
    throw new Error('Error connecting to server');
  }
  const type = response.headers.get('content-type') || '';
  if (!response.ok) throw new Error(response.status >= 500 || type.includes('html') ? 'Error connecting to server' : await response.text());
  return type.includes('json') ? response.json() : response.text();
}

export const getLeaderboard = () => request('/leaderboard/' + GAME);
export const getComments = () => request('/comment/' + GAME);
export const getAverageRating = () => request('/rating/' + GAME);
export const postComment = (comment, rating) => request('/comment', {game: GAME, comment, rating});
export const saveScore = points => request('/score', {game: GAME, points});
export const currentUser = () => request('/user/me');
