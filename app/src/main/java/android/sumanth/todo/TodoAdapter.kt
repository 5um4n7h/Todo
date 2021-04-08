package android.sumanth.todo

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoAdapter (
    private val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent, false
            )
        )
    }

    private fun toggleStrike(todoTitle: TextView, isChecked: Boolean){
        if (isChecked){
            todoTitle.paintFlags = todoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG

        }else
            todoTitle.paintFlags = todoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position];
        holder.itemView.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            toggleStrike(tvTodoTitle, curTodo.isChecked)
            cbDone.setOnCheckedChangeListener{ _, isChecked ->
                toggleStrike(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked;
            }


        }
    }

    override fun getItemCount(): Int {
            return todos.size
    }

    fun addTodo(todo: Todo){
        todos.add(todo);
        notifyItemInserted(todos.size - 1)
        val gson = Gson() // Or use new GsonBuilder().create();

        val json = gson.toJson(todos) // serializes target to Json


    }

    fun deleteTodo(){
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }
}