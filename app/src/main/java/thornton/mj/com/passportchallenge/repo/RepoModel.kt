package thornton.mj.com.passportchallenge.repo


import thornton.mj.com.passportchallenge.util.NetManager
import java.util.ArrayList

class RepoModel(val netManager : NetManager) {

    private val localDataSource = RepoLocalDataSource()
    private val remoteDataSource = RepoRemoteDataSource()


    fun getProfiles(onRepositoryReadyCallback: OnRepositoryReadyCallback){


        netManager.isConnectedToInternet?.let {
            if(it) {
                remoteDataSource.getProfiles(object : RepoRemoteDataSource.OnRepoRemoteReadyCallback {
                    override fun onRemoteDataReady(data: ArrayList<Profile>) {
                        localDataSource.saveProfiles(data)
                        onRepositoryReadyCallback.onDataReady(data)
                    }
                })
            } else {
                localDataSource.getProfiles(object : RepoLocalDataSource.OnRepoLocalReadyCallback{
                    override fun onLocalDataReady(data: ArrayList<Profile>) {
                        onRepositoryReadyCallback.onDataReady(data)
                    }
                })
            }
        }

    }


    // Interface used for communication between VM
    interface OnRepositoryReadyCallback {
        fun onDataReady(data : ArrayList<Profile>)
    }

}