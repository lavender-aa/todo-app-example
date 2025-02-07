package com.example.astutorial

import android.content.ClipData.Item
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astutorial.databinding.ItemTodoBinding

class TodoAdapter(private val todos: MutableList<Todo>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    // provide reference to the type of views that are being used
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // define click listener for the ViewHolder's View
        val todoName: TextView = view.findViewById(R.id.todoName)
        val todoDone: CheckBox = view.findViewById(R.id.todoDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikethrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curTodo = todos[position]

        holder.apply {
            todoName.text = curTodo.title
            todoDone.isChecked = curTodo.isChecked
            toggleStrikethrough(todoName, curTodo.isChecked)
            todoDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikethrough(todoName, todoDone.isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}