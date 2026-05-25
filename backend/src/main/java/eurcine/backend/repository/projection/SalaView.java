package eurcine.backend.repository.projection;

public interface SalaView {

    String getNome();

    String getDescrizione();

    String getCaratteristicheNomi();

    Long getPostiTotali();

    Long getFileTotali();

    String getSequenzaFile();
}
