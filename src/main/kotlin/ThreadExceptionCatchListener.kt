import java.lang.Exception

interface ThreadExceptionCatchListener {
    fun onStart(thread: Thread, exception: Exception): Boolean
    fun onEnd(thread: Thread, exception: Exception)
}