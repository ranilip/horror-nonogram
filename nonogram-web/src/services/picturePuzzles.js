import {generatePuzzle} from './scoreMode';

export function pixelGrid(rgba, size) {
  const values = Array.from({length: size * size}, (_, i) => {
    const p = i * 4, alpha = rgba[p + 3] / 255;
    return Math.round((.2126 * rgba[p] + .7152 * rgba[p + 1] + .0722 * rgba[p + 2]) * alpha + 255 * (1 - alpha));
  });
  const histogram = Array(256).fill(0);
  values.forEach(v => histogram[v]++);
  const sum = values.reduce((a, b) => a + b, 0);
  let weight = 0, accumulated = 0, best = -1, threshold = 127;
  for (let t = 0; t < 255; t++) {
    weight += histogram[t]; accumulated += t * histogram[t];
    if (!weight || weight === values.length) continue;
    const difference = accumulated / weight - (sum - accumulated) / (values.length - weight);
    const variance = weight * (values.length - weight) * difference * difference;
    if (variance > best) {best = variance; threshold = t;}
  }
  const grid = Array.from({length: size}, (_, r) => Array.from({length: size}, (_, c) => values[r * size + c] <= threshold ? 1 : 0));
  const count = grid.flat().filter(Boolean).length;
  if (!count || count === size * size) throw new Error('The image has no usable contrast.');
  return grid;
}

async function rasterize(blob, size, signal) {
  const url = URL.createObjectURL(blob);
  try {
    const img = new Image();
    await new Promise((resolve, reject) => {
      const stop = () => {img.src = ''; reject(new Error('Picture timeout.'));};
      if (signal.aborted) return stop();
      signal.addEventListener('abort', stop, {once: true});
      const cleanup = () => signal.removeEventListener('abort', stop);
      img.onload = () => {cleanup(); resolve();};
      img.onerror = () => {cleanup(); reject(new Error('Picture could not be read.'));};
      img.src = url;
    });
    const canvas = document.createElement('canvas');
    canvas.width = canvas.height = size;
    const ctx = canvas.getContext('2d', {willReadFrequently: true});
    ctx.fillStyle = '#fff'; ctx.fillRect(0, 0, size, size);
    ctx.imageSmoothingEnabled = false;
    ctx.drawImage(img, 0, 0, size, size);
    return pixelGrid(ctx.getImageData(0, 0, size, size).data, size);
  } finally {URL.revokeObjectURL(url);}
}

export async function picturePuzzle(size = 16, {fetcher = fetch, decode = rasterize, timeout = 6000} = {}) {
  const controller = new AbortController();
  let timer;
  try {
    const seed = crypto.randomUUID();
    const url = 'https://api.dicebear.com/10.x/pixel-art/svg?' + new URLSearchParams({seed, backgroundColor: 'ffffff'});
    const work = async () => {
      const response = await fetcher(url, {signal: controller.signal, credentials: 'omit'});
      if (!response.ok) throw new Error('Picture API unavailable.');
      return {solution: await decode(await response.blob(), size, controller.signal), origin: 'api'};
    };
    const deadline = new Promise((_, reject) => {timer = setTimeout(() => {controller.abort(); reject(new Error('Picture timeout.'));}, timeout);});
    return await Promise.race([work(), deadline]);
  } catch {
    return {solution: generatePuzzle(), origin: 'fallback'};
  } finally {clearTimeout(timer);}
}

export function nextPicture(mode, size) {
  return mode === 'random' ? Promise.resolve({solution: generatePuzzle(), origin: 'random'}) : picturePuzzle(size);
}
