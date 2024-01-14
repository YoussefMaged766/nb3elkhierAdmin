package com.devYoussef.nb3elkhieradmin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.ActivityMainBinding
import com.devYoussef.nb3elkhieradmin.ui.home.order.OrdersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            )
        )



        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController, appBarConfiguration)
        setSupportActionBar(binding.toolbar)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = ""
            binding.toolbar.title=""
            when(destination.id){
                R.id.splashFragment -> {
                    binding.appBar.visibility = View.GONE

                }
                R.id.loginFragment -> {
                    binding.appBar.visibility = View.GONE
                }
                R.id.homeFragment -> {
                    binding.appBar.visibility = View.GONE
                    binding.txtTitleToolBar.text = ""
                }
                R.id.productsFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "المنتجات"
                }
                R.id.promoCodesFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "البرومو كود"
                }
                R.id.ordersFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "الفواتير"
                }
                R.id.bannersFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "البانرات"
                }
                R.id.statisticsFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "الاحصائيات"

                }
                R.id.regionsFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "المناطق"
                }
                R.id.blocksFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "القائمه السوداء"
                }
                R.id.addAndEditFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "اضافه منتج"


                }
                R.id.detailsOrderFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.txtTitleToolBar.text = "تفاصيل الفاتوره"


                }
            }
        }

        Log.e( "onStart: ",intent?.getStringExtra("navigate").toString() )
        if (intent?.getStringExtra("navigate") == "navigate") {
            navController.navigate(R.id.ordersFragment)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }




}