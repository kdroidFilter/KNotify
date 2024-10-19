package com.kdroid.composenotification.platform.windows.nativeintegration

import com.kdroid.composenotification.platform.windows.types.*
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.WString
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.W32APIOptions

interface WinToastLibC : Library {
    companion object {
        val INSTANCE: WinToastLibC = Native.load(
            "wintoastlibc",
            WinToastLibC::class.java,
            W32APIOptions.UNICODE_OPTIONS
        )
    }

    fun WTLC_isCompatible(): Boolean
    fun WTLC_Instance_Create(): WTLC_Instance?
    fun WTLC_Instance_Destroy(instance: WTLC_Instance)

    fun WTLC_setAppName(instance: WTLC_Instance, appName: WString)
    fun WTLC_setAppUserModelId(instance: WTLC_Instance, aumi: WString)
    fun WTLC_setShortcutPolicy(instance: WTLC_Instance, policy: Int)

    fun WTLC_initialize(instance: WTLC_Instance, error: IntByReference): Boolean
    fun WTLC_strerror(error: WTLC_Error): WString

    fun WTLC_Template_Create(templateType: WTLC_TemplateType): WTLC_Template?
    fun WTLC_Template_Destroy(template: WTLC_Template)

    fun WTLC_Template_setTextField(template: WTLC_Template, text: WString, field: WTLC_TextField)
    fun WTLC_Template_setAudioOption(template: WTLC_Template, option: WTLC_AudioOption)
    fun WTLC_Template_setExpiration(template: WTLC_Template, milliseconds: Long)
    fun WTLC_Template_setImagePath(template: WTLC_Template, imagePath: WString)
    fun WTLC_Template_addAction(template: WTLC_Template, label: WString)

    fun WTLC_showToast(
        instance: WTLC_Instance,
        template: WTLC_Template,
        userData: Pointer?,
        activatedCallback: ToastActivatedCallback?,
        activatedActionCallback: ToastActivatedActionCallback?,
        dismissedCallback: ToastDismissedCallback?,
        failedCallback: ToastFailedCallback?,
        error: IntByReference
    ): Long
}