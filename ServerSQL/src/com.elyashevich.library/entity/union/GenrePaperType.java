//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.03.11 at 03:55:35 AM MSK 
//


package com.elyashevich.library.entity.union;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;
import java.util.Objects;


/**
 * <p>Java class for genrePaperType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genrePaperType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paperEditionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="genreID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genrePaperType", propOrder = {
    "paperEditionID",
    "genreID"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class GenrePaperType {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String paperEditionID;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String genreID;
    @XmlAttribute(name = "id")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String id;

    public GenrePaperType() {
    }

    public GenrePaperType(String paperEditionID, String genreID) {
        this.paperEditionID = paperEditionID;
        this.genreID = genreID;
    }

    /**
     * Gets the value of the paperEditionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getPaperEditionID() {
        return paperEditionID;
    }

    /**
     * Sets the value of the paperEditionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setPaperEditionID(String value) {
        this.paperEditionID = value;
    }

    /**
     * Gets the value of the genreID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getGenreID() {
        return genreID;
    }

    /**
     * Sets the value of the genreID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setGenreID(String value) {
        this.genreID = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-03-11T03:55:35+03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setId(String value) {
        this.id = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenrePaperType)) return false;
        GenrePaperType that = (GenrePaperType) o;
        return Objects.equals(paperEditionID, that.paperEditionID) &&
                Objects.equals(genreID, that.genreID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(paperEditionID, genreID);
    }

    @Override
    public String toString() {
        return "GenrePaperType{" +
                "paperEditionID='" + paperEditionID + '\'' +
                ", genreID='" + genreID + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
