package thornton.mj.com.passportchallenge.repo


import android.content.Context
import thornton.mj.com.passportchallenge.util.NetManager
import java.util.ArrayList

// Abstraction layer for the local and remote repos. Allows ViewModel to deal directly with this

class RepoModel(val netManager : NetManager) {

    private val localDataSource = RepoLocalDataSource()
    private val remoteDataSource = RepoRemoteDataSource()


    fun getProfiles(onRepositoryReadyCallback: OnRepositoryReadyCallback, context : Context){

        netManager.isConnectedToInternet?.let {
            if(it) {
                remoteDataSource.getProfiles(object : RepoRemoteDataSource.OnRepoRemoteReadyCallback {
                    override fun onRemoteDataReady(data: ArrayList<Profile>) {
                        localDataSource.saveProfiles(data, context)
                        onRepositoryReadyCallback.onDataReady(data)
                    }
                })
            } else {
                localDataSource.getProfiles(object : RepoLocalDataSource.OnRepoLocalReadyCallback{
                    override fun onLocalDataReady(data: ArrayList<Profile>) {
                        onRepositoryReadyCallback.onDataReady(data)
                    }
                }, context)
            }
        }
    }


    // Interface used for communication between VM
    interface OnRepositoryReadyCallback {
        fun onDataReady(data : ArrayList<Profile>)
    }

}