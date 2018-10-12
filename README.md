# Driver Tracker Bot

A bot for slack which monitors updates of the receipt printer drivers.
The bot checks for updates and notifies you when an update is available every day at the same time.

### Vendors

- Atol (only 10.x versions)
- Atol (only 9.x versions)
- Dreamkas
- Shtrih M

### Build 

```sbt assembly``` - build jar file

### Usage

```java -jar driver-tracker-bot-1.0.jar $SLACK_TOKEN```
