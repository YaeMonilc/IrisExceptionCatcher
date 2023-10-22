interface ThreadExceptionHandle {
    fun handle(thread: Thread, exception: Exception)
}