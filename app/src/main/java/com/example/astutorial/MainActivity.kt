package com.example.astutorial

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    // everything in the main activity
    private lateinit var rvTodoItems: RecyclerView
    private lateinit var btnAddTodo: Button
    private lateinit var btnDeleteDone: Button
    private lateinit var etTodoTitle: EditText
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var list: MutableList<Todo>
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create, set list of items from data file
        file = File(this.filesDir, "data.txt")
        list = getData(file)

        // initialize the recycle view adapter
        todoAdapter = TodoAdapter(list)

        // assign local variables to their corresponding layout items
        rvTodoItems = findViewById(R.id.rvTodoItems)
        btnAddTodo = findViewById(R.id.btnAddTodo)
        etTodoTitle = findViewById(R.id.etTodoTitle)
        btnDeleteDone = findViewById(R.id.btnDeleteDoneTodos)

        // set the recycle view adapter and layout manager
        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        // create and add a new item if the add button is clicked
        btnAddTodo.setOnClickListener {
            val title = etTodoTitle.text.toString()
            if(title.isNotEmpty()) {
                val todo = Todo(title)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }

        // delete marked-off items if the delete button is clicked
        btnDeleteDone.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
    }

    override fun onPause() {
        super.onPause()

        // save data to file
        storeData()
    }

    private fun getData(file: File): MutableList<Todo> {
        // if the file doesn't exist, create it and return an empty list
        if(!file.exists()) {
            file.createNewFile()
            return mutableListOf()
        }

        // if it does exist, get the data from it
        file.bufferedReader().use { reader ->
            var name: String?
            var checked: String?
            val list: MutableList<Todo> = mutableListOf()

            // breaks appropriately:
            // - when task name read is null (all tasks read)
            // - when last task has no checked option (returns empty list)
            while(true) {

                // read first line
                name = reader.readLine()

                // stop processing if first read is empty
                if(name == null) {
                    reader.close()
                    return list
                }

                // continue to second element (checked)
                checked = reader.readLine()

                // return an empty list if second line is empty, print message
                if(checked == null) {
                    println("Error reading data file; using empty list")
                    reader.close()
                    return mutableListOf()
                }

                // add new task item to list
                list.add(Todo(name, checked == "true"))
            }
        }
    }

    private fun storeData() {

        /*
        storage pattern (plaintext, file starts next line):
        name1
        checked1 (ex. true)
        name2
        checked2
        */

        // delete old file, create new empty file
        file.delete()
        file.createNewFile()

        // for each item in the list, print it to the file as specified above
        file.printWriter().use { writer ->
            list.forEach {
                val checked: String = if(it.isChecked) "true" else "false"
                writer.println("${it.title}\n${checked}")
            }

            // close the writer
            writer.close()
        }
    }
}