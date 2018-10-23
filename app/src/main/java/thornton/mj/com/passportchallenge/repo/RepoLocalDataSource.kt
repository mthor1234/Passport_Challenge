package thornton.mj.com.passportchallenge.repo

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import thornton.mj.com.passportchallenge.repo.room.AppDatabase
import thornton.mj.com.passportchallenge.repo.room.ProfileDao
import java.util.*
import java.util.concurrent.TimeUnit

// Holds the Local Data
// Utilizes Room to save to SQLite DB
class RepoLocalDataSource() {

    private var db: AppDatabase? = null
    private var profileDao: ProfileDao? = null

    // Gets profiles from Room / SQLite DB
    fun getProfiles(onRepositoryReadyCallback : OnRepoLocalReadyCallback, context : Context){
        var arrayList = ArrayList<Profile>()

        Observable.fromCallable({
            db = AppDatabase.getAppDataBase(context)
            profileDao = db?.profileDao()

            db?.profileDao()?.getProfiles()
        }).doOnNext({ list ->
            list?.map {
                arrayList.add(it)
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()


        Handler().postDelayed({ onRepositoryReadyCallback.onLocalDataReady(arrayList) }, 2000)
    }

    // Saves profiles to Room / SQLite DB
        fun saveProfiles(arrayList: ArrayList<Profile>, context : Context) : Completable {
            //The Completable class represents a deferred computation without any value but only indication for completion or exception.

                db = AppDatabase.getAppDataBase(context)
                profileDao = db?.profileDao()

            doAsync {
                with(profileDao){
                    arrayList.forEach {
                        this?.insertProfile(it)
                    }
                }
            }
            return Single.just(1).delay(1,TimeUnit.SECONDS).toCompletable()
        }

    interface OnRepoLocalReadyCallback {
        fun onLocalDataReady(data: ArrayList<Profile>)
    }
}