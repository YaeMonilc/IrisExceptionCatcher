import java.lang.NullPointerException

fun main() {
    val irisExceptionCatcher = IrisExceptionCatcher()
    irisExceptionCatcher
        .setThreadExceptionCatchListener(object : ThreadExceptionCatchListener {
            override fun onStart(thread: Thread, exception: Exception): Boolean {
                println("Start Handle")
                return thread.name.contains("New")
            }

            override fun onEnd(thread: Thread, exception: Exception) {
                println("End Handle")
            }

        })
        .setDefaultThreadExceptionHandle(object : ThreadExceptionHandle {
            override fun handle(thread: Thread, exception: Exception) {
                println("Default Handle ${thread.name}")
            }
        })
        .addThreadExceptionHandleByName("ThreadNew", object : ThreadExceptionHandle {
            override fun handle(thread: Thread, exception: Exception) {
                println("Diy Handle ${thread.name} ${exception.message}")
            }
        })

    Thread {
        throw NullPointerException("This is a exception message")
    }.apply {
        name = "ThreadNew"
    }.start()

    Thread {
        Thread.sleep(2000)
        throw NullPointerException("This is a default handle")
    }.apply {
        name = "DefaultHandle"
    }.start()
}