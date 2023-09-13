package com.varunbarad.toreadmanager.screens.home

import java.io.OutputStream

sealed class ExportFileChooserResult {
    object Error : ExportFileChooserResult()
    data class Success(val fileOutputStream: OutputStream): ExportFileChooserResult()
}
