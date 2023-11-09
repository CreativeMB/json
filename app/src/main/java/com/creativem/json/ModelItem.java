package com.creativem.json;

public class ModelItem {
    private String title;
    private String detail;
    private int imagen;
    private String consejos;
    private int cantidad;

    public ModelItem(String title, String detail, int imagen, String consejos) {
        this.title = title;
        this.detail = detail;
        this.imagen = imagen;
        this.consejos = consejos;
        this.cantidad = cantidad;
    }

    public ModelItem(String title, String detail, String imagen, String consejos, int cantidad) {
        this.title = title;
        this.detail = detail;
        this.consejos = consejos;
        this.imagen = Integer.parseInt(imagen);
        this.cantidad = cantidad;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getConsejos() {
        return consejos;
    }

    public void setConsejos(String consejos) {
        this.consejos = consejos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
