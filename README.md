# Passport_Challenge

Utilizes a MVVM with a Repository(s), DataBinding, and LiveData Design Pattern.

Decided to use a Master/Detail layout to easily display profile views while supporting wider screen devices like Tablets


Firebase backend.



Issues:

*** Having trouble on deciding how handle the ViewModel between the Master and Detail Activities.

   - Trying to figure out how to share it since the data is the same, but using ViewModelProviders inside of the Master Activity     keeps the scope to the Master Activity. 
    - Instead of sharing the ViewModel, usually in a MVVM you want a ViewModel for each View. I could create a new ViewModel for the DetailActivity, but then I am just recreating / refetching the data and finding the element that was clicked. If there is a database of 1,000,000 items, this could really affect performance. Seems unecessary.... If I create an instance to hold the clicked Item in the VM, I would need to somehow pass that information to the newly created ViewModel for the DetailActivity.  Could I pass the selected item as a parcelable and update the ViewModel for the Activity class?

    - If I can't figure out how to share the ViewModel, then I'll pass the data to the Detail Activity/Fragment in a Bundle/Extra but I'd like it to be seamless with the MVVM pattern and update the data in realtime instead of relying on an update data method. 


    - Was stuck so I used Gson to package up the selected Profile and pass it in the Bundle to the DetailActivity, then retrieve it in the DetailFragment


 
*** Tried using Dagger 2 for dependency injection but am struggling. Will come back if there is time
