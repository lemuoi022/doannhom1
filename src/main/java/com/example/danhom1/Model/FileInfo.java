package com.example.danhom1.Model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;

@Setter
@Getter
public class FileInfo {
    private String name;
    private String url;

    @Contract(pure = true)
    public FileInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
