package com.example.dream_house2.Modules;

public class favorites {
    private Post post;

    public favorites() { }

    public favorites(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
