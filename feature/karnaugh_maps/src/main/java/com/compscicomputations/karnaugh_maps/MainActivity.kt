package com.compscicomputations.karnaugh_maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.compscicomputations.karnaugh_maps.databinding.ActivityKarnaughBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKarnaughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKarnaughBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
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

}