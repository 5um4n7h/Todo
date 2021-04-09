package android.sumanth.todo

import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class Todo (
    val title : String,

    var isChecked : Boolean = false
)




