package thornton.mj.com.passportchallenge.repo

import android.content.Context
import android.os.Handler
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import thornton.mj.com.passportchallenge.repo.room.AppDatabase
import thornton.mj.com.passportchallenge.repo.room.ProfileDao
import java.util.*
import java.util.concurrent.TimeUnit

// Holds the Local Data
class RepoLocalDataSource() {

    private var db: AppDatabase? = null
    private var profileDao: ProfileDao? = null

    fun getProfiles(onRepositoryReadyCallback : OnRepoLocalReadyCallback, context : Context){
        var arrayList = ArrayList<Profile>()

//        arrayList.add(Profile("1234", 1, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Football")))
//        arrayList.add(Profile("1234",2, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Snowboarding")))
//        arrayList.add(Profile("1234",3, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, MMA")))
//        arrayList.add(Profile("1234",4, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile("1234",5, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile("1234",6, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile("1234",7, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))

        Observable.fromCallable({
            db = AppDatabase.getAppDataBase(context)
            profileDao = db?.profileDao()

            var pro1 = Profile("1234", 1, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Football"))
            var pro2 = Profile("1234",7, "John Jacobson", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android"))


            with(profileDao){
                this?.insertProfile(pro1)
                this?.insertProfile(pro2)
            }
            db?.profileDao()?.getProfiles()
        }).doOnNext({ list ->
            var finalString = ""
            list?.map { finalString+= it.profileName+" - "
                println("ROOM: " + finalString)
                arrayList.add(it)
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        Handler().postDelayed({ onRepositoryReadyCallback.onLocalDataReady(arrayList) }, 2000)
    }

        fun saveProfiles(arrayList: ArrayList<Profile>) : Completable {
            //The Completable class represents a deferred computation without any value but only indication for completion or exception.
            return Single.just(1).delay(1,TimeUnit.SECONDS).toCompletable()
        }

    interface OnRepoLocalReadyCallback {
        fun onLocalDataReady(data: ArrayList<Profile>)
    }
}