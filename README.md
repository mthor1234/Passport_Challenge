# Passport_Challenge


/////////// LATEST COMMIT //////////////
Submission Push
Summary: Removed Test files



Utilizes a MVVM with a Repository(s), DataBinding, and LiveData Design Pattern.

Decided to use a Master/Detail layout to easily display profile views while supporting wider screen devices like Tablets


Firebase backend using a Realtime Database



If I had more time:

    - I would like to use Dagger2 for dependency injection
    - Handle profile image processing with Glide or Picasso
    - Remain true to the MVVM design pattern (To save time, I started move away from the design and a few classes are messier than I would like)
    - Create a singleton DB to ensure only one instance
    - Explore using RxJava or Coroutines
    - Smarter DB calls / deciding when to use local db vs remote db and keeping eachother updated
    - Nicer layouts
    - Continued to use Databinding past ItemListActivity
    - More toasts / snackbar messages to the user on item changes
    - Implicit camera call for user to take user profile or implicit call to grab a photo from somewhere





Issues:


//////////// VIEWMODEL /////////////////

*** Having trouble on deciding how handle the ViewModel between the Master and Detail Activities.

   - Trying to figure out how to share it since the data is the same, but using ViewModelProviders inside of the Master Activity     keeps the scope to the Master Activity. 
    - Instead of sharing the ViewModel, usually in a MVVM you want a ViewModel for each View. I could create a new ViewModel for the DetailActivity, but then I am just recreating / refetching the data and finding the element that was clicked. If there is a database of 1,000,000 items, this could really affect performance. Seems unecessary.... If I create an instance to hold the clicked Item in the VM, I would need to somehow pass that information to the newly created ViewModel for the DetailActivity.  Could I pass the selected item as a parcelable and update the ViewModel for the Activity class?

    - If I can't figure out how to share the ViewModel, then I'll pass the data to the Detail Activity/Fragment in a Bundle/Extra but I'd like it to be seamless with the MVVM pattern and update the data in realtime instead of relying on an update data method. 


    - Was stuck so I used Gson to package up the selected Profile and pass it in the Bundle to the DetailActivity, then retrieve it in the DetailFragment


//////////// DEPENDENCY INJECTION /////////////////
 
*** Tried using Dagger 2 for dependency injection but am struggling. Will come back if there is time


////////// FIREBASE //////////////////////

   - Need to generate unique ID's -> Firebase has a built in method for it for storing the items. But each user's unique ID can be generated / guaranteed unique by using a HashList

    - Need to figure out how to handle storing / retrieving photos




////////// MAIN SCREEN //////////////////////

    - How should I handle creating a new user profile? Dialog Fragment???
        * Used a Dialog Fragment that pops up and displays a form to the user. FB backend is updated when the user clicks submit

    - How do I add a filter to the RecyclerView?
        * Guessing I make a copy of the mutablelivedata from the database because if I add a filter to the data directly, it will overwrite the data?

////////// DETAIL SCREEN //////////////////////
    
    - Would like to add an onItemLongPress() to bring up a context menu to delete / edit Hobby items on the RecyclerView but this is too much. 
    * Instead, I used a swipe to delete function





