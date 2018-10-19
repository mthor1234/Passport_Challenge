package thornton.mj.com.passportchallenge.repo

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

// Holds the Local Data
class RepoLocalDataSource() {

    fun getProfiles(): Observable<ArrayList<Profile>> {
        var arrayList = ArrayList<Profile>()
        arrayList.add(Profile(1, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Football")))
        arrayList.add(Profile(2, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Snowboarding")))
        arrayList.add(Profile(3, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, MMA")))
        arrayList.add(Profile(4, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
        arrayList.add(Profile(5, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
        arrayList.add(Profile(6, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
        arrayList.add(Profile(7, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))

        return Observable.just(arrayList).delay(2, TimeUnit.SECONDS)
    }

        fun saveProfiles(arrayList: ArrayList<Profile>) : Completable {
            //The Completable class represents a deferred computation without any value but only indication for completion or exception.
            return Single.just(1).delay(1,TimeUnit.SECONDS).toCompletable()
        }
}