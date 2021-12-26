# Notification App

![App screenshot!](/screenshot.png "App screenshot")

Features:
- Tracks device's notifications;
- Shows the last 20;
- Shows the device's active notifications;

Main Libraries:
- Room: ORM library
- Coroutine: Multitasking management;
- Koin: Dependency injection;
- Leak Canary: Check memory leak;
- MockK: Mock dependencies;

Reports:
- Please find the test and lint report in the reports folder;
- Leak report is automatically generated when you run this app in debug mode;

Versioning:
- On this project, I used git-flow. I left the features and release branch on purpose so you can check it;

Improvements:
- Create a class to handle NotificationListener's events. It would remove business logic from the service
    implementation and make the project more testable and less compiling;
- Better naming;
- Improve management of showing and hiding view components/or use Jetpack compose;
- Create smaller and more meaningful commits;

