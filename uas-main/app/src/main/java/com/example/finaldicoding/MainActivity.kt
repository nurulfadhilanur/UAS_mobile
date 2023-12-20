package com.example.finaldicoding

import ListGambar
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finaldicoding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Kpop>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listview.setHasFixedSize(true)

        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 500
        itemAnimator.removeDuration = 500
        binding.listview.itemAnimator = itemAnimator

        list.addAll(getListHeroes())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {
                binding.listview.layoutManager = LinearLayoutManager(this)
            }
            R.id.action_grid -> {
                binding.listview.layoutManager = GridLayoutManager(this, 2)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListHeroes(): ArrayList<Kpop> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataSoundNames = resources.getStringArray(R.array.data_sound)

        val listHero = ArrayList<Kpop>()
        val minSize = minOf(dataName.size, dataDescription.size, dataPhoto.length(), dataSoundNames.size)

        for (i in 0 until minSize) {
            val photoResourceId = if (i < dataPhoto.length()) dataPhoto.getResourceId(i, -1) else -1
            val soundName = if (i < dataSoundNames.size) dataSoundNames[i] else "chicken"
            val hero = Kpop(dataName[i], dataDescription[i], photoResourceId, soundName)
            listHero.add(hero)
        }

        dataPhoto.recycle()
        return listHero
    }

    private fun showRecyclerList() {
        binding.listview.layoutManager = LinearLayoutManager(this)
        val listGambar = ListGambar(this, list)
        binding.listview.adapter = listGambar

        listGambar.setOnItemClickCallback(object : ListGambar.OnItemClickCallback {
            override fun onItemClicked(data: Kpop) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_DETAIL, data)
                startActivity(detailIntent)
            }
        })
    }
}
