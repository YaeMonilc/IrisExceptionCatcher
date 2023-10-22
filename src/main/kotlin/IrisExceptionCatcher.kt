class IrisExceptionCatcher {
    private val threadExceptionByName: MutableMap<String, ThreadExceptionHandle> = mutableMapOf()
    private val threadExceptionByPid: MutableMap<Long, ThreadExceptionHandle> = mutableMapOf()
    private var defaultThreadExceptionHandle: ThreadExceptionHandle = object : ThreadExceptionHandle {
        override fun handle(thread: Thread, exception: Exception) {
            exception.printStackTrace()
        }
    }
    private var threadExceptionCatchListener: ThreadExceptionCatchListener = object : ThreadExceptionCatchListener {
        override fun onStart(thread: Thread, exception: Exception): Boolean { return true }
        override fun onEnd(thread: Thread, exception: Exception) { }
    }

    init {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            var handleTimes = 0
            throwable as Exception
            if (!threadExceptionCatchListener.onStart(thread, throwable)) {
                throwable.printStackTrace()
                return@setDefaultUncaughtExceptionHandler
            }
            threadExceptionByName.forEach { (threadName, handle) ->
                if (thread.name == threadName) {
                    handle.handle(thread, throwable)
                    handleTimes++
                }
            }
            threadExceptionByPid.forEach { (id, handle) ->
                if (thread.id == id) {
                    handle.handle(thread, throwable)
                    handleTimes++
                }
            }
            if (handleTimes == 0) {
                defaultThreadExceptionHandle.handle(thread, throwable)
            }
            threadExceptionCatchListener.onEnd(thread, throwable)
        }
    }

    fun addThreadExceptionHandleByName(
        threadName: String,
        threadExceptionHandle: ThreadExceptionHandle
    ): IrisExceptionCatcher {
        threadExceptionByName[threadName] = threadExceptionHandle
        return this
    }

    fun addThreadExceptionHandleByName(
        id: Long,
        threadExceptionHandle: ThreadExceptionHandle
    ): IrisExceptionCatcher {
        threadExceptionByPid[id] = threadExceptionHandle
        return this
    }

    fun setThreadExceptionCatchListener(
        threadExceptionCatchListener: ThreadExceptionCatchListener
    ): IrisExceptionCatcher {
        this.threadExceptionCatchListener = threadExceptionCatchListener
        return this
    }

    fun setDefaultThreadExceptionHandle(
        threadExceptionHandle: ThreadExceptionHandle
    ): IrisExceptionCatcher {
        defaultThreadExceptionHandle = threadExceptionHandle
        return this
    }

}