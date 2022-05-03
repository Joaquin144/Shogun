package com.devcommop.joaquin.shogun

data class PushNotification(
    val data: NotificationData,
    val to: String//recipient
)
