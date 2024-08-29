package com.compscicomputations.number_systems

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.CSCActivity
import com.compscicomputations.number_systems.data.source.local.datastore.BaseNDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.ComplementDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.ExcessDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.FloatingPointDataStore
import com.compscicomputations.number_systems.ui.NumberSystems
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel
import com.compscicomputations.number_systems.ui.excess.ExcessViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseNViewModel = BasesViewModel(
            this,
            generateContentUseCase,
            aiResponseDao,
        )
        val complementViewModel = ComplementViewModel(
            this,
            generateContentUseCase,
            aiResponseDao,
        )
        val excessViewModel = ExcessViewModel(
            this,
            generateContentUseCase,
            aiResponseDao,
        )
        val floatingPointViewModel = FloatingPointViewModel(
            this,
            generateContentUseCase,
            aiResponseDao,
        )

        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                NumberSystems(
                    basesViewModel = baseNViewModel,
                    complementViewModel = complementViewModel,
                    excessViewModel = excessViewModel,
                    floatingPointViewModel = floatingPointViewModel,
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}