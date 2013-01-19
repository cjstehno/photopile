package com.stehno.photopile.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name="PHOTOS")
public class Photo {

    @Id
    @Column(name="ID")
    @GeneratedValue
    private Long id;

    // TODO: get this to work?
//    private Long version;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="WIDTH")
    private int width;

    @Column(name="HEIGHT")
    private int height;

    // TODO: implement
//    private Date dateUploaded;
//    private Date dateUpdated;
//    private Date dateTaken;
//    private String cameraInfo;
    // private Location location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("id", id)
            .append("name", name)
            .append("description", description)
            .append("width", width)
            .append("height", height)
            .toString();
    }
}
