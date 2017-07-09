package me.zsj.dan.data.executor

import java.io.File

/**
 * @author zsj
 */
interface DownloadCallback {
    fun onDownloadFile(file: File)
}