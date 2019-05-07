package com.framework.model;

public class UploadImgV2Bean{
    int status;
    String message;
    PathData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PathData getData() {
        return data;
    }

    public void setData(PathData data) {
        this.data = data;
    }

    public static class PathData{
        String id;
        String path;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}