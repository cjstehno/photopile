package com.stehno.photopile.common

public class WrappedCollection<T> {

    private final Map<String, Object> meta = new HashMap<>()
    private final Collection<T> data

    public WrappedCollection( final Collection<T> data ){
        this.data = data
    }

    public Map<String, Object> getMeta(){
        return meta
    }

    public Collection<T> getData(){
        return data
    }
}
