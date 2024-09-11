package com.luoyu.camellia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

abstract class BaseActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onDestroy() {
        super.onDestroy();
    }
    
    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
    
    protected open fun loadFragment(fragment: Fragment , id: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment)
        transaction.commit()
    }
  
}
