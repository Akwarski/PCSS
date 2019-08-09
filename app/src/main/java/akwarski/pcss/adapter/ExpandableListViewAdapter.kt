package akwarski.pcss.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_child.view.*
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.list_parent.view.*
import java.lang.Exception


class ExpandableListViewAdapter(private val context: Context, private var identity: ArrayList<String>,
                                private var children: MutableList<MutableList<String>>): BaseExpandableListAdapter() {
    //specific name of group
    override fun getGroup(p0: Int): String {
        return identity[p0]
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    //view my all groups
    @SuppressLint("InflateParams")
    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        var convertView = p2
        //set my group view
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(akwarski.pcss.R.layout.list_parent, null)
        }
        //set text all of group name
        //val title = convertView!!.findViewById<TextView>(akwarski.pcss.R.id.title)
        convertView!!.title.text = getGroup(p0)
        //title.text = getGroup(p0)
        return convertView
    }

    //get amount of elements
    override fun getChildrenCount(p0: Int): Int {
        return children[p0].size
    }

    //get specific element of group
    override fun getChild(p0: Int, p1: Int): String {
        return children[p0][p1]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    //view my all elements of groups
    @SuppressLint("InflateParams")
    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        var convertView = p3
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(akwarski.pcss.R.layout.list_child, null)
        }
        val picture = convertView!!.picture

        //load picture from url
        Picasso.get().load(getChild(p0,p1)).into(picture, object: Callback{
            override fun onSuccess() {
                convertView.progressBar.isVisible = false
            }
            override fun onError(e: Exception?) {
                Toast.makeText(context, "Internet is disconnected", Toast.LENGTH_LONG).show()
            }

        })

        //show id inside specific group when specific element was clicked
        picture.setOnClickListener {
            Toast.makeText(context, "${getChildId(p0,p1)+1}", Toast.LENGTH_LONG).show()
        }

        return convertView
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun getGroupCount(): Int {
        return identity.size
    }
}