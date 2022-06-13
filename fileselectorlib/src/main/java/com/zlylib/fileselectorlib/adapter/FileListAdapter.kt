package com.zlylib.fileselectorlib.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zlylib.fileselectorlib.R
import com.zlylib.fileselectorlib.bean.EssFile
import com.zlylib.fileselectorlib.utils.FileSizeUtil
import com.zlylib.fileselectorlib.utils.FileUtils
import java.util.*

/**
 * FileListAdapter
 * Created by zhangliyang on 2020/6/20.
 */
class FileListAdapter(data: MutableList<EssFile>) :
    BaseQuickAdapter<EssFile, BaseViewHolder>(R.layout.item_file_list, data) {
    var loadFileCountListener: onLoadFileCountListener? = null

    interface onLoadFileCountListener {
        fun onLoadFileCount(posistion: Int)
    }

    override fun convert(helper: BaseViewHolder, item: EssFile) {
        val textView = helper.getView<TextView>(R.id.tv_item_file_list_desc)
        if (item.isDirectory) {
            helper.setVisible(R.id.iv_item_file_select_right, true)
            if (item.childFolderCount == "加载中") {
                //查找数量
                if (loadFileCountListener != null) {
                    loadFileCountListener!!.onLoadFileCount(helper.adapterPosition)
                }
            }
            textView.text = String.format(
                context.getString(R.string.folder_desc),
                item.childFileCount,
                item.childFolderCount
            )
        } else {
            helper.setVisible(R.id.iv_item_file_select_right, false)
            textView.text = String.format(
                context.getString(R.string.file_desc),
                FileUtils.getDateTime(item.absolutePath),
                FileSizeUtil.getAutoFileOrFilesSize(item.file)
            )
        }
        helper.setText(R.id.tv_item_file_list, item.name)
        if (item.isChecked) {
            helper.setVisible(R.id.checkbox_item_file_list, true)
        } else {
            helper.setVisible(R.id.checkbox_item_file_list, false)
        }
        val imageView = helper.getView<ImageView>(R.id.iv_item_file_select_left)
        val fileNameExtension = FileUtils.getExtension(item.name).lowercase(Locale.getDefault())
        when (fileNameExtension) {
            "apk" -> imageView.setImageResource(R.mipmap.apk)
            "avi" -> imageView.setImageResource(R.mipmap.avi)
            "doc", "docx" -> imageView.setImageResource(R.mipmap.doc)
            "exe" -> imageView.setImageResource(R.mipmap.exe)
            "flv" -> imageView.setImageResource(R.mipmap.flv)
            "gif" -> {
                val options = RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.gif)
                Glide
                    .with(context)
                    .load(item.absolutePath)
                    .apply(options)
                    .into(imageView)
            }
            "jpg", "jpeg", "png" -> {
                val options2 = RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.png)
                Glide
                    .with(context)
                    .load(item.absolutePath)
                    .apply(options2)
                    .into(imageView)
            }
            "mp3" -> imageView.setImageResource(R.mipmap.mp3)
            "mp4", "f4v" -> imageView.setImageResource(R.mipmap.movie)
            "pdf" -> imageView.setImageResource(R.mipmap.pdf)
            "ppt", "pptx" -> imageView.setImageResource(R.mipmap.ppt)
            "wav" -> imageView.setImageResource(R.mipmap.wav)
            "xls", "xlsx" -> imageView.setImageResource(R.mipmap.xls)
            "zip" -> imageView.setImageResource(R.mipmap.zip)
            "ext" -> if (item.isDirectory) {
                imageView.setImageResource(R.mipmap.folder)
            } else {
                imageView.setImageResource(R.mipmap.documents)
            }
            else -> if (item.isDirectory) {
                imageView.setImageResource(R.mipmap.folder)
            } else {
                imageView.setImageResource(R.mipmap.documents)
            }
        }
    }
}