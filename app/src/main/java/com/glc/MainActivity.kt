package com.glc

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.glc.Bean.WeatherBean
import com.glc.adapter.ReAdapter
import com.glc.adapter.ReAdapter2
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

class MainActivity : AppCompatActivity() {
    private lateinit var tv_zhunagtai: TextView

    var cityName = "佛山"

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    private lateinit var tv4: TextView
    private lateinit var tv5: TextView
    private lateinit var tv6: TextView
    private lateinit var tv11: TextView
    private lateinit var tv22: TextView
    private lateinit var tv33: TextView
    private lateinit var tv44: TextView
    private lateinit var tv55: TextView
    private lateinit var tv66: TextView
    private lateinit var sheshidu: TextView
    private lateinit var fengxiang: TextView
    private lateinit var img_zhungkuang: ImageView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var weatherBean: WeatherBean
    private lateinit var tv_cityName: TextView
    private lateinit var editText: EditText
    private lateinit var btn_chaxun: Button
    private lateinit var qiehuan: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val GET_PERMISSION_REQUEST = 100 //权限申请自定义码
    private lateinit var btn_photo: Button
    private lateinit var openMap: Button
    private lateinit var main_layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_zhunagtai = findViewById(R.id.tv_zhuangtai)
        sheshidu = findViewById(R.id.tv_sheshidu)
        fengxiang = findViewById(R.id.tv_fengxiang)
        img_zhungkuang = findViewById(R.id.img_now_zhuangkuang)
        recyclerView = findViewById(R.id.ry_recycleView)
        recyclerView2 = findViewById(R.id.ry_recycleView2)
        refreshLayout = findViewById(R.id.srl_swipe)
        tv_cityName = findViewById(R.id.tv_cityName)
        qiehuan = findViewById(R.id.tv_qiehuan)
        openMap = findViewById(R.id.openMap)
        btn_photo = findViewById(R.id.btn_photo)
        main_layout = findViewById(R.id.main_layout)
        openMap.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, BdMap::class.java)
            startActivity(intent)
        })
        btn_photo.setOnClickListener(View.OnClickListener { photo })

        tv_cityName.setOnClickListener(View.OnClickListener {
            val myDialog = MyDialog(this@MainActivity)
            myDialog.show()
        })

        qiehuan.setOnClickListener(View.OnClickListener {
            val myDialog = MyDialog(this@MainActivity)
            myDialog.show()
        })

        //下拉刷新
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            Handler().postDelayed(
                { selectWeather(tv_cityName.getText().toString()) }, 3000
            )
        })
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.setLayoutManager(linearLayoutManager)
        val linearLayoutManager1 = LinearLayoutManager(this)
        linearLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView2.setLayoutManager(linearLayoutManager1)
        tv1 = findViewById(R.id.foot_tv1)
        tv2 = findViewById(R.id.foot_tv2)
        tv3 = findViewById(R.id.foot_tv3)
        tv4 = findViewById(R.id.foot_tv4)
        tv5 = findViewById(R.id.foot_tv5)
        tv6 = findViewById(R.id.foot_tv6)
        tv11 = findViewById(R.id.foot_tv11)
        tv22 = findViewById(R.id.foot_tv22)
        tv33 = findViewById(R.id.foot_tv33)
        tv44 = findViewById(R.id.foot_tv44)
        tv55 = findViewById(R.id.foot_tv55)
        tv66 = findViewById(R.id.foot_tv66)

        //得到sp存储的值
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val s_chengshi = sharedPreferences.getString("chengshi", "")
        //判断sp的值时候为空 为空就执行默认城市
        if (s_chengshi == "") {
            selectWeather(cityName)
        } else { //否则查询sp存储的值
            selectWeather(s_chengshi)
        }
    }

    /**
     * 请求
     * 注意请将秘钥替换为：自己到京东万象申请秘钥
     * @param cityName 城市名称
     */
    private fun selectWeather(cityName: String?) {
        val jsonObject = JSONObject()
        val url =
            "https://way.jd.com/he/freeweather?city=$cityName&appkey=29c819cf4eaae0f99b70dc9ede753122"
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, jsonObject, { jsonObject ->
                val gson = Gson()
                weatherBean = gson.fromJson(jsonObject.toString(), WeatherBean::class.java)
                if (weatherBean.result!!.heWeather5!![0].status == "unknown location") {
                    Toast.makeText(this@MainActivity, "输入城市有误", Toast.LENGTH_SHORT).show()
                } else {
                    tv_zhunagtai!!.text = weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt
                    sheshidu!!.text = weatherBean.result!!.heWeather5!![0].now!!.tmp + "℃"
                    fengxiang!!.text = weatherBean.result!!.heWeather5!![0].now!!.wind!!.dir
                    //根据天气状况 来判断图片
                    if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "多云" || weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "阴") {
                        img_zhungkuang!!.setImageResource(R.mipmap.yin)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "晴") {
                        img_zhungkuang!!.setImageResource(R.mipmap.qinglang)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "雨夹雪" || weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "小雪") {
                        img_zhungkuang!!.setImageResource(R.mipmap.xiaoxue)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "小雨") {
                        img_zhungkuang!!.setImageResource(R.mipmap.xiaoyu)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "中雨" || weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "大雨") {
                        img_zhungkuang!!.setImageResource(R.mipmap.dayu)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "雷阵雨") {
                        img_zhungkuang!!.setImageResource(R.mipmap.leizhenyu)
                    } else if (weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt == "雾") {
                        img_zhungkuang!!.setImageResource(R.mipmap.wu)
                    }
                    //3小时 recyclerView滑动适配
                    val reAdapter =
                        ReAdapter(weatherBean.result!!.heWeather5!![0].hourly_forecast!!)
                    recyclerView!!.adapter = reAdapter
                    //未来七天  recyclerView滑动适配
                    val reAdapter2 = weatherBean.result!!.heWeather5!![0].daily_forecast?.let {
                        ReAdapter2(
                            it
                        )
                    }
                    recyclerView2!!.adapter = reAdapter2
                    tv_cityName!!.text = weatherBean.result!!.heWeather5!![0].basic!!.city
                    //生活指数部分
                    tv1!!.text =
                        "舒适度指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.comf!!.brf
                    tv2!!.text =
                        "洗车指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.cw!!.brf
                    tv3!!.text =
                        "穿衣指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.drsg!!.brf
                    tv4!!.text =
                        "感冒指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.flu!!.brf
                    tv5!!.text =
                        "运动指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.sport!!.brf
                    tv6!!.text =
                        "旅游指数：" + weatherBean.result!!.heWeather5!![0].suggestion!!.trav!!.brf
                    tv11!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.comf!!.txt
                    tv22!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.cw!!.txt
                    tv33!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.drsg!!.txt
                    tv44!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.flu!!.txt
                    tv55!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.sport!!.txt
                    tv66!!.text =
                        "      " + weatherBean.result!!.heWeather5!![0].suggestion!!.trav!!.txt
                    refreshLayout!!.isRefreshing = false

                    //启动前台服务
                    val startIntent = Intent(this@MainActivity, MyService::class.java)
                    startIntent.putExtra(
                        "zhuangtai",
                        weatherBean.result!!.heWeather5!![0].now!!.cond!!.txt
                    )
                    startIntent.putExtra(
                        "cityname",
                        weatherBean.result!!.heWeather5!![0].basic!!.city
                    )
                    startIntent.putExtra(
                        "wendu",
                        weatherBean.result!!.heWeather5!![0].now!!.tmp + "℃"
                    )
                    startService(startIntent)

                    //广播
                    val intent = Intent("com.glc.MY_BROADCAST")
                    intent.setPackage(packageName)
                    val bundle = Bundle()
                    bundle.putString(
                        "new", weatherBean.result!!.heWeather5
                            ?.get(0)?.basic!!.update!!.loc
                    )
                    intent.putExtras(bundle)
                    sendOrderedBroadcast(intent, null)
                }
            }) {
                Toast.makeText(this@MainActivity, "网络有误", Toast.LENGTH_SHORT).show()
                refreshLayout!!.isRefreshing = false
            }
        requestQueue.add(jsonObjectRequest)
    }

    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(uri!!, filePathColumns, null, null, null)
            c!!.moveToFirst()
            val columnIndex = c.getColumnIndex(filePathColumns[0])
            val imagePath = c.getString(columnIndex)
            val bm = BitmapFactory.decodeFile(imagePath)
            //            img.setImageBitmap(bm);
            main_layout!!.background = BitmapDrawable(resources,bm)
            c.close()
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val sdState = Environment.getExternalStorageState()
            if (sdState != Environment.MEDIA_MOUNTED) {
                return
            }
            DateFormat()
            val name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
                .toString() + ".jpg"
            val bundle = data.extras
            //获取相机返回的数据，并转换为图片格式
            val bitmap = bundle!!["data"] as Bitmap?
            var fout: FileOutputStream? = null
            val file = File("/sdcard/pintu/")
            file.mkdirs()
            val filename = file.path + name
            try {
                fout = FileOutputStream(filename)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fout) //图像压缩
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                if (fout != null) {
                    try {
                        fout.flush()
                        fout.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            //插入图片

//            img.setImageBitmap(bitmap);
            main_layout!!.background = BitmapDrawable(resources,bitmap)
        }
    }//权限已授予，执行//权限还没有授予，需要在这里写申请权限的代码

    //调用相册权限
    private val photo: Unit
        private get() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GET_PERMISSION_REQUEST
                )
            } else {
                //权限已授予，执行
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 0)
            }
        }
    //
    /**
     * Dialog对话框
     */
    internal inner class MyDialog(context: Context?) : Dialog(
        context!!
    ) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog)
            editText = findViewById(R.id.edit_chengshi)
            btn_chaxun = findViewById(R.id.btn_quding)
            btn_chaxun.setOnClickListener(View.OnClickListener {
                if (editText.getText().toString() == "") {
                    Toast.makeText(this@MainActivity, "输入为空,重新输入", Toast.LENGTH_SHORT).show()
                } else {
                    selectWeather(editText.getText().toString())
                    //sp存储
                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("chengshi", editText.getText().toString().trim { it <= ' ' })
                    editor.commit()
                    dismiss()
                }
            })
        }
    }

}