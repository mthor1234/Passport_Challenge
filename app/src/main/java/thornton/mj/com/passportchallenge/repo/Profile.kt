package thornton.mj.com.passportchallenge.repo

// Holds the user's information to build a profile
data class Profile(val id : Int, val name : String, val age : Int, val gender : String, val hobbies : ArrayList<String>) {

    private var sb = StringBuilder()


    fun getStringOfHobbies() : String{
        hobbies.forEach {
            sb.append(it)
            sb.append("\t")
        }
        return sb.toString()
    }
}