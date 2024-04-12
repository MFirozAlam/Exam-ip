package com.example.mcaproject

data class NotesItems(val title: String, val description: String,val noteId: String){
    constructor() : this("", "",  "")
}
