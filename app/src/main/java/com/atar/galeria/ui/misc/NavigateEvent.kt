package com.atar.galeria.ui.misc

import android.os.Bundle
import androidx.navigation.fragment.FragmentNavigator

/**
 * An event that is used for navigating between fragment through ViewModel.
 * This event should be wrapped inside [SingleEvent] only.
 */
class NavigateEvent(
    val id: Int,
    val bundle: Bundle? = null,
    val extras: FragmentNavigator.Extras? = null
)