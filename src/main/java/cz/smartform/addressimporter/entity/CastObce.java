package cz.smartform.addressimporter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cast_obce")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CastObce {

    @Id
    private Integer kod;

    @Column(nullable = false)
    private String nazev;

    @Column(name = "obec_kod", nullable = false)
    private Integer obecKod;
}