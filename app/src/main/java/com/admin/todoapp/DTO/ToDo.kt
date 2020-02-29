package com.admin.todoapp.DTO

class ToDo (var id: Long = -1,
            var name: String = "",
            var createdAt: String = "",
            var items: MutableList<ToDoItem> = ArrayList()) {
}