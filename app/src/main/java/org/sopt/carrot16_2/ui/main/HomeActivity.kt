package org.sopt.carrot16_2.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import org.sopt.carrot16_2.R
import org.sopt.carrot16_2.data.remote.RetrofitBuilder
import org.sopt.carrot16_2.databinding.ActivityHomeBinding
import org.sopt.carrot16_2.ui.base.BaseActivity
import org.sopt.carrot16_2.ui.create.CreateActivity
import org.sopt.carrot16_2.ui.main.viewmodel.HomeViewModel
import org.sopt.carrot16_2.ui.read.ReadActivity
import org.sopt.carrot16_2.util.enqueueUtil
import org.sopt.carrot16_2.util.ItemDecoration

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private lateinit var sellAdapter: SellAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.homeViewModel = homeViewModel
        binding.rvHomeContent.addItemDecoration(ItemDecoration("#F1F1F1"))
        initAdapter()
        initWriteBtnClickListener()
    }

    private fun initAdapter() {
        sellAdapter = SellAdapter{
            val intent = Intent(this,ReadActivity::class.java)
            intent.apply {
                intent.putExtra("id",it.id)
                Log.e("dksfjls",it.id)
            }
            startActivity(intent)
        }
        binding.rvHomeContent.adapter = sellAdapter
        val call = RetrofitBuilder.homeService.getHomeFeed()
        call.enqueueUtil(
            onSuccess = {
                sellAdapter.dataList.clear()
                sellAdapter.dataList.addAll(it.data)
                sellAdapter.notifyDataSetChanged()
            },
            onError = {
                when (it) {
                    500 -> Toast.makeText(this, "서버 내부 오류", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    private fun initWriteBtnClickListener() {
        binding.btnHomeWrite.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
    }
}
