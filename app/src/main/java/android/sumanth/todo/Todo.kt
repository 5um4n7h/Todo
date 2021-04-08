package android.sumanth.todo

import android.view.View
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

data class Todo (
    val title : String,

    var isChecked : Boolean = false
)