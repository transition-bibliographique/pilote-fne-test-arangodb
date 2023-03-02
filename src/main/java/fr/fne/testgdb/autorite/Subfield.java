package fr.fne.testgdb.autorite;



import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


@Getter
@NoArgsConstructor
public class Subfield {
    @XmlAttribute(name="code")
    private String code;

    @XmlValue
    private String value;
}
