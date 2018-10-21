package thornton.mj.com.passportchallenge.repo

// Holds the user's information to build a profile
class Profile(var dbID : String, var id : Int, var profileName : String, var age: Int, var gender: String,
              var hobbies: ArrayList<String> ) {

    private var sb = StringBuilder()


    fun getStringOfHobbies() : String{
        hobbies.forEach {
            sb.append(it)
            sb.append("\t")
        }
        return sb.toString()
    }

    fun printProfile(){
        println("id : $id Name: $profileName  Age: $age  Gender: $gender  Hobbies: ${getStringOfHobbies()}")
    }
}