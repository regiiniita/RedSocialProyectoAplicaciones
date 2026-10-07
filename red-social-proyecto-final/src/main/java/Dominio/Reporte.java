package Dominio;

import java.time.LocalDateTime;

public class Reporte {

    private Long id;
    private Resenia resenia;
    private Usuario reportante;
    private String motivo;
    private LocalDateTime fechaReporte;

    public Reporte() {
        this.fechaReporte = LocalDateTime.now();
    }

    public Reporte(Long id, Resenia resenia, Usuario reportante, String motivo) {
        this.id = id;
        this.resenia = resenia;
        this.reportante = reportante;
        this.motivo = motivo;
        this.fechaReporte = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Resenia getResenia() { return resenia; }
    public void setResenia(Resenia resenia) { this.resenia = resenia; }
    public Usuario getReportante() { return reportante; }
    public void setReportante(Usuario reportante) { this.reportante = reportante; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }
}