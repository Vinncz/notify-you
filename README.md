# Releases

### NotifyYou v1.1.1 
Update released on December 24th, 2023.
\
\
**Changelogs**:
- Fixed the bug where NotifyYou only launches once if you clicked the notification\
  (Now, it'll always open NotifyYou, should any notification gets pressed)
- Added support for "click BACK twice to go back" on the edit screen
- Whenever you press the "dismiss" button on a notification, the homescreen will now refresh along with it
- Fixed the bug where NotifyYou on devices below Android 13 cannot send a notification nor an alarm
- Whenever a pinned TileItem gets edited, the notification associated with said TileItem will now get automatically edited aswel.

Download this release, here:\
[NotifyYou v1.1.1](https://github.com/Vinncz/notify-you/tree/main/app/release)

# Issues
Android 14 came with a new feature, where it grants the user to forcefully remove the previously persistent notification. 

Currently, there are no workarounds to address this issue -- one idea is to observe if any of the notification gets dismissed, then put on a new one to replace the dismissed.

However, that may come with the user having to grant the READ_ALL_NOTIFICATIONS, which is very suspicious coming from a productivity app.

Therefore, for the forseeable future, we will be consulting together at the best approach to patch the following issue.

Thank you for your understanding.
