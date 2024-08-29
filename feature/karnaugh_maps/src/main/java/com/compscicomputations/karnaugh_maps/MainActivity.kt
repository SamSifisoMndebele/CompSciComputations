package com.compscicomputations.karnaugh_maps

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.compscicomputations.CSCActivity
import com.compscicomputations.karnaugh_maps.databinding.ActivityKarnaughBinding
import com.compscicomputations.utils.AIStepsSheetDialog
import com.compscicomputations.karnaugh_maps.utils.MODULE_NAME
import com.compscicomputations.theme.DarkColorScheme
import com.compscicomputations.theme.LightColorScheme
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : CSCActivity() {

    private lateinit var binding: ActivityKarnaughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkTheme = themeState.value.darkTheme ?: (resources.getString(R.string.theme_mode) == "Dark")
        delegate.localNightMode = when(darkTheme) {
            true -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
        }

        val colorScheme = when {
            themeState.value.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
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
        binding.tabLayout.setTabTextColors(colorScheme.primary.toArgb(), colorScheme.primary.toArgb())
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

        val prefs = getSharedPreferences("KarnaughMaps", Context.MODE_PRIVATE)
        binding.aiActionButton.setOnClickListener {
            val expressionString = when(binding.tabLayout.selectedTabPosition) {
                0 -> prefs.getString("field_4var", null)
                1 -> prefs.getString("field_ 3var", null)
                else -> prefs.getString("field_2var", null)
            }
            if (expressionString.isNullOrBlank()) {
                Toast.makeText(this, "Expression is empty", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                bundle.putString("expression", expressionString)
                bundle.putInt("tap", binding.tabLayout.selectedTabPosition)

                val modal = AIStepsSheetDialog()
                modal.arguments = bundle
                supportFragmentManager.let { modal.show(it, AIStepsSheetDialog.TAG) }
            }
        }

    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        finish()
        return super.getOnBackInvokedDispatcher()
    }

}