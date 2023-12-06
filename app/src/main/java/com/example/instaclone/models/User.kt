package com.example.instaclone.models

class User {
    var image:String? = null;
    var name:String? = null;
    var password:String? = null;
    var email:String? = null;

    constructor()
    constructor(image: String?, name: String?, password: String?, email: String?) {
        this.image = image
        this.name = name
        this.password = password
        this.email = email
    }

    constructor(name: String?, password: String?, email: String?) {
        this.name = name
        this.password = password
        this.email = email
    }
}