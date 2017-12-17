package com.zentcode.parks.models;

public class Travesia {

    private Integer idServidor;
    private String responsable1;
    private String responsable2;
    private String fechaInicio;
    private String fechaFin;
    private String destino;
    private String observacion;
    private String resumen;
    private Integer nroViaje;
    private Boolean sync;
    private Boolean finalizado;
    private Integer nroAppSync;
    private Integer unidadId;
    private Integer estadoId;
    private Boolean eliminado;

    public Travesia() {}

    public Travesia(Integer idServidor, String responsable1, String responsable2, String fechaInicio, String fechaFin, String destino, String observacion, String resumen, Integer nroViaje, Boolean sync, Boolean finalizado, Integer nroAppSync, Integer unidadId, Integer estadoId, Boolean eliminado) {
        this.idServidor = idServidor;
        this.responsable1 = responsable1;
        this.responsable2 = responsable2;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.destino = destino;
        this.observacion = observacion;
        this.resumen = resumen;
        this.nroViaje = nroViaje;
        this.sync = sync;
        this.finalizado = finalizado;
        this.nroAppSync = nroAppSync;
        this.unidadId = unidadId;
        this.estadoId = estadoId;
        this.eliminado = eliminado;
    }

    public Integer getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Integer idServidor) {
        this.idServidor = idServidor;
    }

    public String getResponsable1() {
        return responsable1;
    }

    public void setResponsable1(String responsable1) {
        this.responsable1 = responsable1;
    }

    public String getResponsable2() {
        return responsable2;
    }

    public void setResponsable2(String responsable2) {
        this.responsable2 = responsable2;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Integer getNroViaje() {
        return nroViaje;
    }

    public void setNroViaje(Integer nroViaje) {
        this.nroViaje = nroViaje;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Integer getNroAppSync() {
        return nroAppSync;
    }

    public void setNroAppSync(Integer nroAppSync) {
        this.nroAppSync = nroAppSync;
    }

    public Integer getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Integer unidadId) {
        this.unidadId = unidadId;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Travesia{" +
                "idServidor=" + idServidor +
                ", responsable1='" + responsable1 + '\'' +
                ", responsable2='" + responsable2 + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                ", destino='" + destino + '\'' +
                ", observacion='" + observacion + '\'' +
                ", resumen='" + resumen + '\'' +
                ", nroViaje=" + nroViaje +
                ", sync=" + sync +
                ", finalizado=" + finalizado +
                ", nroAppSync=" + nroAppSync +
                ", unidadId=" + unidadId +
                ", estadoId=" + estadoId +
                ", eliminado=" + eliminado +
                '}';
    }
}