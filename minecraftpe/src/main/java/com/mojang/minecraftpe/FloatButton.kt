package com.mojang.minecraftpe

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton

class FloatButton(private val mContext: Context) : PopupWindow() {
    init {
        init()
    }

    fun showHoverMenu() {
        val optionsListView = ListView(mContext)
        val values = arrayOf<String?>("AlertDialog", "Toast")
        optionsListView.setAdapter(
            ArrayAdapter<String?>(
                mContext,
                android.R.layout.simple_list_item_1, android.R.id.text1, values
            )
        )

        optionsListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                val textView = view as TextView
                val text = textView.getText().toString()
                if (text == "AlertDialog") {
                    val mDialog = AlertDialog.Builder(mContext)
                    mDialog.setTitle("About")
                    mDialog.setMessage("SculptLauncher - open source Minecraft launcher")
                    mDialog.show()
                } else if (text == "Toast") {
                    Toast.makeText(
                        mContext,
                        "SculptLauncher - open source Minecraft launcher",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val mDialog = AlertDialog.Builder(mContext)
        mDialog.setTitle("About")
        mDialog.setView(optionsListView)
        mDialog.show()
    }

    fun init() {
        // Create layout and the button.
        val layout = LinearLayout(mContext)
        val button = AppCompatButton(mContext)
        //button.setBackgroundResource(R.mipmap.ic_launcher_round);
        button.setOnClickListener(View.OnClickListener { v: View? -> showHoverMenu() })
        layout.addView(button)
        setContentView(layout)

        // Set dimensions.
        width = 128
        height = 128
    }
}