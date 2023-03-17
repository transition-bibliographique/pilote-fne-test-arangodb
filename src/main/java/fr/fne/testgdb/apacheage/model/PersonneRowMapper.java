package fr.fne.testgdb.apacheage.model;

import fr.fne.testgdb.model.Personne;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonneRowMapper implements RowMapper<Personne> {
    @Override
    public Personne mapRow(ResultSet rs, int rowNum) throws SQLException {
        Personne personne = new Personne();

        JSONObject json = new JSONObject(rs.getString(1));
        JSONObject props = json.optJSONObject("properties");
        if (props != null) {
            personne.setPpn(props.getString("ppn"));
            personne.setUrlPerenne(props.getString("urlPerenne"));
            personne.setIsni(props.getString("idISNI"));
            personne.setNom(props.getString("nom"));
            personne.setPrenom(props.getString("prenom"));
            personne.setDateDeNaissance(props.getString("dateNaissance"));
            personne.setDateDeDeces(props.getString("dateDeces"));
            personne.setActivite(props.getString("activite"));
            personne.setNoteBibliographique(props.getString("noteBio"));
            personne.setLangue(props.getString("langue"));
            personne.setPointAcces(props.getString("pointAcces"));
        }
        return personne;
    }
}
