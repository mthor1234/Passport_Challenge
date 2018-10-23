package thornton.mj.com.passportchallenge.repo.room

import android.arch.persistence.room.*
import thornton.mj.com.passportchallenge.repo.Profile

// Used to interact with Room DB
@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: Profile)

    @Update
    fun updateProfile(profile: Profile)

    @Delete
    fun deleteProfile(profile: Profile)

    @Query("SELECT * FROM Profile WHERE id == :id")
    fun getProfileByName(id: String): List<Profile>

    @Query("SELECT * FROM Profile")
    fun getProfiles(): List<Profile>
    
}

