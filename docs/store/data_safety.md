# Play Console — Data safety answers

**Does your app collect or share any of the required user data types?** Yes.
**Is all of the user data collected by your app encrypted in transit?** Yes.
**Do you provide a way for users to request that their data is deleted?** Yes — in-app (Settings → Delete account) and https://adilxankenesov.github.io/Puzzle15_Game/account-deletion

| Data type (Play category) | Collected | Shared | Optional? | Purposes |
|---|---|---|---|---|
| Personal info → Name (player name) | Yes | No | Optional (guest still picks a name) | App functionality |
| Personal info → Email address (Google sign-in) | Yes | No | Optional (guest play) | Account management |
| Personal info → User IDs (Firebase UID) | Yes | No | Required | App functionality, Account management |
| App activity → Other user-generated content (best moves/time) | Yes | No | Required to appear on the leaderboard | App functionality |
| App activity → App interactions (Analytics) | Yes | No | Required | Analytics |
| App info and performance → Diagnostics (Analytics) | Yes | No | Required | Analytics |
| Device or other IDs (Analytics app instance ID) | Yes | No | Required | Analytics |

"Shared" is No: Firebase is a service provider processing data on our behalf.

**Ads:** the app contains no ads.
**Target audience:** 13+ (not designed for children).
**Content rating:** Everyone — no violence, no user chat, leaderboard shows player-chosen names only.
