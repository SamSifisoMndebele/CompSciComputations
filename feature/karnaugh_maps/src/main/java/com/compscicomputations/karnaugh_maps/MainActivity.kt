package com.compscicomputations.karnaugh_maps

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.compscicomputations.karnaugh_maps.databinding.ActivityKarnaughBinding
import com.compscicomputations.theme.DarkColorScheme
import com.compscicomputations.theme.LightColorScheme
import com.compscicomputations.ui.main.settings.PhoneThemes
import com.compscicomputations.ui.main.settings.SettingsUiState
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.DYNAMIC_COLOR_KEY
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.PHONE_THEME_KEY
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.PREFS_NAME
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKarnaughBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val themeState = SettingsUiState(
            when(PhoneThemes.entries[sharedPreferences.getInt(PHONE_THEME_KEY, PhoneThemes.System.ordinal)]) {
                PhoneThemes.Dark -> true
                PhoneThemes.Light -> false
                else -> null
            },
            sharedPreferences.getBoolean(DYNAMIC_COLOR_KEY, true)
        )

        val darkTheme = themeState.darkTheme ?: (resources.getString(R.string.theme_mode) == "Dark")
        delegate.localNightMode = when(darkTheme) {
            true -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
//            null -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        val colorScheme = when {
            themeState.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(this) else dynamicLightColorScheme(this)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme

        binding = ActivityKarnaughBinding.inflate(layoutInflater)

        binding.titleCard.setCardBackgroundColor(colorScheme.secondaryContainer.toArgb())
        binding.title.setTextColor(colorScheme.onSecondaryContainer.toArgb())
        binding.upButton.setColorFilter(colorScheme.onSecondaryContainer.toArgb())
        binding.tabLayout.setSelectedTabIndicatorHeight(8)
        binding.tabLayout.setTabTextColors(colorScheme.onSecondaryContainer.toArgb(), colorScheme.primary.toArgb())
        binding.tabLayout.setSelectedTabIndicatorColor(colorScheme.primary.toArgb())
        binding.tabLayout.setBackgroundColor(colorScheme.background.toArgb())
        binding.titleBar.setBackgroundColor(colorScheme.background.toArgb())
        binding.container.setBackgroundColor(colorScheme.background.toArgb())
        binding.bottomAppBar.setBackgroundColor(colorScheme.surfaceContainer .toArgb())
        binding.aiActionButton.setBackgroundColor(colorScheme.secondaryContainer.toArgb())

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""


        val navController = findNavController(R.id.nav_host_fragment_activity_karnaugh)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_karnaugh2,
                R.id.navigation_karnaugh3,
                R.id.navigation_karnaugh4
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                navController.popBackStack(navController.graph.startDestinationId, true)
                when(tab?.position) {
                    0 -> navController.navigate(R.id.navigation_karnaugh4)
                    1 -> navController.navigate(R.id.navigation_karnaugh3)
                    2 -> navController.navigate(R.id.navigation_karnaugh2)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.upButton.setOnClickListener { finish() }

//        binding.bottomAppBar.setOnMenuItemClickListener  { menuItem ->
//            when (menuItem.itemId) {
//                R.id.show_keyboard -> {
//                    // Handle accelerator icon press
//                    true
//                }
//                else -> false
//            }
//        }

        binding.aiActionButton.setOnClickListener {
            ///Todo: Generate content
        }

    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        finish()
        return super.getOnBackInvokedDispatcher()
    }

}