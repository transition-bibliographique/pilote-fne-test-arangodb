package fr.fne.testgdb.model;

import fr.fne.testgdb.autorite.Controlfield;
import fr.fne.testgdb.autorite.Datafield;
import fr.fne.testgdb.autorite.Record;
import fr.fne.testgdb.autorite.Subfield;
import org.springframework.stereotype.Service;

@Service
public class DtoAutoriteToPersonne {

    public Personne unmarshallerNotice(Record r) {

        Personne p = new Personne();

        //ControlFields :
        for (Controlfield c : r.getControlfieldList()) {
            if (c.getTag().equalsIgnoreCase("003")) {
                p.setUrlPerenne(c.getValue());
            }
        }

        //DataFields :
        for (Datafield d : r.getDatafieldList()) {

            if (d.getTag().equalsIgnoreCase("010")) {
                for (Subfield s : d.getSubfieldList()) {
                    if (s.getCode().equalsIgnoreCase("a")) {
                       p.setIsni(s.getValue());
                    }
                }
            }

            if (d.getTag().equalsIgnoreCase("101")) {
                for (Subfield s : d.getSubfieldList()) {
                    if (s.getCode().equalsIgnoreCase("a")) {
                        p.setLangue(s.getValue());
                    }
                }
            }

            if (d.getTag().equalsIgnoreCase("103")) {
                for (Subfield s : d.getSubfieldList()) {
                    if (s.getCode().equalsIgnoreCase("a")) {
                        p.setDateDeNaissance(s.getValue());
                    }
                    else if (s.getCode().equalsIgnoreCase("b")) {
                        p.setDateDeDeces(s.getValue());
                    }
                }
            }

            if (d.getTag().equalsIgnoreCase("200")){
                for (Subfield s : d.getSubfieldList()){
                    if (s.getCode().equalsIgnoreCase("a")){
                        p.setNom(s.getValue());
                    }
                    else if (s.getCode().equalsIgnoreCase("b")){
                        p.setPrenom(s.getValue());
                    }
                }
            }

            if (d.getTag().equalsIgnoreCase("300")){
                for (Subfield s : d.getSubfieldList()){
                    if (s.getCode().equalsIgnoreCase("a")){
                        p.setNoteBibliographique(s.getValue());
                    }
                }
            }

            if (d.getTag().equalsIgnoreCase("340")){
                for (Subfield s : d.getSubfieldList()){
                    if (s.getCode().equalsIgnoreCase("a")){
                        p.setActivite(s.getValue());
                    }
                }
            }
        }
        return p;
    }
}
