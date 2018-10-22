package thornton.mj.com.passportchallenge.repo.room

import android.arch.persistence.room.*
import android.content.Context
import thornton.mj.com.passportchallenge.repo.Profile

@Database(entities = [Profile::class], version = 1)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}