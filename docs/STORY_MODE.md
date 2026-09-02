# Story Mode teaser and web front end

Base: the deployed preview (horror-nonogram-vira-preview), recovered from its published source maps.
The recovered 23 source files matched the preview byte for byte (SHA-256), and the build reproduced the
deployed bundle names main.acdcc630.js / main.d2790797.css. The tiles come from the preview,
other images and fonts from the original nonogram-web (same SHA-256 as the preview).

## Added
- `/story`: teaser in the preview's style (scaled 16:9 site, fonts Love / Mom / BILLH, stone mode buttons).
  Sea → room → first night (footsteps closing in) → "I turn. In the window…" → 1.1 s screamer →
  window-face nonogram → "Face. Pale. Hollow." / «Who's there?..» / "Silence." → "She saw you." /
  Full story mode coming soon. Lost (time out) → Try again.
- Face puzzle = original from story.txt (7×8, unlimited lives, 60 s), drawn with the Score Mode tiles
  and board code. It is a layout-editor page: "Story Mode · Нонограма".
- Art: original 1920×1080 PNGs from Nonogram/nonogram-web/src/main/resources (nonogram-web/public/images/story).
- Procedural sound (no audio files): ambience, footsteps, screamer, UI clicks/hover, tile fill/mistake,
  solved/failed. "Sound on/off" toggle top-right, remembered.

## Files
New: nonogram-web/src/story/trailer.js, nonogram-web/src/sound.js, nonogram-web/src/SoundToggle.jsx, nonogram-web/src/pages/StoryModePage.jsx/.css,
nonogram-web/src/pages/StoryPuzzlePage.jsx, nonogram-web/public/images/story/*.png.
Changed: nonogram-web/src/App.js (route /story, sound toggle), nonogram-web/src/PageSurface.jsx (story page),
nonogram-web/src/layout.js (story page for the editor), nonogram-web/src/pages/ScoreModePage.jsx (tile sounds only).

## Loading (18.11.2026, approved: "Order + WebP q90")
- Backgrounds and story art are shown from WebP q90 copies (lossy, visually identical): 3.3–3.7 MB → 0.4–0.55 MB
  per background, ~28 MB → ~3.4 MB for all art. The original PNGs stay in nonogram-web/public/images next to them.
- One URL per image: CSS images are CSS variables defined in public/index.html (#art-urls), so CRA no longer
  bundles a second copy under /static/media/.
- Loading order (nonogram-web/src/preload.js, nonogram-web/src/ScaledSite.jsx): a page appears only once its background, sidebar icons,
  controls and fonts are decoded (max 4 s wait), with a short fade; afterwards every other page's art is
  fetched one by one in idle time, so later page changes are instant.
- Not fixable in code: the preview hosting serves everything with `cache-control: max-age=0, must-revalidate`.

## Later changes (20.11.2026)
- Leaderboard and Comments pages (see SERVER.md for the API).
- Layout editor removed; positions come only from layout.js + confirmed-layouts.json.
- Score Mode: no picture source / size choice; pictures at most 8 × 8.
- Scrollbars in the panel-edge colour (#35332d, picked from back.png), nicknames in the star colour (#918a7f).
