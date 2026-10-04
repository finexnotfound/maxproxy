package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ServerRepository
import com.example.model.VpnProtocol
import com.example.model.VpnState
import com.example.vpn.VpnStateManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MAX PROXY", appName)
    }

    @Test
    fun `verify server repository contains required countries and at least 10 countries`() {
        val servers = ServerRepository.getAllServers()
        val countries = servers.map { it.country }.distinct()

        assertTrue("Expected at least 10 countries, found ${countries.size}", countries.size >= 10)
        assertTrue("Expected United States", countries.contains("United States"))
        assertTrue("Expected United Kingdom", countries.contains("United Kingdom"))
        assertTrue("Expected China", countries.contains("China"))
    }

    @Test
    fun `verify vpn state transitions`() {
        assertEquals(VpnState.DISCONNECTED, VpnStateManager.vpnState.value)
        VpnStateManager.setConnecting()
        assertEquals(VpnState.CONNECTING, VpnStateManager.vpnState.value)
        VpnStateManager.setConnected()
        assertEquals(VpnState.CONNECTED, VpnStateManager.vpnState.value)
        VpnStateManager.setDisconnected()
        assertEquals(VpnState.DISCONNECTED, VpnStateManager.vpnState.value)
    }
}
