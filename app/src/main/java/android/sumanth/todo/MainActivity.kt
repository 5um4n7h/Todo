package android.sumanth.todo


import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.view.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var sp: SharedPreferences
    private lateinit var todos: MutableList<Todo>


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())
        todos = mutableListOf()



        sp = getSharedPreferences("TODO", MODE_PRIVATE)
        val fetcheddata = sp.getString("name_key", "nothing")
        Log.e(" ", "onCreate: $fetcheddata")
        Toast.makeText(this, fetcheddata, Toast.LENGTH_LONG).show()

        val obj = JSONArray(fetcheddata)

        val length: Int = obj.length()




        for (i in 0 until length) {
            try {
                val innerobj = obj.getJSONObject(i)
                val title: String = innerobj.getString("title")
                val isCheck: Boolean = innerobj.getBoolean("isChecked")
                val todo = Todo(title, isCheck)
                todoAdapter.addTodo(todo, sp)
                Log.e("TAG", "onCreate: Called ")


            } catch (e: IndexOutOfBoundsException) {
                break
            }
        }






        rvTodo.adapter = todoAdapter
        rvTodo.layoutManager = LinearLayoutManager(this)


        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo, sp)


            }
        }

        btnDel.setOnClickListener {
            todoAdapter.deleteTodo(sp)
        }


    }


    class TodoAdapter(
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

        private fun toggleStrike(todoTitle: TextView, isChecked: Boolean) {
            if (isChecked) {
                todoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            } else
                todoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val curTodo = todos[position]
            holder.itemView.apply {
                tvTodoTitle.text = curTodo.title
                cbDone.isChecked = curTodo.isChecked
                toggleStrike(tvTodoTitle, curTodo.isChecked)
                cbDone.setOnCheckedChangeListener { _, isChecked ->
                    toggleStrike(tvTodoTitle, isChecked)
                    curTodo.isChecked = !curTodo.isChecked


                }


            }
        }


        override fun getItemCount(): Int {
            return todos.size
        }

        fun addTodo(todo: Todo, sp: SharedPreferences) {
            todos.add(todo)
            notifyItemInserted(todos.size - 1)
            val gson = Gson() // Or use new GsonBuilder().create();


            val json = gson.toJson(todos)


            val editor: SharedPreferences.Editor = sp.edit()

            editor.putString("name_key", json)
            editor.apply()
            editor.commit()
            Log.e("TAG", "json: $json")


            // val editor: SharedPreferences.Editor =  m.sp.edit()

            // editor.putString("data",json)
            // editor.apply()


        }

        fun deleteTodo(sp: SharedPreferences) {
            todos.removeAll { todo ->
                todo.isChecked
            }
            notifyDataSetChanged()
            val gson = Gson() // Or use new GsonBuilder().create();


            val json = gson.toJson(todos)


            val editor: SharedPreferences.Editor = sp.edit()

            editor.putString("name_key", json)
            editor.apply()
            editor.commit()


        }


    }
}


