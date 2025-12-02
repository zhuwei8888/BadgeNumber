package com.sol.badgenumber

import android.util.Log
import com.sol.badgenumber.setter.HonorBadgeSetter
import com.sol.badgenumber.setter.HuaiweiBadgeSetter
import com.sol.badgenumber.setter.MeizuBadgeSetter
import com.sol.badgenumber.setter.OppoBadgeSetter
import com.sol.badgenumber.setter.VivoBadgeSetter
import com.sol.badgenumber.setter.XiaomiBadgeSetter

internal object BadgeSetterFactory {
    fun createSetter(manufacturer: String): BadgeSetter? {
        Log.i("BadgeSetterFactory", "manufacturer: $manufacturer")
        return when (manufacturer.lowercase()) {
            "xiaomi" -> XiaomiBadgeSetter()
            "huawei" -> HuaiweiBadgeSetter()
            "oppo" -> OppoBadgeSetter()
            "vivo" -> VivoBadgeSetter()
            "meizu" -> MeizuBadgeSetter()
            "honor" -> HonorBadgeSetter()
            else -> null
        }
    }
}