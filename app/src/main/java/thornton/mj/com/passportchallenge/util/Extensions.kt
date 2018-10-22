package thornton.mj.com.passportchallenge.util

fun String.equalsIgnoreCase(other: String) =
        (this as java.lang.String).equalsIgnoreCase(other)