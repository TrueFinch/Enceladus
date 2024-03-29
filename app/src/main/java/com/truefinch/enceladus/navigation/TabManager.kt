package com.truefinch.enceladus.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.MainActivity
import com.truefinch.enceladus.R
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.EventMode
import kotlinx.android.synthetic.main.activity_main.*

class TabManager(private val mainActivity: MainActivity) {

    val navStartController: NavController
        get() = _navStartController

    val navMonthController: NavController
        get() = _navMonthController

    val navNewEventController: NavController
        get() = _navNewEventController

    val navWeekController: NavController
        get() = _navWeekController

    val navDayController: NavController
        get() = _navDayController

    val navScheduleController: NavController
        get() = _navScheduleController

    var currentNavController: NavController? = null


    private val startDestinations = mapOf(
//        R.id.navigation_start to R.id.splashFragment,
        R.id.navigation_month to R.id.monthFragment,
        R.id.navigation_week to R.id.weekFragment,
        R.id.navigation_event to R.id.eventFragment,
        R.id.navigation_day to R.id.dayFragment,
        R.id.navigation_schedule to R.id.scheduleFragment
    )
    var currentTabId: Int = R.id.navigation_month
    private var tabHistory = TabHistory()

    private val _navStartController: NavController by lazy {
        mainActivity.findNavController(R.id.start_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_start)
        }
    }

    private val _navMonthController: NavController by lazy {
        mainActivity.findNavController(R.id.month_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_month)
        }
    }

    private val _navWeekController: NavController by lazy {
        mainActivity.findNavController(R.id.week_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_week)
        }
    }

    private val _navNewEventController: NavController by lazy {
        mainActivity.findNavController(R.id.event_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_new_event)
        }
    }

    private val _navDayController: NavController by lazy {
        mainActivity.findNavController(R.id.day_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_day)
        }
    }

    private val _navScheduleController: NavController by lazy {
        mainActivity.findNavController(R.id.schedule_tab).apply {
            graph = navInflater.inflate(R.navigation.navigation_graph_schedule)
        }
    }

    private val startTabContainer: View by lazy { mainActivity.start_tab_container }
    private val monthTabContainer: View by lazy { mainActivity.month_tab_container }
    private val weekTabContainer: View by lazy { mainActivity.week_tab_container }
    private val newEventTabContainer: View by lazy { mainActivity.event_tab_container }
    private val dayTabContainer: View by lazy { mainActivity.day_tab_container }
    private val scheduleTabContainer: View by lazy { mainActivity.schedule_tab_container }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(KEY_TAB_HISTORY, tabHistory)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            tabHistory = it.getSerializable(KEY_TAB_HISTORY) as TabHistory

            switchTab(mainActivity.nav_view.selectedItemId, false)
        }
    }

    fun supportNavigateUpTo(upIntent: Intent) {
        currentNavController?.navigateUp()
    }

    fun onBackPressed() {
        currentNavController?.let {
            if (it.currentDestination == null
                || it.currentDestination?.id == startDestinations.getValue(currentTabId)
            ) {
                if (tabHistory.size > 1) {
                    val tabId = tabHistory.popPrevious()
                    switchTab(tabId, false)
                    mainActivity.nav_view.menu.findItem(tabId)?.isChecked = true
                } else {
                    mainActivity.finish()
                }
            }
            it.popBackStack()
        } ?: run {
            mainActivity.finish()
        }
    }

    fun switchTab(tabId: Int, addToTabHistory: Boolean = true, initial: Boolean = false) {
        mainActivity.navView.menu.findItem(tabId).isChecked = true
        if (addToTabHistory/* && currentTabId != R.id.navigation_start*/) {
            tabHistory.push(tabId)
        }
        currentTabId = tabId
        var viewToShow: View = monthTabContainer
        when (tabId) {
            //TODO: maybe we want user to be able to auth during runtime, so we need to provide a way to do this
//            R.id.navigation_start -> {
//                currentNavController = _navStartController
//                invisibleTabContainerExcept(startTabContainer)
//            }
            R.id.navigation_month -> {
                currentNavController = _navMonthController
                viewToShow = monthTabContainer
            }
            R.id.navigation_week -> {
                currentNavController = _navWeekController
                viewToShow = weekTabContainer
            }
            R.id.navigation_event -> {
                currentNavController = _navNewEventController
                viewToShow = newEventTabContainer
                EnceladusApp.instance.sharedViewModel.eventViewModel.also {
                    if (it.eventMode.value == EventMode.NONE) {
                        it.setEventData(EventModel.emptyEvent(), EventMode.CREATE)
                    }
                }
            }
            R.id.navigation_day -> {
                currentNavController = _navDayController
                viewToShow = dayTabContainer
            }
            R.id.navigation_schedule -> {
                currentNavController = _navScheduleController
                viewToShow = scheduleTabContainer
            }
        }
        if (!initial) {
            invisibleTabContainerExcept(viewToShow)
        }
    }

    private fun invisibleTabContainerExcept(container: View) {
        startTabContainer.isInvisible = true
        monthTabContainer.isInvisible = true
        weekTabContainer.isInvisible = true
        newEventTabContainer.isInvisible = true
        dayTabContainer.isInvisible = true
        scheduleTabContainer.isInvisible = true

        container.isInvisible = false
    }

    companion object {
        private const val KEY_TAB_HISTORY = "key_tab_history"
    }
}