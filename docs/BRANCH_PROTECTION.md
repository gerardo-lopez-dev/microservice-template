# Branch Protection Rules

These rules apply to the `main` branch and must be configured manually in
GitHub repository settings for every repository cloned from this template.

## Required Rules

| Rule | Value | Rationale |
|---|---|---|
| Require a pull request before merging | Enabled | No direct pushes to `main` |
| Required approvals | 1 | At least one reviewer must approve |
| Dismiss stale approvals on new commits | Enabled | New commits invalidate previous reviews |
| Require conversation resolution | Enabled | All review threads must be resolved |
| Require status checks to pass | Enabled | CI must be green before merge |
| Status checks required | `lint`, `test`, `build`, `coverage` | All CI jobs must pass |
| Do not allow bypassing | Enabled | Administrators must follow the same rules |
| Do not allow force pushes | Enabled | Prevent history rewriting |
| Do not allow deletions | Enabled | Prevent accidental branch deletion |

## How to Apply

1. Go to the repository on GitHub → **Settings** → **Branches**
2. Under "Branch protection rules", click **Add rule**
3. In "Branch name pattern", enter `main`
4. Enable each rule from the table above
5. In "Status checks that are required", search and select: `lint`, `test`,
   `build`, `coverage`
   - Note: these check names must match the job names in
     `.github/workflows/ci.yml`
   - Run CI at least once on `main` after setting up the workflow for the
     check names to appear in the list
6. Click **Create** at the bottom of the page

## Verification

After applying the rules, verify:

1. A direct push to `main` is rejected:
   ```bash
   git checkout main && touch test.txt && git add test.txt && \
   git commit -m "force test" && git push origin main
   # → ERROR: refusing to allow a Personal Access Token to create
   #   or update .git/refs/heads/main
   ```

2. A PR without green CI cannot be merged:
   - Create a PR that breaks a test
   - The merge button should be disabled
   - The status check list should show `test` as failed
