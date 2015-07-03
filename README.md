Android Project Starter Pack "Empty-Shell"
==========================================

 ![Empty Shell][shell]

*This project features all of the core of the business logic that I use in my applications.*

Some of the features included are:

 - Dependency Injection with [Dagger 2](http://google.github.io/dagger/)
 - Reactive Extension via [RxJava](https://github.com/ReactiveX/RxJava) / [RxAndroid](https://github.com/ReactiveX/RxAndroid)
 - View injections from [Butterknife](http://jakewharton.github.io/butterknife/)
 - API Clients by [Retrofit](http://square.github.io/retrofit/)
 - Simple state saving/restoring with [Icepick](https://github.com/frankiesardo/icepick)

Some custom utilities included are:

 - `CompleteRecyclerView` which handled loading messages and empty states
 - Property animators, wraps the native animation framework to provide a consistent API
 - `WeakList` for holding a list of weak references to objects
 - `Typeface` management
 - `Preference` management
 - `ApiEndpoint` switching for dev, staging, and production
 - `Activity`/`Fragment` helper methods for dialogs and toast
 - Complete dependency injection set up
 - Crashlytics
 - Unit Tests
 - Instrumentation Tests


 [shell]: http://i.imgur.com/ljuhxvM.jpg