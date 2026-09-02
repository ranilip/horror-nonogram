# Server notes

How to run it: see the main README.

## What was changed in the original Java code
- Filled the empty files `RatingService`, `RatingServiceJPA`, `RatingServiceRestClient`, `LeaderboardRest`,
  `CommentServiceRest`, `RatingServiceRest` (the project did not compile without them).
- `ScoreServiceRest.addScore` read the player from a path variable that is not in the URL; now from the session.
- `UserServiceRest`: login stores the session; `/me`, `/logout`; register validates email/password.
- Passwords hashed (`PasswordHash`, PBKDF2); old plain-text rows still log in.
- `User.login` accepts `player` from the React sign-up form; the password is never returned in JSON.
- `Comment` has `rating` (stars given with the comment) and `demo`; comments sorted newest first.
- `pom.xml`: `start-class` = GameStudioServer (two main classes existed). `application.properties`: horror_nonogram.

## Deploying
The preview host serves static files only. Leaderboard/Comments need this Java server and PostgreSQL running
somewhere (e.g. a VPS or a Java PaaS with a Postgres add-on) and /api routed to it.
