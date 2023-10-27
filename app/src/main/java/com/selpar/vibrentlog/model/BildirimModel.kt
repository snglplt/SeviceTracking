package com.selpar.vibrentlog.model

 class BildirimModel {
     val bildirim_key: String
     val bildirim_title:String
     val bildirim_body:String
     constructor(bildirim_key: String, bildirim_title: String, bildirim_body: String) {
         this.bildirim_key = bildirim_key
         this.bildirim_title = bildirim_title
         this.bildirim_body = bildirim_body
     }




 }