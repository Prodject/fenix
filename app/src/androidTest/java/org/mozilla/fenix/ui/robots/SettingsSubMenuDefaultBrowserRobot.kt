/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

@file:Suppress("TooManyFunctions")

package org.mozilla.fenix.ui.robots

import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.mozilla.fenix.helpers.TestAssetHelper.waitingTime
import org.mozilla.fenix.helpers.TestHelper.scrollToElementByText
import org.mozilla.fenix.helpers.click

/**
 * Implementation of Robot Pattern for the settings DefaultBrowser sub menu.
 */
class SettingsSubMenuDefaultBrowserRobot {

    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun verifyOpenLinksInPrivateTabEnabled() = assertOpenLinksInPrivateTabEnabled()

    fun verifyOpenLinksInPrivateTabUnchecked() = assertOpenLinksInPrivateTabUnchecked()

    fun clickOpenLinksInPrivateTabCheckbox() = openLinksInPrivateTabCheckbox().click()

    fun clickSetDefaultBrowserToggle() = setDefaultBrowserToggle().clickAndWaitForNewWindow()

    fun selectFenixDefaultBrowser() {
//        mDevice.waitNotNull(
//            Until.findObject(By.text("Browser app")),
//            TestAssetHelper.waitingTime
//        )
        assertAndroidSettingsPackage()
        defaultBrowserAppList().waitForExists(waitingTime)
        defaultBrowserAppList().clickAndWaitForNewWindow()
        fenixDebugOption().click()
        mDevice.pressBack()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDevice.pressBack()
        }
    }

    class Transition {
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        fun goBack(interact: SettingsRobot.() -> Unit): SettingsRobot.Transition {
            mDevice.waitForIdle()
            goBackButton().perform(ViewActions.click())

            SettingsRobot().interact()
            return SettingsRobot.Transition()
        }
    }
}

private fun assertOpenLinksInPrivateTab() {
    onView(withText("Open links in private tab"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertOpenLinksInPrivateTabEnabled() {
    onView(withText("Open links in a private tab"))
        .check(matches(isEnabled()))
}

private fun assertOpenLinksInPrivateTabUnchecked() {
    assertFalse(mDevice.findObject(UiSelector().resourceId("android:id/checkbox")).isChecked())
}

private fun goBackButton() = onView(withContentDescription("Navigate up"))

private fun setDefaultBrowserToggle() =
    mDevice.findObject(UiSelector().resourceId("org.mozilla.fenix.debug:id/switch_widget"))
// onView(withId(R.id.switch_widget))

private fun openLinksInPrivateTabCheckbox() = onView(withText("Open links in a private tab"))

private fun defaultBrowserAppList() = mDevice.findObject(
    (UiSelector()
        .className("android.widget.TextView"))
        .resourceId("android:id/title")
        .text("Browser app")
)

private fun assertAndroidSettingsPackage() = assertTrue(
    mDevice.findObject(
        (UiSelector().packageName("com.android.settings"))
    ).waitForExists(waitingTime)
)

private fun fenixDebugOption() = mDevice.findObject(UiSelector().text("Firefox Preview"))
